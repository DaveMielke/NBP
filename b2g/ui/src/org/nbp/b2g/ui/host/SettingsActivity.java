package org.nbp.b2g.ui.host;
import org.nbp.b2g.ui.*;

import java.util.List;
import java.util.ArrayList;

import org.nbp.common.ProgrammaticActivity;
import org.nbp.common.LaunchUtilities;

import android.util.Log;

import android.view.View;
import android.view.ViewGroup;

import android.widget.LinearLayout;
import android.widget.GridLayout;

import android.widget.TextView;
import android.widget.Button;

import android.widget.CompoundButton;
import android.widget.Switch;

import android.app.AlertDialog;
import android.content.DialogInterface;

public class SettingsActivity extends ProgrammaticActivity {
  private final static String LOG_TAG = SettingsActivity.class.getName();

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

  private final List<View> developerViews = new ArrayList<View>();

  private void setDeveloperViewVisibility (boolean visible) {
    int visibility = visible? View.VISIBLE: View.GONE;

    for (View view : developerViews) {
      view.setVisibility(visibility);
    }
  }

  private void setDeveloperViewVisibility (Control control) {
    setDeveloperViewVisibility(((BooleanControl)control).getBooleanValue());
  }

  private void addDeveloperEnabledControlListener () {
    Control control = Controls.getDeveloperEnabledControl();
    setDeveloperViewVisibility(control);

    addControlValueChangedListener(control,
      new Control.OnValueChangedListener() {
        @Override
        public void onValueChanged (final Control control) {
          updateWidget(new Runnable() {
            @Override
            public void run () {
              setDeveloperViewVisibility(control);
            }
          });
        }
      }
    );
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

  private View createControlLabelView (Control control) {
    TextView view = newTextView(control.getLabel());
    return view;
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

  private View createControlsView () {
    final GridLayout view = new GridLayout(this);
    view.setOrientation(view.VERTICAL);

    Controls.forEachControl(new ControlProcessor() {
      private boolean isForDevelopers;

      private void setColumn (int row, int column, View content) {
        view.addView(content, new GridLayout.LayoutParams(view.spec(row), view.spec(column)));
        if (isForDevelopers) developerViews.add(content);
      }

      private void setRow (int row, Control control) {
        isForDevelopers = control.isForDevelopers();
        setColumn(row, 0, createControlLabelView(control));

        if (control instanceof BooleanControl) {
          setColumn(row, 1, createBooleanValueView(control));
        } else {
          setColumn(row, 1, createIntegerValueView(control));

          if (control instanceof EnumerationControl) {
            setColumn(row, 2, createEnumerationChangeButton(control));
          } else {
            setColumn(row, 2, createPreviousValueButton(control));
            setColumn(row, 3, createNextValueButton(control));
          }
        }
      }

      @Override
      public boolean processControl (Control control) {
        int row = view.getRowCount();
        setRow(row, control);
        return true;
      }
    });

    addDeveloperEnabledControlListener();
    return newVerticalScrollContainer(view);
  }

  @Override
  protected final View createContentView () {
    return createVerticalGroup(
      createActionsView(),
      createControlsView()
    );
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
