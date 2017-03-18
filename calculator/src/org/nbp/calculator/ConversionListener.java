package org.nbp.calculator;
import org.nbp.calculator.conversion.*;

import java.util.Map;
import java.util.LinkedHashMap;

import java.util.Set;
import java.util.LinkedHashSet;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Button;

import android.app.AlertDialog;
import android.content.DialogInterface;

public class ConversionListener {
  private final static String[] unitTypeNames;
  private final static String[][] unitNames;

  static {
    Conversion conversion = Conversion.getInstance();
    UnitType[] unitTypes = UnitType.get();
    Map<UnitType, Set<String>> map = new LinkedHashMap<UnitType, Set<String>>();

    for (UnitType type : unitTypes) {
      map.put(type, new LinkedHashSet<String>());
    }

    for (Unit unit : Unit.get()) {
      String symbol = unit.getSymbol();
      if (symbol == null) continue;

      StringBuilder sb = new StringBuilder(symbol);
      sb.append(" [");
      sb.append(unit.getName());
      sb.append("]");

      map.get(unit.getType()).add(sb.toString());
    }

    int unitTypeCount = unitTypes.length;
    unitTypeNames = new String[unitTypeCount];
    unitNames = new String[unitTypeCount][];

    for (int unitTypeIndex=0; unitTypeIndex<unitTypeCount; unitTypeIndex+=1) {
      UnitType type = unitTypes[unitTypeIndex];
      unitTypeNames[unitTypeIndex] = type.getName();
      unitNames[unitTypeIndex] = ApplicationUtilities.toArray(map.get(type));
    }
  }

  private final int conversionIdentifier;
  private final ViewGroup conversionRow;

  private final Button typeButton;
  private final Button fromButton;
  private final Button toButton;
  private final Button convertButton;

  private final String unitSetting;
  private final String fromSetting;
  private final String toSetting;

  private final String makeSetting (String name) {
    return "conversion" + conversionIdentifier + "-" + name;
  }

  private int selectedUnitType = 0;

  private final DialogInterface.OnClickListener typeSelectedListener =
    new DialogInterface.OnClickListener() {
      @Override
      public void onClick (DialogInterface dialog, int index) {
        selectedUnitType = index;
      }
    };

  private final View.OnClickListener typeChangeListener =
    new View.OnClickListener() {
      @Override
      public void onClick (View view) {
        AlertDialog.Builder builder = ApplicationUtilities.newAlertDialogBuilder(
          R.string.title_conversion_type
        );

        builder.setItems(unitTypeNames, typeSelectedListener);
        builder.show();
      }
    };

  private final DialogInterface.OnClickListener fromSelectedListener =
    new DialogInterface.OnClickListener() {
      @Override
      public void onClick (DialogInterface dialog, int index) {
      }
    };

  private final View.OnClickListener fromChangeListener =
    new View.OnClickListener() {
      @Override
      public void onClick (View view) {
        AlertDialog.Builder builder = ApplicationUtilities.newAlertDialogBuilder(
          R.string.title_conversion_from
        );

        builder.setItems(unitNames[selectedUnitType], fromSelectedListener);
        builder.show();
      }
    };

  private final DialogInterface.OnClickListener toSelectedListener =
    new DialogInterface.OnClickListener() {
      @Override
      public void onClick (DialogInterface dialog, int index) {
      }
    };

  private final View.OnClickListener toChangeListener =
    new View.OnClickListener() {
      @Override
      public void onClick (View view) {
        AlertDialog.Builder builder = ApplicationUtilities.newAlertDialogBuilder(
          R.string.title_conversion_to
        );

        builder.setItems(unitNames[selectedUnitType], toSelectedListener);
        builder.show();
      }
    };

  private ConversionListener (int identifier, ViewGroup row, Button.OnClickListener convertListener) {
    conversionIdentifier = identifier;
    conversionRow = row;

    typeButton = (Button)row.getChildAt(0);
    fromButton = (Button)row.getChildAt(1);
    toButton = (Button)row.getChildAt(2);
    convertButton = (Button)row.getChildAt(3);

    unitSetting = makeSetting("unit");
    fromSetting = makeSetting("from");
    toSetting = makeSetting("to");

    typeButton.setOnClickListener(typeChangeListener);
    fromButton.setOnClickListener(fromChangeListener);
    toButton.setOnClickListener(toChangeListener);
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
