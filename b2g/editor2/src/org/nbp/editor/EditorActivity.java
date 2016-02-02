package org.nbp.editor;

import android.util.Log;

import android.content.Context;
import android.app.Activity;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuItem;

import com.aspose.words.AsposeWordsApplication;
import com.aspose.words.License;

public class EditorActivity extends Activity {
  private final static String LOG_TAG = EditorActivity.class.getName();

  protected final Context getContext () {
    return this;
  }

  private final void prepareAsposeWords () {
    new Thread() {
      @Override
      public void run () {
        AsposeWordsApplication app = new AsposeWordsApplication();
        app.loadLibs(getContext());

        try {
          License license = new License();
          license.setLicense(getAssets().open("Aspose.Words.lic"));
          Log.d(LOG_TAG, "Aspose Words license set");
        } catch (Exception exception) {
          Log.w(LOG_TAG, ("Aspose Words license failure: " + exception.getMessage()));
        }
      }
    }.start();
  }

  @Override
  public void onCreate (Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    prepareAsposeWords();
    setContentView(R.layout.editor_activity);
  }

  @Override
  public boolean onCreateOptionsMenu (Menu menu) {
    getMenuInflater().inflate(R.menu.options, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected (MenuItem item) {
    switch (item.getItemId()) {
      case  R.id.options_new:
        return true;

      case  R.id.options_open:
        return true;

      case  R.id.options_save:
        return true;

      case  R.id.options_saveAs:
        return true;

      case  R.id.options_send:
        return true;

      default:
        return false;
    }
  }
}
