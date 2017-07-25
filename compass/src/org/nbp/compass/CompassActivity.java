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
  private TextView directionRelative;
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
  private TextView altitudeMagnitude;
  private TextView latitudeDecimal;
  private TextView latitudeDMS;
  private TextView longitudeDecimal;
  private TextView longitudeDMS;

  private final void findViews () {
    // accuracy
    accuracySatellites = (TextView)findViewById(R.id.accuracy_satellites);
    accuracyHorizontal = (TextView)findViewById(R.id.accuracy_horizontal);

    // location
    locationName = (TextView)findViewById(R.id.location_name);
    distanceMagnitude = (TextView)findViewById(R.id.distance_magnitude);
    directionRelative = (TextView)findViewById(R.id.direction_relative);
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
    altitudeMagnitude = (TextView)findViewById(R.id.altitude_magnitude);
    latitudeDecimal = (TextView)findViewById(R.id.latitude_decimal);
    latitudeDMS = (TextView)findViewById(R.id.latitude_dms);
    longitudeDecimal = (TextView)findViewById(R.id.longitude_decimal);
    longitudeDMS = (TextView)findViewById(R.id.longitude_dms);
  }

  private final boolean isAccessibilityEnabled () {
    if (accessibilityManager == null) return false;
    return accessibilityManager.isEnabled();
  }

  private final DelayedAction getChangedTextAnnouncer (TextView view) {
    int key = R.string.text_tag_announcer;

    synchronized (view) {
      DelayedAction action = (DelayedAction)view.getTag(key);

      if (action == null) {
        action = new DelayedAction(
          ApplicationParameters.ANNOUNCE_MINIMUM_TIME, "announce-text"
        );

        view.setTag(key, action);
      }

      return action;
    }
  }

  private final void setText (final TextView view, CharSequence text) {
    DelayedAction announcer = getChangedTextAnnouncer(view);

    synchronized (announcer) {
      synchronized (view) {
        if (!TextUtils.equals(text, view.getText())) {
          view.setText(text);

          announcer.setAction(
            new Runnable() {
              @Override
              public void run () {
                synchronized (view) {
                  int key = R.string.text_tag_announcement;
                  CharSequence text = view.getText();
                  boolean cancel = true;

                  if (isAccessibilityEnabled()) {
                    if (CommonUtilities.haveAndroidSDK(Build.VERSION_CODES.LOLLIPOP)) {
                      if (view.isAccessibilityFocused()) {
                        cancel = false;

                        if (text.length() > 0) {
                          if (!TextUtils.equals(text, (CharSequence)view.getTag(key))) {
                            accessibilityManager.interrupt();

                            AccessibilityEvent event = AccessibilityEvent.obtain();
                            view.onInitializeAccessibilityEvent(event);

                            event.setEventType(AccessibilityEvent.TYPE_ANNOUNCEMENT);
                            event.getText().add(text);

                            accessibilityManager.sendAccessibilityEvent(event);
                          }
                        }
                      }
                    }
                  }

                  if (cancel) text = null;
                  view.setTag(key, text);
                }
              }
            }
          );
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

  private final void setDistance (TextView view, double meters) {
    setText(view, ApplicationUtilities.toDistanceText(meters));
  }

  private final void setSpeed (TextView view, float metersPerSecond) {
    setText(view, ApplicationUtilities.toSpeedText(metersPerSecond));
  }

  private final void setAngle (TextView view, float degrees) {
    setText(view, ApplicationUtilities.toAngleText(degrees));
  }

  private final void setHeading (TextView view, float degrees) {
    setText(view, ApplicationUtilities.toHeadingText(degrees));
  }

  private final void setLatitude (TextView view, double degrees) {
    setText(view, ApplicationUtilities.toLatitudeText(degrees));
  }

  private final void setLongitude (TextView view, double degrees) {
    setText(view, ApplicationUtilities.toLongitudeText(degrees));
  }

  private final void setCoordinate (TextView view, double degrees) {
    setText(view, ApplicationUtilities.toCoordinateText(degrees));
  }

  private final void setPoint (TextView view, float degrees) {
    setText(view, ApplicationUtilities.toPointText(degrees));
  }

  private final static float NO_HEADING = 0f;
  private float savedDirectionHeading = NO_HEADING;
  private float savedCompassHeading = NO_HEADING;

  private final void setRelativeDirection () {
    if (savedDirectionHeading == NO_HEADING) return;
    if (savedCompassHeading == NO_HEADING) return;

    setText(directionRelative,
      ApplicationUtilities.toOClockText(
        savedDirectionHeading, savedCompassHeading
      )
    );
  }

  private final void saveDirectionHeading (float heading) {
    savedDirectionHeading = heading;
    setRelativeDirection();
  }

  private final void saveCompassHeading (float heading) {
    savedCompassHeading = heading;
    setRelativeDirection();
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

    heading = ApplicationUtilities.toHeading(heading);
    setHeading(headingDegrees, heading);
    setPoint(headingPoint, heading);
    saveCompassHeading(heading);

    setAngle(pitchDegrees, pitch);
    setAngle(rollDegrees, roll);
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

  private Geocoder geocoder;

  private final void prepareLocationMonitor () {
    geocoder = Geocoder.isPresent()? new Geocoder(this): null;

    setText(locationName,
      (geocoder != null)?
      R.string.message_waiting:
      R.string.message_unsupported
    );
  }

  private boolean atNewLocation = false;
  private boolean amGeocodingLocation = false;

  private double locationLatitude;
  private double locationLongitude;

  private final void geocodeLocation () {
    synchronized (this) {
      if (!amGeocodingLocation) {
        amGeocodingLocation = true;

        new AsyncTask<Void, Object, Void>() {
          @Override
          protected void onProgressUpdate (Object... arguments) {
            CharSequence name = (CharSequence)arguments[0];
            Float distance    = (Float)       arguments[1];
            Float direction   = (Float)       arguments[2];

            setText(locationName, name);

            if (distance != null) {
              setDistance(distanceMagnitude, distance);
            } else {
              setText(distanceMagnitude);
            }

            if (direction != null) {
              float heading = ApplicationUtilities.toHeading(direction);
              setHeading(directionDegrees, heading);
              setPoint(directionPoint, heading);
              saveDirectionHeading(heading);
            } else {
              setText(directionDegrees);
              setText(directionPoint);
            }
          }

          private final boolean geocode (double latitude, double longitude) {
            String problem = null;

            try {
              List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);

              if (addresses != null) {
                if (!addresses.isEmpty()) {
                  Address address = addresses.get(0);

                  if (ApplicationSettings.LOG_GEOCODING) {
                    Log.d(LOG_TAG, ("address: " + LocationUtilities.toString(address)));
                  }

                  CharSequence name = LocationUtilities.getName(address);
                  if (name.length() == 0) name = getString(R.string.message_unknown);

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
                      sb.append(ApplicationUtilities.toCoordinatesText(latitude, longitude));

                      sb.append(' ');
                      sb.append(ApplicationUtilities.toDistanceText(distance));
                      sb.append('@');
                      sb.append(ApplicationUtilities.toAngleText(direction));

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
                  amGeocodingLocation = false;
                  return null;
                }

                latitude = locationLatitude;
                longitude = locationLongitude;
                atNewLocation = false;
              }

              geocode(latitude, longitude);
            }
          }
        }.execute();
      }
    }
  }

  private final void geocodeLocation (double latitude, double longitude) {
    if (geocoder != null) {
      synchronized (this) {
        locationLatitude = latitude;
        locationLongitude = longitude;
        atNewLocation = true;
        geocodeLocation();
      }
    }
  }

  private final void setPosition (double latitude, double longitude) {
    setCoordinate(latitudeDecimal, latitude);
    setCoordinate(longitudeDecimal, longitude);

    setLatitude(latitudeDMS, latitude);
    setLongitude(longitudeDMS, longitude);

    geocodeLocation(latitude, longitude);
  }

  public final void onLocationReceived (Location location) {
    setPosition(location.getLatitude(), location.getLongitude());

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
      setAngle(bearingDegrees, degrees);
      setPoint(bearingPoint, degrees);
    } else {
      setText(bearingDegrees);
      setText(bearingPoint);
    }

    if (location.hasAccuracy()) {
      float distance = location.getAccuracy();
      setText(accuracyHorizontal, ("±" + ApplicationUtilities.toDistanceText(distance)));
    } else {
      setText(accuracyHorizontal);
    }

    {
      String satelliteCount = "";
      Bundle extras = location.getExtras();

      if (extras != null) {
        {
          String key = "satellites";

          if (extras.containsKey(key)) {
            satelliteCount = Integer.toString(extras.getInt(key));
          }
        }
      }

      setText(accuracySatellites, satelliteCount);
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

  private static CompassActivity compassActivity = null;

  public final static CompassActivity getCompassActivity () {
    return compassActivity;
  }

  @Override
  protected void onCreate (Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    compassActivity = this;

    accessibilityManager = (AccessibilityManager)getSystemService(ACCESSIBILITY_SERVICE);
    Controls.restore();

    setContentView(R.layout.main);
    findViews();

    prepareSensorMonitor();
    prepareLocationMonitor();
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
