package org.nbp.editor.menu.highlight;
import org.nbp.editor.*;

import android.view.MenuItem;
import android.text.style.StrikethroughSpan;

public class Strike extends HighlightAction {
  public Strike () {
    super();
  }

  @Override
  public void performAction (EditorActivity editor, MenuItem item) {
    addStyle(editor, new StrikethroughSpan());
  }
}
