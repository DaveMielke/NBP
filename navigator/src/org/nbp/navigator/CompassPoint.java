package org.nbp.navigator;

import org.nbp.common.CommonContext;
import org.nbp.common.CommonUtilities;

import android.text.style.TtsSpan;

public enum CompassPoint {
  N, NNE, NE, ENE, E, ESE, SE, SSE,
  S, SSW, SW, WSW, W, WNW, NW, NNW;

  private final static int POINT_COUNT = values().length;
  private final static float POINTS_PER_FULL_TURN = (float)POINT_COUNT;
  private final static float DEGREES_PER_POINT = AngleUnit.DEGREES_PER_FULL_TURN / POINTS_PER_FULL_TURN;

  public final static CompassPoint getPoint (float degrees) {
    int ordinal = Math.round(degrees / DEGREES_PER_POINT);
    return values()[ordinal % POINT_COUNT];
  }

  public final float getDegrees () {
    return (float)ordinal() * DEGREES_PER_POINT;
  }

  public final float getOffset (float degrees) {
    float offset = Math.round(degrees - getDegrees());
    if (offset > DEGREES_PER_POINT) offset -= AngleUnit.DEGREES_PER_FULL_TURN;
    return offset;
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
          if (sb.length() > 0) sb.append(' ');

          char character = string.charAt(index);
          int word = 0;

          switch (character) {
            case 'N':
              word = R.string.direction_north;
              break;

            case 'E':
              word = R.string.direction_east;
              break;

            case 'S':
              word = R.string.direction_south;
              break;

            case 'W':
              word = R.string.direction_west;
              break;
          }

          if (word != 0) {
            sb.append(CommonContext.getString(word));
          } else {
            sb.append('?');
          }
        }

        pointPhrase = sb.toString();
      }
    }

    return pointPhrase;
  }

  public final Object getSpeechSpan () {
    synchronized (this) {
      if (ttsSpan == null) {
        if (CommonUtilities.haveLollipop) {
          ttsSpan = new TtsSpan.TextBuilder(getPhrase()).build();
        }
      }
    }

    return ttsSpan;
  }
}
