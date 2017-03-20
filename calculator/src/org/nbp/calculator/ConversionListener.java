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

  private final static UnitType[] defaultUnitTypes = new UnitType[] {
    conversion.TEMPERATURE,
    conversion.LENGTH,
    conversion.AREA,
    conversion.VOLUME,
    conversion.ANGLE,
    conversion.TIME
  };

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

    public final Integer get (String string) {
      return stringMap.get(string);
    }
  }

  private final static StringTable unitTypeNames;
  private final static StringTable[] unitSymbols;
  private final static String[][] unitDescriptions;

  static {
    final UnitType[] unitTypeArray = conversion.getUnitTypes();
    final int unitTypeCount = unitTypeArray.length;
    int unitTypeIndex = 0;

    unitTypeNames = new StringTable(unitTypeCount);
    unitSymbols = new StringTable[unitTypeCount];
    unitDescriptions = new String[unitTypeCount][];

    for (UnitType type : unitTypeArray) {
      Unit[] unitArray = type.getUnits();
      int unitCount = unitArray.length;
      StringTable symbols = new StringTable(unitCount);
      String[] descriptions = new String[unitCount];

      {
        Unit[] secondaryUnits = null;
        int secondaryCount = 0;
        int primaryCount = 0;

        for (Unit unit : unitArray) {
          if (unit instanceof SecondaryUnit) {
            if (secondaryUnits == null) secondaryUnits = new Unit[unitCount];
            secondaryUnits[secondaryCount++] = unit;
          } else {
            unitArray[primaryCount++] = unit;
          }
        }

        if (secondaryUnits != null) {
          System.arraycopy(secondaryUnits, 0, unitArray, primaryCount, secondaryCount);
        }
      }

      for (int unitIndex=0; unitIndex<unitCount; unitIndex+=1) {
        final Unit unit = unitArray[unitIndex];
        symbols.set(unitIndex, unit.getSymbol());
        descriptions[unitIndex] = conversion.makeDescription(unit);
      }

      unitTypeNames.set(unitTypeIndex, type.getName());
      unitSymbols[unitTypeIndex] = symbols;
      unitDescriptions[unitTypeIndex] = descriptions;
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

  private final StringTable getUnitSymbols () {
    return unitSymbols[selectedUnitType];
  }

  private final String getUnitSymbol (int index) {
    return getUnitSymbols().get(index);
  }

  private final String getFromUnitSymbol () {
    return getUnitSymbol(selectedFromUnit);
  }

  private final String getToUnitSymbol () {
    return getUnitSymbol(selectedToUnit);
  }

  private final void saveUnitType () {
    SavedSettings.set(typeSetting, unitTypeNames.get(selectedUnitType));
  }

  private final void saveFromUnit () {
    SavedSettings.set(fromSetting, getFromUnitSymbol());
  }

  private final void saveToUnit () {
    SavedSettings.set(toSetting, getToUnitSymbol());
  }

  private final int getSavedUnit (String setting, Unit defaultUnit) {
    String symbol = SavedSettings.get(setting, null);

    if (symbol == null) {
      if (defaultUnit != null) {
        symbol = defaultUnit.getSymbol();
      }
    }

    if (symbol != null) {
      Integer index = getUnitSymbols().get(symbol);
      if (index != null) return index;
    }

    return 0;
  }

  private final void setConvertButton () {
    String from = getFromUnitSymbol();
    String to = getToUnitSymbol();
    String function = from + ConversionFunction.SEPARATOR + to;

    convertButton.setText(function);
    convertButton.setTag((function + Function.ARGUMENT_PREFIX));
  }

  private final void setUnitType (int index) {
    selectedUnitType = index;
    String typeName = unitTypeNames.get(index);
    UnitType type = conversion.getUnitType(typeName);

    fromSetting = makeSetting(("from-" + typeName));
    selectedFromUnit = getSavedUnit(fromSetting, type.getDefaultFromUnit());

    toSetting = makeSetting(("to-" + typeName));
    selectedToUnit = getSavedUnit(toSetting, type.getDefaultToUnit());

    saveUnitType();
    saveFromUnit();
    saveToUnit();
    setConvertButton();
  }

  private final void setUnitType (String name) {
    setUnitType(unitTypeNames.get(name));
  }

  private final void setUnitType (UnitType type) {
    setUnitType(type.getName());
  }

  private final void restoreUnitType () {
    {
      String name = SavedSettings.get(typeSetting, null);

      if (name != null) {
        Integer index = unitTypeNames.get(name);

        if (index != null) {
          setUnitType(index);
          return;
        }
      }
    }

    if (conversionIdentifier < defaultUnitTypes.length) {
      UnitType defaultUnitType = defaultUnitTypes[conversionIdentifier];

      if (defaultUnitType != null) {
        setUnitType(defaultUnitType);
        return;
      }
    }

    setUnitType(conversion.LENGTH);
  }

  private final void dismissDialog (DialogInterface dialog) {
    ((AlertDialog)dialog).dismiss();
  }

  private final DialogInterface.OnClickListener selectionCancelledListener =
    new DialogInterface.OnClickListener() {
      @Override
      public void onClick (DialogInterface dialog, int index) {
        dismissDialog(dialog);
      }
    };

  private final void showDialog (
    int subtitle, CharSequence[] items, int selectedItem,
    DialogInterface.OnClickListener itemSelectedListener
  ) {
    ApplicationUtilities.newAlertDialogBuilder(subtitle)
      .setNegativeButton(R.string.button_cancel, selectionCancelledListener)
      .setSingleChoiceItems(items, selectedItem, itemSelectedListener)
      .show();
  }

  private final DialogInterface.OnClickListener unitTypeSelectedListener =
    new DialogInterface.OnClickListener() {
      @Override
      public void onClick (DialogInterface dialog, int index) {
        if (index != selectedUnitType) setUnitType(index);
        dismissDialog(dialog);
      }
    };

  private final View.OnClickListener unitTypeChangeListener =
    new View.OnClickListener() {
      @Override
      public void onClick (View view) {
        showDialog(
          R.string.title_conversion_type, unitTypeNames.get(),
          selectedUnitType, unitTypeSelectedListener
        );
      }
    };

  private final DialogInterface.OnClickListener fromUnitSelectedListener =
    new DialogInterface.OnClickListener() {
      @Override
      public void onClick (DialogInterface dialog, int index) {
        if (index != selectedFromUnit) {
          selectedFromUnit = index;
          saveFromUnit();
          setConvertButton();
        }

        dismissDialog(dialog);
      }
    };

  private final View.OnClickListener fromUnitChangeListener =
    new View.OnClickListener() {
      @Override
      public void onClick (View view) {
        showDialog(
          R.string.title_conversion_from, unitDescriptions[selectedUnitType],
          selectedFromUnit, fromUnitSelectedListener
        );
      }
    };

  private final DialogInterface.OnClickListener toUnitSelectedListener =
    new DialogInterface.OnClickListener() {
      @Override
      public void onClick (DialogInterface dialog, int index) {
        if (index != selectedToUnit) {
          selectedToUnit = index;
          saveToUnit();
          setConvertButton();
        }

        dismissDialog(dialog);
      }
    };

  private final View.OnClickListener toUnitChangeListener =
    new View.OnClickListener() {
      @Override
      public void onClick (View view) {
        showDialog(
          R.string.title_conversion_to, unitDescriptions[selectedUnitType],
          selectedToUnit, toUnitSelectedListener
        );
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
    restoreUnitType();

    typeButton.setOnClickListener(unitTypeChangeListener);
    fromButton.setOnClickListener(fromUnitChangeListener);
    toButton.setOnClickListener(toUnitChangeListener);
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
