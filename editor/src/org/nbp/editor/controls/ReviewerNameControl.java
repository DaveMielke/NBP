package org.nbp.editor.controls;
import org.nbp.editor.*;

import java.util.Collection;

import org.nbp.common.StringControl;

public class ReviewerNameControl extends StringControl {
  @Override
  protected int getResourceForLabel () {
    return R.string.control_label_ReviewerName;
  }

  @Override
  protected String getPreferenceKey () {
    return "reviewer-name";
  }

  @Override
  protected String getStringDefault () {
    return ApplicationDefaults.REVIEWER_NAME;
  }

  @Override
  public String getStringValue () {
    return ApplicationSettings.REVIEWER_NAME;
  }

  @Override
  protected boolean setStringValue (String value) {
    ApplicationSettings.REVIEWER_NAME = value;
    return true;
  }

  @Override
  public Collection<String> getSuggestedValues () {
    return new OwnerProfile(ApplicationContext.getContext()).getNames();
  }

  public ReviewerNameControl () {
    super();
  }
}
