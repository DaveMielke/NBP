package org.nbp.b2g.ui.host;
import org.nbp.b2g.ui.*;

import java.util.Map;
import java.util.HashMap;

import android.util.Log;

import android.app.Activity;
import android.os.Bundle;

import android.view.View;
import android.view.ViewGroup;

import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.HorizontalScrollView;

import android.widget.TextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Switch;

import android.content.Intent;
import android.content.ActivityNotFoundException;

public abstract class ProgrammaticActivity extends Activity {
  private final static String LOG_TAG = ProgrammaticActivity.class.getName();

  protected final void addViews (ViewGroup group, ViewGroup.LayoutParams parameters, View... views) {
    for (View view : views) {
      group.addView(view, parameters);
    }
  }

  protected ViewGroup createVerticalGroup (View... views) {
    LinearLayout group = new LinearLayout(this);
    group.setOrientation(group.VERTICAL);

    LinearLayout.LayoutParams parameters = new LinearLayout.LayoutParams(
      LinearLayout.LayoutParams.MATCH_PARENT,
      LinearLayout.LayoutParams.WRAP_CONTENT
    );

    addViews(group, parameters, views);
    return group;
  }

  protected ViewGroup createHorizontalGroup (View... views) {
    LinearLayout group = new LinearLayout(this);
    group.setOrientation(group.HORIZONTAL);

    LinearLayout.LayoutParams parameters = new LinearLayout.LayoutParams(
      LinearLayout.LayoutParams.WRAP_CONTENT,
      LinearLayout.LayoutParams.MATCH_PARENT
    );

    addViews(group, parameters, views);
    return group;
  }

  protected ViewGroup newVerticalScrollContainer () {
    ScrollView container = new ScrollView(this);
    return container;
  }

  protected ViewGroup newVerticalScrollContainer (View content) {
    ViewGroup container = newVerticalScrollContainer();
    container.addView(content);
    return container;
  }

  protected ViewGroup newHorizontalScrollContainer () {
    HorizontalScrollView container = new HorizontalScrollView(this);
    return container;
  }

  protected ViewGroup newHorizontalScrollContainer (View content) {
    ViewGroup container = newHorizontalScrollContainer();
    container.addView(content);
    return container;
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

  private final int getLeftOffset () {
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

    view.setPadding(getLeftOffset(), 0, 0, 0);
    setContentView(view);
  }

  @Override
  public void onCreate (Bundle state) {
    super.onCreate(state);
    ApplicationContext.setContext(this);
    setContentView();
  }

  protected interface ActivityResultHandler {
    public void handleActivityResult (int code, Intent intent);
  }

  private int requestCode = 0;
  private final Map<Integer, ActivityResultHandler> activityResultHandlers =
    new HashMap<Integer, ActivityResultHandler>();

  @Override
  protected final void onActivityResult (int requestCode, int resultCode, Intent resultData) {
    ActivityResultHandler handler;

    synchronized (activityResultHandlers) {
      if ((handler = activityResultHandlers.get(requestCode)) != null) {
        activityResultHandlers.remove(requestCode);
      }
    }

    if (handler != null) {
      handler.handleActivityResult(resultCode, resultData);
    }
  }

  protected boolean startRequest (Intent intent, ActivityResultHandler handler) {
    int code;

    synchronized (activityResultHandlers) {
      code = requestCode++;
      activityResultHandlers.put(code, handler);
    }

    try {
      startActivityForResult(intent, code);
      return true;
    } catch (ActivityNotFoundException exception) {
      Log.w(LOG_TAG, "eligible activity not found: " + exception.getMessage());
    }

    synchronized (activityResultHandlers) {
      activityResultHandlers.remove(code);
    }

    return false;
  }

  protected final boolean findFile (ActivityResultHandler handler) {
    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
    intent.setType("file/*");
    return startRequest(intent, handler);
  }
}
