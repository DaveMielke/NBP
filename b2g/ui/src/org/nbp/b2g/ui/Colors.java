package org.nbp.b2g.ui;

import java.util.Map;
import java.util.HashMap;

import org.nbp.common.InputProcessor;
import org.nbp.common.DirectiveProcessor;

import android.util.Log;
import android.graphics.Color;

public abstract class Colors {
  private final static String LOG_TAG = Colors.class.getName();

  private final static Map<Integer, String> colorNames = new HashMap<Integer, String>();

  private static void addColorName (String name, Integer color) {
    if (colorNames.get(color) == null) colorNames.put(color, name);
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

            addColorName(name,
              Integer.valueOf(red),
              Integer.valueOf(green),
              Integer.valueOf(blue)
            );
          }

          return true;
        }
      }
    );

    return directiveProcessor;
  }

  private static void loadX11Names () {
    Log.d(LOG_TAG, "begin loading X11 color names");
    makeX11InputProcessor().processInput("X11.rgb");
    Log.d(LOG_TAG, "end loading X11 color names");
  }

  private static void loadColorNames () {
    loadX11Names();
  }

  private static int square (int value) {
    return value * value;
  }

  private static String findNearestColor (int actualColor) {
    int actualRed = Color.red(actualColor);
    int actualGreen = Color.green(actualColor);
    int actualBlue = Color.blue(actualColor);

    String colorName = null;
    int minimumDistance = (square(0XFF) * 3) + 1;

    for (int currentColor : colorNames.keySet()) {
      int currentRed = Color.red(currentColor);
      int currentGreen = Color.green(currentColor);
      int currentBlue = Color.blue(currentColor);

      int currentDistance = square(currentRed   - actualRed  )
                          + square(currentGreen - actualGreen)
                          + square(currentBlue  - actualBlue )
                          ;

      if (currentDistance < minimumDistance) {
        minimumDistance = currentDistance;
        colorName = colorNames.get(currentColor);
      }
    }

    if (colorName == null) colorName = "unknown";
    return colorName;
  }

  public static String getName (int color) {
    synchronized (colorNames) {
      if (colorNames.isEmpty()) loadColorNames();
      return findNearestColor(color);
    }
  }

  private Colors () {
  }
}
