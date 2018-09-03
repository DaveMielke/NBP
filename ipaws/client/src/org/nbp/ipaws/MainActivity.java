package org.nbp.ipaws;

import android.util.Log;

import org.nbp.common.CommonActivity;
import android.os.Bundle;

import android.content.SharedPreferences;
import android.content.Intent;

import android.view.View;

import android.content.DialogInterface;
import android.app.AlertDialog;
import org.nbp.common.DialogFinisher;
import org.nbp.common.DialogHelper;

import java.io.IOException;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.HashSet;

public class MainActivity extends CommonActivity {
  private final static String LOG_TAG = MainActivity.class.getName();

  @Override
  protected void onCreate (Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);
    Controls.restore();
  }

  public final void stopSpeaking (View view) {
    AlertPlayer.stop();
  }

  private final boolean performServerAction (Object monitor, ServerAction action) {
    if (!action.perform()) {
      showMessage(R.string.message_no_session);
      return false;
    }

    if (monitor != null) {
      try {
        monitor.wait(ApplicationParameters.RESPONSE_TIMEOUT);
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
        public boolean perform (ServerSession session) {
          return session.setAreas();
        }
      }
    );
  }

  private final boolean getStates () {
    List<Areas.State> states = Areas.getStates();

    synchronized (states) {
      if (!states.isEmpty()) return true;

      return performServerAction(states,
        new ServerAction() {
          @Override
          public boolean perform (ServerSession session) {
            return session.getStates();
          }
        }
      );
    }
  }

  private final boolean getCounties (final Areas.State state) {
    List<Areas.County> counties = state.getCounties();

    synchronized (counties) {
      if (!counties.isEmpty()) return true;

      return performServerAction(counties,
        new ServerAction() {
          @Override
          public boolean perform (ServerSession session) {
            return session.getCounties(state);
          }
        }
      );
    }
  }

  private final void showAlert (String identifier) {
    Alerts.Descriptor alert = Alerts.get(identifier);

    if (alert != null) {
      showDialog(R.string.alert_title, R.layout.alert, alert);
    } else {
      showMessage(R.string.message_expired_alert);
    }
  }

  public final void currentAlerts (View view) {
    final String[] identifiers = Alerts.list(true);
    int count = identifiers.length;

    if (count == 0) {
      showMessage(R.string.message_no_alerts);
      return;
    }

    if (count == 1) {
      showAlert(identifiers[0]);
      return;
    }

    String[] summaries = new String[count];

    for (int index=0; index<count; index+=1) {
      String identifier = identifiers[index];
      Alerts.Descriptor alert = Alerts.get(identifier);
      summaries[index] = (alert != null)? alert.getSummary(): identifier;
    }

    DialogInterface.OnClickListener choiceListener =
      new DialogInterface.OnClickListener() {
        @Override
        public void onClick (DialogInterface dialog, int index) {
          showAlert(identifiers[index]);
        }
      };

    newAlertDialogBuilder(R.string.action_selectAlert)
      .setCancelable(true)
      .setNegativeButton(android.R.string.no, null)
      .setItems(summaries, choiceListener)
      .show();
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

  public final void requestedAreas (View view) {
    final Set<String> areas = new HashSet(ApplicationComponent.getRequestedAreas());

    if (areas.isEmpty()) {
      showMessage(R.string.message_no_areas);
      return;
    }

    class Entry {
      public final String area;
      public final String name;

      public Entry (String area, String name) {
        this.area = area;
        this.name = name;
      }
    }

    final int count = areas.size();
    final Entry[] entries = new Entry[count];

    {
      boolean gotStates = false;
      int index = 0;

      for (String area : areas) {
        String name;

        String SAME = area.substring(1, 3);
        Areas.State state = Areas.getStateByArea(SAME);

        if (!gotStates) {
          if (state == null) {
            if (!getStates()) return;
            state = Areas.getStateByArea(SAME);
          }

          gotStates = true;
        }

        if (area.endsWith("000")) {
          if (state == null) {
            name = "(unknown state)";
          } else {
            name = state.getName();
          }
        } else {
          Areas.County county = Areas.getCountyByArea(area);

          if (county == null) {
            if (state != null) {
              if (!getCounties(state)) return;
              county = Areas.getCountyByArea(area);
            }
          }

          if (county == null) {
            name = "(unknown county)";
            if (state != null) name += ", " + state.getAbbreviation();
          } else {
            name = county.getName() + ", " + county.getState().getAbbreviation();
          }
        }

        entries[index] = new Entry(area, name);
        index += 1;
      }
    }

    Arrays.sort(entries,
      new Comparator<Entry>() {
        @Override
        public int compare (Entry entry1, Entry entry2) {
          return entry1.name.compareTo(entry2.name);
        }
      }
    );

    String[] names = new String[count];
    final boolean[] selection = new boolean[count];

    for (int index=0; index<count; index+=1) {
      names[index] = entries[index].name;
      selection[index] = true;
    }

    DialogInterface.OnMultiChoiceClickListener choiceListener =
      new DialogInterface.OnMultiChoiceClickListener() {
        @Override
        public void onClick (DialogInterface dialog, int index, boolean isChecked) {
          selection[index] = isChecked;
        }
      };

    DialogInterface.OnClickListener saveListener =
      new DialogInterface.OnClickListener() {
        @Override
        public void onClick (DialogInterface dialog, int button) {
          Set<String> remove = new HashSet<String>();

          for (int index=0; index<count; index+=1) {
            if (!selection[index]) {
              remove.add(entries[index].area);
            }
          }

          if (!remove.isEmpty()) {
            areas.removeAll(remove);
            setRequestedAreas(areas);
          }
        }
      };

    newAlertDialogBuilder(R.string.action_requestedAreas)
      .setCancelable(true)
      .setNegativeButton(android.R.string.no, null)
      .setPositiveButton(R.string.option_save, saveListener)
      .setMultiChoiceItems(names, selection, choiceListener)
      .show();
  }

  public final void applicationSettings (View view) {
    Intent intent = new Intent(this, SettingsActivity.class);
    startActivity(intent);
  }

  public final void aboutApplication (View view) {
    DialogFinisher finisher = new DialogFinisher() {
      @Override
      public void finishDialog (DialogHelper helper) {
        helper.setText(R.id.about_versionNumber, R.string.NBP_IPAWS_version_name);
        helper.setText(R.id.about_buildTime, R.string.NBP_IPAWS_build_time);
        helper.setText(R.id.about_sourceRevision, R.string.NBP_IPAWS_source_revision);
        helper.setTextFromAsset(R.id.about_copyright, "copyright");
      }
    };

    showDialog(R.string.action_aboutApplication, R.layout.about, finisher);
  }
}
