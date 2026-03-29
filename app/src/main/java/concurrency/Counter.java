package concurrency;

public class Counter extends Thread {
  private final String name;
  private int count;
  private final int limit;

  public Counter(String name, int limit) {
    this.name = name;
    count = 0;
    this.limit = limit;
  }

  private static void printThread(String msg) {
    System.out.println(String.format("thread %s: %s",
        Thread.currentThread().getName(), msg));
  }

  @Override
  public void run() {
    while (count < limit) {
      count++;
      printThread(name + ": " + count);
    }
  }

  public static void main(String[] args) {
    printThread("At start of main()");
    Counter counter1 = new Counter("Counter A", 3);
    counter1.start();
    printThread("At end of main()");
  }
}
