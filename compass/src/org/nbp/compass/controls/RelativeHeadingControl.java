package org.nbp.compass.controls;
import org.nbp.compass.*;

import org.nbp.common.EnumerationControl;

public class RelativeHeadingControl extends EnumerationControl<RelativeHeading> {
  @Override
  protected int getResourceForLabel () {
    return R.string.control_label_RelativeHeading;
  }

  @Override
  protected int getResourceForGroup () {
    return R.string.control_group_general;
  }

  @Override
  protected String getPreferenceKey () {
    return "relative-heading";
  }

  @Override
  protected RelativeHeading getEnumerationDefault () {
    return ApplicationDefaults.RELATIVE_HEADING;
  }

  @Override
  public RelativeHeading getEnumerationValue () {
    return ApplicationSettings.RELATIVE_HEADING;
  }

  @Override
  protected boolean setEnumerationValue (RelativeHeading value) {
    ApplicationSettings.RELATIVE_HEADING = value;
    return true;
  }

  public RelativeHeadingControl () {
    super();
  }
}
