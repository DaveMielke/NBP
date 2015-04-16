package org.nbp.b2g.ui;

import android.content.Context;
import android.content.SharedPreferences;

public abstract class Control {
  public abstract boolean next ();
  public abstract boolean previous ();
  public abstract String getValue ();
  protected abstract String getLabel ();
  protected abstract String getPreferenceKey ();
  protected abstract boolean restoreValue (SharedPreferences prefs);
  protected abstract void saveValue (SharedPreferences.Editor editor);

  protected void reportValue () {
    ApplicationUtilities.message(getLabel() + " " + getValue());
  }

  private static SharedPreferences getSharedPreferences () {
    return ApplicationContext.get().getSharedPreferences("controls", Context.MODE_PRIVATE);
  }

  public boolean restoreValue () {
    return restoreValue(getSharedPreferences());
  }

  public boolean saveValue () {
    SharedPreferences.Editor editor = getSharedPreferences().edit();
    saveValue(editor);
    return editor.commit();
  }

  public Control () {
  }
}
