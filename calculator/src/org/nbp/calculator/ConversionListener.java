package org.nbp.calculator;
import org.nbp.calculator.conversion.*;

import java.util.Map;
import java.util.HashMap;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Button;

import android.app.AlertDialog;
import android.content.DialogInterface;

public class ConversionListener {
  private final static Conversion conversion = Conversion.getInstance();

  private static class StringTable {
    private final String[] stringArray;
    private final Map<String, Integer> stringMap = new HashMap<String, Integer>();

    public StringTable (int size) {
      stringArray = new String[size];
    }

    public final void set (int index, String string) {
      stringArray[index] = string;
      stringMap.put(string, index);
    }

    public final String[] get () {
      return stringArray;
    }

    public final String get (int index) {
      return stringArray[index];
    }

    public final int get (String string) {
      return stringMap.get(string);
    }
  }

  private final static StringTable unitTypeNames;
  private final static StringTable[] unitSymbols;
  private final static String[][] unitNames;

  static {
    final UnitType[] unitTypeArray = conversion.getUnitTypes();

    final int unitTypeCount = unitTypeArray.length;
    int unitTypeIndex = 0;

    unitTypeNames = new StringTable(unitTypeCount);
    unitSymbols = new StringTable[unitTypeCount];
    unitNames = new String[unitTypeCount][];

    for (UnitType type : unitTypeArray) {
      final Unit[] unitArray = type.getUnits();
      final int unitCount = unitArray.length;
      final StringTable symbols = new StringTable(unitCount);
      final String[] names = new String[unitCount];

      for (int unitIndex=0; unitIndex<unitCount; unitIndex+=1) {
        final Unit unit = unitArray[unitIndex];

        String symbol = unit.getSymbol();
        if (symbol == null) continue;
        if (symbol.isEmpty()) continue;
        symbols.set(unitIndex, symbol);

        StringBuilder sb = new StringBuilder(symbol);
        final String name = unit.getName();
        sb.append(" [");
        sb.append(name);
        sb.append("]");
        names[unitIndex] = sb.toString();
      }

      unitTypeNames.set(unitTypeIndex, type.getName());
      unitSymbols[unitTypeIndex] = symbols;
      unitNames[unitTypeIndex] = names;
      unitTypeIndex += 1;
    }
  }

  private final int conversionIdentifier;
  private final ViewGroup conversionRow;

  private final Button typeButton;
  private final Button fromButton;
  private final Button toButton;
  private final Button convertButton;

  private final String typeSetting;
  private int selectedUnitType = -1;

  private String fromSetting;
  private int selectedFromUnit;

  private String toSetting;
  private int selectedToUnit;

  private final String makeSetting (String name) {
    return "conversion" + conversionIdentifier + "-" + name;
  }

  private final void saveSelectedUnitType () {
    SavedSettings.set(typeSetting, unitTypeNames.get(selectedUnitType));
  }

  private final void saveSelectedFromUnit () {
    SavedSettings.set(typeSetting, unitSymbols[selectedUnitType].get(selectedFromUnit));
  }

  private final void saveSelectedToUnit () {
    SavedSettings.set(typeSetting, unitSymbols[selectedUnitType].get(selectedToUnit));
  }

  private final int getUnitIndex (String setting) {
    String name = SavedSettings.get(setting, null);

    if (name != null) {
      Integer index = unitSymbols[selectedUnitType].get(name);
      if (index != null) return index;
    }

    return 0;
  }

  private final void setConvertButton () {
    String from = unitSymbols[selectedUnitType].get(selectedFromUnit);
    String to = unitSymbols[selectedUnitType].get(selectedToUnit);
    String function = from + '2' + to;
    convertButton.setText(function);
    convertButton.setTag((function + Function.ARGUMENT_PREFIX));
  }

  private final  void setUnitType (int index) {
    selectedUnitType = index;
    String typeName = unitTypeNames.get(index);

    fromSetting = makeSetting(("from-" + typeName));
    selectedFromUnit = getUnitIndex(fromSetting);

    toSetting = makeSetting(("to-" + typeName));
    selectedToUnit = getUnitIndex(toSetting);

    saveSelectedUnitType();
    saveSelectedFromUnit();
    saveSelectedToUnit();
    setConvertButton();
  }

  private final void restoreType () {
    String name = SavedSettings.get(typeSetting, conversion.LENGTH.getName());
    Integer index = unitTypeNames.get(name);
    if (index == null) index = 0;
    setUnitType(index);
  }

  private final DialogInterface.OnClickListener typeSelectedListener =
    new DialogInterface.OnClickListener() {
      @Override
      public void onClick (DialogInterface dialog, int index) {
        if (index != selectedUnitType) setUnitType(index);
      }
    };

  private final View.OnClickListener typeChangeListener =
    new View.OnClickListener() {
      @Override
      public void onClick (View view) {
        AlertDialog.Builder builder = ApplicationUtilities.newAlertDialogBuilder(
          R.string.title_conversion_type
        );

        builder.setItems(unitTypeNames.get(), typeSelectedListener);
        builder.show();
      }
    };

  private final DialogInterface.OnClickListener fromSelectedListener =
    new DialogInterface.OnClickListener() {
      @Override
      public void onClick (DialogInterface dialog, int index) {
        if (index != selectedFromUnit) {
          selectedFromUnit = index;
          saveSelectedFromUnit();
          setConvertButton();
        }
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
        if (index != selectedToUnit) {
          selectedToUnit = index;
          saveSelectedToUnit();
          setConvertButton();
        }
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

    typeSetting = makeSetting("type");
    restoreType();

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
