package org.nbp.editor.menu.main;
import org.nbp.editor.*;

import org.nbp.common.DialogFinisher;
import org.nbp.common.DialogHelper;

public class About extends EditorAction implements DialogFinisher {
  public About () {
    super();
  }

  @Override
  public void finishDialog (DialogHelper helper) {
    helper.setTextFromAsset(R.id.about_copyright, "copyright");
  }

  @Override
  public void performAction (EditorActivity editor) {
    editor.showDialog(R.string.menu_main_About, R.layout.about, this);
  }
}
