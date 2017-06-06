package org.nbp.editor.menu.highlight;
import org.nbp.editor.*;

import android.text.style.CharacterStyle;
import android.text.style.SubscriptSpan;

public class Subscript extends HighlightAction {
  public Subscript (EditorActivity editor) {
    super(editor);
  }

  @Override
  public CharacterStyle getCharacterStyle () {
    return new SubscriptSpan();
  }
}
