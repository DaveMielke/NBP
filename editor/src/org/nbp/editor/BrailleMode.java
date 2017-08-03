package org.nbp.editor;

public enum BrailleMode {
  CELLS(
    new Conversions() {
      @Override
      public char symbolToChar (byte symbol) {
        return ASCIIBraille.symbolToCell(symbol);
      }
    }
  ),

  TEXT(
    new Conversions() {
      @Override
      public char symbolToChar (byte symbol) {
        return ASCIIBraille.symbolToText(symbol);
      }
    }
  ),

  ; // end of enumeration

  public interface Conversions {
    public char symbolToChar (byte symbol);
  }

  private final Conversions brailleConversions;

  private BrailleMode (Conversions conversions) {
    brailleConversions = conversions;
  }

  public final Conversions getConversions () {
    return brailleConversions;
  }
}
