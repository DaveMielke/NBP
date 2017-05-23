package org.nbp.common;

import java.util.Map;
import java.util.HashMap;

import android.util.Log;
import android.content.DialogInterface;
import android.app.AlertDialog;

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

import android.widget.ListView;
import android.widget.ArrayAdapter;

import android.content.Intent;
import android.content.ActivityNotFoundException;

public abstract class CommonActivity extends Activity implements ProblemReporter {
  private final static String LOG_TAG = CommonActivity.class.getName();

  private static Activity activity = null;

  protected CommonActivity () {
    super();
    activity = this;
  }

  public final static Activity getActivity () {
    return activity;
  }

  protected final void addViews (ViewGroup group, ViewGroup.LayoutParams parameters, View... views) {
    for (View view : views) {
      group.addView(view, parameters);
    }
  }

  protected ViewGroup newVerticalGroup (View... views) {
    LinearLayout group = new LinearLayout(this);
    group.setOrientation(group.VERTICAL);

    LinearLayout.LayoutParams parameters = new LinearLayout.LayoutParams(
      LinearLayout.LayoutParams.MATCH_PARENT,
      LinearLayout.LayoutParams.WRAP_CONTENT
    );

    addViews(group, parameters, views);
    return group;
  }

  protected ViewGroup newHorizontalGroup (View... views) {
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

  protected TextView newTextView (int text) {
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
    return newButton(getString(label), listener);
  }

  protected CheckBox newCheckBox (CharSequence label, CheckBox.OnCheckedChangeListener listener) {
    CheckBox checkBox = new CheckBox(this);
    checkBox.setText(label);
    checkBox.setOnCheckedChangeListener(listener);
    return checkBox;
  }

  protected CheckBox newCheckBox (int label, CheckBox.OnCheckedChangeListener listener) {
    return newCheckBox(getString(label), listener);
  }

  protected Switch newSwitch (Switch.OnCheckedChangeListener listener) {
    Switch view = new Switch(this);
    view.setOnCheckedChangeListener(listener);
    return view;
  }

  protected final ListView newListView (String[] values) {
    ArrayAdapter<String> adapter = new ArrayAdapter<String>(
      this, android.R.layout.simple_list_item_1, android.R.id.text1, values
    );

    ListView list = new ListView(this);
    list.setAdapter(adapter);
    return list;
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

  public boolean startRequest (Intent intent, ActivityResultHandler handler) {
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

  @Override
  protected void onCreate (Bundle state) {
    super.onCreate(state);
    CommonContext.setContext(this);
  }

  private boolean isResumed = false;

  @Override
  protected void onResume () {
    super.onResume();
    isResumed = true;
  }

  @Override
  protected void onPause () {
    try {
      isResumed = false;
    } finally {
      super.onPause();
    }
  }

  protected final boolean haveWindow () {
    return isResumed;
  }

  protected final void run (Runnable runnable) {
    if (runnable != null) runnable.run();
  }

  protected final void showMessage (int message, String detail, final Runnable onCleared) {
    if (isResumed) {
      AlertDialog dialog = new AlertDialog
        .Builder(this)
        .setTitle((message == 0)? null: getString(message))
        .setMessage(detail)

        .setNeutralButton(
          R.string.showMessage_message_neutral,
          new DialogInterface.OnClickListener() {
            @Override
            public void onClick (DialogInterface dialog, int button)  {
              run(onCleared);
            }
          }
        )

        .create();
      dialog.show();

      {
        int identifier = CommonContext.getAndroidViewIdentifier("alertTitle");

        if (identifier != 0) {
          View view = dialog.findViewById(identifier);

          if (view != null) {
            view.setFocusable(true);
          }
        }
      }
    } else {
      StringBuilder sb = new StringBuilder();
      if (message != 0) sb.append(getString(message));

      if (detail != null) {
        if (sb.length() > 0) sb.append(": ");
        sb.append(detail);
      }

      Log.w(LOG_TAG, sb.toString());
      run(onCleared);
    }
  }

  protected final void showMessage (int message, String detail) {
    showMessage(message, detail, null);
  }

  protected final void showMessage (int message) {
    showMessage(message, null);
  }

  @Override
  public final void reportProblem (String tag, final String message) {
    runOnUiThread(
      new Runnable() {
        @Override
        public void run () {
          showMessage(0, message);
        }
      }
    );
  }

  protected final void showReportedErrors () {
    CommonUtilities.setErrorReporter(this);
  }

  protected final void showReportedWarnings () {
    CommonUtilities.setWarningReporter(this);
  }

  protected final void confirmAction (
    String question, String detail, final Runnable onConfirmed
  ) {
    new AlertDialog
      .Builder(this)
      .setTitle(question)
      .setMessage(detail)

      .setPositiveButton(
        R.string.confirmAction_button_positive,
        new DialogInterface.OnClickListener() {
          @Override
          public void onClick (DialogInterface dialog, int button) {
            run(onConfirmed);
          }
        }
      )

      .setNegativeButton(R.string.confirmAction_button_negative, null)
      .show();
  }

  protected final void confirmAction (int question, String detail, Runnable onConfirmed) {
    confirmAction(getString(question), detail, onConfirmed);
  }
}
