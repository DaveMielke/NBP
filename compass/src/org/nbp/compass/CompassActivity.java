package org.nbp.compass;

import java.util.List;
import java.io.IOException;

import org.nbp.common.CommonActivity;
import org.nbp.common.CommonUtilities;

import org.nbp.common.DialogFinisher;
import org.nbp.common.DialogHelper;

import android.os.Build;
import android.util.Log;

import android.os.Bundle;
import android.os.AsyncTask;
import android.content.Intent;

import android.view.Menu;
import android.view.MenuItem;

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

  // accuracy
  private TextView accuracySatellites;
  private TextView accuracyHorizontal;

  // location
  private TextView locationName;
  private TextView distanceMagnitude;
  private TextView directionDegrees;
  private TextView directionPoint;

  // motion
  private TextView speedMagnitude;
  private TextView bearingDegrees;
  private TextView bearingPoint;

  // orientation
  private TextView headingDegrees;
  private TextView headingPoint;
  private TextView pitchDegrees;
  private TextView rollDegrees;

  // position
  private TextView latitudeDecimal;
  private TextView latitudeDMS;
  private TextView longitudeDecimal;
  private TextView longitudeDMS;
  private TextView altitudeMagnitude;

  private final void findViews () {
    // accuracy
    accuracySatellites = (TextView)findViewById(R.id.accuracy_satellites);
    accuracyHorizontal = (TextView)findViewById(R.id.accuracy_horizontal);

    // location
    locationName = (TextView)findViewById(R.id.location_name);
    distanceMagnitude = (TextView)findViewById(R.id.distance_magnitude);
    directionDegrees = (TextView)findViewById(R.id.direction_degrees);
    directionPoint = (TextView)findViewById(R.id.direction_point);

    // motion
    speedMagnitude = (TextView)findViewById(R.id.speed_magnitude);
    bearingDegrees = (TextView)findViewById(R.id.bearing_degrees);
    bearingPoint = (TextView)findViewById(R.id.bearing_point);

    // orientation
    headingDegrees = (TextView)findViewById(R.id.heading_degrees);
    headingPoint = (TextView)findViewById(R.id.heading_point);
    pitchDegrees = (TextView)findViewById(R.id.pitch_degrees);
    rollDegrees = (TextView)findViewById(R.id.roll_degrees);

    // position
    latitudeDecimal = (TextView)findViewById(R.id.latitude_decimal);
    latitudeDMS = (TextView)findViewById(R.id.latitude_dms);
    longitudeDecimal = (TextView)findViewById(R.id.longitude_decimal);
    longitudeDMS = (TextView)findViewById(R.id.longitude_dms);
    altitudeMagnitude = (TextView)findViewById(R.id.altitude_magnitude);
  }

  private final DelayedAction getAction (View view, int key, long delay) {
    DelayedAction action = (DelayedAction)view.getTag(key);

    if (action == null) {
      action = new DelayedAction(delay, getResources().getString(key));
      view.setTag(key, action);
    }

    return action;
  }

  private final void setText (final TextView view, final CharSequence text) {
    if (!TextUtils.equals(text, view.getText())) {
      view.setText(text);

      /*
      if (text.length() == 0) {
        CharSequence hint = view.getHint();

        if (hint != null) {
          if (hint.length() > 0) {
            text = hint;
          }
        }
      }
      */

      final CharSequence announcement = text;
      getAction(view, R.string.text_action_announce, ApplicationParameters.TEXT_ANNOUNCE_DELAY).setAction(
        new Runnable() {
          @Override
          public void run () {
            if (accessibilityManager != null) {
              if (accessibilityManager.isEnabled()) {
                if (CommonUtilities.haveAndroidSDK(Build.VERSION_CODES.LOLLIPOP)) {
                  if (view.isAccessibilityFocused()) {
                    AccessibilityEvent event = AccessibilityEvent.obtain();
                    view.onInitializeAccessibilityEvent(event);
                    event.getText().add(announcement);
                    event.setEventType(AccessibilityEvent.TYPE_ANNOUNCEMENT);
                    accessibilityManager.sendAccessibilityEvent(event);
                  }
                }
              }
            }
          }
        }
      );
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

  private final float translateOrientation (float radians) {
    return (float)Math.toDegrees((double)radians);
  }

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

      float heading = translateOrientation(-currentOrientation[0]);
      float pitch   = translateOrientation(-currentOrientation[1]);
      float roll    = translateOrientation( currentOrientation[2]);

      setBearing(headingDegrees, heading);
      setPoint(headingPoint, heading);
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
            String name     = (String)arguments[0];
            Float distance  = (Float) arguments[1];
            Float direction = (Float) arguments[2];

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

                  if (ApplicationSettings.LOG_GEOCODING) {
                    Log.d(LOG_TAG, ("address: " + LocationUtilities.toString(address)));
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

                    if (ApplicationSettings.LOG_GEOCODING) {
                      StringBuilder sb = new StringBuilder("orientation:");

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

    if (location.hasAccuracy()) {
      float distance = location.getAccuracy();
      setText(accuracyHorizontal, ("±" + ApplicationUtilities.toDistanceString(distance)));
    } else {
      setText(accuracyHorizontal);
    }

    {
      Bundle extras = location.getExtras();

      if (extras != null) {
        {
          String key = "satellites";

          if (extras.containsKey(key)) {
            int count = extras.getInt(key);
            setText(accuracySatellites, Integer.toString(count));
          } else {
            setText(accuracySatellites);
          }
        }
      }
    }
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
    startSensors();
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
