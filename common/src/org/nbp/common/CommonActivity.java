package org.nbp.common;

import java.util.Collection;
import java.util.Map;
import java.util.HashMap;

import android.util.Log;

import android.app.Activity;
import android.os.Bundle;

import android.content.Intent;
import android.content.ActivityNotFoundException;

import android.view.View;
import android.view.ViewGroup;

import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.HorizontalScrollView;

import android.widget.TableLayout;
import android.widget.TableRow;

import android.widget.ListView;
import android.widget.ArrayAdapter;

import android.widget.TextView;
import android.widget.EditText;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Switch;

import android.content.DialogInterface;
import android.app.Dialog;
import android.app.AlertDialog;

public abstract class CommonActivity extends Activity implements ProblemReporter {
  private final static String LOG_TAG = CommonActivity.class.getName();

  protected CommonActivity () {
    super();
  }

  protected final Activity getActivity () {
    return this;
  }

  public final void addViews (ViewGroup group, ViewGroup.LayoutParams parameters, View... views) {
    for (View view : views) {
      group.addView(view, parameters);
    }
  }

  public final void addViews (ViewGroup group, View... views) {
    for (View view : views) {
      group.addView(view);
    }
  }

  public ViewGroup newVerticalGroup (View... views) {
    LinearLayout group = new LinearLayout(this);
    group.setOrientation(group.VERTICAL);

    LinearLayout.LayoutParams parameters = new LinearLayout.LayoutParams(
      LinearLayout.LayoutParams.MATCH_PARENT,
      LinearLayout.LayoutParams.WRAP_CONTENT
    );

    addViews(group, parameters, views);
    return group;
  }

  public ViewGroup newHorizontalGroup (View... views) {
    LinearLayout group = new LinearLayout(this);
    group.setOrientation(group.HORIZONTAL);

    LinearLayout.LayoutParams parameters = new LinearLayout.LayoutParams(
      LinearLayout.LayoutParams.WRAP_CONTENT,
      LinearLayout.LayoutParams.MATCH_PARENT
    );

    addViews(group, parameters, views);
    return group;
  }

  public ViewGroup newVerticalScrollContainer () {
    ScrollView container = new ScrollView(this);
    return container;
  }

  public ViewGroup newVerticalScrollContainer (View content) {
    ViewGroup container = newVerticalScrollContainer();
    container.addView(content);
    return container;
  }

  public ViewGroup newHorizontalScrollContainer () {
    HorizontalScrollView container = new HorizontalScrollView(this);
    return container;
  }

  public ViewGroup newHorizontalScrollContainer (View content) {
    ViewGroup container = newHorizontalScrollContainer();
    container.addView(content);
    return container;
  }

  public ViewGroup newTable (View... rows) {
    TableLayout table = new TableLayout(this);
    addViews(table, rows);
    return table;
  }

  public ViewGroup newTableRow (View... cells) {
    TableRow row = new TableRow(this);
    addViews(row, cells);
    return row;
  }

  public TextView newTextView () {
    TextView view = new TextView(this);
    view.setFocusable(true);
    return view;
  }

  public TextView newTextView (CharSequence text) {
    TextView view = newTextView();
    view.setText(text);
    return view;
  }

  public TextView newTextView (int text) {
    TextView view = newTextView();
    view.setText(text);
    return view;
  }

  public EditText newEditText () {
    EditText view = new EditText(this);
    view.setFocusable(true);
    return view;
  }

  public Button newButton (CharSequence label, Button.OnClickListener listener) {
    Button button = new Button(this);
    button.setText(label);
    button.setOnClickListener(listener);
    return button;
  }

  public Button newButton (int label, Button.OnClickListener listener) {
    return newButton(getString(label), listener);
  }

  public CheckBox newCheckBox (CharSequence label, CheckBox.OnCheckedChangeListener listener) {
    CheckBox checkBox = new CheckBox(this);
    checkBox.setText(label);
    checkBox.setOnCheckedChangeListener(listener);
    return checkBox;
  }

  public CheckBox newCheckBox (int label, CheckBox.OnCheckedChangeListener listener) {
    return newCheckBox(getString(label), listener);
  }

