package org.nbp.editor.menu.highlight;
import org.nbp.editor.*;

import android.view.MenuItem;
import android.text.style.CharacterStyle;
import android.text.style.SuperscriptSpan;

public class Superscript extends HighlightAction {
  public Superscript () {
    super();
  }

  @Override
  public CharacterStyle getCharacterStyle () {
    return new SuperscriptSpan();
  }
}
