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
          String problem;

          switch (code) {
            case CommonActivity.RESULT_OK: {
              if (intent != null) {
                ArrayList<String> text = intent.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                if (text != null) textHandler.handleText(text);
              }
             
              return;
            }

            case CommonActivity.RESULT_CANCELED:
              problem = "request cancelled";
              break;

            case SpeechRecognizer.ERROR_AUDIO:
              problem = "recording error";
              break;

            case SpeechRecognizer.ERROR_CLIENT:
              problem = "client error";
              break;

            case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
              problem = "not permitted";
              break;

            case SpeechRecognizer.ERROR_NETWORK:
              problem = "network error";
              break;

            case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
              problem = "network timeout";
              break;

            case SpeechRecognizer.ERROR_NO_MATCH:
              problem = "no match";
              break;

            case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
              problem = "service busy";
              break;

            case SpeechRecognizer.ERROR_SERVER:
              problem = "server error";
              break;

            case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
              problem = "no input";
              break;

            default:
              problem = "unknown problem";
              break;
          }

          textHandler.reportProblem(problem);
        }
      };

      return start(resultHandler);
    }
  }
}
