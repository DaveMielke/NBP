package org.nbp.b2g.ui.controls;
import org.nbp.b2g.ui.*;

import org.nbp.b2g.ui.remote.RemoteEndpoint;

public class RemoteDisplayControl extends BooleanControl {
  @Override
  public int getLabel () {
    return R.string.control_label_RemoteDisplay;
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
    RemoteEndpoint endpoint = Endpoints.remote.get();

    if ((ApplicationSettings.REMOTE_DISPLAY = value)) {
      endpoint.start();
    } else {
      endpoint.stop();
    }

    return true;
  }

  public RemoteDisplayControl () {
    super(ControlGroup.REMOTE);
  }
}
