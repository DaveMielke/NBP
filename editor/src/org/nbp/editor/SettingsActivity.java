package org.nbp.editor;

import org.nbp.common.CommonSettingsActivity;
import org.nbp.common.controls.Control;

public class SettingsActivity extends CommonSettingsActivity {
  @Override
  protected final Control[] getControlsInCreationOrder () {
    return Controls.inCreationOrder;
  }

  @Override
  protected final Control[] getControlsInRestoreOrder () {
    return Controls.inRestoreOrder;
  }
}
