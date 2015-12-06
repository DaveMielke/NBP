package org.nbp.b2g.ui.host;
import org.nbp.b2g.ui.*;

import android.util.Log;

import android.os.AsyncTask;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.text.util.Linkify;

import java.io.IOException;
import java.io.InputStream;

public abstract class ViewerActivity extends ProgrammaticActivity {
  private final static String LOG_TAG = ViewerActivity.class.getName();

  protected abstract InputStream getInputStream ();

  private View createDocumentView () {
    final TextView view = newTextView();
    view.setAutoLinkMask(Linkify.WEB_URLS);

    new AsyncTask<Void, Integer, CharSequence>() {
      @Override
      protected void onPreExecute () {
      }

      private final String loadDocument () {
        final StringBuilder result = new StringBuilder();
        InputStream inputStream = getInputStream();

        if (inputStream != null) {
          new InputProcessor() {
            @Override
            protected final boolean handleLine (String text, int number) {
              result.append(text);
              result.append('\n');
              return true;
            }
          }.processInput(inputStream);
        } else {
          result.append(ApplicationContext.getString(R.string.message_no_document));
        }

        return result.toString();
      }

      @Override
      protected CharSequence doInBackground (Void... argument) {
        publishProgress(R.string.message_loading_document);
        Thread.yield();
        String document = loadDocument();

        publishProgress(R.string.message_formatting_document);
        Thread.yield();
        CharSequence text;

        {
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
    }.execute();

    View container = newVerticalScrollContainer(view);
    view.setVerticalScrollBarEnabled(true);

    return container;
  }

  @Override
  protected final View createContentView () {
    return createVerticalGroup(
      createDocumentView()
    );
  }
}
