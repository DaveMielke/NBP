package org.nbp.b2g.ui.host;
import org.nbp.b2g.ui.*;

import android.app.Activity;
import android.os.Bundle;

import android.view.View;
import android.view.ViewGroup;

import android.widget.ScrollView;
import android.widget.HorizontalScrollView;

import android.widget.TextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Switch;

public abstract class ProgrammaticActivity extends Activity {
  protected ViewGroup newVerticalScrollContainer () {
    ScrollView view = new ScrollView(this);
    return view;
  }

  protected ViewGroup newVerticalScrollContainer (View content) {
    ViewGroup view = newVerticalScrollContainer();
    view.addView(content);
    return view;
  }

  protected ViewGroup newHorizontalScrollContainer () {
    HorizontalScrollView view = new HorizontalScrollView(this);
    return view;
  }

  protected ViewGroup newHorizontalScrollContainer (View content) {
    ViewGroup view = newHorizontalScrollContainer();
    view.addView(content);
    return view;
  }

  protected TextView newTextView () {
    TextView view = new TextView(this);
    view.setFocusable(true);
    return view;
  }

  protected TextView newTextView (CharSequence text) {
    TextView view = newTextView();
    view.setText(text);
    return view;
  }

  protected Button newButton (CharSequence label, Button.OnClickListener listener) {
    Button button = new Button(this);
    button.setText(label);
    button.setOnClickListener(listener);
    return button;
  }

  protected Button newButton (int label, Button.OnClickListener listener) {
    return newButton(ApplicationContext.getString(label), listener);
  }

  protected CheckBox newCheckBox (CharSequence label, CheckBox.OnCheckedChangeListener listener) {
    CheckBox checkBox = new CheckBox(this);
    checkBox.setText(label);
    checkBox.setOnCheckedChangeListener(listener);
    return checkBox;
  }

  protected CheckBox newCheckBox (int label, CheckBox.OnCheckedChangeListener listener) {
    return newCheckBox(ApplicationContext.getString(label), listener);
  }

  protected Switch newSwitch (Switch.OnCheckedChangeListener listener) {
    Switch view = new Switch(this);
    view.setOnCheckedChangeListener(listener);
    return view;
  }
  protected abstract View createContentView ();

  protected final int getLeftMargin () {
    return ApplicationContext.dipsToPixels(
      ApplicationParameters.SCREEN_LEFT_OFFSET
    );
  }

  private final void setContentView () {
    ViewGroup.LayoutParams parameters = new ViewGroup.LayoutParams(
      ViewGroup.LayoutParams.MATCH_PARENT,
      ViewGroup.LayoutParams.MATCH_PARENT
    );

    View view = createContentView();
    view.setLayoutParams(parameters);

    setContentView(view);
  }

  @Override
  public void onCreate (Bundle state) {
    super.onCreate(state);
    ApplicationContext.setContext(this);
    setContentView();
  }
}
