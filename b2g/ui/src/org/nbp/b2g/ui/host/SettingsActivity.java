package org.nbp.b2g.ui.host;
import org.nbp.b2g.ui.*;

import org.nbp.common.CommonSettingsActivity;
import org.nbp.common.controls.Control;

import android.view.View;
import android.widget.Button;

import org.nbp.common.LaunchUtilities;

public class SettingsActivity extends CommonSettingsActivity {
  @Override
  protected final Control[] getControlsInCreationOrder () {
    return Controls.inCreationOrder;
  }

  @Override
  protected final Control[] getControlsInRestoreOrder () {
    return Controls.inRestoreOrder;
  }

  private View createSystemMaintenanceButton () {
    Button button = newButton(
      R.string.SystemMaintenance_action_label,
      new Button.OnClickListener() {
        @Override
        public void onClick (View view) {
          LaunchUtilities.launchActivity(MaintenanceActivity.class);
        }
      }
    );

    return button;
  }

  @Override
  protected final View[] getExtraMainScreenActions () {
    return new View[] {
      createSystemMaintenanceButton()
    };
  }

  @Override
  protected void saveSettings () {
    Controls.saveValues();
  }

  @Override
  protected void restoreSettings () {
    Controls.restoreSavedValues();
  }

  @Override
  protected void resetSettings () {
    Controls.restoreDefaultValues();
  }
}
