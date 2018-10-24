package org.nbp.editor;

import org.nbp.common.CommonSettingsActivity;

public class SettingsActivity extends CommonSettingsActivity {
  private final static String LOG_TAG = SettingsActivity.class.getName();

  public SettingsActivity () {
    super(Controls.inCreationOrder, Controls.inRestoreOrder);
  }
}
