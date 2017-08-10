package org.nbp.common;

import org.nbp.common.IntegerControl;

public abstract class TimeControl extends IntegerControl {
  private final static Integer MINIMUM_VALUE = 1;

  @Override
  protected Integer getIntegerMinimum () {
    return MINIMUM_VALUE;
  }

  protected final static int MILLISECONDS_PER_SECOND = 1000;
  protected final static int SECONDS_PER_MINUTE = 60;
  protected final static int MINUTES_PER_HOUR = 60;

  @Override
  public CharSequence getValue () {
    StringBuilder sb = new StringBuilder();
    int value = getIntegerValue();

    int milliseconds = value % MILLISECONDS_PER_SECOND;
    value /= MILLISECONDS_PER_SECOND;

    int seconds = value % SECONDS_PER_MINUTE;
    value /= SECONDS_PER_MINUTE;

    int minutes = value % MINUTES_PER_HOUR;
    value /= MINUTES_PER_HOUR;
    int hours = value;

    if (hours != 0) {
      if (sb.length() > 0) sb.append(' ');
      sb.append(hours);
      sb.append('h');
    }

    if (minutes != 0) {
      if (sb.length() > 0) sb.append(' ');
      sb.append(minutes);
      sb.append('m');
    }

    {
      int oldLength = sb.length();

      if (milliseconds != 0) {
        sb.append(
          String.format("%.3f",
            ((float)seconds + ((float)milliseconds / MILLISECONDS_PER_SECOND))
          )
        );

        while (true) {
          int last = sb.length() - 1;
          if (sb.charAt(last) != '0') break;
          sb.setLength(last);
        }
      } else if (seconds != 0) {
        sb.append(seconds);
      }

      if (sb.length() > oldLength) {
        if (oldLength > 0) sb.insert(oldLength, ' ');
        sb.append('s');
      }
    }

    return sb.toString();
  }

  public TimeControl () {
    super();
  }
}
