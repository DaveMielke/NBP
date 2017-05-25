package org.nbp.editor.menu.review;
import org.nbp.editor.*;

import android.view.MenuItem;

public class Summary extends EditorAction {
  public Summary () {
    super();
  }

  @Override
  public void performAction (EditorActivity editor, MenuItem item) {
    EditArea editArea = editor.getEditArea();
    ReviewSummary summary = new ReviewSummary(editArea.getText());

    {
      String uri = null;
      ContentHandle handle = editArea.getContentHandle();

      if (handle != null) {
        uri = handle.getNormalizedString();
      }

      if (uri == null) uri = editor.getString(R.string.hint_new_file);
      summary.setContentURI(uri);
    }

    editor.showDialog(
      R.string.menu_review_Summary, R.layout.review_summary, summary
    );
  }
}
