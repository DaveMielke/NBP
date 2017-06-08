package org.nbp.editor.menu.spell;
import org.nbp.editor.*;

import android.text.style.SuggestionSpan;

public class NextWord extends MoveAction {
  public NextWord (EditorActivity editor) {
    super(editor);
  }

  private final boolean moveToNextWord () {
    return moveToNextPosition(
      findNextSpan(SuggestionSpan.class)
    );
  }

  @Override
  public void performAction () {
    if (!moveToNextWord()) {
      showMessage(R.string.message_no_next_word);
    }
  }
}
