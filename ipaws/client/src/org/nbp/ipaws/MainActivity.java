package org.nbp.ipaws;

import android.util.Log;

import org.nbp.common.CommonActivity;
import android.os.Bundle;
import android.content.Intent;

import android.view.View;
import android.widget.Switch;

import android.os.AsyncTask;
import android.content.DialogInterface;

import java.io.IOException;
import java.util.List;

public class MainActivity extends CommonActivity {
  private final static String LOG_TAG = MainActivity.class.getName();

  private Switch mainSwitch = null;

  @Override
  protected void onCreate (Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.main);
    mainSwitch = (Switch)findViewById(R.id.main_switch);
  }

  @Override
  protected void onResume () {
    super.onResume();
    mainSwitch.setChecked((AlertService.getAlertService() != null));
  }

  public final void onMainSwitchToggled (View view) {
    boolean isOn = mainSwitch.isChecked();

    if (isOn) {
      startService(AlertService.makeIntent(this));
    } else {
      stopService(AlertService.makeIntent(this));
    }
  }

  private final void selectState () {
    List<Areas.State> states = Areas.getStates();
    int count = states.size();
    String[] names = new String[count];

    for (int index=0; index<count; index+=1) {
      names[index] = states.get(index).getName();
    }

    DialogInterface.OnClickListener itemListener =
      new DialogInterface.OnClickListener() {
        @Override
        public void onClick (DialogInterface dialog, int button) {
        }
      };

    DialogInterface.OnClickListener cancelListener =
      new DialogInterface.OnClickListener() {
        @Override
        public void onClick (DialogInterface dialog, int button) {
        }
      };

    newAlertDialogBuilder(R.string.action_selectState)
      .setCancelable(true)
      .setNegativeButton(android.R.string.no, cancelListener)
      .setItems(names, itemListener)
      .show();
  }

  private final boolean getStates () {
    List<Areas.State> states = Areas.getStates();

    synchronized (states) {
      if (states.isEmpty()) {
        final AlertSession session = AlertService.getAlertSession();

        if (session == null) {
          showMessage(R.string.message_no_session);
          return false;
        }

        new AsyncTask<Void, Void, String>() {
          @Override
          protected String doInBackground (Void... arguments) {
            try {
              session.writeCommand("getStates");
              return null;
            } catch (IOException exception) {
              return exception.getMessage();
            }
          }

          @Override
          protected void onPostExecute (String error) {
          }
        }.execute();

        try {
          states.wait(ApplicationParameters.RESPONSE_TIMEOUT * 1000);
        } catch (InterruptedException exception) {
          showMessage(R.string.message_long_response);
          return false;
        }
      }
    }

    return true;
  }

  public final void addArea (View view) {
    if (getStates()) selectState();
  }

  public final void removeArea (View view) {
  }
}
