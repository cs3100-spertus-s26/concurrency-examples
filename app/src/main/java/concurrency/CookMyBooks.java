package concurrency;

public class CookMyBooks {
  private static final long START_TIME = System.currentTimeMillis();

  private static void log(String message) {
    long elapsed = System.currentTimeMillis() - START_TIME;
    String thread = Thread.currentThread().getName();
    System.out.printf("%4dms %s %s%n", elapsed, thread, message);
  }

  private static void fetchRecipe() {
    log("Fetching recipe...");
    try {
      Thread.sleep(2000); // simulates slow network call
    } catch (InterruptedException e) {
      // this can't happen in our program
    }
    log("Got recipe!");
    updateUI("Cookie Recipe");
  }

  private static void updateUI(String recipe) {
    if (!Thread.currentThread().getName().equals("main")) {
      throw new IllegalStateException(
        "updateUI must be called on the main thread");
    }
    log("Updating UI: " + recipe);
  }

  // This runs in the UI thread.
  private static void handleFetchRecipeRequest()  {
    log("UI thread received fetch recipe request");
    log("UI is non-responsive");

    // Create a worker thread to fetch the recipe
    new Thread(CookMyBooks::fetchRecipe).start();
    log("UI is now responsive again");
  }

  public static void main(String[] args) {
    handleFetchRecipeRequest();
  }
}
