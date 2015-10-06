package org.nbp.b2g.ui.host;
import org.nbp.b2g.ui.*;

import java.io.File;

import android.util.Log;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Button;

import android.os.PowerManager;
import android.os.RecoverySystem;

import android.os.AsyncTask;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class MaintenanceActivity extends ProgrammaticActivity {
  private final static String LOG_TAG = MaintenanceActivity.class.getName();

  private Context getContext () {
    return MaintenanceActivity.this;
  }

  private PowerManager getPowerManager () {
    return (PowerManager)getSystemService(Context.POWER_SERVICE);
  }

  private TextView messageView;

  private void setMessage (String message, boolean rewrite) {
    if (rewrite) {
      Endpoints.popup.get().rewrite(message);
    } else {
      Endpoints.setPopupEndpoint(message);
    }

    messageView.setText(message);
    Log.d(LOG_TAG, "system maintenance: " + message);
  }

  private void setMessage (String message) {
    setMessage(message, false);
  }

  private void setMessage (int message) {
    setMessage(getString(message));
  }

  private String getRebootFailureMessage () {
    return getString(R.string.maintenance_message_reboot_failed);
  }

  private final void showActivityResultCode (int code) {
    switch (code) {
      case RESULT_OK:
        setMessage(R.string.maintenance_message_operation_succeeded);
        break;

      case RESULT_CANCELED:
        setMessage(R.string.maintenance_message_operation_cancelled);
        break;

      default:
        setMessage(String.format(
          "%s: %d",
          getString(R.string.maintenance_message_operation_failed),
          code
        ));
        break;
    }
  }

  private abstract class MaintenanceTask extends AsyncTask<Void, String, String> {
    protected abstract String runMaintenanceTask ();

    @Override
    protected final String doInBackground (Void... arguments) {
      return runMaintenanceTask();
    }

    @Override
    public final void onProgressUpdate (String... messages) {
      setMessage(messages[0], true);
    }

    protected void onSuccess () {
    }

    @Override
    protected final void onPostExecute (String result) {
      if (result == null) {
        onSuccess();
      } else {
        setMessage(result);
      }
    }
  }

  @Override
  protected boolean startRequest (Intent intent, ActivityResultHandler handler) {
    Endpoints.setHostEndpoint();
    return super.startRequest(intent, handler);
  }

  private void rebootDevice (String reason) {
    getPowerManager().reboot(reason);
    setMessage(getRebootFailureMessage());
  }

  private View createRestartSystemButton () {
    Button button = newButton(
      R.string.maintenance_RestartSystem_label,
      new Button.OnClickListener() {
        @Override
        public void onClick (View view) {
          setMessage(R.string.maintenance_RestartSystem_starting);
          rebootDevice(null);
        }
      }
    );

    return button;
  }

  private View createRecoveryModeButton () {
    Button button = newButton(
      R.string.maintenance_RecoveryMode_label,
      new Button.OnClickListener() {
        @Override
        public void onClick (View view) {
          setMessage(R.string.maintenance_RecoveryMode_starting);
          rebootDevice("recovery");
        }
      }
    );

    return button;
  }

  private View createBootLoaderButton () {
    Button button = newButton(
      R.string.maintenance_BootLoader_label,
      new Button.OnClickListener() {
        @Override
        public void onClick (View view) {
          setMessage(R.string.maintenance_BootLoader_starting);
          rebootDevice("bootloader");
        }
      }
    );

    return button;
  }

  private void applyUpdate (final File file) {
    setMessage(R.string.maintenance_UpdateSystem_start);

    new MaintenanceTask() {
      @Override
      protected String runMaintenanceTask () {
        String failure = getRebootFailureMessage();

        try {
          RecoverySystem.installPackage(getContext(), file);
        } catch (IOException exception) {
          failure = exception.getMessage();
        }

        return failure;
      }
    }.execute();
  }

  private void verifyUpdate (final File file, final boolean apply) {
    final String progressMessage = getString(R.string.maintenance_VerifyUpdate_progress);
    setMessage(progressMessage);

    new MaintenanceTask() {
      @Override
      protected String runMaintenanceTask () {
        String result = getRebootFailureMessage();

        RecoverySystem.ProgressListener progressListener = new RecoverySystem.ProgressListener() {
          @Override
          public void onProgress (int percentage) {
            String message = String.format("%s: %d%%", progressMessage, percentage);
            publishProgress(message);
          }
        };

        try {
          RecoverySystem.verifyPackage(file, progressListener, null);
          result = null;
        } catch (IOException exception) {
          result = "system update not readable: " + exception.getMessage();
        } catch (GeneralSecurityException exception) {
          result = "invalid system update: " + exception.getMessage();
        }

        return result;
      }

      @Override
      protected final void onSuccess () {
        if (apply) {
          applyUpdate(file);
        } else {
          setMessage(R.string.maintenance_VerifyUpdate_done);
        }
      }
    }.execute();
  }

  private void verifyUpdate (final boolean apply) {
    setMessage(R.string.maintenance_VerifyUpdate_finding);

    findFile(
      new ActivityResultHandler() {
        @Override
        public void handleActivityResult (int code, Intent intent) {
          switch (code) {
            case RESULT_OK:
              verifyUpdate(new File(intent.getData().getPath()), apply);
              break;

            default:
              showActivityResultCode(code);
              break;
          }
        }
      }
    );
  }

  private View createVerifyUpdateButton () {
    Button button = newButton(
      R.string.maintenance_VerifyUpdate_label,
      new Button.OnClickListener() {
        @Override
        public void onClick (View view) {
          verifyUpdate(false);
        }
      }
    );

    return button;
  }

  private View createUpdateSystemButton () {
    Button button = newButton(
      R.string.maintenance_UpdateSystem_label,
      new Button.OnClickListener() {
        @Override
        public void onClick (View view) {
          verifyUpdate(true);
        }
      }
    );

    return button;
  }

  private View createClearCacheButton () {
    Button button = newButton(
      R.string.maintenance_ClearCache_label,
      new Button.OnClickListener() {
        @Override
        public void onClick (View view) {
          setMessage(R.string.maintenance_ClearCache_starting);
          String failure = getRebootFailureMessage();

          try {
            RecoverySystem.rebootWipeCache(getContext());
          } catch (IOException exception) {
            failure = exception.getMessage();
          }

          setMessage(failure);
        }
      }
    );

    return button;
  }

  private View createFactoryResetButton () {
    Button button = newButton(
      R.string.maintenance_FactoryReset_label,
      new Button.OnClickListener() {
        @Override
        public void onClick (View view) {
          setMessage(R.string.maintenance_FactoryReset_starting);
          String failure = getRebootFailureMessage();

          try {
            RecoverySystem.rebootWipeUserData(getContext());
          } catch (IOException exception) {
            failure = exception.getMessage();
          }

          setMessage(failure);
        }
      }
    );

    return button;
  }

  @Override
  protected final View createContentView () {
    return createVerticalGroup(
      (messageView = newTextView()),

      createRestartSystemButton(),

      createVerifyUpdateButton(),
      createUpdateSystemButton(),

      createRecoveryModeButton(),

      createClearCacheButton(),
      createFactoryResetButton(),
      createBootLoaderButton()
    );
  }
}
