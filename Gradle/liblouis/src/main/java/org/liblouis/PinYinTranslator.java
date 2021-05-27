package org.liblouis;

import com.duxburysystems.BrlTrn;
import com.duxburysystems.PinyinForwardTranslator;
import com.duxburysystems.PinyinBackwardTranslator;

import android.content.Context;

public class PinYinTranslator extends DuxburyTranslator {
  private final BrlTrn forwardTranslator;
  private final BrlTrn backwardTranslator;

  public PinYinTranslator () {
    super();

    Context context = Louis.getContext();
    forwardTranslator = new PinyinForwardTranslator(context);
    backwardTranslator = new PinyinBackwardTranslator(context);
  }

  @Override
  protected final BrlTrn getForwardTranslator () {
    return forwardTranslator;
  }

  @Override
  protected final BrlTrn getBackwardTranslator () {
    return backwardTranslator;
  }
}
