package org.nbp.editor;

import android.text.Spanned;

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

  protected final boolean verifyWritableText () {
    if (!ApplicationSettings.PROTECT_TEXT) return true;
    if (!getEditArea().getEnforceTextProtection()) return true;

    showMessage(R.string.message_protected_text);
    return false;
  }

  protected final boolean verifyWritableRegion (Spanned text, int start, int end) {
    if (verifyWritableText()) {
      if (!getEditArea().containsProtectedText(text, start, end)) return true;
      showMessage(R.string.message_protected_region);
    }

    return false;
  }

  protected final boolean verifyWritableRegion (int start, int end) {
    return verifyWritableRegion(getEditArea().getText(), start, end);
  }

  protected final boolean verifyWritableRegion () {
    EditArea editArea = getEditArea();

    return verifyWritableRegion(
      editArea.getSelectionStart(), editArea.getSelectionEnd()
    );
  }

  protected final String getAuthorName () {
    {
      String name = Controls.authorName.getValue();
      if (!name.isEmpty()) return name;
    }

    showMessage(R.string.message_no_author_name);
    return null;
  }
}
