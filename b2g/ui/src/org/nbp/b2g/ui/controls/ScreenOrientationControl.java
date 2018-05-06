package org.nbp.b2g.ui.controls;
import org.nbp.b2g.ui.*;

import org.nbp.common.controls.EnumerationControl;

public class ScreenOrientationControl extends EnumerationControl<ScreenOrientation> {
  private ScreenOrientationWindow window = null;

  @Override
  protected int getResourceForLabel () {
    return R.string.control_label_ScreenOrientation;
  }

  @Override
  protected int getResourceForGroup () {
    return R.string.control_group_advanced;
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

    if (window == null) window = new ScreenOrientationWindow(ApplicationContext.getContext());
    window.setInvisible();
    window.setVisible();

    return true;
  }

  public ScreenOrientationControl () {
    super();
  }
}
