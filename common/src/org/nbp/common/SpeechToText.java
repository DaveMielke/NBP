package org.nbp.common;

import android.content.Intent;
import android.speech.RecognizerIntent;
import java.util.Locale;

public class SpeechToText {
  private final CommonActivity mainActivity;

  public SpeechToText (CommonActivity activity) {
    mainActivity = activity;
  }

  public enum LanguageModel {
    FREE_FORM(RecognizerIntent.LANGUAGE_MODEL_FREE_FORM),
    WEB_SEARCH(RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);

    private final String languageModel;

    public final String getValue () {
      return languageModel;
    }

    private LanguageModel (String model) {
      languageModel = model;
    }
  }

  private String selectedLanguage = null;
  private LanguageModel selectedModel = null;
  private String selectedPrompt = null;

  public final String getLanguage () {
    return selectedLanguage;
  }

  public final SpeechToText setLanguage (String language) {
    selectedLanguage = language;
    return this;
  }

  public final SpeechToText setLanguage (Locale locale) {
    return setLanguage(locale.toString());
  }

  public final LanguageModel getModel () {
    return selectedModel;
  }

  public final SpeechToText setModel (LanguageModel model) {
    selectedModel = model;
    return this;
  }

  public final String getPrompt () {
    return selectedPrompt;
  }

  public final SpeechToText setPrompt (String prompt) {
    selectedPrompt = prompt;
    return this;
  }

  public final SpeechToText setPrompt (int prompt) {
    return setPrompt(mainActivity.getString(prompt));
  }

  public final boolean start (ActivityResultHandler handler) {
    Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

    {
      String language = getLanguage();
      if (language == null) language = Locale.getDefault().toString();
      intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, language);
    }

    {
      LanguageModel model = getModel();
      if (model == null) model = LanguageModel.FREE_FORM;
      intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, model.getValue());
    }

    {
      String prompt = getPrompt();
      if (prompt == null) prompt = "";
      intent.putExtra(RecognizerIntent.EXTRA_PROMPT, prompt);
    }

    return mainActivity.startRequest(intent, handler);
  }
}
