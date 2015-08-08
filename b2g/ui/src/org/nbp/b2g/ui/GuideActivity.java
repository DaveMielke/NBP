package org.nbp.b2g.ui;

import android.util.Log;
import android.os.Bundle;
import android.os.AsyncTask;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import android.text.Spanned;
import android.text.Html;
import android.text.util.Linkify;

public class GuideActivity extends ProgrammaticActivity {
  private final static String LOG_TAG = GuideActivity.class.getName();

  private View createGuideView () {
    final TextView view = createTextView();
    view.setAutoLinkMask(Linkify.WEB_URLS);

    AsyncTask<String, String, Spanned> task = new AsyncTask<String, String, Spanned>() {
      private final String readDocument (String name) {
        final StringBuilder result = new StringBuilder();

        InputProcessor inputProcessor = new InputProcessor() {
          @Override
          protected final boolean handleLine (String text, int number) {
            result.append(text);
            result.append('\n');
            return true;
          }
        };

        inputProcessor.processInput(name);
        return result.toString();
      }

      @Override
      protected Spanned doInBackground (String... names) {
        publishProgress("reading");
        String html = readDocument(names[0]);

        publishProgress("formatting");
        Spanned text = Html.fromHtml(html);

        publishProgress("done");
        return text;
      }

      @Override
      protected void onProgressUpdate (String... values) {
        view.setText(values[0]);
      }

      @Override
      protected void onPostExecute (Spanned result) {
        view.setText(result);
      }
    };

    task.execute("guide.html");
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
