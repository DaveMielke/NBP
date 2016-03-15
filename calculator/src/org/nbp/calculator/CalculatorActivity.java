package org.nbp.calculator;

import org.nbp.common.CommonActivity;

import android.app.Activity;
import android.os.Bundle;

import android.view.View;
import android.view.KeyEvent;

import android.view.ViewGroup;
import android.widget.GridLayout;

import android.widget.TextView;
import android.widget.EditText;
import android.widget.Button;

import android.app.AlertDialog;
import android.content.DialogInterface;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class CalculatorActivity extends CommonActivity {
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

  private final Activity getActivity () {
    return this;
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
  private ViewGroup mainKeypadView;
  private ViewGroup functionsKeypadView;

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
      mainKeypadView,
      functionsKeypadView
    };

    for (View view : views) {
      view.setVisibility((view == keypad)? View.VISIBLE: View.GONE);
    }
  }

  private final void setClearButtonListener () {
    setButtonListener(
      R.id.button_clear,
      new Button.OnClickListener() {
        @Override
        public void onClick (View view) {
          expressionView.setText("");
          resultView.setText("");
          expressionView.requestFocus();
        }
      }
    );
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

  private final void setFunctionsButtonListener () {
    setButtonListener(
      R.id.button_functions,
      new Button.OnClickListener() {
        @Override
        public void onClick (View view) {
          showKeypad(functionsKeypadView);
          functionsKeypadView.getChildAt(0).requestFocus();
        }
      }
    );
  }

  private final void setVariablesButtonListener () {
    setButtonListener(
      R.id.button_variables,
      new Button.OnClickListener() {
        @Override
        public void onClick (View view) {
          GridLayout listing = (GridLayout)getLayoutInflater().inflate(R.layout.variable_listing, null);
          int row = 0;

          final AlertDialog dialog = new AlertDialog
            .Builder(getActivity())
            .setView(newVerticalScrollContainer(listing))
            .setNegativeButton(R.string.button_cancel, null)
            .setCancelable(true)
            .create();

          Button.OnClickListener listener = new Button.OnClickListener() {
            @Override
            public void onClick (View view) {
              Button button = (Button)view;
              insertExpressionText(button.getText().toString());

              dialog.dismiss();
              expressionView.requestFocus();
            }
          };

          for (String name : Variables.getUserVariableNames()) {
            setColumn(listing, row, 0, newButton(name, listener));
            setColumn(listing, row, 1, formatValue(Variables.get(name)));
            row += 1;
          }

          for (String name : Variables.getSystemVariableNames()) {
            SystemVariable variable = Variables.getSystemVariable(name);
            setColumn(listing, row, 0, newButton(name, listener));
            setColumn(listing, row, 1, formatValue(variable.getValue()));
            setColumn(listing, row, 2, variable.getDescription());
            row += 1;
          }

          dialog.show();
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
      Variables.set(Variables.RESULT, result);
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

        if (keypad != mainKeypadView) showKeypad(mainKeypadView);
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
    mainKeypadView = (ViewGroup)findViewById(R.id.keypad_main);
    functionsKeypadView = (ViewGroup)findViewById(R.id.keypad_functions);

    setEvaluateListener();
    expressionView.requestFocus();

    setClearButtonListener();
    setDeleteButtonListener();
    setFunctionsButtonListener();
    setVariablesButtonListener();

    setKeypadListeners(mainKeypadView);
    setKeypadListeners(functionsKeypadView);
    showKeypad(mainKeypadView);
  }
}
