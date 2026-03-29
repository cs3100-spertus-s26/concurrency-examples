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

  @Override
  public void run() {
    while (count < limit) {
      count++;
      System.out.println(name + ": " + count);
    }
  }

  public static void main(String[] args) {
    Counter counter1 = new Counter("Counter A", 5);
    counter1.start();
  }
}
