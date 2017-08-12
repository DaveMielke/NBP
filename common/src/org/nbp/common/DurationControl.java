package org.nbp.common;

import org.nbp.common.IntegerControl;

public abstract class DurationControl extends IntegerControl {
  private final static Integer MINIMUM_VALUE = 1;

  @Override
  protected Integer getIntegerMinimum () {
    return MINIMUM_VALUE;
  }

  protected final static int MILLISECONDS_PER_SECOND = 1000;
  protected final static int SECONDS_PER_MINUTE = 60;
  protected final static int MINUTES_PER_HOUR = 60;
  protected final static int HOURS_PER_DAY = 24;

  @Override
  public final CharSequence getValue () {
    StringBuilder sb = new StringBuilder();
    int milliseconds = getIntegerValue();

    int seconds = milliseconds / MILLISECONDS_PER_SECOND;
    milliseconds %= MILLISECONDS_PER_SECOND;

    int minutes = seconds / SECONDS_PER_MINUTE;
    seconds %= SECONDS_PER_MINUTE;

    int hours = minutes / MINUTES_PER_HOUR;
    minutes %= MINUTES_PER_HOUR;

    int days = hours / HOURS_PER_DAY;
    hours %= HOURS_PER_DAY;

    if (days != 0) {
      if (sb.length() > 0) sb.append(' ');
      sb.append(days);
      sb.append('d');
    }

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

  protected DurationControl () {
    super();
  }
}
