package concurrency;

import javafx.application.Platform;
import javafx.concurrent.Task;

public class CookMyBooks {
  private static final long START_TIME = System.currentTimeMillis();
  private static volatile Task<String> calculatePiTask = null;

  private static void log(String message) {
    long elapsed = System.currentTimeMillis() - START_TIME;
    String thread = Thread.currentThread().getName();
    System.out.printf("%4dms %s %s%n", elapsed, thread, message);
  }

  private static String calculatePi() {
    double pi = 0;
    for (int i = 0; i < 100_000_000; i++) {
        pi += Math.pow(-1, i) / (2 * i + 1);
        if (calculatePiTask != null && calculatePiTask.isCancelled()) {
          log("calculatePi was cancelled after " + i + " iterations");
          return "Cancelled";
        }
    }
    pi *= 4;
    log("Calculated Pi: " + pi);
    return Double.toString(pi);
  }

  private static void updateUI(String result) {
    if (!Thread.currentThread().getName().equals(
        "JavaFX Application Thread")) {
      throw new IllegalStateException(
          "updateUI must be called on the UI thread");
    }
    log("Updating UI: " + result);
  }

  private static void showError(Throwable error) {
    log("Error: " + error.getMessage());
  }

  // This runs in the UI thread
  private static void handleCalculatePiRequest() {
    log("UI thread received calculate Pi request");
    calculatePiTask = BackgroundTaskRunner.run(
        () -> calculatePi(),
        pi -> updateUI(pi),
        error -> showError(error));
    log("UI is now responsive again");
  }

  // This runs in the UI thread
  private static void handleCancelCalculatePiRequest() {
    log("UI thread received cancel request");
    if (calculatePiTask != null) {
      calculatePiTask.cancel();
      updateUI("calculate Pi request cancelled");
      calculatePiTask = null;
    }
  }

  public static void main(String[] args) throws InterruptedException {
    Platform.startup(() -> {}); // initialize JavaFX without a GUI
    Platform.runLater(() -> handleCalculatePiRequest());
    // Wait 1 second before cancelling request
    Thread.sleep(1000);
    Platform.runLater(() -> handleCancelCalculatePiRequest());
  }
}
