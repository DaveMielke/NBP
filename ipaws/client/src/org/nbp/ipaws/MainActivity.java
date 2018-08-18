package org.nbp.ipaws;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;

public class MainActivity extends Activity {
  private final Intent makeAlertServiceIntent () {
    return new Intent(this, AlertService.class);
  }

  @Override
  public void onCreate (Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);
    startService(makeAlertServiceIntent());
  }
}
