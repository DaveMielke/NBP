package org.liblouis;

public abstract class Translator {
  protected Translator () {
  }

  private enum ResultValuesIndex {
    INPUT_LENGTH,
    OUTPUT_LENGTH,
    CURSOR_OFFSET,
    ; // end of enumeration
  }

  public final static int RESULT_VALUES_COUNT = ResultValuesIndex.values().length;
  public final static int RVI_INPUT_LENGTH = ResultValuesIndex.INPUT_LENGTH.ordinal();
  public final static int RVI_OUTPUT_LENGTH = ResultValuesIndex.OUTPUT_LENGTH.ordinal();
  public final static int RVI_CURSOR_OFFSET = ResultValuesIndex.CURSOR_OFFSET.ordinal();

  public abstract boolean translate (
    CharSequence inputBuffer, char[] outputBuffer,
    int[] outputOffsets, int[] inputOffsets,
    boolean backTranslate, boolean includeHighlighting,
    int[] resultValues
  );
}
