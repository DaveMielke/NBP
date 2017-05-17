package org.nbp.b2g.ui.controls;
import org.nbp.b2g.ui.*;

public class SecureConnectionControl extends BooleanControl {
  @Override
  public int getLabel () {
    return R.string.SecureConnection_control_label;
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
    super(ControlGroup.REMOTE);
  }
}
