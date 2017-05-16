package org.nbp.b2g.ui.host;
import org.nbp.b2g.ui.*;

import java.util.List;
import java.util.ArrayList;

import org.nbp.common.ProgrammaticActivity;
import org.nbp.common.LaunchUtilities;

import android.util.Log;
import android.os.Bundle;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;

import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;

import android.widget.FrameLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

import android.app.AlertDialog;
import android.content.DialogInterface;

public class SettingsActivity extends ProgrammaticActivity {
  private final static String LOG_TAG = SettingsActivity.class.getName();

  private final static int FRAGMENT_CONTAINER_ID = 1;

  private void updateWidget (Runnable runnable) {
    runOnUiThread(runnable);
  }

  private static class ControlValueChangedListener {
    public final Control control;
    public final Control.OnValueChangedListener listener;

    public ControlValueChangedListener (Control control, Control.OnValueChangedListener listener) {
      this.control = control;
      this.listener = listener;
    }
  }

  private final List<ControlValueChangedListener> controlValueChangedListeners = new ArrayList<ControlValueChangedListener>();

  private void addControlValueChangedListener (Control control, Control.OnValueChangedListener listener) {
    control.addOnValueChangedListener(listener);
    controlValueChangedListeners.add(new ControlValueChangedListener(control, listener));
  }

  private void removeControlValueChangedListeners () {
    for (ControlValueChangedListener entry : controlValueChangedListeners) {
      entry.control.removeOnValueChangedListener(entry.listener);
    }

    controlValueChangedListeners.clear();
  }

  private static void setChecked (CompoundButton button, Control control) {
    button.setChecked(((BooleanControl)control).getBooleanValue());
  }

  private View createSaveControlsButton () {
    Button button = newButton(
      R.string.SaveSettings_action_label,
      new Button.OnClickListener() {
        @Override
        public void onClick (View view) {
          Controls.saveValues();
          ApplicationUtilities.message(R.string.SaveSettings_action_confirmation);
        }
      }
    );

    return button;
  }

  private View createRestoreControlsButton () {
    Button button = newButton(
      R.string.RestoreSettings_action_label,
      new Button.OnClickListener() {
        @Override
        public void onClick (View view) {
          Controls.restoreSavedValues();
          ApplicationUtilities.message(R.string.RestoreSettings_action_confirmation);
        }
      }
    );

    return button;
  }

  private View createResetControlsButton () {
    Button button = newButton(
      R.string.ResetSettings_action_label,
      new Button.OnClickListener() {
        @Override
        public void onClick (View view) {
          Controls.restoreDefaultValues();
          ApplicationUtilities.message(R.string.ResetSettings_action_confirmation);
        }
      }
    );

    return button;
  }

  private View createSystemMaintenanceButton () {
    Button button = newButton(
      R.string.SystemMaintenance_action_label,
      new Button.OnClickListener() {
        @Override
        public void onClick (View view) {
          LaunchUtilities.launchActivity(MaintenanceActivity.class);
        }
      }
    );

    return button;
  }

  private View createActionsView () {
    return createHorizontalGroup(
      createSaveControlsButton(),
      createRestoreControlsButton(),
      createResetControlsButton(),
      createSystemMaintenanceButton()
    );
  }

  private View createEnumerationChangeButton (final Control control) {
    final EnumerationControl ec = (EnumerationControl)control;
    final CharSequence[] labels = ec.getValueLabels();

    final DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
      @Override
      public void onClick (DialogInterface dialog, int which) {
        ec.setValue(which);
        dialog.dismiss();
      }
    };

    final AlertDialog.Builder builder = new AlertDialog.Builder(this)
      .setTitle(ec.getLabel())
      .setNeutralButton(R.string.button_dialog_cancel, null)
      .setCancelable(true);

    Button button = newButton(
      R.string.button_settings_change,
      new Button.OnClickListener() {
        @Override
        public void onClick (View view) {
          builder.setSingleChoiceItems(labels, ec.getIntegerValue(), listener)
                 .show();
        }
      }
    );

