package org.nbp.b2g.ui.controls;
import org.nbp.b2g.ui.*;

import org.nbp.b2g.ui.remote.RemoteEndpoint;

import org.nbp.common.BooleanControl;

public class RemoteDisplayControl extends BooleanControl {
  @Override
  protected int getResourceForGroup () {
    return R.string.control_group_remote;
  }

  @Override
  protected int getResourceForLabel () {
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
    super();
  }
}
