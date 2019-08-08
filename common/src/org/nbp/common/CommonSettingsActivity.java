package org.nbp.common;

import java.util.Collection;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.Set;

import org.nbp.common.controls.*;

import android.util.Log;
import android.os.Bundle;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;

import android.view.View;
import android.view.ViewGroup;

import android.widget.ListView;
import android.widget.AdapterView;

import android.widget.TextView;
import android.widget.EditText;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

import android.app.AlertDialog;
import android.content.DialogInterface;

public abstract class CommonSettingsActivity extends CommonActivity {
  private final static String LOG_TAG = CommonSettingsActivity.class.getName();

  private final Control[] controlsInCreationOrder;
  private final Control[] controlsInRestoreOrder;

  protected CommonSettingsActivity (Control[] inCreationOrder, Control[] inRestoreOrder) {
    super();
    controlsInCreationOrder = inCreationOrder;
    controlsInRestoreOrder = inRestoreOrder;
  }

  protected CommonSettingsActivity (Control[] controls) {
    this(controls, controls);
  }

  protected CommonSettingsActivity () {
    controlsInCreationOrder = null;
    controlsInRestoreOrder = null;
  }

  private final static int FRAGMENT_CONTAINER_ID = R.id.settings_fragment_container;

  private void updateWidget (Runnable runnable) {
    runOnUiThread(runnable);
  }

  private static class ControlValueChangedListener {
    public final Control control;
    public final Control.OnValueChangedListener listener;

    public ControlValueChangedListener (Control control, Control.OnValueChangedListener listener) {
      this.control = control;
      this.listener = listener;
    }
  }

  private final List<ControlValueChangedListener> controlValueChangedListeners = new ArrayList<ControlValueChangedListener>();

  private void addControlValueChangedListener (Control control, Control.OnValueChangedListener listener) {
    control.addOnValueChangedListener(listener);
    controlValueChangedListeners.add(new ControlValueChangedListener(control, listener));
  }

  private void removeControlValueChangedListeners () {
    for (ControlValueChangedListener entry : controlValueChangedListeners) {
      entry.control.removeOnValueChangedListener(entry.listener);
    }

    controlValueChangedListeners.clear();
  }

  private static void setText (TextView view, CharSequence text) {
    view.setText(text);

    if (text.length() == 0) {
      view.setHint(R.string.control_hint_not_set);
    } else {
      view.setHint("");
    }
  }

  private static void setText (TextView view, Control control) {
    setText(view, control.getValue());
  }

  private static void setChecked (CompoundButton button, Control control) {
    button.setChecked(((BooleanControl)control).getBooleanValue());
  }

  protected void saveSettings () {
    Control.saveValues(controlsInCreationOrder);
  }

  private View createSaveControlsButton () {
    Button button = newButton(
      R.string.control_save_label,
      new Button.OnClickListener() {
        @Override
        public void onClick (View view) {
          saveSettings();
          Control.confirm(R.string.control_save_confirmation);
        }
      }
    );

    return button;
  }

  protected void restoreSettings () {
    Control.restoreSavedValues(controlsInRestoreOrder);
  }

  private View createRestoreControlsButton () {
    Button button = newButton(
      R.string.control_restore_label,
      new Button.OnClickListener() {
        @Override
        public void onClick (View view) {
          restoreSettings();
          Control.confirm(R.string.control_restore_confirmation);
        }
      }
    );

    return button;
  }

  protected void resetSettings () {
    Control.restoreDefaultValues(controlsInRestoreOrder);
  }

  private View createResetControlsButton () {
    Button button = newButton(
      R.string.control_reset_label,
      new Button.OnClickListener() {
        @Override
        public void onClick (View view) {
          resetSettings();
          Control.confirm(R.string.control_reset_confirmation);
        }
      }
    );

    return button;
  }

  protected View[] getExtraMainScreenActions () {
    return null;
  }

  private View createActionsView () {
    ViewGroup container = newHorizontalGroup(
      createSaveControlsButton(),
      createRestoreControlsButton(),
      createResetControlsButton()
    );

    {
      View[] views = getExtraMainScreenActions();
      if (views != null) addViews(container, views);
    }

    return container;
  }

