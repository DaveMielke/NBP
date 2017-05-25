package org.nbp.editor.menu.highlight;
import org.nbp.editor.*;

import android.view.MenuItem;
import android.text.style.SubscriptSpan;

public class Subscript extends HighlightAction {
  public Subscript () {
    super();
  }

  @Override
  public void performAction (EditorActivity editor, MenuItem item) {
    addStyle(editor, new SubscriptSpan());
  }
}
