package org.nbp.editor.menu.main;
import org.nbp.editor.*;

import android.view.MenuItem;
import org.nbp.common.LaunchUtilities;

public class Settings extends EditorAction {
  public Settings () {
    super();
  }

  @Override
  public void performAction (EditorActivity editor, MenuItem item) {
    LaunchUtilities.launchActivity(SettingsActivity.class);
  }
}
