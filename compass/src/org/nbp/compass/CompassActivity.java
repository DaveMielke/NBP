package org.nbp.compass;

import java.util.List;
import java.io.IOException;

import org.nbp.common.CommonActivity;
import org.nbp.common.CommonUtilities;

import android.os.Build;
import android.util.Log;

import android.os.Bundle;
import android.os.AsyncTask;
import android.content.Intent;

import android.view.View;
import android.widget.TextView;

import android.view.accessibility.AccessibilityManager;
import android.view.accessibility.AccessibilityEvent;

import android.text.TextUtils;

import android.hardware.SensorManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;

import android.location.Location;
import android.location.Address;
import android.location.Geocoder;

public class CompassActivity extends CommonActivity implements SensorEventListener {
  private final static String LOG_TAG = CompassActivity.class.getName();

  private AccessibilityManager accessibilityManager;

  private TextView latitudeDecimal;
  private TextView latitudeDMS;
  private TextView longitudeDecimal;
  private TextView longitudeDMS;
  private TextView altitudeMagnitude;
  private TextView locationName;
  private TextView distanceMagnitude;
  private TextView directionDegrees;
  private TextView directionPoint;
  private TextView speedMagnitude;
  private TextView bearingDegrees;
  private TextView bearingPoint;
  private TextView azimuthDegrees;
  private TextView azimuthPoint;
  private TextView pitchDegrees;
  private TextView rollDegrees;

  private final void findViews () {
    latitudeDecimal = (TextView)findViewById(R.id.latitude_decimal);
    latitudeDMS = (TextView)findViewById(R.id.latitude_dms);
    longitudeDecimal = (TextView)findViewById(R.id.longitude_decimal);
    longitudeDMS = (TextView)findViewById(R.id.longitude_dms);
    altitudeMagnitude = (TextView)findViewById(R.id.altitude_magnitude);
    locationName = (TextView)findViewById(R.id.location_name);
    distanceMagnitude = (TextView)findViewById(R.id.distance_magnitude);
    directionDegrees = (TextView)findViewById(R.id.direction_degrees);
    directionPoint = (TextView)findViewById(R.id.direction_point);
    speedMagnitude = (TextView)findViewById(R.id.speed_magnitude);
    bearingDegrees = (TextView)findViewById(R.id.bearing_degrees);
    bearingPoint = (TextView)findViewById(R.id.bearing_point);
    azimuthDegrees = (TextView)findViewById(R.id.azimuth_degrees);
    azimuthPoint = (TextView)findViewById(R.id.azimuth_point);
    pitchDegrees = (TextView)findViewById(R.id.pitch_degrees);
    rollDegrees = (TextView)findViewById(R.id.roll_degrees);
  }

