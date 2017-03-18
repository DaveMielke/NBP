package org.nbp.calculator;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Button;

public class ConversionListener {
  private final int conversionIdentifier;
  private final ViewGroup conversionRow;

  private final Button unitButton;
  private final Button fromButton;
  private final Button toButton;
  private final Button convertButton;

  private final String unitSetting;
  private final String fromSetting;
  private final String toSetting;

  private final String makeSetting (String name) {
    return "convert_" + name + "_" + conversionIdentifier;
  }

  private final static View.OnClickListener unitButtonListener =
    new View.OnClickListener() {
      @Override
      public void onClick (View view) {
      }
    };

  private final static View.OnClickListener fromButtonListener =
    new View.OnClickListener() {
      @Override
      public void onClick (View view) {
      }
    };

  private final static View.OnClickListener toButtonListener =
    new View.OnClickListener() {
      @Override
      public void onClick (View view) {
      }
    };

  private ConversionListener (int identifier, ViewGroup row, Button.OnClickListener convertListener) {
    conversionIdentifier = identifier;
    conversionRow = row;

    unitButton = (Button)row.getChildAt(0);
    fromButton = (Button)row.getChildAt(1);
    toButton = (Button)row.getChildAt(2);
    convertButton = (Button)row.getChildAt(3);

    unitSetting = makeSetting("unit");
    fromSetting = makeSetting("from");
    toSetting = makeSetting("to");

    unitButton.setOnClickListener(unitButtonListener);
    fromButton.setOnClickListener(fromButtonListener);
    toButton.setOnClickListener(toButtonListener);
    convertButton.setOnClickListener(convertListener);
  }

  public final static void register (final Button.OnClickListener convertListener) {
    ViewGroup keypad = Keypad.CONVERSION.getView();
    int rowCount = keypad.getChildCount();

    for (int rowIndex=0; rowIndex<rowCount; rowIndex+=1) {
      new ConversionListener(
        rowIndex, (ViewGroup)keypad.getChildAt(rowIndex), convertListener
      );
    }
  }
}
