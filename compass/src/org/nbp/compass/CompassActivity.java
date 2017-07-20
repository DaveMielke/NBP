package org.nbp.compass;

import java.util.List;
import java.io.IOException;

import org.nbp.common.CommonUtilities;

import android.os.Build;
import android.util.Log;

import android.app.Activity;
import android.os.Bundle;
import android.os.AsyncTask;

import android.widget.TextView;

import android.view.accessibility.AccessibilityManager;
import android.view.accessibility.AccessibilityEvent;

import android.text.TextUtils;

import android.hardware.SensorManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;

import android.location.Geocoder;
import android.location.Address;

public class CompassActivity extends Activity implements SensorEventListener {
  private final static String LOG_TAG = CompassActivity.class.getName();

  private AccessibilityManager accessibilityManager;

  private TextView azimuthDegrees;
  private TextView azimuthDirection;
  private TextView pitchDegrees;
  private TextView rollDegrees;
  private TextView latitudeDecimal;
  private TextView latitudeDMS;
  private TextView longitudeDecimal;
  private TextView longitudeDMS;
  private TextView locationName;

  private final void findViews () {
    azimuthDegrees = (TextView)findViewById(R.id.azimuth_degrees);
    azimuthDirection = (TextView)findViewById(R.id.azimuth_direction);
    pitchDegrees = (TextView)findViewById(R.id.pitch_degrees);
    rollDegrees = (TextView)findViewById(R.id.roll_degrees);
    latitudeDecimal = (TextView)findViewById(R.id.latitude_decimal);
    latitudeDMS = (TextView)findViewById(R.id.latitude_dms);
    longitudeDecimal = (TextView)findViewById(R.id.longitude_decimal);
    longitudeDMS = (TextView)findViewById(R.id.longitude_dms);
    locationName = (TextView)findViewById(R.id.location_name);
  }

  private final void setText (TextView view, CharSequence text) {
    if (!TextUtils.equals(text, view.getText())) {
      view.setText(text);

      if (accessibilityManager != null) {
        if (accessibilityManager.isEnabled()) {
          if (CommonUtilities.haveAndroidSDK(Build.VERSION_CODES.LOLLIPOP)) {
            if (view.isAccessibilityFocused()) {
              AccessibilityEvent event = AccessibilityEvent.obtain();
              view.onInitializeAccessibilityEvent(event);
              event.getText().add(text);
              event.setEventType(AccessibilityEvent.TYPE_ANNOUNCEMENT);
              accessibilityManager.sendAccessibilityEvent(event);
            }
          }
        }
      }
    }
  }

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

  private final float[] rotationMatrix = new float[9];
  private final float[] currentOrientation = new float[3];

  private float[] gravityVector = null;
  private float[] geomagneticVector = null;

  private final Measurement azimuthMeasurement = new Measurement();
  private final Measurement pitchMeasurement = new Measurement();
  private final Measurement rollMeasurement = new Measurement();

  private final static String[] directions = new String[] {
    "n", "nne", "ne", "ene", "e", "ese", "se", "sse",
    "s", "ssw", "sw", "wsw", "w", "wnw", "nw", "nnw"
  };

  private final static int DIRECTION_COUNT = directions.length;
  private final static float DIRECTIONS_PER_CIRCLE = (float)DIRECTION_COUNT;
  private final static float DEGREES_PER_CIRCLE = 360f;
  private final static float DEGREES_PER_DIRECTION = DEGREES_PER_CIRCLE / DIRECTIONS_PER_CIRCLE;

  private final float translateValue (Measurement measurement, float value) {
    measurement.add(value);
    return (float)Math.toDegrees(measurement.get());
  }

  private final void log (String type, float[] vector) {
    if (ApplicationParameters.LOG_VECTORS) {
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
          log("accelerometer", gravityVector);
          break;
        }

        case Sensor.TYPE_MAGNETIC_FIELD: {
          if (geomagneticVector == null) {
            geomagneticVector = new float[count];
          }

          System.arraycopy(values, 0, geomagneticVector, 0, count);
          log("geomagnetic", geomagneticVector);
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

      sensorManager.getOrientation(rotationMatrix, currentOrientation);
      log("rotation", rotationMatrix);
      log("orientation", currentOrientation);

      float azimuth = translateValue(azimuthMeasurement, -currentOrientation[0]);
      float pitch   = translateValue(pitchMeasurement  , -currentOrientation[1]);
      float roll    = translateValue(rollMeasurement   ,  currentOrientation[2]);

      azimuth += DEGREES_PER_CIRCLE;
      azimuth %= DEGREES_PER_CIRCLE;

      azimuthDegrees.setText(String.format("%d°", Math.round(azimuth)));
      pitchDegrees.setText(String.format("%d°", Math.round(pitch)));
      rollDegrees.setText(String.format("%d°", Math.round(roll)));

      int direction = Math.round(azimuth / DEGREES_PER_DIRECTION);

      azimuthDirection.setText(
        String.format(
          "%s%+d°",
          directions[direction % DIRECTION_COUNT],
          Math.round(azimuth - ((float)direction * DEGREES_PER_DIRECTION))
        )
      );
    }
  }

