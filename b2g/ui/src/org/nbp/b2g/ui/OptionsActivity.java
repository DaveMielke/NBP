package org.nbp.b2g.ui;

import android.util.Log;
import android.os.Bundle;
import android.app.Activity;

import android.view.ViewGroup;
import android.view.View;

import android.widget.LinearLayout;
import android.widget.TextView;

public class OptionsActivity extends Activity {
  private final static String LOG_TAG = OptionsActivity.class.getName();

  private final static LinearLayout.LayoutParams rootParameters = new LinearLayout.LayoutParams(
    LinearLayout.LayoutParams.MATCH_PARENT,
    LinearLayout.LayoutParams.MATCH_PARENT
  );

  private final static ViewGroup.LayoutParams controlLabelParameters = new ViewGroup.LayoutParams(
    ViewGroup.LayoutParams.FILL_PARENT,
    ViewGroup.LayoutParams.WRAP_CONTENT
  );

  private View createControlLabel (Control control) {
    TextView label = new TextView(this);
    label.setText(control.getLabel());
    label.setLayoutParams(controlLabelParameters);
    return label;
  }

  private View createControlView (Control control) {
    return createControlLabel(control);
  }

  private View createRootView () {
    final LinearLayout rootView = new LinearLayout(this);
    rootView.setOrientation(rootView.VERTICAL);

    Controls.forEachControl(new ControlProcessor() {
      @Override
      public boolean processControl (Control control) {
        rootView.addView(createControlView(control));
        return true;
      }
    });

    rootView.setLayoutParams(rootParameters);
    return rootView;
  }

  @Override
  public void onCreate (Bundle state) {
    super.onCreate(state);
    setContentView(createRootView());
  }
}
