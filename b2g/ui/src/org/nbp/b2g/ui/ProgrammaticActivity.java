package org.nbp.b2g.ui;

import android.app.Activity;

import android.view.View;
import android.view.ViewGroup;

import android.widget.ScrollView;
import android.widget.HorizontalScrollView;

import android.widget.TextView;
import android.widget.Button;

public abstract class ProgrammaticActivity extends Activity {
  protected ViewGroup createVerticalScrollContainer () {
    ScrollView view = new ScrollView(this);
    return view;
  }

  protected ViewGroup createVerticalScrollContainer (View content) {
    ViewGroup view = createVerticalScrollContainer();
    view.addView(content);
    return view;
  }

  protected ViewGroup createHorizontalScrollContainer () {
    HorizontalScrollView view = new HorizontalScrollView(this);
    return view;
  }

  protected ViewGroup createHorizontalScrollContainer (View content) {
    ViewGroup view = createHorizontalScrollContainer();
    view.addView(content);
    return view;
  }

  protected TextView createTextView () {
    TextView view = new TextView(this);
    view.setFocusable(true);
    return view;
  }

  protected TextView createTextView (String text) {
    TextView view = createTextView();
    view.setText(text);
    return view;
  }

  protected Button createButton (String label, Button.OnClickListener listener) {
    Button button = new Button(this);
    button.setText(label);
    button.setOnClickListener(listener);
    return button;
  }

  protected Button createButton (int label, Button.OnClickListener listener) {
    return createButton(ApplicationContext.getString(label), listener);
  }

  protected abstract View createContentView ();

  protected final void setContentView () {
    View view = createContentView();

    view.setLayoutParams(new ViewGroup.LayoutParams(
      ViewGroup.LayoutParams.MATCH_PARENT,
      ViewGroup.LayoutParams.MATCH_PARENT
    ));

    setContentView(view);
  }
}
