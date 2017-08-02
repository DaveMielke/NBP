package org.nbp.editor;

public enum BrailleMode {
  CELLS(
    new Conversions() {
      @Override
      public char asciiToChar (byte ascii) {
        return ASCIIBraille.asciiToCell(ascii);
      }
    }
  ),

  NABCC(
    new Conversions() {
      @Override
      public char asciiToChar (byte ascii) {
        return ASCIIBraille.asciiToNabcc(ascii);
      }
    }
  ),

  ; // end of enumeration

  public interface Conversions {
    public char asciiToChar (byte ascii);
  }

  private final Conversions brailleConversions;

  private BrailleMode (Conversions conversions) {
    brailleConversions = conversions;
  }

  public final Conversions getConversions () {
    return brailleConversions;
  }
}
