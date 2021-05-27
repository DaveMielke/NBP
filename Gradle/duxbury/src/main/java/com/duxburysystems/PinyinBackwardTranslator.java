package com.duxburysystems;

import android.content.Context;

public class PinyinBackwardTranslator extends BrlTrn {
  public PinyinBackwardTranslator (Context context) {
    super(context);

    {
      int error = create("zhmpcb.btb", "gendxb.sct", false);

      if (error != BrailleTranslationErrors.SUCCESS) {
        throw new BrailleTranslationException(error);
      }
    }

    {
      int error = setPrefix("\u001cvrn~t2\u001f");

      if (error != BrailleTranslationErrors.SUCCESS) {
        throw new BrailleTranslationException(error);
      }
    }
  }
}
