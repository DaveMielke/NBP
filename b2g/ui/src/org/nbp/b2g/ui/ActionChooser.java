package org.nbp.b2g.ui;

import java.util.Set;

public class ActionChooser {
  private static String makeText (KeyBindingMap map, Integer cursorKey) {
    boolean haveCursorKey = cursorKey != null;

    StringBuilder sb = new StringBuilder();
    sb.append(ApplicationContext.getString(R.string.ChooseAction_label));

    for (Integer keys : map.keySet()) {
      boolean needsCursorKey = (keys & KeyMask.CURSOR) != 0;
      if (needsCursorKey != haveCursorKey) continue;

      Action action = map.get(keys);
      if (action.isForDevelopers() && !ApplicationSettings.DEVELOPER_ENABLED) continue;
      sb.append('\n');

      sb.append(Wordify.get(action.getName()));

      sb.append(": ");
      sb.append(KeyMask.toString(keys));
    }

    return sb.toString();
  }

  public static void chooseAction (final KeyBindingMap map, Integer cursorKey) {
    Endpoints.setPopupEndpoint(makeText(map, cursorKey),
      new ValueHandler<Integer> () {
        @Override
        public boolean handleValue (Integer index) {
          if ((index -= 1) < 0) return true;
          Integer keys = map.keySet().toArray(new Integer[map.size()])[index];

          Action action = map.get(keys);
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