  public Switch newSwitch (Switch.OnCheckedChangeListener listener) {
    Switch view = new Switch(this);
    view.setOnCheckedChangeListener(listener);
    return view;
  }

  public final ListView newListView (String[] values) {
    ArrayAdapter<String> adapter = new ArrayAdapter<String>(
      this, android.R.layout.simple_list_item_1, android.R.id.text1, values
    );

    ListView list = new ListView(this);
    list.setAdapter(adapter);
    return list;
  }

  public final ListView newListView (Collection<String> values) {
    return newListView(values.toArray(new String[values.size()]));
  }

  public final AlertDialog.Builder newAlertDialogBuilder (int... subtitles) {
    return new AlertDialogBuilder(this, subtitles)
              .setCancelable(false)
              ;
  }

  public final void showChooser (
    int subtitle, CharSequence[] choices,
    DialogInterface.OnClickListener listener
  ) {
    newAlertDialogBuilder(subtitle)
      .setItems(choices, listener)
      .setNegativeButton(android.R.string.no, null)
      .show();
  }

  public final void showDialog (
    int subtitle, int layout, DialogFinisher finisher,
    int action, DialogInterface.OnClickListener listener
  ) {
    AlertDialog.Builder builder = newAlertDialogBuilder(subtitle);
    builder.setView(getLayoutInflater().inflate(layout, null));

    if (listener != null) {
      builder.setPositiveButton(action, listener);
      builder.setNegativeButton(android.R.string.no, null);
    } else {
      builder.setNeutralButton(action, null);
    }

    AlertDialog dialog = builder.show();
    if (finisher != null) finisher.finishDialog(new DialogHelper(dialog));
  }

  public final void showDialog (
    int subtitle, int layout, int action,
    DialogInterface.OnClickListener listener
  ) {
    showDialog(subtitle, layout, null, action, listener);
  }

  public final void showDialog (
    int subtitle, int layout, DialogFinisher finisher, boolean positive
  ) {
    showDialog(
      subtitle, layout, finisher,
      (positive? android.R.string.yes: android.R.string.no), null
    );
  }

  public final void showDialog (
    int subtitle, int layout, DialogFinisher finisher
  ) {
    showDialog(subtitle, layout, finisher, true);
  }

  public final void showDialog (int subtitle, int layout) {
    showDialog(subtitle, layout, null);
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

  protected final void setViewFocusable (Dialog dialog, int identifier) {
    View view = dialog.findViewById(identifier);
    if (view != null) view.setFocusable(true);
  }

  protected final void setViewFocusable (Dialog dialog, String name) {
    int identifier = CommonContext.getAndroidViewIdentifier(name);
    if (identifier != 0) setViewFocusable(dialog, identifier);
  }

  protected final void setTitleFocusable (AlertDialog dialog) {
    setViewFocusable(dialog, "alertTitle");
  }

  public final void showMessage (int message, String detail, final Runnable onCleared) {
    if (isResumed) {
      AlertDialog dialog = new AlertDialog
        .Builder(this)
        .setTitle((message == 0)? null: getString(message))
        .setMessage(detail)

        .setNeutralButton(
          android.R.string.yes,
          new DialogInterface.OnClickListener() {
            @Override
            public void onClick (DialogInterface dialog, int button)  {
              run(onCleared);
            }
          }
        )

        .show();

      setTitleFocusable(dialog);
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

  public final void showMessage (int message, String detail) {
    showMessage(message, detail, null);
  }

  public final void showMessage (int message) {
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

  public final void confirmAction (
    String question, String detail, final Runnable onConfirmed
  ) {
    AlertDialog dialog = new AlertDialog
      .Builder(this)
      .setTitle(question)
      .setMessage(detail)

      .setPositiveButton(android.R.string.yes,
        new DialogInterface.OnClickListener() {
          @Override
          public void onClick (DialogInterface dialog, int button) {
            run(onConfirmed);
          }
        }
      )

      .setNegativeButton(android.R.string.no, null)
      .show();

    setTitleFocusable(dialog);
  }

  public final void confirmAction (int question, String detail, Runnable onConfirmed) {
    confirmAction(getString(question), detail, onConfirmed);
  }
}
