package org.nbp.editor.controls;
import org.nbp.editor.*;

import java.util.Collection;

import org.nbp.common.StringControl;

public class AuthorNameControl extends StringControl {
  @Override
  protected int getResourceForLabel () {
    return R.string.control_label_AuthorName;
  }

  @Override
  protected String getPreferenceKey () {
    return "author-name";
  }

  @Override
  protected String getStringDefault () {
    return ApplicationDefaults.AUTHOR_NAME;
  }

  @Override
  public String getStringValue () {
    return ApplicationSettings.AUTHOR_NAME;
  }

  @Override
  protected boolean setStringValue (String value) {
    ApplicationSettings.AUTHOR_NAME = value;
    return true;
  }

  @Override
  public Collection<String> getSuggestedValues () {
    return new OwnerProfile(ApplicationContext.getContext()).getNames();
  }

  public AuthorNameControl () {
    super();
  }
}
