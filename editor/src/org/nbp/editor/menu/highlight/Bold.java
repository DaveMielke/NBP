package org.nbp.editor.menu.highlight;
import org.nbp.editor.*;

import android.view.MenuItem;
import android.text.style.CharacterStyle;
import android.text.style.StyleSpan;
import android.graphics.Typeface;

public class Bold extends HighlightAction {
  public Bold () {
    super();
  }

  @Override
  public CharacterStyle getCharacterStyle () {
    return new StyleSpan(Typeface.BOLD);
  }
}
