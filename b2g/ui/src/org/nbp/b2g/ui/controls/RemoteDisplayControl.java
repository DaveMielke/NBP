package org.nbp.b2g.ui.controls;
import org.nbp.b2g.ui.*;

import org.nbp.b2g.ui.display.DisplayEndpoint;

public class RemoteDisplayControl extends BooleanControl {
  @Override
  public CharSequence getLabel () {
    return toHeader(R.string.RemoteDisplay_control_label);
  }

  @Override
  protected String getPreferenceKey () {
    return "remote-display";
  }

  @Override
  protected boolean getBooleanDefault () {
    return ApplicationDefaults.REMOTE_DISPLAY;
  }

  @Override
  public boolean getBooleanValue () {
    return ApplicationSettings.REMOTE_DISPLAY;
  }

  @Override
  protected boolean setBooleanValue (boolean value) {
    DisplayEndpoint endpoint = Endpoints.display.get();

    if ((ApplicationSettings.REMOTE_DISPLAY = value)) {
      endpoint.start();
    } else {
      endpoint.stop();
    }

    return true;
  }

  public RemoteDisplayControl () {
    super(ControlGroup.DISPLAY);
  }
}
