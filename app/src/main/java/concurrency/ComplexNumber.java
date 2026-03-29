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

  public static void main(String[] args) {
      ComplexNumber c = new ComplexNumber(1, 1);
      printThread("Original: " + c);
      new Thread(c::negate).start(); // easy way to create thread
      printThread("Negated: " + c);
  }
}
