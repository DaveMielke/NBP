package org.nbp.editor;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.util.Log;

import org.nbp.common.CommonContext;
import android.content.Context;

import android.text.SpannableStringBuilder;

import com.aspose.words.*;

public class AsposeOperations extends AsposeWordsApplication implements ContentOperations {
  private final static String LOG_TAG = AsposeOperations.class.getName();

  private final static License license = new License();
  private static Throwable failure = null;

  public AsposeOperations () throws IOException {
    super();

    Context context = CommonContext.getContext();
    loadLibs(context);

    try {
      license.setLicense(context.getAssets().open("Aspose.Words.lic"));
      Log.d(LOG_TAG, "Aspose Words ready");
    } catch (Throwable throwable) {
      Log.w(LOG_TAG, ("Aspose Words license failure: " + throwable.getMessage()));
      failure = throwable;
      throw new IOException("Aspose Words initialization failed", throwable);
    }
  }

  @Override
  public final void read (InputStream stream, SpannableStringBuilder content) throws IOException {
    try {
      Document doc = new Document(stream);
    } catch (Exception exception) {
    }
  }

  @Override
  public final void write (OutputStream stream, CharSequence content) {
  }
}
