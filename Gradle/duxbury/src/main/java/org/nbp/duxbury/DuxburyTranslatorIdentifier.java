package org.nbp.duxbury;

import org.liblouis.Louis;
import org.liblouis.Translator;
import org.liblouis.TranslatorIdentifier;

import android.util.Log;

public enum DuxburyTranslatorIdentifier implements TranslatorIdentifier {
  PINYIN(PinYinTranslator.class, R.string.duxbury_ttd_PINYIN),
  ; // end of enumeration

  private final static String LOG_TAG = DuxburyTranslatorIdentifier.class.getName();

  private final int translatorDescription;
  private final Class<? extends DuxburyTranslator> translatorClass;
  private Translator translatorObject = null;

  DuxburyTranslatorIdentifier (Class<? extends DuxburyTranslator> translator, int description) {
    translatorClass = translator;
    translatorDescription = description;
  }

  @Override
  public final String getName () {
    return name();
  }

  @Override
  public final String getDescription () {
    if (translatorDescription != 0) {
      return Louis.getContext().getString(translatorDescription);
    }

    return null;
  }

  @Override
  public final Translator getTranslator () {
    synchronized (this) {
      if (translatorObject == null) {
        try {
          translatorObject = translatorClass.newInstance();
        } catch (InstantiationException exception) {
          Log.w(LOG_TAG,
            String.format(
              "translator instantiation failed: %s(%s): %s",
              name(), translatorClass.getName(),
              exception.getMessage()
            )
          );
        } catch (IllegalAccessException exception) {
          Log.w(LOG_TAG,
            String.format(
              "translator object access denied: %s(%s): %s",
              name(), translatorClass.getName(),
              exception.getMessage()
            )
          );
        }
      }
    }

    return translatorObject;
  }
}
