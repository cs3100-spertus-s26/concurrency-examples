package concurrency;

import java.time.Duration;
import java.time.Instant;

import static concurrency.Utilities.printThread;

public class Plant implements Runnable {

  private static final int WATERING_INTERVAL_DAYS = 3;
  private Instant lastTimeWatered;
  private int numTimesWatered = 0;
  private final Object huskyPlush = new Object();

  private int daysSinceWatered() {
    return (int) Duration.between(lastTimeWatered,
        Instant.now()).toDays();
  }

  private void waterPlant() {
    printThread("Watering plant.");
    numTimesWatered++;
    lastTimeWatered = Instant.now();
  }

  private void waterPlantIfNeeded() {
    if (lastTimeWatered == null
        || daysSinceWatered() >= WATERING_INTERVAL_DAYS) {
      printThread("About to water plant.");
      waterPlant();
      printThread("numTimesWatered: " + numTimesWatered);
    } else {
      printThread("No need to water plant.");
    }
  }

  @Override
  public synchronized void run() {
    waterPlantIfNeeded();
  }

  public static void main(String[] args) {
    Plant plant = new Plant();
    new Thread(plant).start(); // roommate 1
    new Thread(plant::waterPlantIfNeeded).start(); // roommate 2
  }
}
