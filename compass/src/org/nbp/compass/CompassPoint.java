package org.nbp.compass;

import org.nbp.common.CommonContext;
import org.nbp.common.CommonUtilities;

import android.os.Build;
import android.text.style.TtsSpan;

public enum CompassPoint {
  N, NNE, NE, ENE, E, ESE, SE, SSE,
  S, SSW, SW, WSW, W, WNW, NW, NNW;

  private final static int POINT_COUNT = values().length;
  private final static float POINTS_PER_CIRCLE = (float)POINT_COUNT;
  private final static float DEGREES_PER_CIRCLE = 360f;
  private final static float DEGREES_PER_POINT = DEGREES_PER_CIRCLE / POINTS_PER_CIRCLE;

  public final static CompassPoint getPoint (float degrees) {
    int ordinal = Math.round(degrees / DEGREES_PER_POINT);
    return values()[ordinal % POINT_COUNT];
  }

  public final float getDegrees () {
    return ((float)ordinal() * DEGREES_PER_POINT);
  }

  private String pointPhrase = null;
  private TtsSpan ttsSpan = null;

  public final String getPhrase () {
    synchronized (this) {
      if (pointPhrase == null) {
        StringBuilder sb = new StringBuilder();

        String string = name();
        int length = string.length();

        for (int index=0; index<length; index+=1) {
          char character = string.charAt(index);
          String word;

          switch (character) {
            case 'N':
              word = CommonContext.getString(R.string.direction_north);
              break;

            case 'E':
              word = CommonContext.getString(R.string.direction_east);
              break;

            case 'S':
              word = CommonContext.getString(R.string.direction_south);
              break;

            case 'W':
              word = CommonContext.getString(R.string.direction_west);
              break;

            default:
              word = "?";
              break;
          }

          if (sb.length() > 0) sb.append(' ');
          sb.append(word);
        }

        pointPhrase = sb.toString();
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
