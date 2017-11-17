package org.liblouis;

import com.duxburysystems.BrlTrn;
import com.duxburysystems.PinyinForwardTranslator;
import com.duxburysystems.PinyinBackwardTranslator;

import android.content.Context;

public class PinyinTranslator extends DuxburyTranslator {
  private final BrlTrn forwardTranslator;
  private final BrlTrn backwardTranslator;

  public PinyinTranslator () {
    super();

    Context context = Louis.getContext();
    forwardTranslator = new PinyinForwardTranslator(context);
    backwardTranslator = new PinyinBackwardTranslator(context);
  }

  @Override
  public final boolean translate (
    CharSequence inputBuffer, char[] outputBuffer,
    int[] outputOffsets, int[] inputOffsets,
    boolean backTranslate, boolean includeHighlighting,
    int[] resultValues
  ) {
    return false;
  }
}
