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

  private final boolean requestData (Object object, final String command) {
    final AlertSession session = AlertService.getAlertSession();

    if (session == null) {
      showMessage(R.string.message_no_session);
      return false;
    }

    new AsyncTask<Void, Void, String>() {
      @Override
      protected String doInBackground (Void... arguments) {
        try {
          session.writeCommand(command);
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
      object.wait(ApplicationParameters.RESPONSE_TIMEOUT * 1000);
    } catch (InterruptedException exception) {
      showMessage(R.string.message_long_response);
      return false;
    }

    return true;
  }

  private final boolean getCounties (Areas.State state) {
    List<Areas.County> counties = state.getCounties();

    synchronized (counties) {
      if (!counties.isEmpty()) return true;
      return requestData(counties, ("getCounties " + state.getAbbreviation()));
    }
  }

  private final void selectCounties (Areas.State state) {
    final List<Areas.County> counties = state.getCounties();
    String[] names;

    synchronized (counties) {
      int count = counties.size();
      names = new String[count];

      for (int index=0; index<count; index+=1) {
        names[index] = counties.get(index).getName();
      }
    }

    DialogInterface.OnClickListener itemListener =
      new DialogInterface.OnClickListener() {
        @Override
        public void onClick (DialogInterface dialog, int index) {
        }
      };

    DialogInterface.OnClickListener cancelListener =
      new DialogInterface.OnClickListener() {
        @Override
        public void onClick (DialogInterface dialog, int button) {
        }
      };

    newAlertDialogBuilder(R.string.action_selectCounties)
      .setCancelable(true)
      .setNegativeButton(android.R.string.no, cancelListener)
      .setItems(names, itemListener)
      .show();
  }

  private final boolean getStates () {
    List<Areas.State> states = Areas.getStates();

    synchronized (states) {
      if (!states.isEmpty()) return true;
      return requestData(states, "getStates");
    }
  }

  private final void selectState () {
    final List<Areas.State> states = Areas.getStates();
    String[] names;

    synchronized (states) {
      int count = states.size();
      names = new String[count];

      for (int index=0; index<count; index+=1) {
        names[index] = states.get(index).getName();
      }
    }

    DialogInterface.OnClickListener itemListener =
      new DialogInterface.OnClickListener() {
        @Override
        public void onClick (DialogInterface dialog, int index) {
          Areas.State state = states.get(index);
          if (getCounties(state)) selectCounties(state);
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

  public final void manageAreas (View view) {
    if (getStates()) selectState();
  }
}