  private View createItemChangeButton (final Control control) {
    final ItemControl ic = (ItemControl)control;

    Button button = newButton(
      R.string.control_change_label,
      new Button.OnClickListener() {
        @Override
        public void onClick (View view) {
          if (ic instanceof CollectionControl) {
            CollectionControl cc = (CollectionControl)ic;
            cc.refreshCollection();
          }

          final CharSequence[] labels = ic.getHighlightedItemLabels();

          final DialogInterface.OnClickListener listener =
            new DialogInterface.OnClickListener() {
              @Override
              public void onClick (DialogInterface dialog, int which) {
                ic.setValue(which);
                dialog.dismiss();
              }
            };

          new AlertDialog.Builder(CommonSettingsActivity.this)
            .setTitle(ic.getLabel())
            .setSingleChoiceItems(labels, ic.getIntegerValue(), listener)
            .setNegativeButton(android.R.string.no, null)
            .setCancelable(true)
            .show();
        }
      }
    );

    return button;
  }

  private final void setStringControl (final StringControl control, CharSequence value) {
    final EditText view = newEditText();
    setText(view, value);
    view.setSelection(value.length());

    DialogInterface.OnClickListener listener =
      new DialogInterface.OnClickListener() {
        @Override
        public void onClick (DialogInterface dialog, int button) {
          control.setValue(view.getText().toString().trim());
        }
      };

    new AlertDialog.Builder(getActivity())
      .setTitle(control.getLabel())
      .setView(view)
      .setPositiveButton(android.R.string.yes, listener)
      .setNegativeButton(android.R.string.no, null)
      .setCancelable(true)
      .show();
  }

  private final boolean setStringControl (final StringControl control, final String[] choices) {
    if (choices == null) return false;
    int count = choices.length;
    if (count == 0) return false;

    if (count == 1) {
      setStringControl(control, choices[0]);
    } else {
      DialogInterface.OnClickListener listener =
        new DialogInterface.OnClickListener() {
          @Override
          public void onClick (DialogInterface dialog, int which) {
            setStringControl(control, choices[which]);
          }
        };

      new AlertDialog.Builder(getActivity())
        .setTitle(control.getLabel())
        .setItems(choices, listener)
        .setNegativeButton(android.R.string.no, null)
        .setCancelable(true)
        .show();
    }

    return true;
  }

  private final boolean setStringControl (StringControl control, Collection<String> choices) {
    if (choices == null) return false;
    return setStringControl(control, choices.toArray(new String[choices.size()]));
  }

  private final void setStringControl (StringControl control) {
    CharSequence value = control.getValue();

    if (value.length() == 0) {
      if (setStringControl(control, control.getSuggestedValues())) {
        return;
      }
    }

    setStringControl(control, value);
  }

  private View createStringEditButton (final Control control) {
    Button button = newButton(
      R.string.control_edit_label,
      new Button.OnClickListener() {
        @Override
        public void onClick (View view) {
          setStringControl((StringControl)control);
        }
      }
    );

    return button;
  }

  private View createPreviousValueButton (final Control control) {
    Button button = newButton(
      control.getLabelForPrevious(),
      new Button.OnClickListener() {
        @Override
        public void onClick (View view) {
          if (!control.previousValue()) Tones.beep();
        }
      }
    );

    return button;
  }

  private View createNextValueButton (final Control control) {
    Button button = newButton(
      control.getLabelForNext(),
      new Button.OnClickListener() {
        @Override
        public void onClick (View view) {
          if (!control.nextValue()) Tones.beep();
        }
      }
    );

    return button;
  }

  private View createLabelView (CharSequence label) {
    TextView view = newTextView(label);
    return view;
  }

  private View createLabelView (int label) {
    return createLabelView(getString(label));
  }

  private View createLabelView (Control control) {
    return createLabelView(control.getLabel());
  }

  private View createBooleanValueView (final Control control) {
    final Switch view = newSwitch(
      new Switch.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged (CompoundButton button, boolean isChecked) {
          if (isChecked) {
            control.nextValue();
          } else {
            control.previousValue();
          }
        }
      }
    );

    view.setTextOff(control.getLabelForPrevious());
    view.setTextOn(control.getLabelForNext());
    setChecked(view, control);

