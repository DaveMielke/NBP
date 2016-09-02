package org.nbp.b2g.ui.host;
import org.nbp.b2g.ui.*;

import android.content.Context;
import android.content.ContentResolver;
import android.provider.Settings;

public abstract class SystemSettingAction extends Action {
  protected abstract String getSettingName ();
  protected abstract String getSettingValue ();
  protected final static String FALSE = "0";
  protected final static String TRUE = "1";

  protected abstract int getSettingLabel ();
  protected abstract int getSettingDescription ();
  protected final static int OFF = R.string.boolean_control_previous;
  protected final static int ON = R.string.boolean_control_next;

  protected final ContentResolver getContentResolver () {
    return getContext().getContentResolver();
  }

  protected final String getString (int resource) {
    return ApplicationContext.getString(resource);
  }

  @Override
  public final boolean performAction () {
    ContentResolver resolver = getContentResolver();
    String name = getSettingName();
    String value = getSettingValue();

    if (value.equals(Settings.System.getString(resolver, name))) return false;
    Settings.System.putString(resolver, name, value);

    StringBuilder sb = new StringBuilder();
    sb.append(getString(getSettingLabel()));
    sb.append(' ');
    sb.append(getString(getSettingDescription()));
    ApplicationUtilities.message(sb.toString());

    return true;
  }

  protected SystemSettingAction (Endpoint endpoint) {
    super(endpoint, false);
  }
}
