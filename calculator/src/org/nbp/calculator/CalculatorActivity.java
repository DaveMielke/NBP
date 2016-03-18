package org.nbp.calculator;

import java.util.Collection;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;

import org.nbp.common.CommonActivity;

import android.util.Log;

import android.app.Activity;
import android.os.Bundle;

import android.view.View;
import android.view.ViewGroup;
import android.view.KeyEvent;

import android.widget.TextView;
import android.widget.EditText;
import android.widget.Button;
import android.widget.CompoundButton;

import android.text.InputFilter;
import android.text.Spanned;

import android.app.AlertDialog;
import android.content.DialogInterface;

public class CalculatorActivity extends CommonActivity {
  private final static String LOG_TAG = CalculatorActivity.class.getName();

  private static String[] toArray (Collection<String> collection) {
    return collection.toArray(new String[collection.size()]);
  }

  private final View findView (DialogInterface dialog, int id) {
    return ((AlertDialog)dialog).findViewById(id);
  }

  private final AlertDialog.Builder newAlertDialogBuilder (Integer... subtitles) {
    StringBuilder title = new StringBuilder();
    title.append(getString(R.string.app_name));

    for (Integer subtitle : subtitles) {
      title.append(" - ");
      title.append(getString(subtitle));
    }

    return new AlertDialog.Builder(this)
                          .setTitle(title.toString())
                          .setNegativeButton(R.string.button_cancel, null)
                          .setCancelable(true);
  }

  private final void setClickListener (int id, View.OnClickListener listener) {
    View view = findViewById(id);
    view.setOnClickListener(listener);
  }

  private final void setLongClickListener (int id, View.OnLongClickListener listener) {
    View view = findViewById(id);
    view.setOnLongClickListener(listener);
  }

