package org.nbp.b2g.ui.host;
import org.nbp.b2g.ui.*;

public abstract class AirplaneModeAction extends AndroidSettingAction {
  @Override
  protected final String getSettingName () {
    return android.provider.Settings.System.AIRPLANE_MODE_ON;
  }

  @Override
  protected final int getSettingLabel () {
    return R.string.AndroidSetting_AirplaneMode_label;
  }

  protected AirplaneModeAction (Endpoint endpoint) {
    super(endpoint);
  }
}
