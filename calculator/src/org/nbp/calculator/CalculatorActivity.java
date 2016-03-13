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
  private ViewGroup keysView;

  private final void setButtonListener (int button, Button.OnClickListener listener) {
    ((Button)findViewById(button)).setOnClickListener(listener);
  }

  private final void addClearListener () {
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

  private final void addEvaluateListener () {
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

  private final void addKeyListeners () {
    int count = keysView.getChildCount();

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
          expressionView.getText().replace(start, end, text);

          int cursor = start + text.length();
          if (text.endsWith("()")) cursor -= 1;
          expressionView.setSelection(cursor);

          expressionView.requestFocus();
        }
      }
    };

    for (int index=0; index<count; index+=1) {
      View view = keysView.getChildAt(index);
      view.setOnClickListener(listener);
    }
  }

  @Override
  protected void onCreate (Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.calculator);
    expressionView = (EditText)findViewById(R.id.expression);
    resultView = (TextView)findViewById(R.id.result);
    keysView = (ViewGroup)findViewById(R.id.keys);

    addClearListener();
    addEvaluateListener();
    addKeyListeners();

    expressionView.requestFocus();
  }
}