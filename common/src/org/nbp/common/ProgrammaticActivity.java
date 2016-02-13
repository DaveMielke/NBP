package org.nbp.common;

import android.os.Bundle;

import android.view.View;
import android.view.ViewGroup;

public abstract class ProgrammaticActivity extends CommonActivity {
  protected abstract View createContentView ();

  private final int getLeftOffset () {
    return CommonContext.dipsToPixels(
      CommonParameters.SCREEN_LEFT_OFFSET
    );
  }

  private final void setContentView () {
    ViewGroup.LayoutParams parameters = new ViewGroup.LayoutParams(
      ViewGroup.LayoutParams.MATCH_PARENT,
      ViewGroup.LayoutParams.MATCH_PARENT
    );

    View view = createContentView();
    view.setLayoutParams(parameters);

    view.setPadding(getLeftOffset(), 0, 0, 0);
    setContentView(view);
  }

  @Override
  protected void onCreate (Bundle state) {
    super.onCreate(state);
    setContentView();
  }
}
