package org.nbp.b2g.ui.controls;
import org.nbp.b2g.ui.*;

import org.nbp.common.controls.EnumerationControl;

import org.nbp.common.dictionary.DictionaryDatabase;
import org.nbp.common.dictionary.DictionaryStrategy;

public class DictionaryStrategyControl extends EnumerationControl<DictionaryStrategy> {
  @Override
  protected int getResourceForLabel () {
    return R.string.control_label_DictionaryStrategy;
  }

  @Override
  protected int getResourceForGroup () {
    return R.string.control_group_dictionary;
  }

  @Override
  protected String getPreferenceKey () {
    return "dictionary-strategy";
  }

  @Override
  protected String getValueLabel (DictionaryStrategy strategy) {
    String label = strategy.getDescription();
    if (label == null) label = super.getValueLabel(strategy);
    return label;
  }

  @Override
  protected DictionaryStrategy getEnumerationDefault () {
    return ApplicationDefaults.DICTIONARY_STRATEGY;
  }

  @Override
  public DictionaryStrategy getEnumerationValue () {
    return ApplicationSettings.DICTIONARY_STRATEGY;
  }

  @Override
  protected boolean setEnumerationValue (DictionaryStrategy value) {
    ApplicationSettings.DICTIONARY_STRATEGY = value;
    return true;
  }

  public DictionaryStrategyControl () {
    super();
  }
}
