import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class Test {

    public static void main(String[] args) {

		//String apiUrl = "https://extuat.esafbank.com/esaf/sendSMS?mobile=918329395183&msgText=Dear Customer, login to ESAF Bank Mobile banking application was successful on 18/09/2023 at 16:23:35 hrs. If not done by you, call 18001033723 immediately.&tempId=1107169036499743287"; // Replace with your API endpoint



       try {

            String baseUrl = "https://extuat.esafbank.com/esaf/sendSMS";


            String mobile = "918329395183";
            String msgText = "Dear Customer, login to ESAF Bank Mobile banking application was successful on 18/09/2023 at 16:23:35 hrs. If not done by you, call 18001033723 immediately.";
            String tempId = "1107169036499743287";

            // Encode the parameters
            String encodedMsgText = URLEncoder.encode(msgText, StandardCharsets.UTF_8.toString());

            // Construct the full URL
            String apiUrl = baseUrl + "?mobile=" + mobile + "&msgText=" + encodedMsgText + "&tempId=" + tempId;

            // Create URL object
            URL url = new URL(apiUrl);

            // Create connection
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            // Send request
            int responseCode = connection.getResponseCode();

            // Check if the response is successful (HTTP 200)
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                // Print the response
                System.out.println("Response: " + response.toString());
            } else {
                System.err.println("HTTP request failed with error code: " + responseCode);
            }
        } catch (Exception e) {
            System.err.println("Exception: " + e.getMessage());
        }
    }}
