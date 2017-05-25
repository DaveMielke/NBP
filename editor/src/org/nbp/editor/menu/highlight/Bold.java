package org.nbp.editor.menu.highlight;
import org.nbp.editor.*;

import android.view.MenuItem;
import android.text.style.StyleSpan;
import android.graphics.Typeface;

public class Bold extends HighlightAction {
  public Bold () {
    super();
  }

  @Override
  public void performAction (EditorActivity editor, MenuItem item) {
    addStyle(editor, new StyleSpan(Typeface.BOLD));
  }
}
