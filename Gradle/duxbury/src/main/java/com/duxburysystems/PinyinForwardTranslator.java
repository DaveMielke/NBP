package com.duxburysystems;

import android.content.Context;

public class PinyinForwardTranslator extends BrlTrn {
  public PinyinForwardTranslator (Context context) {
    super(context);

    {
      int error = create("zhmpcp.btb", "gendxp.sct", true);

      if (error != BrailleTranslationErrors.SUCCESS) {
        throw new BrailleTranslationException(error);
      }
    }

    {
      int error = setPrefix("\u001cvrn~t2\u001f"); // require Mandarin tones

      if (error != BrailleTranslationErrors.SUCCESS) {
        throw new BrailleTranslationException(error);
      }
    }
  }
}