  private final void setCompoundButtonListener (
    int id, final String setting, boolean checked
  ) {
    CompoundButton button = (CompoundButton)findViewById(id);
    button.setChecked(SavedSettings.get(setting, checked));

    button.setOnCheckedChangeListener(
      new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged (CompoundButton button, boolean isChecked) {
          SavedSettings.set(setting, isChecked);
        }
      }
    );
  }

  private EditText expressionView;
  private TextView resultView;

  private final void saveExpression () {
    SavedSettings.set(SavedSettings.EXPRESSION, expressionView.getText().toString());
    SavedSettings.set(SavedSettings.START, expressionView.getSelectionStart());
    SavedSettings.set(SavedSettings.END, expressionView.getSelectionEnd());
  }

  private final void restoreExpression () {
    String expression = SavedSettings.get(SavedSettings.EXPRESSION, "");
    expressionView.setText(expression);

    int start = SavedSettings.get(SavedSettings.START, -1);
    int end = SavedSettings.get(SavedSettings.END, -1);
    int length = expression.length();

    if ((0 <= start) && (start <= end) && (end <= length)) {
      expressionView.setSelection(start, end);
    } else {
      expressionView.setSelection(length);
    }
  }

  private final void evaluateExpression () {
    saveExpression();
    String expression = expressionView.getText().toString();

    try {
      ExpressionEvaluation evaluation = new ExpressionEvaluation(expression);
      double result = evaluation.getResult();

      resultView.setText(Number.toString(result));
      SavedSettings.set(SavedSettings.RESULT, result);
    } catch (ExpressionException exception) {
      resultView.setText(exception.getMessage());
      expressionView.setSelection(exception.getLocation());
    }
  }

  private final void setEvaluateListener () {
    expressionView.setOnEditorActionListener(
      new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction (TextView view, int action, KeyEvent event) {
          if (event != null) {
            if (event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
              if (event.getAction() == KeyEvent.ACTION_DOWN) {
                evaluateExpression();
                resultView.requestFocus();
              }

              return true;
            }
          }

          return false;
        }
      }
    );
  }

  private final void insertExpressionText (String text) {
    int start = expressionView.getSelectionStart();
    int end = expressionView.getSelectionEnd();
    int cursor = start + text.length();

    if (Functions.get(text) != null) {
      text += "()";
      cursor += 1;
    }

    expressionView.getText().replace(start, end, text);
    expressionView.setSelection(cursor);
  }

  private ViewGroup[] keypadViews;
  private int currentKeypad;

  private final void showKeypad () {
    View keypad = keypadViews[currentKeypad];

    for (View view : keypadViews) {
      view.setVisibility((view == keypad)? View.VISIBLE: View.GONE);
    }
  }

  private final void showKeypad (int index) {
    currentKeypad = index;
    showKeypad();
  }

  private final void prepareKeypads (Integer... ids) {
    View.OnClickListener listener = new View.OnClickListener() {
      @Override
      public void onClick (View view) {
        Button button = (Button)view;
        String text = button.getText().toString();

        if (text.equals("=")) {
          evaluateExpression();
          resultView.requestFocus();
        } else {
          insertExpressionText(text);
          expressionView.requestFocus();
        }

        showKeypad(0);
      }
    };

    keypadViews = new ViewGroup[ids.length];
    int keypadCount = 0;

    for (int id : ids) {
      ViewGroup keypad = (ViewGroup)findViewById(id);
      keypadViews[keypadCount++] = keypad;
      int keyCount = keypad.getChildCount();

      for (int keyIndex=0; keyIndex<keyCount; keyIndex+=1) {
        View key = keypad.getChildAt(keyIndex);
        key.setOnClickListener(listener);
      }
    }
  }

  private final void setDeleteButtonListener () {
    setClickListener(
      R.id.button_delete,
      new View.OnClickListener() {
        @Override
        public void onClick (View view) {
          int start = expressionView.getSelectionStart();
          int end = expressionView.getSelectionEnd();
          if (start == end) start -= 1;

          if (start >= 0) {
            expressionView.getText().replace(start, end, "");
            expressionView.setSelection(start);
            expressionView.requestFocus();
          }
        }
      }
    );

    setLongClickListener(
      R.id.button_delete,
      new View.OnLongClickListener() {
        @Override
        public boolean onLongClick (View view) {
          expressionView.setText("");
          expressionView.requestFocus();
          return true;
        }
      }
    );
  }

  private final void setClearButtonListener () {
    setClickListener(
      R.id.button_clear,
      new View.OnClickListener() {
        @Override
        public void onClick (View view) {
          resultView.setText("");
          SavedSettings.set(SavedSettings.RESULT, Double.NaN);

          expressionView.setText("");
          expressionView.requestFocus();
        }
      }
    );
  }

  private final void setDegreesCheckBoxListener () {
    setCompoundButtonListener(
      R.id.checkbox_degrees,
      SavedSettings.DEGREES,
      DefaultSettings.DEGREES
    );
  }

  private final void setFunctionButtonListener () {
    setClickListener(
      R.id.button_function,
      new View.OnClickListener() {
        @Override
        public void onClick (View view) {
          currentKeypad += 1;
          currentKeypad %= keypadViews.length;
          showKeypad();

          ViewGroup keypad = keypadViews[currentKeypad];
          keypad.getChildAt(0).requestFocus();
        }
      }
    );
  }

  private final String formatVariable (String name, double value, String description) {
    StringBuilder sb = new StringBuilder();

    sb.append(name);
    sb.append(" = ");
    sb.append(Number.toString(value));

    if (description != null) {
      sb.append(" (");
      sb.append(description);
      sb.append(')');
    }

    return sb.toString();
  }

  private final String formatVariable (String name, double value) {
    return formatVariable(name, value, null);
  }

  private final String getVariableName (List<String> variables, int index) {
    String variable = variables.get(index);
    return variable.substring(0, variable.indexOf(' '));
  }

  private final List<String> getUserVariables () {
    List<String> variables = new ArrayList<String>();

    for (String name : Variables.getUserVariableNames()) {
      variables.add(formatVariable(name, Variables.get(name)));
    }

    return variables;
  }

  private final void setRecallButtonListener () {
    setClickListener(
      R.id.button_recall,
      new View.OnClickListener() {
        @Override
        public void onClick (View view) {
          AlertDialog.Builder builder = newAlertDialogBuilder(R.string.button_recall);
          final List<String> variables = getUserVariables();

          for (String name : Variables.getSystemVariableNames()) {
            SystemVariable variable = Variables.getSystemVariable(name);
            variables.add(formatVariable(name, variable.getValue(), variable.getDescription()));
          }

          builder.setItems(
            toArray(variables),
            new DialogInterface.OnClickListener() {
              @Override
              public void onClick (DialogInterface dialog, int index) {
                insertExpressionText(getVariableName(variables, index));
                expressionView.requestFocus();
              }
            }
          );

          builder.show();
        }
      }
    );
  }

  private final void setStoreButtonListener () {
    setClickListener(
      R.id.button_store,
      new View.OnClickListener() {
        @Override
        public void onClick (View view) {
          AlertDialog.Builder builder = newAlertDialogBuilder(R.string.button_store);
          final double result = SavedSettings.getResult();

          if (Double.isNaN(result)) {
            builder.setMessage(R.string.error_no_result);
          } else {
            final List<String> variables = getUserVariables();

            if (variables.isEmpty()) {
              builder.setMessage(R.string.error_no_variables);
            } else {
              builder.setItems(
                toArray(variables),
                new DialogInterface.OnClickListener() {
                  @Override
                  public void onClick (DialogInterface dialog, int index) {
                    Variables.set(getVariableName(variables, index), result);
                  }
                }
              );
            }

            builder.setPositiveButton(
              R.string.button_new,
              new DialogInterface.OnClickListener() {
                @Override
                public void onClick (DialogInterface dialog, int button) {
                  AlertDialog.Builder builder = newAlertDialogBuilder(
                    R.string.button_store,
                    R.string.button_new
                  );

                  builder.setView(
                    getLayoutInflater().inflate(R.layout.variable_name, null)
                  );

                  builder.setPositiveButton(
                    R.string.button_store,
                    new DialogInterface.OnClickListener() {
                      @Override
                      public void onClick (DialogInterface dialog, int button) {
                        EditText view = (EditText)findView(dialog, R.id.variable);
                        String name = view.getText().toString();
                        Variables.set(name, result);
                      }
                    }
                  );

                  AlertDialog alert = builder.create();
                  alert.show();

                  EditText variableName = (EditText)findView(alert, R.id.variable);
                  final Button storeButton = alert.getButton(alert.BUTTON_POSITIVE);
                  storeButton.setEnabled(false);

                  variableName.setFilters(
                    new InputFilter[] {
                      new InputFilter() {
                        @Override
                        public CharSequence filter (
                          CharSequence src, int srcStart, int srcEnd,
                          Spanned dst, int dstStart, int dstEnd
                        ) {
                          int dstIndex = dstStart;

                          for (int srcIndex=srcStart; srcIndex<srcEnd; srcIndex+=1) {
                            if (!Variables.isNameCharacter(src.charAt(srcIndex), (dstIndex == 0))) return "";
                            dstIndex += 1;
                          }

                          boolean isNew = false;
                          StringBuilder name = new StringBuilder(dst.toString());

                          name.delete(dstStart, dstEnd);
                          name.insert(dstStart, src.subSequence(srcStart, srcEnd));

                          if (name.length() > 0) {
                            if (Variables.isNameCharacter(name.charAt(0), true)) {
                              if (Variables.get(name.toString()) == null) {
                                isNew = true;
                              }
                            }
                          }

                          storeButton.setEnabled(isNew);
                          return null;
                        }
                      }
                    }
                  );
                }
              }
            );
          }

          builder.show();
        }
      }
    );
  }

  private final void setForgetButtonListener () {
    setClickListener(
      R.id.button_forget,
      new View.OnClickListener() {
        @Override
        public void onClick (View view) {
          AlertDialog.Builder builder = newAlertDialogBuilder(R.string.button_forget);
          final List<String> variables = getUserVariables();

          if (variables.isEmpty()) {
            builder.setMessage(R.string.error_no_variables);
          } else {
            builder.setItems(
              toArray(variables),
              new DialogInterface.OnClickListener() {
                @Override
                public void onClick (DialogInterface dialog, int index) {
                  Variables.removeUserVariable(getVariableName(variables, index));
                }
              }
            );
          }

          builder.show();
        }
      }
    );

    setLongClickListener(
      R.id.button_forget,
      new View.OnLongClickListener() {
        @Override
        public boolean onLongClick (View view) {
          AlertDialog.Builder builder = newAlertDialogBuilder(R.string.button_forget);
          final List<String> variables = getUserVariables();

          if (variables.isEmpty()) {
            builder.setMessage(R.string.error_no_variables);
          } else {
            final Set<String> names = new HashSet<String>();

            builder.setMultiChoiceItems(
              toArray(variables),
              null,
              new DialogInterface.OnMultiChoiceClickListener() {
                @Override
                public void onClick (DialogInterface dialog, int index, boolean isChecked) {
                  String name = getVariableName(variables, index);

                  if (isChecked) {
                    names.add(name);
                  } else {
                    names.remove(name);
                  }
                }
              }
            );

            builder.setPositiveButton(
              R.string.button_forget,
              new DialogInterface.OnClickListener() {
                @Override
                public void onClick (DialogInterface dialog, int button) {
                  for (String name : names) {
                    Variables.removeUserVariable(name);
                  }
                }
              }
            );
          }

          builder.show();
          return true;
        }
      }
    );
  }

  @Override
  protected void onCreate (Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.calculator);
    expressionView = (EditText)findViewById(R.id.expression);
    resultView = (TextView)findViewById(R.id.result);

    setEvaluateListener();
    restoreExpression();
    expressionView.requestFocus();

    setDeleteButtonListener();
    setClearButtonListener();
    setDegreesCheckBoxListener();
    setFunctionButtonListener();

    setRecallButtonListener();
    setStoreButtonListener();
    setForgetButtonListener();

    prepareKeypads(
      R.id.keypad_numeric,
      R.id.keypad_function
    );

    showKeypad(0);
  }

  @Override
  protected void onPause () {
    try {
      saveExpression();
    } finally {
      super.onPause();
    }
  }
}