  @Override
  public void onAccuracyChanged (Sensor sensor, int accuracy) {
  }

  private boolean atNewLocation;
  private boolean settingLocationName;
  private Geocoder geocoder;
  private LocationMonitor locationMonitor;

  private final void prepareLocationMonitor () {
    atNewLocation = false;
    settingLocationName = false;

    geocoder = Geocoder.isPresent()? new Geocoder(this): null;
    if (geocoder == null) locationName.setText(R.string.message_unsupported);

    // this must be the very last step because the current location will be set
    locationMonitor = new BestLocationMonitor(this);
  }

  private final String getLocationName (double latitude, double longitude) {
    try {
      List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);

      if (addresses != null) {
        if (!addresses.isEmpty()) {
          Address address = addresses.get(0);
          LocationUtilities.log(address);

          String name = LocationUtilities.getName(address);
          if (name != null) return name;
        }
      }
    } catch (IOException exception) {
      Log.w(LOG_TAG, ("geocoder error: " + exception.getMessage()));
    }

    return null;
  }

  private Double currentLatitude = null;
  private Double currentLongitude = null;

  private final boolean isNearCurrentLocation (double latitude, double longitude) {
    synchronized (this) {
      if (currentLatitude == null) return false;
      if (currentLongitude == null) return false;
      return EarthMath.haversineDistance(currentLatitude, currentLongitude, latitude, longitude)
           < ApplicationParameters.CURRENT_LOCATION_RADIUS;
    }
  }

  private final void setLocationName () {
    synchronized (this) {
      if (!settingLocationName) {
        settingLocationName = true;

        new AsyncTask<Void, String, Void>() {
          @Override
          protected void onProgressUpdate (String... arguments) {
            setText(locationName, arguments[0]);
          }

          @Override
          protected Void doInBackground (Void... arguments) {
            while (true) {
              double latitude;
              double longitude;

              synchronized (CompassActivity.this) {
                if (!atNewLocation) {
                  settingLocationName = false;
                  return null;
                }

                latitude = currentLatitude;
                longitude = currentLongitude;
                atNewLocation = false;
              }

              String name = getLocationName(latitude, longitude);
              if (name == null) name = "";
              publishProgress(name);
            }
          }
        }.execute();
      }
    }
  }

  private final void setLocationName (double latitude, double longitude) {
    if (geocoder != null) {
      if (!isNearCurrentLocation(latitude, longitude)) {
        synchronized (this) {
          currentLatitude = latitude;
          currentLongitude = longitude;
          atNewLocation = true;
          setLocationName();
        }
      }
    }
  }

  private final void setLocationCoordinate (
    double degrees,
    TextView decimal, TextView dms,
    char positive, char negative
  ) {
    decimal.setText(String.format("%.5f°", degrees));

    char hemisphere;

    if (degrees < 0f) {
      hemisphere = negative;
      degrees = -degrees;
    } else {
      hemisphere = positive;
    }

    long value = Math.round(degrees * 3600f);
    if (value == 0) hemisphere = ' ';
    StringBuilder sb = new StringBuilder();

    sb.append(value % 60);
    sb.append('"');
    value /= 60;

    if (value > 0) {
      sb.insert(0, "' ");
      sb.insert(0, value%60);
      value /= 60;

      if (value > 0) {
        sb.insert(0, "° ");
        sb.insert(0, value);
      }
    }

    if (hemisphere != ' ') {
      sb.append(' ');
      sb.append(hemisphere);
    }

    dms.setText(sb.toString());
  }

  public final void setLocation (double latitude, double longitude) {
    setLocationCoordinate(latitude, latitudeDecimal, latitudeDMS, 'N', 'S');
    setLocationCoordinate(longitude, longitudeDecimal, longitudeDMS, 'E', 'W');
    setLocationName(latitude, longitude);
  }

  @Override
  public void onCreate (Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    accessibilityManager = (AccessibilityManager)getSystemService(ACCESSIBILITY_SERVICE);

    setContentView(R.layout.compass);
    findViews();

    prepareSensorMonitor();
    prepareLocationMonitor();
  }

  @Override
  protected void onResume () {
    super.onResume();
//  startSensors();
    locationMonitor.start();
  }

  @Override
  protected void onPause () {
    try {
      stopSensors();
      locationMonitor.stop();
    } finally {
      super.onPause();
    }
  }
}
