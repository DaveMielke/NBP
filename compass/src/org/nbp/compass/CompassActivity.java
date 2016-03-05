package org.nbp.compass;

import java.util.Map;
import java.util.HashMap;
import static java.lang.Math.toDegrees;

import android.util.Log;

import android.app.Activity;
import android.os.Bundle;

import android.widget.TextView;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class CompassActivity extends Activity implements SensorEventListener {
  private final static String LOG_TAG = CompassActivity.class.getName();

  private final Map<Integer, String> accuracyNames = new HashMap<Integer, String>();

  private final void addAccuracy (int value, int name) {
    accuracyNames.put(value, getString(name));
  }

  private TextView azimuthDegrees;
  private TextView azimuthDirection;
  private TextView pitchDegrees;
  private TextView rollDegrees;
  private TextView accuracyName;

  private final static int[] sensorTypes = new int[] {
    Sensor.TYPE_ACCELEROMETER,
    Sensor.TYPE_MAGNETIC_FIELD
  };

  private SensorManager sensorManager;
  private final Sensor[] sensorArray = new Sensor[sensorTypes.length];
  private final float[] rotationMatrix = new float[9];
  private final float[] deviceOrientation = new float[3];

  private float[] gravityVector = null;
  private float[] geomagneticVector = null;

  @Override
  public void onCreate (Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.compass);
    azimuthDegrees = (TextView)findViewById(R.id.azimuth_degrees);
    azimuthDirection = (TextView)findViewById(R.id.azimuth_direction);
    pitchDegrees = (TextView)findViewById(R.id.pitch_degrees);
    rollDegrees = (TextView)findViewById(R.id.roll_degrees);
    accuracyName = (TextView)findViewById(R.id.accuracy_name);

    addAccuracy(SensorManager.SENSOR_STATUS_UNRELIABLE, R.string.accuracy_unreliable);
    addAccuracy(SensorManager.SENSOR_STATUS_ACCURACY_LOW, R.string.accuracy_low);
    addAccuracy(SensorManager.SENSOR_STATUS_ACCURACY_MEDIUM, R.string.accuracy_medium);
    addAccuracy(SensorManager.SENSOR_STATUS_ACCURACY_HIGH, R.string.accuracy_high);

    sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);

    {
      int count = 0;

      for (int type : sensorTypes) {
        sensorArray[count++] = sensorManager.getDefaultSensor(type);
      }
    }
  }

  @Override
  protected void onResume () {
    super.onResume();

    for (Sensor sensor : sensorArray) {
      sensorManager.registerListener(this, sensor, R.integer.frequency_usecs);
    }
  }

  @Override
  protected void onPause () {
    super.onPause();

    for (Sensor sensor : sensorArray) {
      sensorManager.unregisterListener(this, sensor);
    }
  }

  private final static String[] directions = new String[] {
    "n", "nne", "ne", "ene", "e", "ese", "se", "sse",
    "s", "ssw", "sw", "wsw", "w", "wnw", "nw", "nnw"
  };

  private final static int DIRECTION_COUNT = directions.length;
  private final static float DIRECTIONS_PER_CIRCLE = (float)DIRECTION_COUNT;
  private final static float DEGREES_PER_CIRCLE = 360f;
  private final static float DEGREES_PER_DIRECTION = DEGREES_PER_CIRCLE / DIRECTIONS_PER_CIRCLE;

  @Override
  public void onSensorChanged (SensorEvent event) {
    {
      float[] values = event.values;
      int count = values.length;

      switch (event.sensor.getType()) {
        case Sensor.TYPE_ACCELEROMETER: {
          if (gravityVector == null) {
            gravityVector = new float[count];
          }

          System.arraycopy(values, 0, gravityVector, 0, count);
          break;
        }

        case Sensor.TYPE_MAGNETIC_FIELD: {
          if (geomagneticVector == null) {
            geomagneticVector = new float[count];
          }

          System.arraycopy(values, 0, geomagneticVector, 0, count);
          break;
        }

        default:
          return;
      }
    }

    if ((gravityVector != null) && (geomagneticVector != null)) {
      sensorManager.getRotationMatrix(
        rotationMatrix, null,
        gravityVector, geomagneticVector
      );

      sensorManager.getOrientation(rotationMatrix, deviceOrientation);
      float azimuth = -(float)toDegrees(deviceOrientation[0]);
      float pitch   = -(float)toDegrees(deviceOrientation[1]);
      float roll    = -(float)toDegrees(deviceOrientation[2]);

      azimuthDegrees.setText(String.format("%d°", Math.round(azimuth)));
      pitchDegrees.setText(String.format("%d°", Math.round(pitch)));
      rollDegrees.setText(String.format("%d°", Math.round(roll)));

      int direction = Math.round(azimuth / DEGREES_PER_DIRECTION);
      direction += DIRECTION_COUNT;
      direction %= DIRECTION_COUNT;

      azimuthDirection.setText(
        String.format(
          "%s%+d°",
          directions[direction],
          Math.round(azimuth - ((float)direction * DEGREES_PER_DIRECTION))
        )
      );
    }
  }

  @Override
  public void onAccuracyChanged (Sensor sensor, int accuracy) {
  }
}
