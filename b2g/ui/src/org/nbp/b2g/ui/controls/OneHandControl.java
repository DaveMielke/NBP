package org.nbp.b2g.ui.controls;
import org.nbp.b2g.ui.*;

public class OneHandControl extends BooleanControl {
  @Override
  protected int getResourceForGroup () {
    return R.string.control_group_keyboard;
  }

  @Override
  protected int getResourceForLabel () {
    return R.string.control_label_OneHand;
  }

  @Override
  protected String getPreferenceKey () {
    return "one-hand";
  }

  @Override
  protected boolean getBooleanDefault () {
    return ApplicationDefaults.ONE_HAND;
  }

  @Override
  public boolean getBooleanValue () {
    return ApplicationSettings.ONE_HAND;
  }

  @Override
  protected boolean setBooleanValue (boolean value) {
    ApplicationSettings.ONE_HAND = value;
    return true;
  }

  public OneHandControl () {
    super();
  }
}
