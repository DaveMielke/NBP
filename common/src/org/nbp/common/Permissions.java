package org.nbp.common;

import android.util.Log;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

import android.app.Activity;
import android.os.Bundle;

import android.widget.TextView;

import java.util.Map;
import java.util.HashMap;

public class Permissions {
  private final static String LOG_TAG = Permissions.class.getName();

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

  private static enum RequestState {
    WAITING, GRANTED, DENIED, UNKNOWN;
  }

  private final static Map<Integer, RequestState> requestStates =
               new HashMap<Integer, RequestState>();

  private final static Object REQUEST_CODE_LOCK = new Object();
  private static int requestCode = 0;

  private final static String EXTRA_REQUEST_CODE = "code";
  private final static String EXTRA_REQUEST_PERMISSION = "permission";

  public static class RequestActivity extends Activity {
    private final void handleIntent (Intent intent) {
      Bundle extras = intent.getExtras();
      String permission = extras.getString(EXTRA_REQUEST_PERMISSION);
      int code = extras.getInt(EXTRA_REQUEST_CODE);

      {
        TextView view = findViewById(R.id.PermissionRequest_permission);
        view.setText(permission);
      }

      requestPermissions(new String[] {permission}, code);
    }

    @Override
    protected void onCreate (Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.permission_request);
      handleIntent(getIntent());
    }

    @Override
    public void onRequestPermissionsResult (int code, String[] permissions, int[] results) {
      RequestState state;

      synchronized (requestStates) {
        state = requestStates.get(code);
      }

      if (state != null) {
        if (state.equals(RequestState.WAITING)) {
          String permission = permissions[0];
          int result = results[0];

          switch (result) {
            case PackageManager.PERMISSION_GRANTED:
              state = RequestState.GRANTED;
              Log.i(LOG_TAG, ("permission granted: " + permission));
              break;

            case PackageManager.PERMISSION_DENIED:
              state = RequestState.DENIED;
              Log.i(LOG_TAG, ("permission denied: " + permission));
              break;

            default:
              state = RequestState.UNKNOWN;
              Log.w(LOG_TAG, String.format("unknown permission request result: %d: %s", result, permission));
              break;
          }

          synchronized (requestStates) {
            requestStates.put(code, state);
            requestStates.notifyAll();
            finish();
          }
        }
      }
    }
  }

  public static boolean request (String permission) {
    if (CommonUtilities.haveMarshmallow) {
      Context context = getContext();
      Intent intent = new Intent(context, RequestActivity.class);
      int code;

      intent.setFlags(
        Intent.FLAG_ACTIVITY_NEW_TASK
      );

      synchronized (REQUEST_CODE_LOCK) {
        code = ++requestCode;
      }

      intent.putExtra(EXTRA_REQUEST_PERMISSION, permission);
      intent.putExtra(EXTRA_REQUEST_CODE, code);

      synchronized (requestStates) {
        requestStates.put(code, RequestState.WAITING);

        Log.i(LOG_TAG, ("requesting permission: " + permission));
        context.startActivity(intent);

        while (true) {
          try {
            requestStates.wait(30000);
            RequestState state = requestStates.get(code);

            if (state != null) {
              if (!state.equals(RequestState.WAITING)) {
                return state.equals(RequestState.GRANTED);
              }
            }
          } catch (InterruptedException exception) {
            Log.w(LOG_TAG, ("permission request wait interrupted: " + permission));
            return false;
          }
        }
      }
    } else {
      return false;
    }
  }
}
