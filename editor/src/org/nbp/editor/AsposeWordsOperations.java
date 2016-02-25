package org.nbp.editor;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.util.Log;

import org.nbp.common.CommonContext;
import android.content.Context;

import android.text.SpannableStringBuilder;

import com.aspose.words.*;

public class AsposeWordsOperations extends AsposeWordsApplication implements ContentOperations {
  private final static String LOG_TAG = AsposeWordsOperations.class.getName();

  private final static License license = new License();
  private static Throwable licenseProblem = null;

  public AsposeWordsOperations () throws IOException {
    super();

    synchronized (LOG_TAG) {
      if (licenseProblem == null) {
        Context context = CommonContext.getContext();
        loadLibs(context);

        try {
          license.setLicense(context.getAssets().open("Aspose.Words.lic"));
          Log.d(LOG_TAG, "Aspose Words ready");
          return;
        } catch (Throwable problem) {
          licenseProblem = problem;
        }

        Log.w(LOG_TAG, ("Aspose Words license problem: " + licenseProblem.getMessage()));
        throw new IOException("Aspose Words license problem", licenseProblem);
      }
    }
  }

  @Override
  public final void read (InputStream stream, SpannableStringBuilder content) throws IOException {
    try {
    //Document doc = new Document(stream);
    } catch (Exception exception) {
    }
  }

  @Override
  public final void write (OutputStream stream, CharSequence content) {
  }
}
