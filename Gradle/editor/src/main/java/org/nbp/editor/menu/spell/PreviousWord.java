package org.nbp.editor.menu.spell;
import org.nbp.editor.*;

import android.text.style.SuggestionSpan;

public class PreviousWord extends MoveAction {
  public PreviousWord (EditorActivity editor) {
    super(editor);
  }

  private final boolean moveToPreviousWord () {
    return moveToPreviousPosition(
      findPreviousSpan(SuggestionSpan.class)
    );
  }

  @Override
  public void performAction () {
    if (!moveToPreviousWord()) {
      showMessage(R.string.message_no_previous_word);
    }
  }
}
