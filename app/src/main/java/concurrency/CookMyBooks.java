package concurrency;

import javafx.application.Platform;
import javafx.concurrent.Task;

public class CookMyBooks {
  private static final long START_TIME = System.currentTimeMillis();
  private static Task<String> fetchRecipeTask = null;

  private static void log(String message) {
    long elapsed = System.currentTimeMillis() - START_TIME;
    String thread = Thread.currentThread().getName();
    System.out.printf("%4dms %s %s%n", elapsed, thread, message);
  }

  private static String fetchRecipe() {
    log("Fetching recipe...");
    try {
      Thread.sleep(2000); // simulates slow network call
    } catch (InterruptedException e) {
      log("fetchRecipe() was interrupted");
      log("isCancelled is: " + fetchRecipeTask.isCancelled());
      return null;
    }
    log("Got recipe!");
    return "Cookie Recipe";
  }

  private static void updateUI(String recipe) {
    if (!Thread.currentThread().getName().equals(
        "JavaFX Application Thread")) {
      throw new IllegalStateException(
          "updateUI must be called on the UI thread");
    }
    log("Updating UI: " + recipe);
  }

  private static void showError(Throwable error) {
    log("Error: " + error.getMessage());
  }

  // This runs in the UI thread
  private static void handleFetchRecipeRequest() {
    log("UI thread received fetch recipe request");
    fetchRecipeTask = BackgroundTaskRunner.run(
        () -> fetchRecipe(),
        recipe -> updateUI(recipe),
        error -> showError(error));
    log("UI is now responsive again");
  }

  // This runs in the UI thread
  private static void handleCancelFetchRecipeRequest() {
    log("UI thread received cancel request");
    if (fetchRecipeTask != null) {
      fetchRecipeTask.cancel();
    }
  }

  public static void main(String[] args) throws InterruptedException {
    Platform.startup(() -> {
    }); // initialize JavaFX without a GUI
    Platform.runLater(() -> handleFetchRecipeRequest());
    // Wait 1 second before cancelling request
    Thread.sleep(1000);
    handleCancelFetchRecipeRequest();
  }
}
