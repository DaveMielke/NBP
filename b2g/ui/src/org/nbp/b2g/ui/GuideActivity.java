package org.nbp.b2g.ui;

import android.util.Log;
import android.os.Bundle;
import android.os.AsyncTask;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import android.text.Html;
import android.text.util.Linkify;

public class GuideActivity extends ProgrammaticActivity {
  private final static String LOG_TAG = GuideActivity.class.getName();

  private View createGuideView () {
    final TextView view = createTextView();
    view.setAutoLinkMask(Linkify.WEB_URLS);

    new AsyncTask<String, String, CharSequence>() {
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

      private CharSequence trimText (CharSequence text) {
        String string = text.toString();
        int from = string.indexOf('\n');
        int to = string.length();

        if (from < 0) {
          from = 0;
        } else {
          while (from < to) {
            if (!Character.isWhitespace(string.charAt(from))) break;
            from += 1;
          }
        }

        while (to > from) {
          if (!Character.isWhitespace(string.charAt(--to))) {
            to += 1;
            break;
          }
        }

        return text.subSequence(from, to);
      }

      @Override
      protected CharSequence doInBackground (String... names) {
        publishProgress("loading document");
        String html = loadDocument(names[0]);

        publishProgress("formatting document");
        CharSequence text = trimText(Html.fromHtml(html));

        publishProgress("rendering document");
        return text;
      }

      @Override
      protected void onProgressUpdate (String... values) {
        String report = values[0];
        view.setText(report);
        Log.v(LOG_TAG, report);
      }

      @Override
      protected void onPostExecute (CharSequence result) {
        view.setText(result);
      }
    }.execute("guide.html");

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

    view.addView(createGuideView());
    return view;
  }

  @Override
  public void onCreate (Bundle state) {
    super.onCreate(state);
    ApplicationContext.setContext(this);
    setContentView();
  }
}
