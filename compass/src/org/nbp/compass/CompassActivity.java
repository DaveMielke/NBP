package org.nbp.compass;

import java.util.Map;
import java.util.HashMap;

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

  private SensorManager sensorManager;
  private TextView headingDegrees;
  private TextView headingDirection;
  private TextView pitchDegrees;
  private TextView rollDegrees;
  private TextView accuracyName;

  @Override
  public void onCreate (Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);

    setContentView(R.layout.compass);
    headingDegrees = (TextView)findViewById(R.id.heading_degrees);
    headingDirection = (TextView)findViewById(R.id.heading_direction);
    pitchDegrees = (TextView)findViewById(R.id.pitch_degrees);
    rollDegrees = (TextView)findViewById(R.id.roll_degrees);
    accuracyName = (TextView)findViewById(R.id.accuracy_name);

    addAccuracy(SensorManager.SENSOR_STATUS_UNRELIABLE, R.string.accuracy_unreliable);
    addAccuracy(SensorManager.SENSOR_STATUS_ACCURACY_LOW, R.string.accuracy_low);
    addAccuracy(SensorManager.SENSOR_STATUS_ACCURACY_MEDIUM, R.string.accuracy_medium);
    addAccuracy(SensorManager.SENSOR_STATUS_ACCURACY_HIGH, R.string.accuracy_high);
  }

  @Override
  protected void onResume () {
    super.onResume();

    sensorManager.registerListener(this,
      sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
      R.integer.frequency_usecs
    );
  }

  @Override
  protected void onPause () {
    super.onPause();

    sensorManager.unregisterListener(this);
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
    float heading = event.values[0];
    float pitch   = event.values[1];
    float roll    = event.values[2];

    String accuracy = accuracyNames.get(event.accuracy);
    if (accuracy == null) accuracy = getString(R.string.accuracy_unknown);

    if ((heading >= 0f) && (heading < DEGREES_PER_CIRCLE)) {
      int direction = Math.round(heading / DEGREES_PER_DIRECTION);
      direction %= DIRECTION_COUNT;

      accuracyName.setText(accuracy);
      headingDegrees.setText(String.format("%d°", Math.round(heading)));
      pitchDegrees.setText(String.format("%d°", Math.round(pitch)));
      rollDegrees.setText(String.format("%d°", Math.round(roll)));

      headingDirection.setText(
        String.format(
          "%s%+d°",
          directions[direction],
          Math.round(heading - ((float)direction * DEGREES_PER_DIRECTION))
        )
      );
    }
  }

  @Override
  public void onAccuracyChanged (Sensor sensor, int accuracy) {
  }
}