    return button;
  }

  private View createPreviousValueButton (final Control control) {
    Button button = newButton(
      control.getPreviousLabel(),
      new Button.OnClickListener() {
        @Override
        public void onClick (View view) {
          if (!control.previousValue()) {
            Devices.tone.get().beep();
          }
        }
      }
    );

    return button;
  }

  private View createNextValueButton (final Control control) {
    Button button = newButton(
      control.getNextLabel(),
      new Button.OnClickListener() {
        @Override
        public void onClick (View view) {
          if (!control.nextValue()) {
            Devices.tone.get().beep();
          }
        }
      }
    );

    return button;
  }

  private View createLabelView (CharSequence label) {
    TextView view = newTextView(label);
    return view;
  }

  private View createLabelView (int label) {
    return createLabelView(getString(label));
  }

  private View createLabelView (Control control) {
    return createLabelView(control.getLabel());
  }

  private View createBooleanValueView (final Control control) {
    final Switch view = newSwitch(
      new Switch.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged (CompoundButton button, boolean isChecked) {
          if (isChecked) {
            control.nextValue();
          } else {
            control.previousValue();
          }
        }
      }
    );

    view.setTextOff(control.getPreviousLabel());
    view.setTextOn(control.getNextLabel());
    setChecked(view, control);

    addControlValueChangedListener(control,
      new Control.OnValueChangedListener() {
        @Override
        public void onValueChanged (final Control control) {
          updateWidget(new Runnable() {
            @Override
            public void run () {
              setChecked(view, control);
            }
          });
        }
      }
    );

    return view;
  }

  private View createIntegerValueView (Control control) {
    final TextView view = newTextView(control.getValue());

    addControlValueChangedListener(control,
      new Control.OnValueChangedListener() {
        @Override
        public void onValueChanged (final Control control) {
          updateWidget(new Runnable() {
            @Override
            public void run () {
              view.setText(control.getValue());
            }
          });
        }
      }
    );

    return view;
  }

  private Fragment createFragment (final View view) {
    return new Fragment() {
      @Override
      public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle state) {
        return view;
      }
    };
  }

  private Fragment createControlGroupListFragment () {
    int groupCount = ControlGroup.values().length;
    String[] groupLabels = new String[groupCount];
    final Fragment[] groupFragments = new Fragment[groupCount];

    for (ControlGroup group : ControlGroup.values()) {
      TableLayout table = new TableLayout(this);

      {
        int index = group.ordinal();

        String label = getString(group.getLabel());
        groupLabels[index] = label;

        View view = createVerticalGroup(createLabelView(label), table);
        groupFragments[index] = createFragment(view);
      }

      for (Control control : group.getControls()) {
        TableRow row = new TableRow(this);
        table.addView(row);
        row.addView(createLabelView(control));

        if (control instanceof BooleanControl) {
          row.addView(createBooleanValueView(control));
        } else {
          row.addView(createIntegerValueView(control));

          if (control instanceof EnumerationControl) {
            row.addView(createEnumerationChangeButton(control));
          } else {
            row.addView(createPreviousValueButton(control));
            row.addView(createNextValueButton(control));
          }
        }
      }
    }

    ArrayAdapter<String> adapter = new ArrayAdapter<String>(
      this, android.R.layout.simple_list_item_1, android.R.id.text1, groupLabels
    );

    ListView list = new ListView(this);
    list.setAdapter(adapter);

    list.setOnItemClickListener(
      new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick (AdapterView adapter, View view, int position, long id) {
          FragmentTransaction transaction = getFragmentManager().beginTransaction();
          transaction.replace(FRAGMENT_CONTAINER_ID, groupFragments[position])
                     .addToBackStack(null)
                     .commit();
        }
      }
    );

    return createFragment(createVerticalGroup(createActionsView(), list));
  }

  @Override
  protected final View createContentView () {
    ViewGroup container = new FrameLayout(this);
    container.setId(FRAGMENT_CONTAINER_ID);
    return container;
  }

  @Override
  protected void onContentViewSet () {
    super.onContentViewSet();

    FragmentTransaction transaction = getFragmentManager().beginTransaction();
    transaction.add(FRAGMENT_CONTAINER_ID, createControlGroupListFragment());
    transaction.commit();
  }

  @Override
  public void onBackPressed () {
    FragmentManager fm = getFragmentManager();

    if (fm.getBackStackEntryCount() > 0) {
      fm.popBackStack();
    } else {
      super.onBackPressed();
    }
  }

  @Override
  public void onDestroy () {
    try {
      removeControlValueChangedListeners();
    } finally {
      super.onDestroy();
    }
  }
}
