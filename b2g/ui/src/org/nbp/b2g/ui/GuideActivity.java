package org.nbp.b2g.ui;

import android.util.Log;
import android.os.Bundle;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import android.text.Html;
import android.text.util.Linkify;

public class GuideActivity extends ProgrammaticActivity {
  private final static String LOG_TAG = GuideActivity.class.getName();

  private View createGuideView () {
    final StringBuilder guideText = new StringBuilder();

    InputProcessor guideProcessor = new InputProcessor() {
      @Override
      protected final boolean handleLine (String text, int number) {
        guideText.append(text);
        guideText.append('\n');
        return true;
      }
    };

    guideProcessor.processInput("guide.html");
    TextView view = createTextView();
    view.setText(Html.fromHtml(guideText.toString()));
    view.setAutoLinkMask(Linkify.WEB_URLS);
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
