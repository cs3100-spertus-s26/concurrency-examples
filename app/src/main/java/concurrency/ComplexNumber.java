package concurrency;

import static concurrency.Utilities.printThread;

public class ComplexNumber {
  private int real;
  private int imaginary;

  public ComplexNumber(int real, int imaginary) {
    this.real = real;
    this.imaginary = imaginary;
  }

  public void negate() {
    printThread("At start of negate()");
    real = -real;
    imaginary = -imaginary;
    printThread("At end of negate()");
  }

  @Override
  public String toString() {
    return String.format("%d + %di", real, imaginary);
  }

  public static void main(String[] args) throws InterruptedException {
    ComplexNumber c = new ComplexNumber(1, 1);
    printThread("Original: " + c);
    Thread thread0 = new Thread(c::negate);
    thread0.start();
    thread0.join(); // force main thread to wait for thread0 to finish
    printThread("Negated: " + c);
  }
}
