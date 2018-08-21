package org.nbp.ipaws;

import android.util.Log;

import org.nbp.common.CommonActivity;
import android.os.Bundle;

import android.content.SharedPreferences;
import android.content.Intent;

import android.view.View;
import android.widget.Switch;

import android.content.DialogInterface;
import android.app.AlertDialog;

import java.io.IOException;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.HashSet;

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

  private interface ServerAction {
    public void perform (ServerSession session) throws IOException;
  }

  private final boolean performServerAction (Object object, final ServerAction action) {
    final ServerSession session = AlertService.getServerSession();

    if (session == null) {
      showMessage(R.string.message_no_session);
      return false;
    }

    new Thread() {
      @Override
      public void run () {
        try {
          action.perform(session);
        } catch (IOException exception) {
          Log.w(LOG_TAG, ("server communication error: " + exception.getMessage()));
        }
      }
    }.start();

    if (object != null) {
      try {
        object.wait(ApplicationParameters.RESPONSE_TIMEOUT * 1000);
      } catch (InterruptedException exception) {
        showMessage(R.string.message_long_response);
        return false;
      }
    }

    return true;
  }

  private final boolean performServerAction (ServerAction action) {
    return performServerAction(null, action);
  }

  private final void setRequestedAreas (Set<String> areas) {
    ApplicationComponent.getSettings()
                        .edit()
                        .putStringSet(ApplicationComponent.SETTING_REQUESTED_AREAS, areas)
                        .apply();

    performServerAction(
      new ServerAction() {
        @Override
        public void perform (ServerSession session) throws IOException {
          session.setAreas();
        }
      }
    );
  }

  private final boolean getCounties (final Areas.State state) {
    List<Areas.County> counties = state.getCounties();

    synchronized (counties) {
      if (!counties.isEmpty()) return true;

      return performServerAction(counties,
        new ServerAction() {
          @Override
          public void perform (ServerSession session) throws IOException {
            session.getCounties(state);
          }
        }
      );
    }
  }

  private final void selectCounties (Areas.State state) {
    final Set<String> allAreas = new HashSet(ApplicationComponent.getRequestedAreas());
    Set<String> stateAreas = new HashSet<String>();

    {
      String stateArea = state.getSAME();

      for (String area : allAreas) {
        if (stateArea.equals(area.substring(1, 3))) {
          stateAreas.add(area);
        }
      }

      allAreas.remove(stateAreas);
    }

    final List<Areas.County> counties = state.getCounties();
    String[] names;
    final String[] areas;
    final boolean[] selection;

    synchronized (counties) {
      int count = counties.size() + 1;
      names = new String[count];
      areas = new String[count];
      selection = new boolean[count];

      int index = 0;
      names[index] = "Entire State";
      areas[index] = "0" + state.getSAME() + "000";

      while (++index < count) {
        Areas.County county = counties.get(index - 1);
        names[index] = county.getName();
        areas[index] = county.getSAME();;
      }
    }

    {
      int count = selection.length;

      for (int index=0; index<count; index+=1) {
        selection[index] = stateAreas.contains(areas[index]);
      }
    }

    DialogInterface.OnMultiChoiceClickListener choiceListener =
      new DialogInterface.OnMultiChoiceClickListener() {
        public void deselectItem (DialogInterface dialog, int index) {
          ((AlertDialog)dialog).getListView().setItemChecked(index, false);
          selection[index] = false;
        }

        @Override
        public void onClick (DialogInterface dialog, int index, boolean isChecked) {
          selection[index] = isChecked;

          if (isChecked) {
            if (index == 0) {
              int count = selection.length;
              while (++index < count) deselectItem(dialog, index);
            } else {
              deselectItem(dialog, 0);
            }
          }
        }
      };

    DialogInterface.OnClickListener saveListener =
      new DialogInterface.OnClickListener() {
        @Override
        public void onClick (DialogInterface dialog, int button) {
          int count = selection.length;

          for (int index=0; index<count; index+=1) {
            if (selection[index]) {
              allAreas.add(areas[index]);
            }
          }

          setRequestedAreas(allAreas);
        }
      };

    newAlertDialogBuilder(R.string.action_selectCounties)
      .setCancelable(true)
      .setNegativeButton(android.R.string.no, null)
      .setPositiveButton(R.string.option_save, saveListener)
      .setMultiChoiceItems(names, selection, choiceListener)
      .show();
  }

  private final boolean getStates () {
    List<Areas.State> states = Areas.getStates();

    synchronized (states) {
      if (!states.isEmpty()) return true;

      return performServerAction(states,
        new ServerAction() {
          @Override
          public void perform (ServerSession session) throws IOException {
            session.getStates();
          }
        }
      );
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

    DialogInterface.OnClickListener choiceListener =
      new DialogInterface.OnClickListener() {
        @Override
        public void onClick (DialogInterface dialog, int index) {
          Areas.State state = states.get(index);
          if (getCounties(state)) selectCounties(state);
        }
      };

    newAlertDialogBuilder(R.string.action_selectState)
      .setCancelable(true)
      .setNegativeButton(android.R.string.no, null)
      .setItems(names, choiceListener)
      .show();
  }

  public final void manageAreas (View view) {
    if (getStates()) selectState();
  }

  public final void removeAreas (View view) {
    Set<String> areas = ApplicationComponent.getRequestedAreas();

    if (areas.isEmpty()) {
      showMessage(R.string.message_no_areas);
      return;
    }

    if (!getStates()) return;

    int count = areas.size();
    final String[] names = new String[count];
    final boolean[] selection = new boolean[count];

    {
      int index = 0;

      for (String area : areas) {
        String name;
        Areas.State state;

        {
          String SAME = area.substring(1, 3);
          state = Areas.getStateByArea(SAME);

          if (state == null) {
            if (!getStates()) return;
            state = Areas.getStateByArea(SAME);
          }
        }

        if (area.endsWith("000")) {
          name = state.getName();
        } else {
          Areas.County county = Areas.getCountyByArea(area);

          if (county == null) {
            if (!getCounties(state)) return;
            county = Areas.getCountyByArea(area);
          }

          name = county.getName() + ", " + state.getAbbreviation();
        }

        names[index] = name;
        selection[index] = false;
        index += 1;
      }
    }

    DialogInterface.OnMultiChoiceClickListener choiceListener =
      new DialogInterface.OnMultiChoiceClickListener() {
        @Override
        public void onClick (DialogInterface dialog, int index, boolean isChecked) {
          selection[index] = isChecked;
        }
      };

    DialogInterface.OnClickListener removeListener =
      new DialogInterface.OnClickListener() {
        @Override
        public void onClick (DialogInterface dialog, int button) {
        }
      };

    newAlertDialogBuilder(R.string.action_removeAreas)
      .setCancelable(true)
      .setNegativeButton(android.R.string.no, null)
      .setPositiveButton(R.string.option_remove, removeListener)
      .setMultiChoiceItems(names, selection, choiceListener)
      .show();
  }
}
