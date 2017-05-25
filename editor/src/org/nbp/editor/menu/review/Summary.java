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

    ContentHandle contentHandle = editArea.getContentHandle();
    summary.setContentURI(contentHandle.getNormalizedString());

    editor.showDialog(
      R.string.menu_review_Summary, R.layout.review_summary, summary
    );
  }
}
