package org.nbp.calculator.conversion;

public class SecondaryUnit extends Unit {
  public SecondaryUnit (Unit reference, double multiplier, String... names) {
    super(reference, multiplier, names);
  }
}
