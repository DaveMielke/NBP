package org.nbp.b2g.ui.host;
import org.nbp.b2g.ui.*;

import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;

import android.util.Log;

import android.app.Activity;

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

public class MaintenanceActivity extends ProgrammaticActivity {
  private final static String LOG_TAG = MaintenanceActivity.class.getName();

  private Context getContext () {
    return MaintenanceActivity.this;
  }

  private PowerManager getPowerManager () {
    return (PowerManager)getSystemService(Context.POWER_SERVICE);
  }

  private TextView messageView;

  private void showMessage (String message) {
    messageView.setText(message);
    Log.d(LOG_TAG, "system maintenance: " + message);
  }

  private void updateMessage (String message) {
    Endpoints.popup.get().rewrite(message);
    showMessage(message);
  }

  private void setMessage (String message) {
    Endpoints.setPopupEndpoint(message);
    showMessage(message);
  }

  private void setMessage (int message) {
    setMessage(getString(message));
  }

  private abstract class MaintenanceTask extends AsyncTask<Void, String, String> {
    protected abstract String runMaintenanceTask ();

    @Override
    protected final String doInBackground (Void... arguments) {
      return runMaintenanceTask();
    }

    @Override
    public final void onProgressUpdate (String... messages) {
      updateMessage(messages[0]);
    }

    protected void onSuccess () {
      setMessage(R.string.maintenance_message_operation_succeeded);
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

  private abstract class RebootTask extends MaintenanceTask {
    protected abstract String runRebootTask ();

    @Override
    protected final String runMaintenanceTask () {
      ApplicationUtilities.sleep(ApplicationParameters.MAINTENANCE_REBOOT_DELAY);

      try {
        String result = runRebootTask();
        if (result != null) return result;
      } catch (SecurityException exception) {
        return exception.getMessage();
      }

      return getString(R.string.maintenance_message_reboot_failed);
    }
  }

  @Override
  protected boolean startRequest (Intent intent, ActivityResultHandler handler) {
    Endpoints.setHostEndpoint();
    return super.startRequest(intent, handler);
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

  private void rebootDevice (String reason) {
    getPowerManager().reboot(reason);
  }

  private View createRestartSystemButton () {
    Button button = newButton(
      R.string.maintenance_RestartSystem_label,
      new Button.OnClickListener() {
        @Override
        public void onClick (View view) {
          setMessage(R.string.maintenance_RestartSystem_starting);

          new RebootTask() {
            @Override
            protected String runRebootTask () {
              rebootDevice(null);
              return null;
            }
          }.execute();
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

          new RebootTask() {
            @Override
            protected String runRebootTask () {
              rebootDevice("recovery");
              return null;
            }
          }.execute();
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

          new RebootTask() {
            @Override
            protected String runRebootTask () {
              rebootDevice("bootloader");
              return null;
            }
          }.execute();
        }
      }
    );

    return button;
  }

  private void applyUpdate (final File file) {
    setMessage(R.string.maintenance_UpdateSystem_start);

    new RebootTask() {
      @Override
      protected String runRebootTask () {
        try {
          RecoverySystem.installPackage(getContext(), file);
        } catch (IOException exception) {
          return exception.getMessage();
        }

        return null;
      }
    }.execute();
  }

  private void verifyUpdate (final File file, final boolean apply) {
    final String progressMessage = getString(R.string.maintenance_VerifyUpdate_progress);
    setMessage(progressMessage);

    new MaintenanceTask() {
      @Override
      protected String runMaintenanceTask () {
        RecoverySystem.ProgressListener progressListener = new RecoverySystem.ProgressListener() {
          @Override
          public void onProgress (int percentage) {
            String message = String.format("%s: %d%%", progressMessage, percentage);
            publishProgress(message);
          }
        };

        try {
          RecoverySystem.verifyPackage(file, progressListener, null);
          return null;
        } catch (IOException exception) {
          return "system update not readable: " + exception.getMessage();
        } catch (GeneralSecurityException exception) {
          return "invalid system update: " + exception.getMessage();
        }
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

          new RebootTask() {
            @Override
            protected String runRebootTask () {
              try {
                RecoverySystem.rebootWipeCache(getContext());
              } catch (IOException exception) {
                return exception.getMessage();
              }

              return null;
            }
          }.execute();
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

          new RebootTask() {
            @Override
            protected String runRebootTask () {
              try {
                RecoverySystem.rebootWipeUserData(getContext());
              } catch (IOException exception) {
                return exception.getMessage();
              }

              return null;
            }
          }.execute();
        }
      }
    );

    return button;
  }

  private void launchActivity (Class<? extends Activity> activity) {
    Intent intent = new Intent(this, activity);

    intent.addFlags(
      Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS |
      Intent.FLAG_ACTIVITY_CLEAR_TOP |
      Intent.FLAG_ACTIVITY_SINGLE_TOP |
      Intent.FLAG_ACTIVITY_NEW_TASK
    );

    startActivity(intent);
  }

  private View createRecoveryLogButton () {
    Button button = newButton(
      R.string.maintenance_RecoveryLog_label,
      new Button.OnClickListener() {
        @Override
        public void onClick (View view) {
          launchActivity(RecoveryLogActivity.class);
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

      createRecoveryLogButton(),
      createRecoveryModeButton(),

      createClearCacheButton(),
      createFactoryResetButton(),
      createBootLoaderButton()
    );
  }
}
