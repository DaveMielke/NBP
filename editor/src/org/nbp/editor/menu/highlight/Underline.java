package org.nbp.editor.menu.highlight;
import org.nbp.editor.menu.*;;
import org.nbp.editor.*;;

import android.view.MenuItem;
import android.text.style.UnderlineSpan;

public class Underline extends HighlightAction {
  public Underline () {
    super();
  }

  @Override
  public void performAction (EditorActivity editor, MenuItem item) {
    addStyle(editor, new UnderlineSpan());
  }
}
