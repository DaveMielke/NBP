package org.nbp.b2g.ui.host;
import org.nbp.b2g.ui.*;

import java.util.Date;
import java.util.TimeZone;
import java.text.SimpleDateFormat;

import android.util.Log;
import android.os.Build;

import android.view.View;
import android.widget.GridLayout;

public class BuildActivity extends ProgrammaticActivity {
  private final static String LOG_TAG = BuildActivity.class.getName();

  private GridLayout buildDetails;
  private int rowIndex = 0;

  private void setColumn (int row, int column, CharSequence text) {
    GridLayout grid = buildDetails;

    grid.addView(
      newTextView(text),
      new GridLayout.LayoutParams(grid.spec(row), grid.spec(column))
    );
  }

  private void addBuildDetail (String label, CharSequence value) {
    if (value != null) {
      if (value.length() > 0) {
        setColumn(rowIndex, 0, label);
        setColumn(rowIndex, 1, value);
        rowIndex += 1;
      }
    }
  }

  private void addBuildDetail (int label, CharSequence value) {
    addBuildDetail(getString(label), value);
  }

  private void addSystemProperty (int label, String property) {
    addBuildDetail(label, System.getProperty(property));
  }

  private boolean addUserInterfaceProperty (String property, final int... labels) {
    InputProcessor inputProcessor = new InputProcessor() {
      @Override
      protected final boolean handleLine (String text, int number) {
        int label = labels[number];
        text = text.trim();

        if (!text.isEmpty()) {
          addBuildDetail(label, text);
        } else {
          Log.w(LOG_TAG, "build property not available: " + label);
        }

        return number < (labels.length - 1);
      }
    };

    return inputProcessor.processInput(("build." + property));
  }

  private final static String TIME_FORMAT = "yyyy-MM-dd@HH:mm zzz";
  private final static String TIME_ZONE = "UTC";
  private final static SimpleDateFormat dateFormatter = new SimpleDateFormat(TIME_FORMAT);

  static {
    dateFormatter.setTimeZone(TimeZone.getTimeZone(TIME_ZONE));
  }

  private void addTimeDetail (int label, long time) {
    addBuildDetail(label, dateFormatter.format(time));
  }

  @Override
  protected final View createContentView () {
    buildDetails = new GridLayout(this);
    buildDetails.setOrientation(buildDetails.VERTICAL);

    addUserInterfaceProperty("revision", R.string.build_activity_label_ui_revision);
    addUserInterfaceProperty("time", R.string.build_activity_label_ui_time);

    addBuildDetail(R.string.build_activity_label_android_build, Build.ID);
    addTimeDetail(R.string.build_activity_label_android_time, Build.TIME);
    addBuildDetail(R.string.build_activity_label_android_type, Build.TYPE);

    addSystemProperty(R.string.build_activity_label_linux_version, "os.version");

    addBuildDetail(
      R.string.build_activity_label_firmware_version, 
      String.format("%d.%d",
        FirmwareVersion.getMajor(),
        FirmwareVersion.getMinor()
      )
    );

    addBuildDetail(
      R.string.build_activity_label_metec_version,
      Devices.braille.get().getDriverVersion()
    );

    return createVerticalGroup(
      buildDetails
    );
  }
}
