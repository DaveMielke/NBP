package org.nbp.calculator;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public abstract class LocaleData {
  public final static Object LOCK = new Object();
  private static char decimalSeparator;
  private static char groupingSeparator;
  private static int groupingSize;

  public final static void reset () {
    synchronized (LOCK) {
      DecimalFormat format = new DecimalFormat();
      groupingSize = format.getGroupingSize();

      DecimalFormatSymbols symbols = format.getDecimalFormatSymbols();
      decimalSeparator = symbols.getDecimalSeparator();
      groupingSeparator = symbols.getGroupingSeparator();
    }
  }

  static {
    reset();
  }

  public final static char getDecimalSeparator () {
    return decimalSeparator;
  }

  public final static char getGroupingSeparator () {
    return groupingSeparator;
  }

  public final static int getGroupingSize () {
    return groupingSize;
  }

  private LocaleData () {
  }
}
