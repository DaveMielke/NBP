package org.nbp.editor.menu.feedback;
import org.nbp.editor.*;

public class SummarizeFeedback extends EditorAction {
  public SummarizeFeedback (EditorActivity editor) {
    super(editor);
  }

  @Override
  public void performAction (EditorActivity editor) {
    EditArea editArea = editor.getEditArea();
    FeedbackSummary summary = new FeedbackSummary(editArea.getText());

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
      R.string.menu_feedback_SummarizeFeedback, R.layout.feedback_summary, summary
    );
  }
}
