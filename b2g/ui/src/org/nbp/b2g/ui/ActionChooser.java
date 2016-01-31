package org.nbp.b2g.ui;

import java.util.Set;

public class ActionChooser {
  private static String makeText (KeyBindingMap map) {
    StringBuilder sb = new StringBuilder();
    sb.append(ApplicationContext.getString(R.string.ChooseAction_label));

    for (Integer keys : map.keySet()) {
      Action action = map.get(keys);
      if (action.isForDevelopers() && !ApplicationSettings.DEVELOPER_ENABLED) continue;
      sb.append('\n');

      sb.append(action.getName());

      sb.append(": ");
      sb.append(KeyMask.toString(keys));
    }

    return sb.toString();
  }

  public static void chooseAction (final KeyBindingMap map) {
    Endpoints.setPopupEndpoint(makeText(map),
      new IndexHandler () {
        @Override
        public boolean handleIndex (int index) {
          if ((index -= 1) < 0) return true;
          Integer keys = map.keySet().toArray(new Integer[map.size()])[index];

          if ((keys & KeyMask.CURSOR) != 0) {
            ApplicationUtilities.message(R.string.ChooseAction_message_cursor);
            return false;
          }

          Action action = map.get(keys);
          return KeyEvents.performAction(action);
        }
      }
    );
  }

  public ActionChooser () {
  }
}
