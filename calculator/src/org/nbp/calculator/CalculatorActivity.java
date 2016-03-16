package org.nbp.calculator;

import java.util.Collection;
import java.util.List;
import java.util.ArrayList;

import org.nbp.common.CommonActivity;

import android.util.Log;

import android.app.Activity;
import android.os.Bundle;

import android.view.View;
import android.view.KeyEvent;

import android.view.ViewGroup;
import android.widget.GridLayout;

import android.widget.TextView;
import android.widget.EditText;
import android.widget.Button;

import android.text.InputFilter;
import android.text.Spanned;

import android.app.AlertDialog;
import android.content.DialogInterface;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class CalculatorActivity extends CommonActivity {
  private final static String LOG_TAG = CalculatorActivity.class.getName();

  private static String[] toArray (Collection<String> collection) {
    return collection.toArray(new String[collection.size()]);
  }

  private final static Pattern REAL_PATTERN = Pattern.compile(
    "^([-+])?0*(\\d*?)(?:\\.(\\d*?)0*)?(?:[eE]([-+])?0*(\\d+?))?$"
  );

  private static String getMatch (String string, Matcher matcher, int group) {
    int start = matcher.start(group);
    if (start < 0) return "";

    int end = matcher.end(group);
    if (end < 0) return "";

    return string.substring(start, end);
  }

  private static String formatValue (double value) {
    String string = String.format("%.12E", value);
    Matcher matcher = REAL_PATTERN.matcher(string);

    if (matcher.lookingAt()) {
      String sign = getMatch(string, matcher, 1);
      String before = getMatch(string, matcher, 2);
      String after = getMatch(string, matcher, 3);
      String exponentSign = getMatch(string, matcher, 4);
      String exponentValue = getMatch(string, matcher, 5);

      if (!exponentSign.equals("-")) exponentSign = "";
      if (exponentValue.isEmpty()) exponentValue = "0";
      int exponent = Integer.valueOf((exponentSign + exponentValue));

      StringBuilder sb = new StringBuilder();
      sb.append(before);
      exponent += before.length();
      sb.append(after);

      {
        int length = sb.length();

        if (length == 0) {
          sb.append('0');
        } else {
          while (length > 1) {
            int last = length - 1;
            if (sb.charAt(last) != '0') break;
            sb.delete(last, length);
            length = last;
          }
        }
      }

      if ((exponent >= 1) && (exponent <= 12)) {
        if (sb.length() > exponent) {
          sb.insert(exponent, '.');
        } else {
          while (sb.length() < exponent) sb.append('0');
        }

        while ((exponent -= 3) > 0) sb.insert(exponent, ',');
        exponent = 0;
      } else {
        if (sb.length() > 1) sb.insert(1, '.');
        exponent -= 1;
      }

      if (exponent != 0) {
        sb.append("×10^");
        sb.append(exponent);
      }

      if (sign.equals("-")) sb.insert(0, '−');
      string = sb.toString();
    }

    return string;
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

  private final void setColumn (GridLayout grid, int row, int column, View content) {
    grid.addView(content, new GridLayout.LayoutParams(grid.spec(row), grid.spec(column)));
  }

  private final void setColumn (GridLayout grid, int row, int column, String content) {
    TextView view = newTextView(content);
    setColumn(grid, row, column, view);
  }

  private final void setButtonListener (int button, Button.OnClickListener listener) {
    ((Button)findViewById(button)).setOnClickListener(listener);
  }

  private EditText expressionView;
  private TextView resultView;
  private ViewGroup numericKeypadView;
  private ViewGroup functionKeypadView;

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

  private final void showKeypad (ViewGroup keypad) {
    ViewGroup[] views = new ViewGroup[] {
      numericKeypadView,
      functionKeypadView
    };

    for (View view : views) {
      view.setVisibility((view == keypad)? View.VISIBLE: View.GONE);
    }
  }

  private final void setDeleteButtonListener () {
    setButtonListener(
      R.id.button_delete,
      new Button.OnClickListener() {
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
  }

  private final void setClearButtonListener () {
    setButtonListener(
      R.id.button_clear,
      new Button.OnClickListener() {
        @Override
        public void onClick (View view) {
          resultView.setText("");
          Variables.removeSystemVariable(Variables.RESULT);

          expressionView.setText("");
          expressionView.requestFocus();
        }
      }
    );
  }

  private final void setFunctionButtonListener () {
    setButtonListener(
      R.id.button_function,
      new Button.OnClickListener() {
        @Override
        public void onClick (View view) {
          showKeypad(functionKeypadView);
          functionKeypadView.getChildAt(0).requestFocus();
        }
      }
    );
  }

  private final String formatVariable (String name, double value, String description) {
    StringBuilder sb = new StringBuilder();

    sb.append(name);
    sb.append(" = ");
    sb.append(formatValue(value));

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
    setButtonListener(
      R.id.button_recall,
      new Button.OnClickListener() {
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
    setButtonListener(
      R.id.button_store,
      new Button.OnClickListener() {
        @Override
        public void onClick (View view) {
          AlertDialog.Builder builder = newAlertDialogBuilder(R.string.button_store);
          final SystemVariable result = Variables.getSystemVariable(Variables.RESULT);

          if (result == null) {
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
                    Variables.set(getVariableName(variables, index), result.getValue());
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
                        Variables.set(name, result.getValue());
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
    setButtonListener(
      R.id.button_forget,
      new Button.OnClickListener() {
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
  }

  private final void evaluateExpression () {
    String expression = expressionView.getText().toString();

    try {
      ExpressionEvaluation evaluation = new ExpressionEvaluation(expression);
      double result = evaluation.getResult();

      resultView.setText(formatValue(result));
      Variables.setSystemVariable(Variables.RESULT, result);
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

  private final void setKeypadListeners (final ViewGroup keypad) {
    int count = keypad.getChildCount();

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

        if (keypad != numericKeypadView) showKeypad(numericKeypadView);
      }
    };

    for (int index=0; index<count; index+=1) {
      View view = keypad.getChildAt(index);
      view.setOnClickListener(listener);
    }
  }

  @Override
  protected void onCreate (Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.calculator);
    expressionView = (EditText)findViewById(R.id.expression);
    resultView = (TextView)findViewById(R.id.result);
    numericKeypadView = (ViewGroup)findViewById(R.id.keypad_numeric);
    functionKeypadView = (ViewGroup)findViewById(R.id.keypad_function);

    setEvaluateListener();
    expressionView.requestFocus();

    setDeleteButtonListener();
    setClearButtonListener();
    setFunctionButtonListener();

    setRecallButtonListener();
    setStoreButtonListener();
    setForgetButtonListener();

    setKeypadListeners(numericKeypadView);
    setKeypadListeners(functionKeypadView);
    showKeypad(numericKeypadView);
  }
}
