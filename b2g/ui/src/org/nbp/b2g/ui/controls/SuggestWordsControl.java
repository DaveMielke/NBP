package org.nbp.b2g.ui.controls;
import org.nbp.b2g.ui.*;

import org.nbp.common.controls.BooleanControl;

public class SuggestWordsControl extends BooleanControl {
  @Override
  protected int getResourceForLabel () {
    return R.string.control_label_SuggestWords;
  }

  @Override
  protected int getResourceForGroup () {
    return R.string.control_group_dictionary;
  }

  @Override
  protected String getPreferenceKey () {
    return "suggest-words";
  }

  @Override
  protected boolean getBooleanDefault () {
    return ApplicationDefaults.SUGGEST_WORDS;
  }

  @Override
  public boolean getBooleanValue () {
    return ApplicationSettings.SUGGEST_WORDS;
  }

  @Override
  protected boolean setBooleanValue (boolean value) {
    ApplicationSettings.SUGGEST_WORDS = value;
    return true;
  }

  public SuggestWordsControl () {
    super();
  }
}
