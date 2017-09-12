package org.nbp.navigator;

import android.util.Log;
import android.content.Context;

import android.hardware.SensorManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;

public class SensorOrientationMonitor extends OrientationMonitor implements SensorEventListener {
  private final static String LOG_TAG = SensorOrientationMonitor.class.getName();

  private final static int[] sensorTypes = new int[] {
    Sensor.TYPE_ACCELEROMETER,
    Sensor.TYPE_MAGNETIC_FIELD
  };

  private SensorManager sensorManager;
  private Sensor[] sensorArray;

  public SensorOrientationMonitor () {
    super();

    Context context = getContext();
    sensorManager = (SensorManager)context.getSystemService(Context.SENSOR_SERVICE);
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

  @Override
  protected final boolean startProvider () {
    for (Sensor sensor : sensorArray) {
      if (sensor != null) {
        sensorManager.registerListener(this, sensor, ApplicationParameters.SENSOR_UPDATE_INTERVAL);
      }
    }

    return true;
  }

  @Override
  protected final void stopProvider () {
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

  private final void setOrientation () {
    float heading = translateOrientationAngle( currentOrientation[0]);
    float pitch   = translateOrientationAngle(-currentOrientation[1]);
    float roll    = translateOrientationAngle( currentOrientation[2]);

    {
      ScreenOrientation.Conversions conversions = ApplicationSettings.SCREEN_ORIENTATION.getConversions(getContext());

      if (conversions != null) {
        float h = heading;
        float p = pitch;
        float r = roll;

        heading = conversions.getHeading(h);
        pitch = conversions.getPitch(p, r);
        roll = conversions.getRoll(p, r);
      }
    }

    heading = ApplicationUtilities.toUnsignedAngle(heading);
    setOrientation(heading, pitch, roll);
  }

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

        setOrientation();
      }
    }
  }

  @Override
  public void onAccuracyChanged (Sensor sensor, int accuracy) {
  }
}
