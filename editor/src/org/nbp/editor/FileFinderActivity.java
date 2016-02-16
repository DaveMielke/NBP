package org.nbp.editor;

import java.io.File;

import org.nbp.common.CommonActivity;
import org.nbp.common.FileFinder;

import android.util.Log;
import android.os.Bundle;

import android.content.Intent;
import android.net.Uri;

public class FileFinderActivity extends CommonActivity implements FileFinder.FileHandler {
  private final static String LOG_TAG = FileFinderActivity.class.getName();

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
  protected void onCreate (Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    String problem = null;

    Intent intent = getIntent();
    String action = intent.getAction();

    if (action != null) {
      if (action.equals(Intent.ACTION_GET_CONTENT)) {
        FileFinder.Builder builder = new FileFinder.Builder(this);

        Uri uri = intent.getData();
        if (uri != null) builder.addRootLocation(new File(uri.getPath()));

        builder.findFile(this);
      } else {
        problem = "unsupported action: " + action;
      }
    } else {
      problem = "action not specified in intent";
    }

    if (problem != null) {
      Log.w(LOG_TAG, problem);
      setResult(RESULT_CANCELED);
      finish();
    }
  }
}
