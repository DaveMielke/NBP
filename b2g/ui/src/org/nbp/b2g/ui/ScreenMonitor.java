package org.nbp.b2g.ui;
import org.nbp.b2g.ui.host.HostEndpoint;

import java.util.List;

import android.util.Log;

import android.accessibilityservice.AccessibilityService;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.AccessibilityEvent;

import android.content.Intent;

public class ScreenMonitor extends AccessibilityService {
  private final static String LOG_TAG = ScreenMonitor.class.getName();

  private static ScreenMonitor screenMonitor = null;

  public static ScreenMonitor getScreenMonitor () {
    if (screenMonitor == null) Log.w(LOG_TAG, "screen monitor not runnig");
    return screenMonitor;
  }

  private static HostEndpoint getHostEndpoint () {
    return Endpoints.getHostEndpoint();
  }

  @Override
  public void onCreate () {
    super.onCreate();

    Log.d(LOG_TAG, "screen monitor started");
    screenMonitor = this;

    ApplicationContext.setContext(this);
  }

  @Override
  public void onDestroy () {
    super.onDestroy();
    screenMonitor = null;
    Log.d(LOG_TAG, "screen monitor stopped");
  }

  @Override
  protected void onServiceConnected () {
    Log.d(LOG_TAG, "screen monitor connected");

    {
      HostEndpoint endpoint = getHostEndpoint();
      AccessibilityNodeInfo node = ScreenUtilities.getCurrentNode();

      if (node != null) {
        endpoint.write(node, true);
      } else {
        endpoint.write(R.string.message_no_screen_content);
      }
    }
  }

  @Override
  public boolean onUnbind (Intent intent) {
    Log.d(LOG_TAG, "screen monitor disconnected");
    return false;
  }

  private void logMissingEventComponent (String component) {
    if (ApplicationParameters.CURRENT_LOG_UPDATES) {
      Log.d(LOG_TAG, "no accessibility event " + component);
    }
  }

  private void logEventComponent (AccessibilityNodeInfo node, String description) {
    if (ApplicationParameters.CURRENT_LOG_UPDATES) {
      Log.d(LOG_TAG,  String.format(
        "accessibility event %s: %s",
        description, ScreenUtilities.toString(node)
      ));
    }
  }

  @Override
  public void onAccessibilityEvent (AccessibilityEvent event) {
    HostEndpoint endpoint = getHostEndpoint();
    int type = event.getEventType();
    List<CharSequence> text = event.getText();
    AccessibilityNodeInfo source = event.getSource();

    if (ApplicationParameters.CURRENT_LOG_UPDATES) {
      Log.d(LOG_TAG, "accessibility event: " + event.toString());
    }

    if (type == AccessibilityEvent.TYPE_VIEW_SELECTED) {
      if ((source == null) || ScreenUtilities.isSeekable(source)) {
        int count = event.getItemCount();

        if (count != -1) {
          int index = event.getCurrentItemIndex();

          StringBuilder sb = new StringBuilder();
          sb.append((index * 100) / count);
          sb.append('%');
          ApplicationUtilities.message(sb.toString());
        }
      }
    }

    if (source != null) {
      logEventComponent(source, "source");
      AccessibilityNodeInfo node = ScreenUtilities.getCurrentNode(source);

      if (node != null) {
        logEventComponent(node, "node");

        switch (type) {
          case AccessibilityEvent.TYPE_VIEW_FOCUSED:
          case AccessibilityEvent.TYPE_VIEW_ACCESSIBILITY_FOCUSED:
          case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED:
            endpoint.write(node, true);
            text = null;
            break;

          default:
            endpoint.write(node, false);
          case AccessibilityEvent.TYPE_VIEW_TEXT_SELECTION_CHANGED:
            text = null;
            break;
        }

        node.recycle();
      } else {
        logMissingEventComponent("node");
      }

      source.recycle();
    } else {
      logMissingEventComponent("source");
    }

    if (text != null) {
      switch (type) {
        default:
          break;
      }

      if (text != null) {
        StringBuilder sb = new StringBuilder();

        for (CharSequence line : text) {
          if (line.length() == 0) continue;
          if (sb.length() > 0) sb.append('\n');
          sb.append(line);
        }

        if (sb.length() > 0) {
          endpoint.write(sb.toString());
        } else {
          logMissingEventComponent("text");
        }
      }
    }
  }

  @Override
  public void onInterrupt () {
  }
}
