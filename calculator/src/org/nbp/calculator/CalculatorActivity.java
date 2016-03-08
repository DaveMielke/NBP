package org.nbp.calculator;

import android.app.Activity;
import android.os.Bundle;

import android.widget.EditText;

public class CalculatorActivity extends Activity {
  private EditText expressionView;

  @Override
  protected void onCreate (Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.calculator);
    expressionView = (EditText)findViewById(R.id.expression);
  }
}
