import java.time.LocalDateTime;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @Purpose: run thread in repetitively, max 10 times or if the future object gives satisfying condition
 * @Author: Rahul Suryawanshi
 */

public class ScheduledThreads{

    public static void main(String[] args) {
        final AtomicInteger atomicIntegr = new AtomicInteger();

        Callable<String> taskRun = () -> {
            System.err.println(
                atomicIntegr.getAndIncrement() + " *** " + System.currentTimeMillis() + " " + LocalDateTime.now());
                return "Fail";
            };
            ScheduledExecutorService scExService = Executors.newScheduledThreadPool(1);

            AtomicReference<ScheduledFuture<String>> automicfutureRef = new AtomicReference<>();

            automicfutureRef.set(scExService.schedule(taskRun, 0, TimeUnit.SECONDS));

            scExService.scheduleWithFixedDelay(() -> {
                try {
                    String futureStr = automicfutureRef.get().get();

                    System.err.println("Future Response " + futureStr);

                    if (futureStr.equals("Success") || atomicIntegr.get() >= 10) {
                        scExService.shutdown();
                        scExService.shutdownNow();
                    } else {
                        automicfutureRef.set(scExService.schedule(taskRun, 10, TimeUnit.SECONDS));
                    }

                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }

            }, 0, 10, TimeUnit.SECONDS);

        }
    }