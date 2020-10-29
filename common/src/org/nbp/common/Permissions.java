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

  public static boolean have (String permission) {
    Context context = getContext();
    int result;

    if (CommonUtilities.haveMarshmallow) {
      result = context.checkSelfPermission(permission);
    } else {
      PackageManager pm = context.getPackageManager();
      result = pm.checkPermission(permission, context.getPackageName());
    }

    return result == PackageManager.PERMISSION_GRANTED;
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
        TextView view = findViewById(R.id.PermissionRequest_permission);
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

      if (monitor != null) {
        synchronized (monitor) {
          monitor.set(true);
          monitor.notify();
        }
      }

      finish();
    }
  }

  public static void request (boolean wait, String... permissions) {
    if (CommonUtilities.haveMarshmallow) {
      Log.i(LOG_TAG, ("requesting permissions: " + joinStrings(", ", permissions)));

      Context context = getContext();
      Intent intent = new Intent(context, RequestActivity.class);
      int code;

      synchronized (REQUEST_CODE_LOCK) {
        code = ++requestCode;
      }

      intent.putExtra(EXTRA_REQUEST_CODE, code);
      intent.putExtra(EXTRA_REQUEST_PERMISSIONS, permissions);

      intent.setFlags(
        Intent.FLAG_ACTIVITY_NEW_TASK
      );

      if (wait) {
        AtomicBoolean monitor = wait? new AtomicBoolean(): null;

        synchronized (requestMonitors) {
          requestMonitors.put(code, monitor);
        }

        synchronized (monitor) {
          context.startActivity(intent);

          while (true) {
            try {
              monitor.wait(30000);
              if (monitor.get()) break;
            } catch (InterruptedException exception) {
              Log.w(LOG_TAG, ("permission request wait interrupted: " + joinStrings(", ", permissions)));
              break;
            }
          }
        }
      } else {
        context.startActivity(intent);
      }
    }
  }

  public static void request (String... permissions) {
    request(false, permissions);
  }
}
