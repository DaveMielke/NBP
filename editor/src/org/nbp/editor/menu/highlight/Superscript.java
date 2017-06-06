package org.nbp.editor.menu.highlight;
import org.nbp.editor.*;

import android.text.style.CharacterStyle;
import android.text.style.SuperscriptSpan;

public class Superscript extends HighlightAction {
  public Superscript (EditorActivity editor) {
    super(editor);
  }

  @Override
  public CharacterStyle getCharacterStyle () {
    return new SuperscriptSpan();
  }
}
