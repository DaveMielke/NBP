package org.liblouis;

public abstract class TranslationComponent {
  protected TranslationComponent () {
  }

  private enum ResultValuesIndex {
    INPUT_LENGTH,
    OUTPUT_LENGTH,
    CURSOR_OFFSET,
    ; // end of enumeration
  }

  protected final static int RESULT_VALUES_COUNT = ResultValuesIndex.values().length;
  protected final static int RVI_INPUT_LENGTH = ResultValuesIndex.INPUT_LENGTH.ordinal();
  protected final static int RVI_OUTPUT_LENGTH = ResultValuesIndex.OUTPUT_LENGTH.ordinal();
  protected final static int RVI_CURSOR_OFFSET = ResultValuesIndex.CURSOR_OFFSET.ordinal();

  protected final static int NO_CURSOR = -1;
}
