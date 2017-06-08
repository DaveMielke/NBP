package org.nbp.editor;

public abstract class EditorComponent {
  private final EditorActivity editorActivity;

  protected EditorComponent (EditorActivity editor) {
    editorActivity = editor;
  }

  protected final EditorActivity getEditor () {
    return editorActivity;
  }

  protected final EditArea getEditArea () {
    return getEditor().getEditArea();
  }

  protected final void postAction (Runnable action) {
    getEditArea().post(action);
  }

  protected final String getString (int resource) {
    return getEditor().getString(resource);
  }

  protected final void showMessage (int message) {
    getEditor().showMessage(message);
  }
}
