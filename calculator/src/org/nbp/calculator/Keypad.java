package org.nbp.calculator;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import android.util.TypedValue;

public enum Keypad {
  DECIMAL(R.id.keypad_decimal, true),
  FUNCTION(R.id.keypad_function, false),
  HEXADECIMAL(R.id.keypad_hexadecimal, true);

  private final int keypadIdentifier;
  private final boolean isNumeric;

  public final int getIdentifier () {
    return keypadIdentifier;
  }

  private Keypad (int identifier, boolean numeric) {
    keypadIdentifier = identifier;
    isNumeric = numeric;
  }

  private ViewGroup keypadView = null;

  public final ViewGroup getView () {
    return keypadView;
  }

  public interface KeyHandler {
    public void handleKey (TextView key);
  }

  public final void forEachKey (KeyHandler keyHandler) {
    int rowCount = keypadView.getChildCount();

    for (int rowIndex=0; rowIndex<rowCount; rowIndex+=1) {
      ViewGroup row = (ViewGroup)keypadView.getChildAt(rowIndex);
      int columnCount = row.getChildCount();

      for (int columnIndex=0; columnIndex<columnCount; columnIndex+=1) {
        TextView key = (TextView)row.getChildAt(columnIndex);
        keyHandler.handleKey(key);
      }
    }
  }

  public interface KeypadHandler {
    public void handleKeypad (Keypad keypad);
  }

  public final static void forEachKeypad (KeypadHandler keypadHandler) {
    for (Keypad keypad : values()) {
      keypadHandler.handleKeypad(keypad);
    }
  }

  public final void show () {
    forEachKeypad(
      new KeypadHandler() {
        @Override
        public void handleKeypad (Keypad keypad) {
          int visibility = (keypad == Keypad.this)? View.VISIBLE: View.GONE;
          keypad.getView().setVisibility(visibility);
        }
      }
    );
  }

  public final void focus () {
    keypadView.requestFocus();
  }

  public final static void prepareKeypads (final Activity activity) {
    forEachKeypad(
      new KeypadHandler() {
        @Override
        public void handleKeypad (final Keypad keypad) {
          keypad.keypadView = (ViewGroup)activity.findViewById(keypad.keypadIdentifier);

          keypad.forEachKey(
            new KeyHandler() {
              @Override
              public void handleKey (TextView key) {
                if (keypad.isNumeric) {
                  key.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                }
              }
            }
          );
        }
      }
    );
  }
}
