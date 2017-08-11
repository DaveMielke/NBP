package org.nbp.navigator.controls;
import org.nbp.navigator.*;

import org.nbp.common.BooleanControl;

public class AnnounceLocationControl extends BooleanControl {
  @Override
  protected int getResourceForLabel () {
    return R.string.control_label_AnnounceLocation;
  }

  @Override
  protected int getResourceForGroup () {
    return R.string.control_group_announcements;
  }

  @Override
  protected String getPreferenceKey () {
    return "announce-location";
  }

  @Override
  protected boolean getBooleanDefault () {
    return ApplicationDefaults.ANNOUNCE_LOCATION;
  }

  @Override
  public boolean getBooleanValue () {
    return ApplicationSettings.ANNOUNCE_LOCATION;
  }

  @Override
  protected boolean setBooleanValue (boolean value) {
    ApplicationSettings.ANNOUNCE_LOCATION = value;
    return true;
  }

  public AnnounceLocationControl () {
    super();
  }
}
