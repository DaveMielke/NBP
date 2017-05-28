package org.nbp.common;

import java.util.Collection;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.Set;

import android.util.Log;
import android.os.Bundle;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;

import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;

import android.widget.ListView;
import android.widget.AdapterView;

import android.widget.TextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

import android.app.AlertDialog;
import android.content.DialogInterface;

public abstract class CommonSettingsActivity extends CommonActivity {
  private final static String LOG_TAG = CommonSettingsActivity.class.getName();

  private final Control[] settingControls;

  protected CommonSettingsActivity (Control[] controls) {
    super();

    int count = controls.length;
    settingControls = new Control[count];
    System.arraycopy(controls, 0, settingControls, 0, count);
  }

  protected CommonSettingsActivity (Collection<Control> controls) {
    super();

    int count = controls.size();
    settingControls = new Control[count];
    controls.toArray(settingControls);
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

  private static void setChecked (CompoundButton button, Control control) {
    button.setChecked(((BooleanControl)control).getBooleanValue());
  }

  private View createSaveControlsButton () {
    Button button = newButton(
      R.string.control_save_label,
      new Button.OnClickListener() {
        @Override
        public void onClick (View view) {
          Control.saveValues(settingControls);
          Control.confirm(R.string.control_save_confirmation);
        }
      }
    );

    return button;
  }

  private View createRestoreControlsButton () {
    Button button = newButton(
      R.string.control_restore_label,
      new Button.OnClickListener() {
        @Override
        public void onClick (View view) {
          Control.restoreSavedValues(settingControls);
          Control.confirm(R.string.control_restore_confirmation);
        }
      }
    );

    return button;
  }

  private View createResetControlsButton () {
    Button button = newButton(
      R.string.control_reset_label,
      new Button.OnClickListener() {
        @Override
        public void onClick (View view) {
          Control.restoreDefaultValues(settingControls);
          Control.confirm(R.string.control_reset_confirmation);
        }
      }
    );

    return button;
  }

  private View createActionsView () {
    return newHorizontalGroup(
      createSaveControlsButton(),
      createRestoreControlsButton(),
      createResetControlsButton()
    );
  }

  private View createEnumerationChangeButton (final Control control) {
    final EnumerationControl ec = (EnumerationControl)control;
    final CharSequence[] labels = ec.getValueLabels();

    final DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
      @Override
      public void onClick (DialogInterface dialog, int which) {
        ec.setValue(which);
        dialog.dismiss();
      }
    };

    final AlertDialog.Builder builder = new AlertDialog.Builder(this)
      .setTitle(ec.getLabel())
      .setNeutralButton(android.R.string.no, null)
      .setCancelable(true);

    Button button = newButton(
      R.string.control_change_label,
      new Button.OnClickListener() {
        @Override
        public void onClick (View view) {
          builder.setSingleChoiceItems(labels, ec.getIntegerValue(), listener)
                 .show();
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

  private View createIntegerValueView (Control control) {
    final TextView view = newTextView(control.getValue());

    addControlValueChangedListener(control,
      new Control.OnValueChangedListener() {
        @Override
        public void onValueChanged (final Control control) {
          updateWidget(new Runnable() {
            @Override
            public void run () {
              view.setText(control.getValue());
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
      group.addView(createIntegerValueView(control));

      if (control instanceof EnumerationControl) {
        group.addView(createEnumerationChangeButton(control));
      } else {
        group.addView(createPreviousValueButton(control));
        group.addView(createNextValueButton(control));
      }
    }

    return group;
  }

  private Fragment createFragment (final View view, final String title) {
    return new Fragment() {
      private TextView titleView = null;
      private View focusedView = null;

      @Override
      public void onCreate (Bundle state) {
        super.onCreate(state);
        titleView = (TextView)findViewById(R.id.settings_fragment_title);
      }

      @Override
      public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle state) {
        return view;
      }

      @Override
      public void onResume () {
        super.onResume();
        titleView.setText(title);

        if (focusedView != null) {
          focusedView.requestFocus();
        } else {
          view.requestFocus();
        }
      }

      @Override
      public void onPause () {
        try {
          if (view instanceof ViewGroup) {
            ViewGroup group = (ViewGroup)view;
            View focused = group.findFocus();
            if (focused != null) focusedView = focused;
          }
        } finally {
          super.onPause();
        }
      }
    };
  }

  private Fragment createFragment (final View view, int title) {
    return createFragment(view, getString(title));
  }

  private Fragment createControlGroupsFragment () {
    class Group {
      public ViewGroup table;
      public Fragment fragment;
    }

    Map<String, Group> groupMap = new LinkedHashMap<String, Group>();

    for (Control control : settingControls) {
      String label = control.getGroup();
      Group group = groupMap.get(label);

      if (group == null) {
        group = new Group();
        group.table = newTable();
        group.fragment = createFragment(group.table, label);
        groupMap.put(label, group);
      }

      group.table.addView(createControlView(control));
    }

    Set<String> labelSet = groupMap.keySet();
    ListView labelList = newListView(labelSet);

    int labelCount = labelSet.size();
    final Fragment[] fragmentArray = new Fragment[labelCount];

    {
      int index = 0;

      for (String label : labelSet) {
        fragmentArray[index++] = groupMap.get(label).fragment;
      }
    }

    final Fragment groupsFragment = createFragment(
      newVerticalGroup(createActionsView(), labelList),
      R.string.control_groups
    );

    labelList.setOnItemClickListener(
      new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick (AdapterView list, View item, int position, long id) {
          list.setSelection(position);

          getFragmentManager().beginTransaction()
            .replace(FRAGMENT_CONTAINER_ID, fragmentArray[position])
            .addToBackStack(null)
            .commit();
        }
      }
    );

    return groupsFragment;
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
  public void onCreate (Bundle state) {
    super.onCreate(state);
    setContentView(R.layout.common_settings);

ViewGroup container = (ViewGroup)findViewById(R.id.settings_fragment_container);
    FragmentTransaction transaction = getFragmentManager().beginTransaction();
    transaction.add(FRAGMENT_CONTAINER_ID, createControlGroupsFragment());
    transaction.commit();
  }

  @Override
  public void onDestroy () {
    try {
      removeControlValueChangedListeners();
    } finally {
      super.onDestroy();
    }
  }
}
