package org.nbp.editor;

import java.io.InputStream;
import java.io.OutputStream;

import android.util.Log;

import org.nbp.common.CommonContext;
import android.content.Context;

import android.text.SpannableStringBuilder;

import com.aspose.words.*;

public class AsposeOperations implements ContentOperations {
  private final static String LOG_TAG = AsposeOperations.class.getName();

  private enum AsposeState {
    UNSTARTED,
    READY,
    FAILED
  }

  private static AsposeState asposeState = AsposeState.UNSTARTED;

  private final boolean startAspose () {
    synchronized (asposeState) {
      if (asposeState == AsposeState.UNSTARTED) {
        asposeState = AsposeState.FAILED;

        Context context = CommonContext.getContext();
        AsposeWordsApplication app = new AsposeWordsApplication();
        app.loadLibs(context);

        try {
          License license = new License();
          license.setLicense(context.getAssets().open("Aspose.Words.lic"));

          asposeState = AsposeState.READY;
          Log.d(LOG_TAG, "Aspose Words ready");
        } catch (Exception exception) {
          Log.w(LOG_TAG, ("Aspose Words license failure: " + exception.getMessage()));
        }
      }

      return asposeState == AsposeState.READY;
    }
  }

  @Override
  public final void read (InputStream stream, SpannableStringBuilder content) {
    if (startAspose()) {
    //Document doc = new Document(stream);
    }
  }

  @Override
  public final void write (OutputStream stream, CharSequence content) {
    if (startAspose()) {
    }
  }

  public AsposeOperations () {
    super();
  }
}