  private final void setText (TextView view, CharSequence text) {
    if (!TextUtils.equals(text, view.getText())) {
      view.setText(text);

      if (text.length() == 0) {
        CharSequence hint = view.getHint();

        if (hint != null) {
          if (hint.length() > 0) {
            text = hint;
          }
        }
      }

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

  private final void setText (TextView view, int resource) {
    setText(view, getString(resource));
  }

  private final void setText (TextView view) {
    setText(view, "");
  }

  private final void setBearing (TextView view, float degrees) {
    setText(view, ApplicationUtilities.toBearingString(degrees));
  }

  private final void setCoordinate (TextView view, double degrees) {
    setText(view, ApplicationUtilities.toCoordinateString(degrees));
  }

  private final void setLatitude (TextView view, double degrees) {
    setText(view, ApplicationUtilities.toLatitudeString(degrees));
  }

  private final void setLongitude (TextView view, double degrees) {
    setText(view, ApplicationUtilities.toLongitudeString(degrees));
  }

  private final void setPoint (TextView view, float degrees) {
    setText(view, ApplicationUtilities.toPointString(degrees));
  }

  private final void setDistance (TextView view, double distance) {
    setText(view, ApplicationUtilities.toDistanceString(distance));
  }

  private final void setSpeed (TextView view, float speed) {
    setText(view, ApplicationUtilities.toSpeedString(speed));
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

  private final float translateValue (Measurement measurement, float value) {
    measurement.add(value);
    return (float)Math.toDegrees(measurement.get());
  }

  private final void log (String type, float[] vector) {
    if (ApplicationSettings.LOG_VECTORS) {
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

      setBearing(azimuthDegrees, azimuth);
      setPoint(azimuthPoint, azimuth);
      setBearing(pitchDegrees, pitch);
      setBearing(rollDegrees, roll);
    }
  }

  @Override
  public void onAccuracyChanged (Sensor sensor, int accuracy) {
  }

  private boolean atNewLocation;
  private boolean settingLocationFields;
  private Geocoder geocoder;
  private LocationMonitor locationMonitor;

  private final void prepareLocationMonitor () {
    atNewLocation = false;
    settingLocationFields = false;
    geocoder = Geocoder.isPresent()? new Geocoder(this): null;

    setText(locationName,
      (geocoder != null)?
      R.string.message_waiting:
      R.string.message_unsupported
    );

    // this must be the very last step because the current location will be set
    locationMonitor = new BestLocationMonitor(this);
  }

  private double currentLatitude;
  private double currentLongitude;

  private final void setLocationFields () {
    synchronized (this) {
      if (!settingLocationFields) {
        settingLocationFields = true;

        new AsyncTask<Void, Object, Void>() {
          @Override
          protected void onProgressUpdate (Object... arguments) {
            String name = (String)arguments[0];
            Float distance = (Float)arguments[1];
            Float direction = (Float)arguments[2];

            setText(locationName, name);

            if (distance != null) {
              setDistance(distanceMagnitude, distance);
            } else {
              setText(distanceMagnitude);
            }

            if (direction != null) {
              setBearing(directionDegrees, direction);
              setPoint(directionPoint, direction);
            } else {
              setText(directionDegrees);
              setText(directionPoint);
            }
          }

          private final boolean setFields (double latitude, double longitude) {
            String problem = null;

            try {
              List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);

              if (addresses != null) {
                if (!addresses.isEmpty()) {
                  Address address = addresses.get(0);

                  if (ApplicationSettings.LOG_ADDRESSES) {
                    LocationUtilities.log(address);
                  }

                  String name = LocationUtilities.getName(address);
                  Float distance = null;
                  Float direction = null;

                  if (address.hasLatitude() && address.hasLongitude()) {
                    float[] results = new float[2];

                    Location.distanceBetween(
                      latitude, longitude,
                      address.getLatitude(), address.getLongitude(),
                      results
                    );

                    distance = results[0];
                    direction = results[1];

                    if (ApplicationSettings.LOG_ADDRESSES) {
                      StringBuilder sb = new StringBuilder("where:");

                      sb.append(' ');
                      sb.append(ApplicationUtilities.toCoordinatesString(latitude, longitude));

                      sb.append(' ');
                      sb.append(ApplicationUtilities.toDistanceString(distance));
                      sb.append('@');
                      sb.append(ApplicationUtilities.toBearingString(direction));

                      Log.d(LOG_TAG, sb.toString());
                    }
                  }

                  publishProgress(name, distance, direction);
                  return true;
                } else {
                  problem = "no addresses";
                }
              } else {
                problem = "no address list";
              }
            } catch (IOException exception) {
              problem = exception.getMessage();
            }

            Log.w(LOG_TAG, String.format(
              "geocoding failure: [%.7f, %.7f]: %s",
              latitude, longitude, problem
            ));

            return false;
          }

          @Override
          protected Void doInBackground (Void... arguments) {
            while (true) {
              double latitude;
              double longitude;

              synchronized (CompassActivity.this) {
                if (!atNewLocation) {
                  settingLocationFields = false;
                  return null;
                }

                latitude = currentLatitude;
                longitude = currentLongitude;
                atNewLocation = false;
              }

              setFields(latitude, longitude);
            }
          }
        }.execute();
      }
    }
  }

  private final void setLocationFields (double latitude, double longitude) {
    if (geocoder != null) {
      synchronized (this) {
        currentLatitude = latitude;
        currentLongitude = longitude;
        atNewLocation = true;
        setLocationFields();
      }
    }
  }

  private final void setLocation (double latitude, double longitude) {
    setCoordinate(latitudeDecimal, latitude);
    setCoordinate(longitudeDecimal, longitude);

    setLatitude(latitudeDMS, latitude);
    setLongitude(longitudeDMS, longitude);

    setLocationFields(latitude, longitude);
  }

  public final void setLocation (Location location) {
    setLocation(location.getLatitude(), location.getLongitude());

    if (location.hasAltitude()) {
      double meters = location.getAltitude();
      setDistance(altitudeMagnitude, meters);
    } else {
      setText(altitudeMagnitude);
    }

    if (location.hasSpeed()) {
      float metersPerSecond = location.getSpeed();
      setSpeed(speedMagnitude, metersPerSecond);
    } else {
      setText(speedMagnitude);
    }

    if (location.hasBearing()) {
      float degrees = location.getBearing();
      setBearing(bearingDegrees, degrees);
      setPoint(bearingPoint, degrees);
    } else {
      setText(bearingDegrees);
      setText(bearingPoint);
    }
  }

  @Override
  public void onCreate (Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    accessibilityManager = (AccessibilityManager)getSystemService(ACCESSIBILITY_SERVICE);
    Controls.restore();

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

  public final void onSettingsButtonClick (View view) {
    Intent intent = new Intent(this, SettingsActivity.class);
    startActivity(intent);
  }
}
