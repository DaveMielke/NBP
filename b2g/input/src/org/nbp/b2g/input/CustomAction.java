package org.nbp.b2g.input;

public abstract class CustomAction extends Action {
  final String actionName;

  @Override
  public String getName () {
    return actionName;
  }

  public CustomAction (String name) {
    super();
    actionName = name;
  }

  public static void add (int keyMask, CustomAction action) {
    addAction(keyMask, action);
  }
}
