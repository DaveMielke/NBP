package org.nbp.compass.controls;
import org.nbp.compass.*;

import org.nbp.common.EnumerationControl;

public class ScreenOrientationControl extends EnumerationControl<ScreenOrientation> {
  @Override
  protected int getResourceForLabel () {
    return R.string.control_label_ScreenOrientation;
  }

  @Override
  protected int getResourceForGroup () {
    return R.string.control_group_general;
  }

  @Override
  protected String getPreferenceKey () {
    return "screen-orientation";
  }

  @Override
  protected ScreenOrientation getEnumerationDefault () {
    return ApplicationDefaults.SCREEN_ORIENTATION;
  }

  @Override
  public ScreenOrientation getEnumerationValue () {
    return ApplicationSettings.SCREEN_ORIENTATION;
  }

  @Override
  protected boolean setEnumerationValue (ScreenOrientation value) {
    ApplicationSettings.SCREEN_ORIENTATION = value;
    return true;
  }

  public ScreenOrientationControl () {
    super();
  }
}
