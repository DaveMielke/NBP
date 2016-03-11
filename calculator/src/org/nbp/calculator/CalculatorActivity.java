package org.nbp.calculator;

import org.nbp.common.CommonActivity;
import android.os.Bundle;

import android.view.View;
import android.widget.TextView;
import android.widget.EditText;

public class CalculatorActivity extends CommonActivity {
  private EditText expressionView;
  private TextView resultView;

  @Override
  protected void onCreate (Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.calculator);
    expressionView = (EditText)findViewById(R.id.expression);
    resultView = (TextView)findViewById(R.id.result);

    expressionView.setOnFocusChangeListener(
      new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange (View view, boolean hasFocus) {
          if (!hasFocus) {
            String expression = expressionView.getText().toString();

            try {
              ExpressionEvaluation evaluation = new ExpressionEvaluation(expression);
              resultView.setText(evaluation.getResult());
            } catch (ExpressionException exception) {
              resultView.setText(exception.getMessage());
              expressionView.setSelection(exception.getLocation());
            }
          }
        }
      }
    );
  }
}
