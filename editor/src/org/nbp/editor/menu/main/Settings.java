package org.nbp.editor.menu.main;
import org.nbp.editor.*;

import org.nbp.common.LaunchUtilities;

public class Settings extends EditorAction {
  public Settings (EditorActivity editor) {
    super(editor);
  }

  @Override
  public void performAction (EditorActivity editor) {
    LaunchUtilities.launchActivity(SettingsActivity.class);
  }
}
