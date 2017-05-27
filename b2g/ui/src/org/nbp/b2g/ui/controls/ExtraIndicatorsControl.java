package org.nbp.b2g.ui.controls;
import org.nbp.b2g.ui.*;

public class ExtraIndicatorsControl extends BooleanControl {
  @Override
  protected int getResourceForGroup () {
    return R.string.control_group_developer;
  }

  @Override
  protected int getResourceForLabel () {
    return R.string.control_label_ExtraIndicators;
  }

  @Override
  protected String getPreferenceKey () {
    return "extra-indicators";
  }

  @Override
  protected boolean getBooleanDefault () {
    return ApplicationDefaults.EXTRA_INDICATORS;
  }

  @Override
  public boolean getBooleanValue () {
    return ApplicationSettings.EXTRA_INDICATORS;
  }

  @Override
  protected boolean setBooleanValue (boolean value) {
    ApplicationSettings.EXTRA_INDICATORS = value;
    return true;
  }

  public ExtraIndicatorsControl () {
    super();
  }
}
