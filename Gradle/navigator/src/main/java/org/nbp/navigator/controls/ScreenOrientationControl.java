package org.nbp.navigator.controls;
import org.nbp.navigator.*;

import org.nbp.common.controls.EnumerationControl;

public class ScreenOrientationControl extends EnumerationControl<ScreenOrientation> {
  @Override
  protected int getResourceForLabel () {
    return R.string.control_label_ScreenOrientation;
  }

  @Override
  protected int getResourceForGroup () {
    return R.string.control_group_developer;
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
    value.setCurrentOrientation(NavigatorActivity.getNavigatorActivity());
    return true;
  }

  public ScreenOrientationControl () {
    super();
  }
}
