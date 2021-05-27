package org.nbp.editor.menu.highlight;
import org.nbp.editor.*;

import android.text.style.CharacterStyle;
import android.text.style.StyleSpan;
import android.graphics.Typeface;

public class Italics extends HighlightAction {
  public Italics (EditorActivity editor) {
    super(editor);
  }

  @Override
  public CharacterStyle getCharacterStyle () {
    return new StyleSpan(Typeface.ITALIC);
  }
}
