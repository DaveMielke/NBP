package org.nbp.b2g.ui.host;
import org.nbp.b2g.ui.*;

import java.util.Date;
import java.util.TimeZone;
import java.text.SimpleDateFormat;

import android.util.Log;
import android.os.Build;

import android.view.View;
import android.widget.GridLayout;

import org.nbp.common.ProgrammaticActivity;

public class BuildDetailsActivity extends ProgrammaticActivity {
  private final static String LOG_TAG = BuildDetailsActivity.class.getName();

  private GridLayout buildDetails;
  private int rowIndex = 0;

  private void setColumn (int row, int column, CharSequence text) {
    GridLayout grid = buildDetails;

    grid.addView(
      newTextView(text),
      new GridLayout.LayoutParams(grid.spec(row), grid.spec(column))
    );
  }

  private void addDetail (String label, CharSequence value) {
    if (value != null) {
      if (value.length() > 0) {
        setColumn(rowIndex, 0, label);
        setColumn(rowIndex, 1, value);
        rowIndex += 1;
      }
    }
  }

  private void addDetail (int label, CharSequence value) {
    addDetail(getString(label), value);
  }

  private void addDetail (int label, int value) {
    addDetail(label, getString(value));
  }

  private void addSystemProperty (int label, String property) {
    addDetail(label, System.getProperty(property));
  }

  private void addAndroidBuildField (int label, String value) {
    if (!Build.UNKNOWN.equals(value)) {
      addDetail(label, value);
    }
  }

  private final static String TIME_FORMAT = "yyyy-MM-dd@HH:mm zzz";
  private final static String TIME_ZONE = "UTC";
  private final static SimpleDateFormat dateFormatter = new SimpleDateFormat(TIME_FORMAT);

  static {
    dateFormatter.setTimeZone(TimeZone.getTimeZone(TIME_ZONE));
  }

  private void addTime (int label, long time) {
    addDetail(label, dateFormatter.format(time));
  }

  @Override
  protected final View createContentView () {
    buildDetails = new GridLayout(this);
    buildDetails.setOrientation(buildDetails.VERTICAL);

    addAndroidBuildField(R.string.build_activity_label_hardware_serial, Build.SERIAL);

    addDetail(R.string.build_activity_label_ui_version, R.string.B2G_UI_version_name);
    addDetail(R.string.build_activity_label_ui_revision, R.string.B2G_UI_source_revision);
    addDetail(R.string.build_activity_label_ui_time, R.string.B2G_UI_build_time);

    addAndroidBuildField(R.string.build_activity_label_android_version, Build.VERSION.RELEASE);
    addAndroidBuildField(R.string.build_activity_label_android_build, Build.ID);
    addTime(R.string.build_activity_label_android_time, Build.TIME);
    addAndroidBuildField(R.string.build_activity_label_android_type, Build.TYPE);

    addSystemProperty(R.string.build_activity_label_linux_version, "os.version");

    addDetail(
      R.string.build_activity_label_firmware_main,
      String.format("%d.%d",
        FirmwareVersion.getMainMajor(),
        FirmwareVersion.getMainMinor()
      )
    );

    addDetail(
      R.string.build_activity_label_firmware_base,
      String.format("%d.%d",
        FirmwareVersion.getBaseMajor(),
        FirmwareVersion.getBaseMinor()
      )
    );

    addDetail(
      R.string.build_activity_label_metec_version,
      Devices.braille.get().getDriverVersion()
    );

    addAndroidBuildField(R.string.build_activity_label_radio_version, Build.getRadioVersion());
    addAndroidBuildField(R.string.build_activity_label_bootloader_version, Build.BOOTLOADER);

    return newVerticalGroup(
      buildDetails
    );
  }
}
