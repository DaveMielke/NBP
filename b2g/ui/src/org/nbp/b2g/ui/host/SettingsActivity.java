package org.nbp.b2g.ui.host;
import org.nbp.b2g.ui.*;

import java.util.List;
import java.util.ArrayList;

import org.nbp.common.CommonActivity;
import org.nbp.common.LaunchUtilities;
import org.nbp.common.Tones;

import android.util.Log;
import android.os.Bundle;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;

import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;

import android.widget.TableLayout;
import android.widget.TableRow;

import android.widget.ListView;
import android.widget.AdapterView;

import android.widget.TextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

import android.app.AlertDialog;
import android.content.DialogInterface;

public class SettingsActivity extends CommonActivity {
  private final static String LOG_TAG = SettingsActivity.class.getName();

  private final static int FRAGMENT_CONTAINER_ID = R.id.settings_fragment_container;

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
    return newHorizontalGroup(
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
          if (!control.previousValue()) Tones.beep();
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
          if (!control.nextValue()) Tones.beep();
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

  private View createControlView (Control control) {
    ViewGroup group = new TableRow(this);
    group.addView(createLabelView(control));

    if (control instanceof BooleanControl) {
      group.addView(createBooleanValueView(control));
    } else {
      group.addView(createIntegerValueView(control));

      if (control instanceof EnumerationControl) {
        group.addView(createEnumerationChangeButton(control));
      } else {
        group.addView(createPreviousValueButton(control));
        group.addView(createNextValueButton(control));
      }
    }

    return group;
  }

  private Fragment createFragment (final View view, final int title) {
    return new Fragment() {
      private CharSequence titleText = null;
      private TextView titleView = null;
      private View focusedView = null;

      @Override
      public void onCreate (Bundle state) {
        super.onCreate(state);
        titleText = getString(title);
        titleView = (TextView)findViewById(R.id.settings_fragment_title);
      }

      @Override
      public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle state) {
        return view;
      }

      @Override
      public void onResume () {
        super.onResume();
        titleView.setText(titleText);

        if (focusedView != null) {
          focusedView.requestFocus();
        } else {
          view.requestFocus();
        }
      }

      @Override
      public void onPause () {
        try {
          if (view instanceof ViewGroup) {
            ViewGroup group = (ViewGroup)view;
            View focused = group.findFocus();
            if (focused != null) focusedView = focused;
          }
        } finally {
          super.onPause();
        }
      }
    };
  }

  private Fragment createControlGroupsFragment () {
    int groupCount = ControlGroup.values().length;
    String[] groupLabels = new String[groupCount];
    final Fragment[] groupFragments = new Fragment[groupCount];

    for (ControlGroup group : ControlGroup.values()) {
      TableLayout table = new TableLayout(this);

      {
        int index = group.ordinal();
        int label = group.getLabel();

        groupLabels[index] = getString(label);
        groupFragments[index] = createFragment(table, label);
      }

      for (Control control : group.getControls()) {
        table.addView(createControlView(control));
      }
    }

    ListView list = newListView(groupLabels);

    final Fragment groupsFragment = createFragment(
      newVerticalGroup(createActionsView(), list),
      R.string.control_groups
    );

    list.setOnItemClickListener(
      new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick (AdapterView list, View item, int position, long id) {
          list.setSelection(position);

          getFragmentManager().beginTransaction()
            .replace(FRAGMENT_CONTAINER_ID, groupFragments[position])
            .addToBackStack(null)
            .commit();
        }
      }
    );

    return groupsFragment;
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
  public void onCreate (Bundle state) {
    super.onCreate(state);
    setContentView(R.layout.settings);

ViewGroup container = (ViewGroup)findViewById(R.id.settings_fragment_container);
    FragmentTransaction transaction = getFragmentManager().beginTransaction();
    transaction.add(FRAGMENT_CONTAINER_ID, createControlGroupsFragment());
    transaction.commit();
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
