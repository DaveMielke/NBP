package org.nbp.calculator;

import org.nbp.common.CommonActivity;
import android.os.Bundle;

import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;
import android.widget.EditText;
import android.widget.Button;

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

  private final void evaluateExpression () {
    String expression = expressionView.getText().toString();

    try {
      ExpressionEvaluation evaluation = new ExpressionEvaluation(expression);

      resultView.setText(
        String.format(
          "%,.12G",
          evaluation.getResult()
        )
      );
    } catch (ExpressionException exception) {
      resultView.setText(exception.getMessage());
      expressionView.setSelection(exception.getLocation());
    }

    resultView.requestFocus();
  }

  private final void addEvaluateListener () {
    expressionView.setOnFocusChangeListener(
      new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange (View view, boolean hasFocus) {
          if (!hasFocus) {
            evaluateExpression();
          }
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
