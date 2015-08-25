package org.nbp.b2g.ui;

import android.util.Log;
import android.os.Bundle;
import android.os.AsyncTask;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.nightwhistler.htmlspanner.HtmlSpanner;
import android.text.util.Linkify;

public class HelpActivity extends ProgrammaticActivity {
  private final static String LOG_TAG = HelpActivity.class.getName();

  private View createDocumentView () {
    final TextView view = createTextView();
    view.setAutoLinkMask(Linkify.WEB_URLS);

    final String TEXT_EXTENSION = "txt";
    final String HTML_EXTENSION = "html";

    new AsyncTask<String, Integer, CharSequence>() {
      @Override
      protected void onPreExecute () {
      }

      private final String loadDocument (String name) {
        final StringBuilder result = new StringBuilder();

        new InputProcessor() {
          @Override
          protected final boolean handleLine (String text, int number) {
            result.append(text);
            result.append('\n');
            return true;
          }
        }.processInput(name);

        return result.toString();
      }

      @Override
      protected CharSequence doInBackground (String... names) {
        publishProgress(R.string.message_loading_document);
        Thread.yield();

        String name = names[0];
        String extension = names[1];
        String document = loadDocument((name + '.' + extension));

        publishProgress(R.string.message_formatting_document);
        Thread.yield();
        CharSequence text;

        if (extension.equals(HTML_EXTENSION)) {
          HtmlSpanner htmlSpanner = new HtmlSpanner();
          text = htmlSpanner.fromHtml(document);
        } else {
          int end = document.length();

          while (end > 0) {
            if (!Character.isWhitespace(document.charAt(--end))) {
              end += 1;
              break;
            }
          }

          text = document.subSequence(0, end);
        }

        publishProgress(R.string.message_rendering_document);
        Thread.yield();
        return text;
      }

      @Override
      protected void onProgressUpdate (Integer... values) {
        String report = ApplicationContext.getString(values[0]);
        view.setText(report);
        Log.v(LOG_TAG, report);
      }

      @Override
      protected void onPostExecute (CharSequence result) {
        view.setText(result);
      }
    }.execute("b2g_ui", TEXT_EXTENSION);

    return view;
  }

  @Override
  protected final View createContentView () {
    LinearLayout view = new LinearLayout(this);
    view.setOrientation(view.VERTICAL);

    LinearLayout.LayoutParams parameters = new LinearLayout.LayoutParams(
      LinearLayout.LayoutParams.MATCH_PARENT,
      LinearLayout.LayoutParams.WRAP_CONTENT
    );

    parameters.leftMargin = ApplicationContext.dipsToPixels(
      ApplicationParameters.SCREEN_LEFT_OFFSET
    );

    view.addView(createDocumentView());
    return view;
  }

  @Override
  public void onCreate (Bundle state) {
    super.onCreate(state);
    ApplicationContext.setContext(this);
    setContentView();
  }
}
