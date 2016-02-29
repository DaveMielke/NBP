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
  private TextView headingView;

  @Override
  public void onCreate (Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);

    setContentView(R.layout.compass);
    headingView = (TextView)findViewById(R.id.heading);

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

      headingView.setText(String.format(
        "%s: %d° [%s%+d]\n%s: %d°\n%s: %d°\n%s: %s",
        getString(R.string.label_heading), Math.round(heading),
        directions[direction],
        Math.round(heading - ((float)direction * DEGREES_PER_DIRECTION)),
        getString(R.string.label_pitch), Math.round(pitch),
        getString(R.string.label_roll), Math.round(roll),
        getString(R.string.label_accuracy), accuracy
      ));
    }
  }

  @Override
  public void onAccuracyChanged (Sensor sensor, int accuracy) {
  }
}
