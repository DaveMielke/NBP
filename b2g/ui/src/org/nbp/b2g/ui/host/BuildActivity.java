package org.nbp.b2g.ui.host;
import org.nbp.b2g.ui.*;

import android.util.Log;
import android.os.Build;

import android.view.View;
import android.widget.TextView;

public class BuildActivity extends ProgrammaticActivity {
  private final static String LOG_TAG = BuildActivity.class.getName();

  private TextView textView;

  @Override
  protected final View createContentView () {
    textView = newTextView();

    return createVerticalGroup(
      textView
    );
  }

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
    startLine(sb, R.string.build_activity_label_android_build);
    sb.append(Build.ID);
  }

  private static void addBuildFirmware (StringBuilder sb) {
    startLine(sb, R.string.build_activity_label_firmware_version);
    sb.append(FirmwareVersion.getMajor());
    sb.append('.');
    sb.append(FirmwareVersion.getMinor());
  }

  @Override
  public void onResume () {
    super.onResume();
    StringBuilder sb = new StringBuilder();

    addBuildProperty(sb, "time", R.string.build_activity_label_ui_time);
    addBuildProperty(sb, "revision", R.string.build_activity_label_ui_revision);
    addBuildSystem(sb);
    addBuildFirmware(sb);

    textView.setText(sb.toString());
  }
}
