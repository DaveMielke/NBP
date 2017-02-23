package org.nbp.common;

import java.util.ArrayList;
import java.util.Locale;

import android.util.Log;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;

public abstract class SpeechToText {
  private final static String LOG_TAG = SpeechToText.class.getName();

  private SpeechToText () {
  }

  public final static int toResultMessage (int code) {
    switch (code) {
      case SpeechRecognizer.ERROR_AUDIO:
        return R.string.SpeechToText_RESULT_ERROR_AUDIO;

      case SpeechRecognizer.ERROR_CLIENT:
        return R.string.SpeechToText_RESULT_ERROR_CLIENT;

      case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
        return R.string.SpeechToText_RESULT_ERROR_INSUFFICIENT_PERMISSIONS;

      case SpeechRecognizer.ERROR_NETWORK:
        return R.string.SpeechToText_RESULT_ERROR_NETWORK;

      case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
        return R.string.SpeechToText_RESULT_ERROR_NETWORK_TIMEOUT;

      case SpeechRecognizer.ERROR_NO_MATCH:
        return R.string.SpeechToText_RESULT_ERROR_NO_MATCH;

      case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
        return R.string.SpeechToText_RESULT_ERROR_RECOGNIZER_BUSY;

      case SpeechRecognizer.ERROR_SERVER:
        return R.string.SpeechToText_RESULT_ERROR_SERVER;

      case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
        return R.string.SpeechToText_RESULT_ERROR_SPEECH_TIMEOUT;

      default:
        return ActivityResultHandler.toResultMessage(code);
    }
  }

  public static class TextHandler {
    public TextHandler () {
    }

    public void handleText (ArrayList<String> text) {
    }

    public void reportProblem (String problem) {
      Log.w(LOG_TAG, problem);
    }
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

  public static class Builder {
    private final CommonActivity mainActivity;

    public Builder (CommonActivity activity) {
      mainActivity = activity;
    }

    public final CommonActivity getActivity () {
      return mainActivity;
    }

    private String selectedLanguage = null;
    private LanguageModel selectedModel = null;
    private String selectedPrompt = null;

    public final String getLanguage () {
      return selectedLanguage;
    }

    public final Builder setLanguage (String language) {
      selectedLanguage = language;
      return this;
    }

    public final Builder setLanguage (Locale locale) {
      return setLanguage(locale.toString());
    }

    public final LanguageModel getModel () {
      return selectedModel;
    }

    public final Builder setModel (LanguageModel model) {
      selectedModel = model;
      return this;
    }

    public final String getPrompt () {
      return selectedPrompt;
    }

    public final Builder setPrompt (String prompt) {
      selectedPrompt = prompt;
      return this;
    }

    public final Builder setPrompt (int prompt) {
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

    public final boolean start (final TextHandler textHandler) {
      ActivityResultHandler resultHandler = new ActivityResultHandler() {
        @Override
        public void handleActivityResult (int code, Intent intent) {
          if (code != CommonActivity.RESULT_OK) {
            textHandler.reportProblem(mainActivity.getString(toResultMessage(code)));
          } else if (intent != null) {
            ArrayList<String> text = intent.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (text != null) textHandler.handleText(text);
          }
        }
      };

      return start(resultHandler);
    }
  }
}
