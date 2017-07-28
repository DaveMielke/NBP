package org.nbp.compass;

import org.nbp.common.DialogFinisher;
import org.nbp.common.DialogHelper;

import android.util.Log;
import android.os.Bundle;

import android.content.Intent;

import android.view.Menu;
import android.view.MenuItem;

import android.hardware.SensorManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;

public class NavigationActivity extends BaseActivity implements SensorEventListener {
  private final static String LOG_TAG = NavigationActivity.class.getName();

  private final static int[] sensorTypes = new int[] {
    Sensor.TYPE_ACCELEROMETER,
    Sensor.TYPE_MAGNETIC_FIELD
  };

  private SensorManager sensorManager;
  private Sensor[] sensorArray;

  private final void prepareSensorMonitor () {
    sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
    sensorArray = new Sensor[sensorTypes.length];

    {
      int count = 0;

      for (int type : sensorTypes) {
        Sensor sensor = sensorManager.getDefaultSensor(type);

        if ((sensorArray[count++] = sensor) != null) {
          Log.i(LOG_TAG, String.format(
            "Sensor: Name:%s Type:%d Vendor:%s",
            sensor.getName(), sensor.getType(), sensor.getVendor()
          ));
        }
      }
    }
  }

  private final void startSensors () {
    for (Sensor sensor : sensorArray) {
      if (sensor != null) {
        sensorManager.registerListener(this, sensor, ApplicationParameters.SENSOR_UPDATE_INTERVAL);
      }
    }
  }

  private final void stopSensors () {
    for (Sensor sensor : sensorArray) {
      if (sensor != null) {
        sensorManager.unregisterListener(this, sensor);
      }
    }
  }

  private float[] gravityVector = null;
  private float[] geomagneticVector = null;

  private final float[] rotationMatrix = new float[9];
  private final float[] currentOrientation = new float[3];

  private final void log (String type, float[] vector) {
    if (ApplicationSettings.LOG_SENSORS) {
      StringBuilder sb = new StringBuilder();
      sb.append(type);
      char delimiter = ':';

      for (float value : vector) {
        sb.append(delimiter);
        delimiter = ',';

        sb.append(' ');
        sb.append(value);
      }

      Log.d(LOG_TAG, sb.toString());
    }
  }

  private final float translateOrientationAngle (float radians) {
    return (float)Math.toDegrees((double)radians);
  }

  private final void setOrientationFields () {
    float heading = translateOrientationAngle( currentOrientation[0]);
    float pitch   = translateOrientationAngle(-currentOrientation[1]);
    float roll    = translateOrientationAngle( currentOrientation[2]);

    {
      ScreenOrientation.Conversions conversions = ApplicationSettings.SCREEN_ORIENTATION.getConversions(this);

      if (conversions != null) {
        float h = heading;
        float p = pitch;
        float r = roll;

        heading = conversions.getHeading(h);
        pitch = conversions.getPitch(p, r);
        roll = conversions.getRoll(p, r);
      }
    }

    heading = ApplicationUtilities.toHeading(heading);
    setOrientationHeading(heading);

    setOrientationPitch(pitch);
    setOrientationRoll(roll);
  }

  private final DelayedAction orientationUpdater = new DelayedAction(
    ApplicationParameters.UPDATE_MINIMUM_TIME, "update-orientation"
  );

  @Override
  public void onSensorChanged (SensorEvent event) {
    {
      float[] values = event.values;
      int count = values.length;

      switch (event.sensor.getType()) {
        case Sensor.TYPE_ACCELEROMETER:
          if (gravityVector == null) gravityVector = new float[count];
          System.arraycopy(values, 0, gravityVector, 0, count);
          log("accelerometer", gravityVector);
          break;

        case Sensor.TYPE_MAGNETIC_FIELD:
          if (geomagneticVector == null) geomagneticVector = new float[count];
          System.arraycopy(values, 0, geomagneticVector, 0, count);
          log("geomagnetic", geomagneticVector);
          break;

        default:
          return;
      }
    }

    if ((gravityVector != null) && (geomagneticVector != null)) {
      boolean success = sensorManager.getRotationMatrix(
        rotationMatrix, null,
        gravityVector, geomagneticVector
      );

      if (success) {
        log("rotation", rotationMatrix);

        sensorManager.getOrientation(rotationMatrix, currentOrientation);
        log("orientation", currentOrientation);

        orientationUpdater.setAction(
          new Runnable() {
            @Override
            public void run () {
              runOnUiThread(
                new Runnable() {
                  @Override
                  public void run () {
                    setOrientationFields();
                  }
                }
              );
            }
          }
        );
      }
    }
  }

  @Override
  public void onAccuracyChanged (Sensor sensor, int accuracy) {
  }

  private final void menuAction_settings () {
    Intent intent = new Intent(this, SettingsActivity.class);
    startActivity(intent);
  }

  private final void menuAction_about () {
    DialogFinisher finisher = new DialogFinisher() {
      @Override
      public void finishDialog (DialogHelper helper) {
        helper.setText(R.id.about_version_number, R.string.NBP_Compass_version_name);
        helper.setText(R.id.about_build_time, R.string.NBP_Compass_build_time);
        helper.setText(R.id.about_source_revision, R.string.NBP_Compass_source_revision);
        helper.setTextFromAsset(R.id.about_copyright, "copyright");
      }
    };

    showDialog(R.string.menu_about, R.layout.about, finisher);
  }

  @Override
  public boolean onOptionsItemSelected (MenuItem item) {
    int identifier = item.getItemId();

    switch (identifier) {
      case R.id.menu_settings:
        menuAction_settings();
        return true;

      case R.id.menu_about:
        menuAction_about();
        return true;
    }

    String name = getResources().getResourceEntryName(identifier);
    if (name == null) name = Integer.toString(identifier);
    Log.w(LOG_TAG, ("unhandled menu action: " + name));
    return false;
  }

  @Override
  public boolean onCreateOptionsMenu (Menu menu) {
    getMenuInflater().inflate(R.menu.options, menu);
    return true;
  }

  private static NavigationActivity navigationActivity = null;

  public final static NavigationActivity getNavigationActivity () {
    return navigationActivity;
  }

  @Override
  protected void onCreate (Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    navigationActivity = this;

    setContentView(R.layout.navigation);
    finishBaseActivityCreation();

    prepareSensorMonitor();
    Controls.restore();
  }

  @Override
  protected void onDestroy () {
    try {
      navigationActivity = null;
    } finally {
      super.onDestroy();
    }
  }

  @Override
  protected void onResume () {
    super.onResume();
    LocationMonitor.startCurrentMonitor();
    startSensors();
  }

  @Override
  protected void onPause () {
    try {
      stopSensors();
      LocationMonitor.stopCurrentMonitor();
    } finally {
      super.onPause();
    }
  }
}
