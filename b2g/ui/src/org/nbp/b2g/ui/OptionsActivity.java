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

public class OptionsActivity extends Activity {
  private final static String LOG_TAG = OptionsActivity.class.getName();

  private void monitorControlValue (Control control, final View view) {
    Control.OnValueChangeListener listener = new Control.OnValueChangeListener() {
      @Override
      public void onValueChange (final Control control) {
        OptionsActivity.this.runOnUiThread(new Runnable() {
          @Override
          public void run () {
            if (view instanceof TextView) {
              TextView textView = (TextView)view;
              textView.setText(control.getValue());
            }
          }
        });
      }
    };

    control.addOnValueChangeListener(listener);
  }

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

  private View createControlValueView (Control control) {
    TextView view = new TextView(this);
    view.setText(control.getValue());
    monitorControlValue(control, view);
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
        View label = createControlLabelView(control);
        View value = createControlValueView(control);
        View decrease = createDecreaseValueButton(control);
        View increase = createIncreaseValueButton(control);

        int row = view.getRowCount();
        setColumn(row, 0, label);
        setColumn(row, 1, decrease);
        setColumn(row, 2, value);
        setColumn(row, 3, increase);

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
