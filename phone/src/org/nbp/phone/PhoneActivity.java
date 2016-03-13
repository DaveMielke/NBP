package org.nbp.phone;

import android.app.Activity;
import android.os.Bundle;

import android.view.View;
import android.view.ViewGroup;
import android.view.KeyEvent;

import android.widget.TextView;
import android.widget.EditText;
import android.widget.Button;

import android.content.Intent;
import android.net.Uri;

public class PhoneActivity extends Activity {
  private EditText phoneNumberView = null;
  private ViewGroup keypadView = null;

  private final void dialPhoneNumber () {
    String phoneNumber = phoneNumberView.getText().toString();

    Intent intent = new Intent(
      Intent.ACTION_DIAL,
      Uri.parse(("tel:" + phoneNumber))
    );

    startActivity(intent);
  }

  private final void addEnterKeyListener () {
    phoneNumberView.setOnEditorActionListener(
      new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction (TextView view, int action, KeyEvent event) {
          if (event != null) {
            if (event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
              if (event.getAction() == KeyEvent.ACTION_DOWN) {
                dialPhoneNumber();
              }
            }
          }

          return true;
        }
      }
    );
  }

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

  private final void addCallListener () {
    ((Button)findViewById(R.id.call)).setOnClickListener(
      new Button.OnClickListener() {
        @Override
        public void onClick (View view) {
          dialPhoneNumber();
        }
      }
    );
  }

  @Override
  protected void onCreate (Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.phone);
    phoneNumberView = (EditText)findViewById(R.id.phone_number);
    keypadView = (ViewGroup)findViewById(R.id.keypad);

    phoneNumberView.addTextChangedListener(new PhoneNumberWatcher());
    addEnterKeyListener();
    addKeypadListeners();
    addCallListener();
  }
}
