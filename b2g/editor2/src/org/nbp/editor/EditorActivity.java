package org.nbp.editor;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import android.view.MenuInflater;
import android.view.Menu;
import android.view.MenuItem;

import com.aspose.words.AsposeWordsApplication;
import com.aspose.words.License;

public class EditorActivity extends Activity {
  private final static String LOG_TAG = EditorActivity.class.getName();

  private final void prepareAsposeWords () {
    AsposeWordsApplication app = new AsposeWordsApplication();
    app.loadLibs(this);

    try {
      License license = new License();
      license.setLicense(getAssets().open("Aspose.Words.lic"));
      Log.d(LOG_TAG, "Aspose Words license set");
    } catch (Exception exception) {
      Log.w(LOG_TAG, ("Aspose Words license failure: " + exception.getMessage()));
    }
  }

  @Override
  public void onCreate (Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    prepareAsposeWords();
    setContentView(R.layout.editor_activity);
  }

  @Override
  public boolean onCreateOptionsMenu (Menu menu) {
    MenuInflater inflater = new MenuInflater(this);
    inflater.inflate(R.menu.options, menu);
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
