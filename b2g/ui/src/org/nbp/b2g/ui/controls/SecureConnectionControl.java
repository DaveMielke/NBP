package org.nbp.b2g.ui.controls;
import org.nbp.b2g.ui.*;

import org.nbp.common.controls.BooleanControl;

public class SecureConnectionControl extends BooleanControl {
  @Override
  protected int getResourceForLabel () {
    return R.string.control_label_SecureConnection;
  }

  @Override
  protected int getResourceForGroup () {
    return R.string.control_group_remote;
  }

  @Override
  protected String getPreferenceKey () {
    return "secure-connection";
  }

  @Override
  protected boolean getBooleanDefault () {
    return ApplicationDefaults.SECURE_CONNECTION;
  }

  @Override
  public boolean getBooleanValue () {
    return ApplicationSettings.SECURE_CONNECTION;
  }

  @Override
  protected boolean setBooleanValue (boolean value) {
    ApplicationSettings.SECURE_CONNECTION = value;
    return true;
  }

  public SecureConnectionControl () {
    super();
  }
}
