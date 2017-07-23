package org.nbp.compass;

import org.nbp.common.CommonUtilities;
import org.nbp.common.CommonContext;

import android.os.Build;
import android.content.Context;
import android.text.style.TtsSpan;

public enum CompassPoint {
  N, NNE, NE, ENE, E, ESE, SE, SSE,
  S, SSW, SW, WSW, W, WNW, NW, NNW;

  private String pointPhrase = null;
  private TtsSpan ttsSpan = null;

  public final String getPhrase () {
    synchronized (this) {
      if (pointPhrase == null) {
        Context context = CommonContext.getContext();
        String resource = "compass_point_" + name();
        int identifier = context.getResources().getIdentifier(resource, "string", context.getPackageName());
        pointPhrase = context.getString(identifier);
      }
    }

    return pointPhrase;
  }

  public final Object getSpeechSpan () {
    synchronized (this) {
      if (ttsSpan == null) {
        if (CommonUtilities.haveAndroidSDK(Build.VERSION_CODES.LOLLIPOP)) {
          ttsSpan = new TtsSpan.TextBuilder(getPhrase()).build();
        }
      }
    }

    return ttsSpan;
  }
}
