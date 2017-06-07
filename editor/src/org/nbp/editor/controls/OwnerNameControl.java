package org.nbp.editor.controls;
import org.nbp.editor.*;

import java.util.Collection;

import org.nbp.common.StringControl;

public class OwnerNameControl extends StringControl {
  @Override
  protected int getResourceForLabel () {
    return R.string.control_label_OwnerName;
  }

  @Override
  protected String getPreferenceKey () {
    return "owner-name";
  }

  @Override
  protected String getStringDefault () {
    return ApplicationDefaults.OWNER_NAME;
  }

  @Override
  public String getStringValue () {
    return ApplicationSettings.OWNER_NAME;
  }

  @Override
  protected boolean setStringValue (String value) {
    ApplicationSettings.OWNER_NAME = value;
    return true;
  }

  @Override
  public Collection<String> getSuggestedValues () {
    return new OwnerProfile(ApplicationContext.getContext()).getNames();
  }

  public OwnerNameControl () {
    super();
  }
}
