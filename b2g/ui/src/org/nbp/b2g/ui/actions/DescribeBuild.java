package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

import android.util.Log;

public class DescribeBuild extends Action {
  private final static String LOG_TAG = DescribeBuild.class.getName();

  private static boolean addBuildProperty (
    final StringBuilder sb, String property, final int... labels
  ) {
    InputProcessor inputProcessor = new InputProcessor() {
      @Override
      protected final boolean processLine (String text, int number) {
        String label = ApplicationContext.getString(labels[number]);
        text = text.trim();

        if (!text.isEmpty()) {
          if (sb.length() > 0) sb.append('\n');
          sb.append(label);
          sb.append(": ");
          sb.append(text);
        } else {
          Log.w(LOG_TAG, "build property not available: " + label);
        }

        return number < (labels.length - 1);
      }
    };

    return inputProcessor.processInput(("build." + property));
  }

  @Override
  public boolean performAction () {
    StringBuilder sb = new StringBuilder();
    addBuildProperty(sb, "time", R.string.build_property_time);
    addBuildProperty(sb, "revision", R.string.build_property_revision);

    if (sb.length() == 0) return false;
    Endpoints.setPopupEndpoint(sb.toString());
    return true;
  }

  public DescribeBuild (Endpoint endpoint) {
    super(endpoint, false);
  }
}
