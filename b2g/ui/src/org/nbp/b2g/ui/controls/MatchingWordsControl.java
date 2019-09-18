package org.nbp.b2g.ui.controls;
import org.nbp.b2g.ui.*;

import org.nbp.common.controls.BooleanControl;

public class MatchingWordsControl extends BooleanControl {
  @Override
  protected int getResourceForLabel () {
    return R.string.control_label_MatchingWords;
  }

  @Override
  protected int getResourceForGroup () {
    return R.string.control_group_dictionary;
  }

  @Override
  protected String getPreferenceKey () {
    return "matching-words";
  }

  @Override
  protected boolean getBooleanDefault () {
    return ApplicationDefaults.MATCHING_WORDS;
  }

  @Override
  public boolean getBooleanValue () {
    return ApplicationSettings.MATCHING_WORDS;
  }

  @Override
  protected boolean setBooleanValue (boolean value) {
    ApplicationSettings.MATCHING_WORDS = value;
    return true;
  }

  public MatchingWordsControl () {
    super();
  }
}
