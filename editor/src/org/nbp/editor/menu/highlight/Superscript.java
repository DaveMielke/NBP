package org.nbp.editor.menu.highlight;
import org.nbp.editor.*;

import android.view.MenuItem;
import android.text.style.SuperscriptSpan;

public class Superscript extends HighlightAction {
  public Superscript () {
    super();
  }

  @Override
  public void performAction (EditorActivity editor, MenuItem item) {
    addStyle(editor, new SuperscriptSpan());
  }
}
