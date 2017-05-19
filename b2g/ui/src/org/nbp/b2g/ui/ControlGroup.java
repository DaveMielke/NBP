package org.nbp.b2g.ui;

import java.util.ArrayList;

public enum ControlGroup {
  GENERAL(R.string.control_group_general),
  INPUT(R.string.control_group_input),
  BRAILLE(R.string.control_group_braille),
  SPEECH(R.string.control_group_speech),
  KEYBOARD(R.string.control_group_keyboard),
  REMOTE(R.string.control_group_remote),
  DEVELOPER(R.string.control_group_developer),
  ;

  private final int groupLabel;

  private ControlGroup (int label) {
    groupLabel = label;
  }

  public final int getLabel () {
    return groupLabel;
  }

  private final static Object CONTROLS_LOCK = new Object();
  private Control[] groupControls;

  private final static void setControls () {
    final ArrayList[] lists = new ArrayList[ControlGroup.values().length];

    Controls.forEachControl(
      new ControlProcessor() {
        @Override
        public boolean processControl (Control control) {
          int ordinal = control.getGroup().ordinal();
          ArrayList list = lists[ordinal];

          if (list == null) {
            lists[ordinal] = list = new ArrayList();
          }

          list.add(control);
          return true;
        }
      }
    );

    for (ControlGroup group : ControlGroup.values()) {
      ArrayList list = lists[group.ordinal()];
      Control[] array = new Control[list.size()];

      list.toArray(array);
      group.groupControls = array;
    }
  }

  public final Control[] getControls () {
    synchronized (CONTROLS_LOCK) {
      if (groupControls == null) setControls();
    }

    return groupControls;
  }
}
