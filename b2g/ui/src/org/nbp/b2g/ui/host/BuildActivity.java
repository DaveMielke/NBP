package org.nbp.b2g.ui.host;
import org.nbp.b2g.ui.*;

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

  private void addBuildDetail (int label, CharSequence value) {
    setColumn(rowIndex, 0, getString(label));
    setColumn(rowIndex, 1, value);
    rowIndex += 1;
  }

  private boolean addBuildProperty (String property, final int... labels) {
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

  @Override
  protected final View createContentView () {
    buildDetails = new GridLayout(this);
    buildDetails.setOrientation(buildDetails.VERTICAL);

    addBuildProperty("time", R.string.build_activity_label_ui_time);
    addBuildProperty("revision", R.string.build_activity_label_ui_revision);

    addBuildDetail(R.string.build_activity_label_android_build, Build.ID);

    addBuildDetail(
      R.string.build_activity_label_firmware_version, 
      String.format("%d.%d",
        FirmwareVersion.getMajor(),
        FirmwareVersion.getMinor()
      )
    );

    return createVerticalGroup(
      buildDetails
    );
  }
}
