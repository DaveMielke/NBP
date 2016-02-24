package org.nbp.compass;

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

  private SensorManager sensorManager;
  private TextView headingView;

  @Override
  public void onCreate (Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);

    setContentView(R.layout.compass);
    headingView = (TextView)findViewById(R.id.heading);
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
    float degrees = event.values[0];

    if ((degrees >= 0f) && (degrees < DEGREES_PER_CIRCLE)) {
      int direction = Math.round(degrees / DEGREES_PER_DIRECTION);
      direction %= DIRECTION_COUNT;

      headingView.setText(String.format(
        "%s%+d [%d]",
        directions[direction],
        Math.round(degrees - ((float)direction * DEGREES_PER_DIRECTION)),
        Math.round(degrees)
      ));
    }
  }

  @Override
  public void onAccuracyChanged (Sensor sensor, int accuracy) {
  }
}
