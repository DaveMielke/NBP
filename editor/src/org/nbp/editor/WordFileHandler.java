package org.nbp.editor;

import android.util.Log;

import org.nbp.common.CommonContext;
import android.content.Context;

import java.io.File;
import android.text.SpannableStringBuilder;

import com.aspose.words.AsposeWordsApplication;
import com.aspose.words.License;

public class WordFileHandler extends FileHandler {
  private final static String LOG_TAG = WordFileHandler.class.getName();

  private final void startAsposeWords () {
    new Thread() {
      @Override
      public void run () {
        Context context = CommonContext.getContext();

        AsposeWordsApplication app = new AsposeWordsApplication();
        app.loadLibs(context);

        try {
          License license = new License();
          license.setLicense(context.getAssets().open("Aspose.Words.lic"));
          Log.d(LOG_TAG, "Aspose Words license set");
        } catch (Exception exception) {
          Log.w(LOG_TAG, ("Aspose Words license failure: " + exception.getMessage()));
        }
      }
    }.start();
  }

  @Override
  public final void read (File file, SpannableStringBuilder sb) {
  }

  @Override
  public final void write (File file, SpannableStringBuilder sb) {
  }

  public WordFileHandler () {
    super();
  }
}