    addControlValueChangedListener(control,
      new Control.OnValueChangedListener() {
        @Override
        public void onValueChanged (final Control control) {
          updateWidget(new Runnable() {
            @Override
            public void run () {
              setChecked(view, control);
            }
          });
        }
      }
    );

    return view;
  }

  private View createTextValueView (Control control) {
    final TextView view = newTextView();
    setText(view, control);

    addControlValueChangedListener(control,
      new Control.OnValueChangedListener() {
        @Override
        public void onValueChanged (final Control control) {
          updateWidget(new Runnable() {
            @Override
            public void run () {
              setText(view, control);
            }
          });
        }
      }
    );

    return view;
  }

  private View createControlView (Control control) {
    ViewGroup group = newTableRow();
    group.addView(createLabelView(control));

    if (control instanceof BooleanControl) {
      group.addView(createBooleanValueView(control));
    } else {
      group.addView(createTextValueView(control));

      if (control instanceof ItemControl) {
        group.addView(createItemChangeButton(control));
      } else if (control instanceof StringControl) {
        group.addView(createStringEditButton(control));
      } else {
        group.addView(createPreviousValueButton(control));
        group.addView(createNextValueButton(control));
      }
    }

    return group;
  }

  private Fragment getFragment (final View view, int title) {
    return CommonSettingsFragment.get(view, getString(title));
  }

  private Fragment createControlGroupsFragment () {
    Map<String, ViewGroup> groupTables = new LinkedHashMap<String, ViewGroup>();

    for (Control control : controlsInCreationOrder) {
      String label = control.getGroup();
      ViewGroup table = groupTables.get(label);

      if (table == null) {
        table = newTable();
        groupTables.put(label, table);
      }

      table.addView(createControlView(control));
    }

    String mainLabel = getString(R.string.control_group_main);
    ViewGroup mainTable = groupTables.get(mainLabel);

    if (mainTable != null) {
      groupTables.remove(mainLabel);
    } else if (groupTables.size() == 1) {
      String[] labels = new String[1];
      String label = groupTables.keySet().toArray(labels)[0];

      mainTable = groupTables.get(label);
      groupTables.remove(label);
    }

    ViewGroup mainContainer = newVerticalGroup(createActionsView());

    if (!groupTables.isEmpty()) {
      Set<String> labelSet = groupTables.keySet();
      int labelCount = labelSet.size();
      final Fragment[] fragmentArray = new Fragment[labelCount];

      {
        int index = 0;

        for (String label : labelSet) {
          fragmentArray[index++] = CommonSettingsFragment.get(groupTables.get(label), label);
        }
      }

      ListView labelList = newListView(labelSet);
      mainContainer.addView(labelList);

      labelList.setOnItemClickListener(
        new AdapterView.OnItemClickListener() {
          @Override
          public void onItemClick (AdapterView list, View item, int position, long id) {
            list.setSelection(position);

            getFragmentManager()
              .beginTransaction()
              .replace(FRAGMENT_CONTAINER_ID, fragmentArray[position])
              .addToBackStack(null)
              .commit();
          }
        }
      );
    }

    if (mainTable != null) {
      mainContainer.addView(newVerticalScrollContainer(mainTable));
    }

    return getFragment(mainContainer, R.string.control_group_main);
  }

  @Override
  public void onBackPressed () {
    FragmentManager fm = getFragmentManager();

    if (fm.getBackStackEntryCount() > 0) {
      fm.popBackStack();
    } else {
      super.onBackPressed();
    }
  }

  @Override
  protected void onCreate (Bundle state) {
    super.onCreate(state);

    lockScreenOrientation();
    setContentView(R.layout.common_settings);

    if (controlsInCreationOrder != null) {
      getFragmentManager()
        .beginTransaction()
        .add(FRAGMENT_CONTAINER_ID, createControlGroupsFragment())
        .commit();
    } else {
      ViewGroup container = (ViewGroup)findViewById(FRAGMENT_CONTAINER_ID);
      container.addView(newTextView(R.string.control_group_none));
    }
  }

  @Override
  protected void onDestroy () {
    try {
      removeControlValueChangedListeners();
    } finally {
      super.onDestroy();
    }
  }
}
