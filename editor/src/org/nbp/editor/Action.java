package org.nbp.editor;

public abstract class Action {
  protected Action () {
  }

  public abstract void performAction (EditorActivity editor);
}
