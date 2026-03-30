package concurrency;

import static concurrency.Utilities.printThread;

public class Dining {
  private final Object fork = new Object();
  private final Object knife = new Object();

  public void roommate1() {
    synchronized (fork) {
      printThread("Roommate 1 picked up the fork.");
      synchronized (knife) {
        printThread("Roommate 1 picked up the knife. Eating!");
      }
    }
  }

  public void roommate2() {
    synchronized (knife) {
      printThread("Roommate 2 picked up the knife.");
      synchronized (fork) {
        printThread("Roommate 2 picked up the fork. Eating!");
      }
    }
  }

  public static void main(String[] args) {
    Dining d = new Dining();
    new Thread(d::roommate1).start();
    new Thread(d::roommate2).start();
  }
}