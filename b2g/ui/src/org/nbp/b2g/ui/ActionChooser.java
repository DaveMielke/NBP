package org.nbp.b2g.ui;

import java.util.List;
import java.util.ArrayList;

import org.nbp.b2g.ui.popup.PopupEndpoint;

public class ActionChooser extends UserInterfaceComponent {
  private ActionChooser () {
  }

  private static String makeText (List<Action> actions, KeyBindingMap map, Integer cursorKey) {
    StringBuilder text = new StringBuilder();
    boolean haveCursorKey = cursorKey != null;

    if (haveCursorKey) {
      text.append(getString(R.string.popup_select_action));
    } else {
      text.append(getString(R.string.popup_select_shortcut));
    }

    for (KeySet keys : map.keySet()) {
      boolean needsCursorKey = keys.get(KeySet.CURSOR);
      if (needsCursorKey != haveCursorKey) continue;

      Action action = map.get(keys);
      if (action.isHidden()) continue;
      if (action.isAdvanced() && !ApplicationSettings.ADVANCED_ACTIONS) continue;

      text.append('\n');
      text.append(Wordify.get(action.getName()));

      text.append(": ");
      text.append(keys.toString());

      text.append(": ");
      text.append(action.getSummary());

      actions.add(action);
    }

    return text.toString();
  }

  public static void chooseAction (KeyBindingMap map, final Integer cursorKey) {
    final List<Action> actions = new ArrayList<Action>();
    String text = makeText(actions, map, cursorKey);

    PopupEndpoint endpoint = new PopupEndpoint();
    endpoint.set(text);
    endpoint.set(1);
    endpoint.set(map);

    endpoint.set(
      new PopupClickHandler () {
        @Override
        public final boolean handleClick (int index) {
          Action action = actions.get(index);

          if (cursorKey != null) return KeyEvents.performAction(action, cursorKey);
          return KeyEvents.performAction(action);
        }
      }
    );

    Endpoints.setCurrentEndpoint(endpoint);
  }

  public static void chooseAction (final KeyBindingMap map) {
    chooseAction(map, null);
  }
}
