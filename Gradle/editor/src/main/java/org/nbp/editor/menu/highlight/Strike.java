package org.nbp.editor.menu.highlight;
import org.nbp.editor.*;

import android.text.style.CharacterStyle;
import android.text.style.StrikethroughSpan;

public class Strike extends HighlightAction {
  public Strike (EditorActivity editor) {
    super(editor);
  }

  @Override
  public CharacterStyle getCharacterStyle () {
    return new StrikethroughSpan();
  }
}
