package org.nbp.b2g.ui;
import org.nbp.b2g.ui.host.ScreenMonitor;

import android.util.Log;

import android.content.Context;
import org.nbp.common.CommonContext;

import android.content.Intent;
import android.content.ComponentName;

import android.view.accessibility.AccessibilityManager;

import android.view.inputmethod.InputMethodManager;
import android.inputmethodservice.InputMethodService;
import android.view.inputmethod.InputMethodInfo;

import org.liblouis.Louis;
import org.liblouis.NewTranslationTablesListener;

public abstract class ApplicationContext extends CommonContext {
  private final static String LOG_TAG = ApplicationContext.class.getName();

  public static boolean setContext (Context context) {
    if (!CommonContext.setContext(context)) return false;
    final Object LOCK = new Object();

    final String logTag = LOG_TAG + ".startup";
    Log.d(logTag, "begin");

    Log.d(logTag, "preparing LibLouis");
    {
      NewTranslationTablesListener listener = new NewTranslationTablesListener() {
        @Override
        public void newTranslationTables () {
          synchronized (LOCK) {
            TranslationUtilities.refresh();

            if (ApplicationSettings.EVENT_MESSAGES) {
              ApplicationUtilities.message(R.string.message_new_literary_tables);
            }
          }
        }
      };

      Louis.setLogLevel(ApplicationParameters.LIBLOUIS_LOG_LEVEL);
      Louis.initialize(context, listener);
    }

    synchronized (LOCK) {
      Log.d(logTag, "starting host monitor");
      HostMonitor.start(context);

      Log.d(logTag, "preparing clipboard object");
      Clipboard.prepare(context);

      Log.d(logTag, "starting phone monitor");
      PhoneMonitor.start(context);

      Log.d(logTag, "restoring controls");
      Controls.restoreCurrentValues();
      Controls.restoreSaneValues();

      Log.d(logTag, "starting speech");
      Devices.speech.get().say(null);

      Log.d(logTag, "starting event monitors");
      EventMonitors.startEventMonitors();

      Log.d(logTag, "starting screen monitor");
      ScreenMonitor.start();

      Log.d(logTag, "starting input service");
      InputService.start();

      Log.d(logTag, "end");
      return true;
    }
  }

  public static AccessibilityManager getAccessibilityManager () {
    Object systemService = getSystemService(Context.ACCESSIBILITY_SERVICE);
    if (systemService == null) return null;
    return (AccessibilityManager)systemService;
  }

  public static boolean isTouchExplorationActive () {
    AccessibilityManager accessibilityManager = getAccessibilityManager();
    if (accessibilityManager == null) return false;
    return accessibilityManager.isTouchExplorationEnabled();
  }

  public static InputMethodManager getInputMethodManager () {
    Object systemService = getSystemService(Context.INPUT_METHOD_SERVICE);
    if (systemService == null) return null;
    return (InputMethodManager)systemService;
  }

  public static InputMethodInfo getInputMethodInfo (Class<? extends InputMethodService> inputMethodClass) {
    String packageName = inputMethodClass.getPackage().getName();
    String className = inputMethodClass.getName();

    InputMethodManager manager = getInputMethodManager();
    if (manager == null) return null;

    for (InputMethodInfo info : manager.getEnabledInputMethodList()) {
      ComponentName component = info.getComponent();

      if (packageName.equals(component.getPackageName())) {
        if (className.equals(component.getClassName())) {
          return info;
        }
      }
    }

    return null;
  }

  private ApplicationContext () {
  }
}
