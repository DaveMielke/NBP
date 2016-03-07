package org.nbp.compass;

import android.util.Log;

import android.app.Activity;
import android.os.Bundle;

import android.widget.TextView;

import android.hardware.SensorManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationListener;
import android.location.Location;

public class CompassActivity extends Activity implements
  GoogleApiClient.ConnectionCallbacks,
  GoogleApiClient.OnConnectionFailedListener,
  LocationListener,
  SensorEventListener
{
  private final static String LOG_TAG = CompassActivity.class.getName();

  private TextView azimuthDegrees;
  private TextView azimuthDirection;
  private TextView pitchDegrees;
  private TextView rollDegrees;
  private TextView latitudeDecimal;
  private TextView latitudeDMS;
  private TextView longitudeDecimal;
  private TextView longitudeDMS;

  private SensorManager sensorManager;
  private GoogleApiClient gapi;
  private boolean canReceiveLocationUpdates = false;
  private boolean amReceivingLocationUpdates = false;

  private final static int[] sensorTypes = new int[] {
    Sensor.TYPE_ACCELEROMETER,
    Sensor.TYPE_MAGNETIC_FIELD
  };

  private final Sensor[] sensorArray = new Sensor[sensorTypes.length];
  private final float[] rotationMatrix = new float[9];
  private final float[] currentOrientation = new float[3];
  private float[] gravityVector = null;
  private float[] geomagneticVector = null;

  private final Measurement azimuthMeasurement = new Measurement();
  private final Measurement pitchMeasurement = new Measurement();
  private final Measurement rollMeasurement = new Measurement();

  @Override
  public void onCreate (Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.compass);
    azimuthDegrees = (TextView)findViewById(R.id.azimuth_degrees);
    azimuthDirection = (TextView)findViewById(R.id.azimuth_direction);
    pitchDegrees = (TextView)findViewById(R.id.pitch_degrees);
    rollDegrees = (TextView)findViewById(R.id.roll_degrees);
    latitudeDecimal = (TextView)findViewById(R.id.latitude_decimal);
    latitudeDMS = (TextView)findViewById(R.id.latitude_dms);
    longitudeDecimal = (TextView)findViewById(R.id.longitude_decimal);
    longitudeDMS = (TextView)findViewById(R.id.longitude_dms);

    sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);

    gapi = new GoogleApiClient.Builder(this)
                              .addConnectionCallbacks(this)
                              .addOnConnectionFailedListener(this)
                              .addApi(LocationServices.API)
                              .build();

    gapi.connect();

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
      sensorManager.registerListener(this, sensor, Parameters.SENSOR_UPDATE_INTERVAL);
    }

    canReceiveLocationUpdates = true;
    if (gapi.isConnected()) startReceivingLocationUpdates();
  }

  @Override
  protected void onPause () {
    try {
      for (Sensor sensor : sensorArray) {
        sensorManager.unregisterListener(this, sensor);
      }

      canReceiveLocationUpdates = false;
      if (gapi.isConnected()) stopReceivingLocationUpdates();
    } finally {
      super.onPause();
    }
  }

  private final float translateValue (Measurement measurement, float value) {
    measurement.add(value);
    return (float)Math.toDegrees(measurement.get());
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

      sensorManager.getOrientation(rotationMatrix, currentOrientation);
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

  private final void setLocation (
    double degrees,
    TextView decimal, TextView dms,
    char positive, char negative
  ) {
    decimal.setText(String.format("%.4f°", degrees));

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

  private final void setLocation (double latitude, double longitude) {
    setLocation(latitude, latitudeDecimal, latitudeDMS, 'N', 'S');
    setLocation(longitude, longitudeDecimal, longitudeDMS, 'E', 'W');
  }

  private final void setLocation (Location location) {
    if (location != null) {
      setLocation(location.getLatitude(), location.getLongitude());
    }
  }

  @Override
  public void onLocationChanged (Location location) {
    setLocation(location);
  }

  private final void startReceivingLocationUpdates () {
    if (canReceiveLocationUpdates) {
      if (!amReceivingLocationUpdates) {
        LocationRequest request = new LocationRequest()
          .setInterval(Parameters.LOCATION_MAXIMUM_INTERVAL)
          .setFastestInterval(Parameters.LOCATION_MINIMUM_INTERVAL)
          .setPriority(Parameters.LOCATION_PRIORITY)
          ;

        LocationServices.FusedLocationApi.requestLocationUpdates(gapi, request, this);
        amReceivingLocationUpdates = true;
      }
    }
  }

  private final void stopReceivingLocationUpdates () {
    if (amReceivingLocationUpdates) {
      LocationServices.FusedLocationApi.removeLocationUpdates(gapi, this);
      amReceivingLocationUpdates = false;
    }
  }

  @Override
  public void onConnected (Bundle connectionHint) {
    setLocation(LocationServices.FusedLocationApi.getLastLocation(gapi));
    startReceivingLocationUpdates();
  }

  @Override
  public void onConnectionSuspended (int cause) {
  }

  @Override
  public void onConnectionFailed (ConnectionResult result) {
  }
}
