package org.nbp.b2g.ui;

import java.util.Set;
import java.util.List;
import java.util.ArrayList;

public class ActionChooser extends UserInterfaceComponent {
  private static String makeText (List<Action> actions, KeyBindingMap map, Integer cursorKey) {
    boolean haveCursorKey = cursorKey != null;

    StringBuilder sb = new StringBuilder();
    sb.append(getString(R.string.popup_select_action));

    for (KeySet keys : map.keySet()) {
      boolean needsCursorKey = keys.get(KeySet.CURSOR);
      if (needsCursorKey != haveCursorKey) continue;

      Action action = map.get(keys);
      if (action.isHidden()) continue;
      if (action.isAdvanced() && !ApplicationSettings.ADVANCED_ACTIONS) continue;

      sb.append('\n');
      sb.append(Wordify.get(action.getName()));

      sb.append(": ");
      sb.append(keys.toString());

      sb.append(": ");
      sb.append(action.getSummary());

      actions.add(action);
    }

    return sb.toString();
  }

  public static void chooseAction (KeyBindingMap map, final Integer cursorKey) {
    final List<Action> actions = new ArrayList<Action>();
    Endpoints.setPopupEndpoint(makeText(actions, map, cursorKey), 1,
      new PopupClickHandler () {
        @Override
        public final boolean handleClick (int index) {
          Action action = actions.get(index);

          if (cursorKey != null) return KeyEvents.performAction(action, cursorKey);
          return KeyEvents.performAction(action);
        }
      }
    );
  }

  public static void chooseAction (final KeyBindingMap map) {
    chooseAction(map, null);
  }

  public ActionChooser () {
  }
}
