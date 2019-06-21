package org.nbp.b2g.ui.controls;
import org.nbp.b2g.ui.*;

import org.nbp.common.controls.EnumerationControl;
import org.liblouis.TranslatorIdentifier;

public class PhoneticAlphabetControl extends EnumerationControl<PhoneticAlphabet> {
  @Override
  protected int getResourceForLabel () {
    return R.string.control_label_PhoneticAlphabet;
  }

  @Override
  protected int getResourceForGroup () {
    return R.string.control_group_advanced;
  }

  @Override
  protected String getPreferenceKey () {
    return "phonetic-alphabet";
  }

  @Override
  protected PhoneticAlphabet getEnumerationDefault () {
    return ApplicationDefaults.PHONETIC_ALPHABET;
  }

  @Override
  public PhoneticAlphabet getEnumerationValue () {
    return ApplicationSettings.PHONETIC_ALPHABET;
  }

  @Override
  protected boolean setEnumerationValue (PhoneticAlphabet value) {
    ApplicationSettings.PHONETIC_ALPHABET = value;

    for (PhoneticAlphabet current : PhoneticAlphabet.values()) {
      TranslatorIdentifier identifier = current.getTranslatorIdentifier();

      if (identifier != null) {
        if (current == value) {
          TranslatorIdentifier.addAuxiliaryTranslator(identifier);
        } else {
          TranslatorIdentifier.removeAuxiliaryTranslator(identifier);
        }
      }
    }

    TranslationUtilities.refresh();
    return true;
  }

  public PhoneticAlphabetControl () {
    super();
  }
}
