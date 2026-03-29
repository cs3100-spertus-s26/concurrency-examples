package concurrency;

public final class Utilities {
  public static void printThread(String msg) {
    System.out.println(String.format("thread %s: %s",
        Thread.currentThread().getName(), msg));
  }
}
