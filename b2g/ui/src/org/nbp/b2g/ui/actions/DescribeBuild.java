package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

import android.util.Log;
import android.os.Build;

public class DescribeBuild extends Action {
  private final static String LOG_TAG = DescribeBuild.class.getName();

  private static void appendString (StringBuilder sb, int string) {
    sb.append(ApplicationContext.getString(string));
  }

  private static void startLine (StringBuilder sb, int label) {
    if (sb.length() > 0) sb.append('\n');
    appendString(sb, label);
    sb.append(": ");
  }

  private static boolean addBuildProperty (
    final StringBuilder sb, String property, final int... labels
  ) {
    InputProcessor inputProcessor = new InputProcessor() {
      @Override
      protected final boolean handleLine (String text, int number) {
        int label = labels[number];
        text = text.trim();

        if (!text.isEmpty()) {
          startLine(sb, label);
          sb.append(text);
        } else {
          Log.w(LOG_TAG, "build property not available: " + label);
        }

        return number < (labels.length - 1);
      }
    };

    return inputProcessor.processInput(("build." + property));
  }

  private static void addBuildSystem (StringBuilder sb) {
    startLine(sb, R.string.DescribeBuild_label_system);
    sb.append(Build.ID);
  }

  private static void addBuildFirmware (StringBuilder sb) {
    startLine(sb, R.string.DescribeBuild_label_firmware);
    sb.append(FirmwareVersion.getMajor());
    sb.append('.');
    sb.append(FirmwareVersion.getMinor());
  }

  @Override
  public boolean performAction () {
    StringBuilder sb = new StringBuilder();
    appendString(sb, R.string.DescribeBuild_title);

    addBuildProperty(sb, "time", R.string.DescribeBuild_label_time);
    addBuildProperty(sb, "revision", R.string.DescribeBuild_label_revision);
    addBuildSystem(sb);
    addBuildFirmware(sb);

    if (sb.length() == 0) return false;
    Endpoints.setPopupEndpoint(sb.toString());
    return true;
  }

  public DescribeBuild (Endpoint endpoint) {
    super(endpoint, false);
  }
}
