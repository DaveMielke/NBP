package org.nbp.b2g.ui;

import android.util.Log;
import android.os.Bundle;
import android.app.Activity;

import android.view.View;
import android.view.ViewGroup;

import android.widget.LinearLayout;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Switch;

public class ControlsActivity extends Activity {
  private final static String LOG_TAG = ControlsActivity.class.getName();

  private View createSaveControlsButton () {
    Button button = new Button(this);
    button.setText(R.string.save_action_label);

    button.setOnClickListener(new Button.OnClickListener() {
      @Override
      public void onClick (View view) {
        Controls.saveControls();
        ApplicationUtilities.message(R.string.save_action_confirmation);
      }
    });

    return button;
  }

  private View createRestoreControlsButton () {
    Button button = new Button(this);
    button.setText(R.string.restore_action_label);

    button.setOnClickListener(new Button.OnClickListener() {
      @Override
      public void onClick (View view) {
        Controls.restoreControls();
        ApplicationUtilities.message(R.string.restore_action_confirmation);
      }
    });

    return button;
  }

  private View createResetControlsButton () {
    Button button = new Button(this);
    button.setText(R.string.reset_action_label);

    button.setOnClickListener(new Button.OnClickListener() {
      @Override
      public void onClick (View view) {
        Controls.resetControls();
        ApplicationUtilities.message(R.string.reset_action_confirmation);
      }
    });

    return button;
  }

  private View createActionsView () {
    LinearLayout view = new LinearLayout(this);
    view.setOrientation(view.HORIZONTAL);

    LinearLayout.LayoutParams parameters = new LinearLayout.LayoutParams(
      LinearLayout.LayoutParams.WRAP_CONTENT,
      LinearLayout.LayoutParams.MATCH_PARENT
    );

    view.addView(createSaveControlsButton(), parameters);
    view.addView(createRestoreControlsButton(), parameters);
    view.addView(createResetControlsButton(), parameters);

    return view;
  }

  private View createIncreaseValueButton (final Control control) {
    Button button = new Button(this);
    button.setText(R.string.numeric_control_increase);

    button.setOnClickListener(new Button.OnClickListener() {
      @Override
      public void onClick (View view) {
        if (!control.setNextValue()) {
          ApplicationUtilities.beep();
        }
      }
    });

    return button;
  }

  private View createDecreaseValueButton (final Control control) {
    Button button = new Button(this);
    button.setText(R.string.numeric_control_decrease);

    button.setOnClickListener(new Button.OnClickListener() {
      @Override
      public void onClick (View view) {
        if (!control.setPreviousValue()) {
          ApplicationUtilities.beep();
        }
      }
    });

    return button;
  }

  private View createControlLabelView (Control control) {
    TextView view = new TextView(this);
    view.setText(control.getLabel());
    return view;
  }

  private View createBooleanValueView (Control control) {
    final Switch view = new Switch(this);
    view.setChecked(((BooleanControl)control).getBooleanValue());

    Control.OnValueChangeListener listener = new Control.OnValueChangeListener() {
      @Override
      public void onValueChange (final Control control) {
        ControlsActivity.this.runOnUiThread(new Runnable() {
          @Override
          public void run () {
            Switch s = (Switch)view;
          }
        });
      }
    };

    control.addOnValueChangeListener(listener);
    return view;
  }

  private View createIntegerValueView (Control control) {
    final TextView view = new TextView(this);
    view.setText(control.getValue());

    Control.OnValueChangeListener listener = new Control.OnValueChangeListener() {
      @Override
      public void onValueChange (final Control control) {
        ControlsActivity.this.runOnUiThread(new Runnable() {
          @Override
          public void run () {
            TextView t = (TextView)view;
            t.setText(control.getValue());
          }
        });
      }
    };

    control.addOnValueChangeListener(listener);
    return view;
  }

  private View createControlsView () {
    final GridLayout view = new GridLayout(this);
    view.setOrientation(view.VERTICAL);

    Controls.forEachControl(new ControlProcessor() {
      private void setColumn (int row, int column, View content) {
        view.addView(content, new GridLayout.LayoutParams(view.spec(row), view.spec(column)));
      }

      @Override
      public boolean processControl (Control control) {
        int row = view.getRowCount();
        setColumn(row, 0, createControlLabelView(control));

        if (control instanceof BooleanControl) {
          setColumn(row, 1, createBooleanValueView(control));
        } else {
          setColumn(row, 1, createIntegerValueView(control));
          setColumn(row, 2, createDecreaseValueButton(control));
          setColumn(row, 3, createIncreaseValueButton(control));
        }

        return true;
      }
    });

    return view;
  }

  private View createRootView () {
    LinearLayout view = new LinearLayout(this);
    view.setOrientation(view.VERTICAL);

     LinearLayout.LayoutParams parameters = new LinearLayout.LayoutParams(
      LinearLayout.LayoutParams.MATCH_PARENT,
      LinearLayout.LayoutParams.WRAP_CONTENT
    );

    view.addView(createActionsView(), parameters);
    view.addView(createControlsView(), parameters);

    view.setLayoutParams(new ViewGroup.LayoutParams(
      ViewGroup.LayoutParams.MATCH_PARENT,
      ViewGroup.LayoutParams.MATCH_PARENT
    ));

    return view;
  }

  @Override
  public void onCreate (Bundle state) {
    super.onCreate(state);
    setContentView(createRootView());
  }
}
