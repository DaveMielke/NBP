package org.nbp.b2g.ui;

import java.util.Map;
import java.util.HashMap;
import org.nbp.common.CacheMap;

import org.nbp.common.InputProcessor;
import org.nbp.common.DirectiveProcessor;

import android.util.Log;
import android.graphics.Color;

public abstract class Colors {
  private final static String LOG_TAG = Colors.class.getName();

  private final static Map<Integer, String> knownColorNames = new HashMap<Integer, String>();
  private final static Map<Integer, String> cachedColorNames = new CacheMap<Integer, String>(0X10);

  private static int normalizedColor (int color) {
    return Color.rgb(
      Color.red(color),
      Color.green(color),
      Color.blue(color)
    );
  }

  private final static int NO_COLOR = normalizedColor(0) - 1;

  private static void addColorName (String name, int color) {
    if (knownColorNames.get(color) == null) {
      knownColorNames.put(color, name);
    }
  }

  private static void addColorName (String name, int red, int green, int blue) {
    addColorName(name, Color.rgb(red, green, blue));
  }

  private static InputProcessor makeX11InputProcessor () {
    DirectiveProcessor directiveProcessor = new DirectiveProcessor();

    directiveProcessor.setUnknownDirectiveHandler(
      new DirectiveProcessor.DirectiveHandler() {
        @Override
        public boolean handleDirective (String[] operands) {
          if (operands.length == 4) {
            String red   = operands[0];
            String green = operands[1];
            String blue  = operands[2];
            String name  = operands[3];

            try {
              addColorName(name,
                Integer.valueOf(red),
                Integer.valueOf(green),
                Integer.valueOf(blue)
              );
            } catch (NumberFormatException exception) {
            }
          }

          return true;
        }
      }
    );

    return directiveProcessor;
  }

  private static void loadX11ColorNames () {
    Log.d(LOG_TAG, "begin loading X11 color names");
    makeX11InputProcessor().processInput("X11.rgb");
    Log.d(LOG_TAG, "end loading X11 color names");
  }

  private static void loadColorNames () {
    loadX11ColorNames();
  }

  private static int square (int value) {
    return value * value;
  }

  private final static int MAXIMUM_INTENSITY = 0XFF;
  private final static int MAXIMUM_DISTANCE = square(MAXIMUM_INTENSITY);

  private static int findNearestColor (int actualColor) {
    int actualRed   = Color.red(actualColor);
    int actualGreen = Color.green(actualColor);
    int actualBlue  = Color.blue(actualColor);

    int nearestColor = NO_COLOR;
    int minimumDistance = MAXIMUM_DISTANCE + 1;

    for (int currentColor : knownColorNames.keySet()) {
      int currentRed   = Color.red(currentColor);
      int currentGreen = Color.green(currentColor);
      int currentBlue  = Color.blue(currentColor);

      int currentDistance = square(currentRed   - actualRed  )
                          + square(currentGreen - actualGreen)
                          + square(currentBlue  - actualBlue )
                          ;

      if (currentDistance < minimumDistance) {
        minimumDistance = currentDistance;
        nearestColor = currentColor;
      }
    }

    return nearestColor;
  }

  public static String getName (int color) {
    color = normalizedColor(color);

    synchronized (knownColorNames) {
      if (knownColorNames.isEmpty()) loadColorNames();

      String name = knownColorNames.get(color);
      if (name != null) return name;

      name = cachedColorNames.get(color);
      if (name != null) return name;

      int nearest = findNearestColor(color);
      if (nearest == NO_COLOR) return "?";

      name = knownColorNames.get(nearest);
      cachedColorNames.put(color, name);
      return name;
    }
  }

  private Colors () {
  }
}
