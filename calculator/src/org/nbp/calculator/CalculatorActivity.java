package org.nbp.calculator;

import org.nbp.common.CommonActivity;
import android.os.Bundle;

import android.view.View;
import android.view.ViewGroup;
import android.view.KeyEvent;

import android.widget.TextView;
import android.widget.EditText;
import android.widget.Button;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class CalculatorActivity extends CommonActivity {
  private EditText expressionView;
  private TextView resultView;
  private ViewGroup mainKeypadView;
  private ViewGroup functionsKeypadView;

  private final void setButtonListener (int button, Button.OnClickListener listener) {
    ((Button)findViewById(button)).setOnClickListener(listener);
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
        }
      }
    );
  }

  private final static Pattern REAL_PATTERN = Pattern.compile(
    "^([-+])?0*(\\d*?)(?:\\.(\\d*?)0*)?(?:[eE]([-+])?0*(\\d+?))?$"
  );

  private final String getMatch (String string, Matcher matcher, int group) {
    int start = matcher.start(group);
    if (start < 0) return "";

    int end = matcher.end(group);
    if (end < 0) return "";

    return string.substring(start, end);
  }

  private final void evaluateExpression () {
    String expression = expressionView.getText().toString();

    try {
      ExpressionEvaluation evaluation = new ExpressionEvaluation(expression);
      double result = evaluation.getResult();
      String text = String.format("%.12E", evaluation.getResult());
      Matcher matcher = REAL_PATTERN.matcher(text);

      if (matcher.lookingAt()) {
        String sign = getMatch(text, matcher, 1);
        String before = getMatch(text, matcher, 2);
        String after = getMatch(text, matcher, 3);
        String exponentSign = getMatch(text, matcher, 4);
        String exponentValue = getMatch(text, matcher, 5);

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

          if (exponent < 0) {
            if (sign.equals("-")) sb.append('−');
            exponent = -exponent;
          }

          sb.append(exponent);
        }

        if (sign.equals("-")) sb.insert(0, '−');
        text = sb.toString();
      }

      resultView.setText(text);
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
          int start = expressionView.getSelectionStart();
          int end = expressionView.getSelectionEnd();
          int cursor = start + text.length();

          if (Functions.get(text) != null) {
            text += "()";
            cursor += 1;
          }

          expressionView.getText().replace(start, end, text);
          expressionView.setSelection(cursor);
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
    setClearButtonListener();
    setDeleteButtonListener();
    setFunctionsButtonListener();
    setKeypadListeners(mainKeypadView);
    setKeypadListeners(functionsKeypadView);

    expressionView.requestFocus();
    showKeypad(mainKeypadView);
  }
}
