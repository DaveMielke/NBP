package org.nbp.phone;

import android.app.Activity;
import android.os.Bundle;

import android.view.View;
import android.view.ViewGroup;

import android.widget.EditText;
import android.widget.Button;

public class PhoneActivity extends Activity {
  private EditText phoneNumberView = null;
  private ViewGroup keypadView = null;

  private final void addKeypadListeners () {
    int count = keypadView.getChildCount();

    View.OnClickListener listener = new View.OnClickListener() {
      @Override
      public void onClick (View view) {
        Button button = (Button)view;
        String text = button.getText().toString();

        int start = phoneNumberView.getSelectionStart();
        int end = phoneNumberView.getSelectionEnd();
        phoneNumberView.getText().replace(start, end, text);

        int cursor = start + text.length();
        phoneNumberView.setSelection(cursor);
      }
    };

    for (int index=0; index<count; index+=1) {
      View view = keypadView.getChildAt(index);
      view.setOnClickListener(listener);
    }
  }

  @Override
  protected void onCreate (Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.phone);
    phoneNumberView = (EditText)findViewById(R.id.phone_number);
    keypadView = (ViewGroup)findViewById(R.id.keypad);

    phoneNumberView.addTextChangedListener(new PhoneNumberWatcher());
    phoneNumberView.addTextChangedListener(new PhoneNumberWatcher());
    addKeypadListeners();
  }
}
