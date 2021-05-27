package org.nbp.common;

import android.util.Log;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

import android.app.Activity;
import android.os.Bundle;

import android.widget.TextView;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.Map;
import java.util.HashMap;

public class Permissions {
  private final static String LOG_TAG = Permissions.class.getName();

  private static String joinStrings (String delimiter, String... strings) {
    StringBuilder builder = new StringBuilder();

    for (String string : strings) {
      if (string == null) continue;
      if (string.isEmpty()) continue;

      if (builder.length() > 0) builder.append(delimiter);
      builder.append(string);
    }

    return builder.toString();
  }

  private static Context getContext () {
    return CommonContext.getContext();
  }

  public static boolean isRequested (String permission) {
    Context context = getContext();
    PackageManager pm = context.getPackageManager();
    return pm.checkPermission(permission, context.getPackageName()) == PackageManager.PERMISSION_GRANTED;
  }

  public static boolean isGranted (String permission) {
    Context context = getContext();

    if (CommonUtilities.haveMarshmallow) {
      return context.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
    }

    return isRequested(permission);
  }

  private final static Map<Integer, AtomicBoolean> requestMonitors =
               new HashMap<Integer, AtomicBoolean>();

  private final static Object REQUEST_CODE_LOCK = new Object();
  private static int requestCode = 0;

  private final static String EXTRA_REQUEST_CODE = "code";
  private final static String EXTRA_REQUEST_PERMISSIONS = "permissions";

  public static class RequestActivity extends Activity {
    private final void handleIntent (Intent intent) {
      Bundle extras = intent.getExtras();
      int code = extras.getInt(EXTRA_REQUEST_CODE);
      String[] permissions = extras.getStringArray(EXTRA_REQUEST_PERMISSIONS);

      {
        TextView view = findViewById(R.id.PermissionRequest_permissions);
        view.setText(joinStrings("\n", permissions));
      }

      requestPermissions(permissions, code);
    }

    @Override
    protected void onCreate (Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.permission_request);
      handleIntent(getIntent());
    }

    @Override
    public void onRequestPermissionsResult (int code, String[] permissions, int[] results) {
      AtomicBoolean monitor;

      synchronized (requestMonitors) {
        monitor = requestMonitors.remove(code);
      }

      {
        int count = permissions.length;

        for (int index=0; index<count; index+=1) {
          String permission = permissions[index];
          int result = results[index];

          switch (result) {
            case PackageManager.PERMISSION_GRANTED:
              Log.i(LOG_TAG, ("permission granted: " + permission));
              break;

            case PackageManager.PERMISSION_DENIED:
              Log.i(LOG_TAG, ("permission denied: " + permission));
              break;

            default:
              Log.w(LOG_TAG, String.format("unknown permission request result: %d: %s", result, permission));
              break;
          }
        }
      }

      synchronized (monitor) {
        monitor.set(true);
        monitor.notifyAll();
      }

      finish();
    }
  }

  public static int request (String... permissions) {
    Log.i(LOG_TAG, ("requesting permissions: " + joinStrings(", ", permissions)));

    Context context = getContext();
    int code;

    synchronized (REQUEST_CODE_LOCK) {
      code = ++requestCode;
    }

    if (CommonUtilities.haveMarshmallow) {
      Intent intent = new Intent(context, RequestActivity.class);

      intent.setFlags(
        Intent.FLAG_ACTIVITY_NEW_TASK
      );

      intent.putExtra(EXTRA_REQUEST_CODE, code);
      intent.putExtra(EXTRA_REQUEST_PERMISSIONS, permissions);

      synchronized (requestMonitors) {
        requestMonitors.put(code, new AtomicBoolean());
      }

      context.startActivity(intent);
    }

    return code;
  }

  public static boolean await (int code) {
    AtomicBoolean monitor;

    synchronized (requestMonitors) {
      monitor = requestMonitors.get(code);
    }

    if (monitor != null) {
      synchronized (monitor) {
        while (!monitor.get()) {
          try {
            monitor.wait(30000);
          } catch (InterruptedException exception) {
            Log.w(LOG_TAG, "permission request wait interrupted");
            return false;
          }
        }
      }
    }

    return true;
  }
}
