package org.nbp.duxbury;

import org.liblouis.Louis;
import android.content.Context;

import com.duxburysystems.BrlTrn;
import com.duxburysystems.PinyinForwardTranslator;
import com.duxburysystems.PinyinBackwardTranslator;

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
