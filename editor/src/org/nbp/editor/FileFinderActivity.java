package org.nbp.editor;

import java.io.File;

import org.nbp.common.FileFinder;

import android.app.Activity;
import android.os.Bundle;

import android.content.Intent;
import android.net.Uri;

public class FileFinderActivity extends Activity implements FileFinder.FileHandler {
  @Override
  public void handleFile (File file) {
    if (file == null) {
      setResult(RESULT_CANCELED);
    } else {
      Intent intent = new Intent();
      intent.setData(Uri.fromFile(file));
      setResult(RESULT_OK, intent);
    }

    finish();
  }

  @Override
  public void onCreate (Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    Intent intent = getIntent();
    String action = intent.getAction();

    if (action != null) {
      if (action.equals(Intent.ACTION_GET_CONTENT)) {
        Uri uri = intent.getData();
        File reference = null;
        if (uri != null) reference = new File(uri.getPath());
        FileFinder.findFile(this, reference, false, this);
      }
    }
  }
}
