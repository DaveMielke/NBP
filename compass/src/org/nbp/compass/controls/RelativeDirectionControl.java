package org.nbp.compass.controls;
import org.nbp.compass.*;

import org.nbp.common.EnumerationControl;

public class RelativeDirectionControl extends EnumerationControl<RelativeDirection> {
  @Override
  protected int getResourceForLabel () {
    return R.string.control_label_RelativeDirection;
  }

  @Override
  protected int getResourceForGroup () {
    return R.string.control_group_general;
  }

  @Override
  protected String getPreferenceKey () {
    return "relative-direction";
  }

  @Override
  protected RelativeDirection getEnumerationDefault () {
    return ApplicationDefaults.RELATIVE_DIRECTION;
  }

  @Override
  public RelativeDirection getEnumerationValue () {
    return ApplicationSettings.RELATIVE_DIRECTION;
  }

  @Override
  protected boolean setEnumerationValue (RelativeDirection value) {
    ApplicationSettings.RELATIVE_DIRECTION = value;
    return true;
  }

  public RelativeDirectionControl () {
    super();
  }
}