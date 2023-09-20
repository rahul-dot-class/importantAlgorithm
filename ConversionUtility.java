import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
// for this two imports you will have to import gson from google in POM
//<dependency><groupId>com.google.code.gson</groupId><
//artifactId>gson</artifactId>
//<version>use the leates version here </version>
//</dependency>

	/**
	 * @purpose to retrieve inner objects from json String like inception
	 * @author Rahul Suryawanshi
	 *
	 **/

	public class ConversionUtility {

		private ConversionUtility() {
		}

		public static Map<String, Object> convertStringToMap(String pStr) {
			return new Gson().fromJson(pStr, new TypeToken<HashMap<String, Object>>() {
			}.getType());
		}

		// $$ send pKay as key and pPayload as String in json format,if the occurrence
		// of key possibly duplicate then
		// additional key can be send with pipe separated
		// ex.{{"payer":{account:123}},{"payee":{account:123}}} if want payer ac then
		// key `payer|account`

		@SuppressWarnings("unchecked")
		public static Object getValueByKeyMultipleKeys(String pKay, Object pPayload) {
			String[] lKeyArr = pKay.split("\\|");
			Object lObj = null;
			if (pPayload instanceof String)
				lObj = convertStringToMap((String) pPayload);
			else
				lObj = pPayload;

			if (lKeyArr.length > 0) {
				for (String key : lKeyArr) {
					if (lObj != null) {
						if (lObj instanceof Map) {
							lObj = getValueByKeyFromMap(key, (Map<String, Object>) lObj);
						} else if (lObj instanceof List) {
							for (Object aObj : (List<?>) lObj) {
								Object lFoundObjectFromArray = getValueByKeyFromListOfMap(key,
										(Map<String, Object>) aObj);
								if (lFoundObjectFromArray != null)
									lObj = lFoundObjectFromArray;
							}
						}
					} else {
						return null;
					}
				}
				return lObj;
			}
			return null;
		}

		@SuppressWarnings("unchecked")
		public static Object getValueByKeyFromMap(String pKay, Map<String, Object> pPayloadMap) {

			Object lCheckKey = pPayloadMap.get(pKay);

			if (lCheckKey == null) {
				for (Map.Entry<String, Object> entry : pPayloadMap.entrySet()) {
					if (entry.getValue() != null) {
						if (entry.getValue() instanceof Map) {
							Object lFoundKey = getValueByKeyFromMap(pKay, (Map<String, Object>) entry.getValue());
							if (lFoundKey != null)
								return lFoundKey;
						} else if (entry.getValue() instanceof List) {
							for (Object aObj : (List<?>) entry.getValue()) {
								Object lFoundObjectFromArray = getValueByKeyFromListOfMap(pKay,
										(Map<String, Object>) aObj);
								if (lFoundObjectFromArray != null)
									return lFoundObjectFromArray;
							}
						}
					}
				}
			} else {
				return lCheckKey;
			}
			return null;
		}

		public static Object getValueByKeyFromListOfMap(String pKay, Map<String, Object> pPayloadMap) {
			boolean lFoundValue = false;
			for (Map.Entry<String, Object> entry : pPayloadMap.entrySet()) {
				if (pKay.equals(entry.getValue())) {
					lFoundValue = true;
					continue;
				}
				if (lFoundValue) {
					if (entry.getValue() instanceof Double) {
						BigDecimal bigDecimalValue = new BigDecimal(Double.toString((Double) entry.getValue()));
						// Convert the BigDecimal to a BigInteger
						// BigInteger bigIntegerValue = bigDecimalValue.toBigInteger();
						return bigDecimalValue;
					} else
						return entry.getValue();
				}
			}
			return null;
		}
	}
