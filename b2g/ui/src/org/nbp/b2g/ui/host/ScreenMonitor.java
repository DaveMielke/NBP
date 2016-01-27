package org.nbp.b2g.ui.host;
import org.nbp.b2g.ui.*;
import org.nbp.b2g.ui.host.HostEndpoint;

import java.util.Collection;
import java.util.List;

import android.util.Log;

import android.accessibilityservice.AccessibilityService;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.AccessibilityEvent;

import android.content.Intent;

import android.text.SpannableStringBuilder;

public class ScreenMonitor extends AccessibilityService {
  private final static String LOG_TAG = ScreenMonitor.class.getName();

  private static ScreenMonitor screenMonitor = null;

  public static ScreenMonitor getScreenMonitor () {
    if (screenMonitor == null) Log.w(LOG_TAG, "screen monitor not runnig");
    return screenMonitor;
  }

  private static HostEndpoint getHostEndpoint () {
    return Endpoints.host.get();
  }

  private static CharSequence toText (Collection<CharSequence> lines) {
    if (lines == null) return null;
    if (lines.isEmpty()) return null;
    SpannableStringBuilder sb = new SpannableStringBuilder();

    for (CharSequence line : lines) {
      if (sb.length() > 0) sb.append('\n');
      sb.append(line);
    }

    return sb.subSequence(0, sb.length());
  }

  private static void showText (Collection<CharSequence> lines) {
    CharSequence text = toText(lines);
    if (text != null) Endpoints.setPopupEndpoint(text.toString());
  }

  private static CharSequence getAccessibilityText (AccessibilityNodeInfo node, AccessibilityEvent event) {
    if (node.getText() != null) return null;
    if (event == null) return null;

    {
      AccessibilityNodeInfo source = event.getSource();

      if (source != null) {
        boolean same = source.equals(node);
        source.recycle();
        if (!same) return null;
      }
    }

    {
      CharSequence text = toText(event.getText());
      if (text != null) return text;
    }

    if (event.getEventType() == AccessibilityEvent.TYPE_VIEW_TEXT_SELECTION_CHANGED) {
      int count = event.getItemCount();

      if (count != -1) {
        StringBuilder sb = new StringBuilder();

        while (count > 0) {
          sb.append(' ');
          count -= 1;
        }

        return sb.toString();
      }
    }

    return null;
  }

  private static boolean write (AccessibilityNodeInfo node, boolean force, AccessibilityEvent event) {
    HostEndpoint endpoint = getHostEndpoint();

    {
      CharSequence text = getAccessibilityText(node, event);
      if (text != null) endpoint.setAccessibilityText(node, text);
    }

    return endpoint.write(node, force);
  }

  private static boolean write (AccessibilityNodeInfo node, boolean force) {
    return write(node, force, null);
  }

  private static boolean write (int string) {
    return getHostEndpoint().write(string);
  }

  @Override
  public void onCreate () {
    super.onCreate();

    Log.d(LOG_TAG, "screen monitor started");
    screenMonitor = this;

    ApplicationContext.setContext(this);
    write(R.string.message_no_screen_content);
  }

  @Override
  public void onDestroy () {
    try {
      screenMonitor = null;
      Log.d(LOG_TAG, "screen monitor stopped");
    } finally {
      super.onDestroy();
    }
  }

  @Override
  protected void onServiceConnected () {
    Log.d(LOG_TAG, "screen monitor connected");

    {
      AccessibilityNodeInfo node = ScreenUtilities.getCurrentNode();

      if (node != null) {
        write(node, true);
        node.recycle();
      }
    }
  }

  @Override
  public boolean onUnbind (Intent intent) {
    Log.d(LOG_TAG, "screen monitor disconnected");
    return false;
  }

  private static void appendProperty (StringBuilder sb, String label, String value) {
    if (value != null) {
      if (!value.isEmpty()) {
        if (sb.length() > 0) sb.append('\n');
        sb.append(label);
        sb.append(": ");
        sb.append(value);
      }
    }
  }

  private static void appendProperty (StringBuilder sb, String label, CharSequence value) {
    if (value != null) appendProperty(sb, label, value.toString());
  }

  private static void appendProperty (StringBuilder sb, String label, int value) {
    if (value != -1) appendProperty(sb, label, Integer.toString(value));
  }

  private static void appendProperty (StringBuilder sb, String label, Collection<CharSequence> value) {
    appendProperty(sb, label, toText(value));
  }

  private static void say (AccessibilityEvent event) {
    StringBuilder sb = new StringBuilder();

    {
      String string = event.toString();
      int index = string.indexOf(" TYPE_");

      string = string.substring(index+1);
      index = string.indexOf('_');

      string = string.substring(index+1);
      index = string.indexOf(' ');

      string = string.substring(0, index).replace('_', ' ').toLowerCase();
      appendProperty(sb, "Event", string);
    }

    appendProperty(sb, "Count", event.getItemCount());
    appendProperty(sb, "Current", event.getCurrentItemIndex());
    appendProperty(sb, "From", event.getFromIndex());
    appendProperty(sb, "To", event.getToIndex());
    appendProperty(sb, "Text", event.getText());

    if (sb.length() > 0) {
      Devices.speech.get().say(sb.toString());
    }
  }

  private static void logMissingEventComponent (String component) {
    if (ApplicationSettings.LOG_UPDATES) {
      Log.d(LOG_TAG, "no accessibility event " + component);
    }
  }

  private void logEventComponent (AccessibilityNodeInfo node, String description) {
    if (ApplicationSettings.LOG_UPDATES) {
      Log.d(LOG_TAG,  String.format(
        "accessibility event %s: %s",
        description, ScreenUtilities.toString(node)
      ));
    }
  }

  private static void setCurrentNode (AccessibilityEvent event) {
    AccessibilityNodeInfo root = event.getSource();

    if (root != null) {
      ScreenUtilities.logNavigation(root, "set event root");

      {
        int childIndex = event.getCurrentItemIndex();

        if (childIndex != -1) {
          int from = event.getFromIndex();
          if (from != -1) childIndex -= from;
        }

        if ((childIndex >= 0) && (childIndex < root.getChildCount())) {
          AccessibilityNodeInfo child = root.getChild(childIndex);

          if (child != null) {
            root.recycle();
            root = child;
            ScreenUtilities.logNavigation(root, "set event child");
          }
        }
      }

      {
        AccessibilityNodeInfo node = ScreenUtilities.findCurrentNode(root);

        if (node != null) {
          write(node, true, event);
          node.recycle();
        }
      }

      root.recycle();
    }
  }

  private String mostRecentAlert = null;

  private String toAlert (AccessibilityEvent event) {
    String alert = null;
    CharSequence text = toText(event.getText());

    if (text != null) {
      alert = text.toString();
    } else {
      int count = event.getItemCount();

      if (count != -1) {
        int index = event.getCurrentItemIndex();
        int percentage =
          (count == 0)? 0:
          ((index * 100) / count);

        alert = String.format("%d%%", percentage);
      }
    }

    if (alert == null) return null;
    if (alert.equals(mostRecentAlert)) return null;
    return (mostRecentAlert = alert);
  }

  private void handleViewSelected (AccessibilityEvent event, AccessibilityNodeInfo view) {
    if (view == null) {
      String alert = toAlert(event);
      if (alert != null) ApplicationUtilities.message(alert.replace("\n", " "));
    } else if (ScreenUtilities.isBar(view)) {
      ScreenUtilities.logNavigation(view, String.format(
        "bar %d/%d", event.getCurrentItemIndex(), event.getItemCount()
      ));

      if (view.isAccessibilityFocused()) {
        String alert = toAlert(event);
        if (alert != null) getHostEndpoint().write(view, alert);
      }
    } else if (view.isFocused()) {
      setCurrentNode(event);
    }
  }

  private static void handleViewAccessibilityFocused (AccessibilityEvent event, AccessibilityNodeInfo view) {
    if (view.isAccessibilityFocused()) {
      write(view, true, event);
    }
  }

  private static void handleViewInputFocused (AccessibilityEvent event, AccessibilityNodeInfo view) {
    AccessibilityNodeInfo inputFocus = null;

    if (view.isFocused())  {
      inputFocus = AccessibilityNodeInfo.obtain(view);
    } else if ((inputFocus = view.findFocus(AccessibilityNodeInfo.FOCUS_INPUT)) != null) {
      ScreenUtilities.logNavigation(inputFocus, "view with input focus");
    } else {
      ScreenUtilities.logNavigation(view, "view not input focused");
    }

    if (inputFocus != null) {
      AccessibilityNodeInfo accessibilityFocus = inputFocus.findFocus(AccessibilityNodeInfo.FOCUS_ACCESSIBILITY);

      if (accessibilityFocus != null) {
        ScreenUtilities.logNavigation(accessibilityFocus, "view with accessibility focus");
        accessibilityFocus.recycle();
      } else {
        setCurrentNode(event);
      }

      inputFocus.recycle();
    }
  }

  private static void handleViewScrolled (AccessibilityEvent event, AccessibilityNodeInfo source) {
    ScrollContainer container = ScrollContainer.getContainer(source);

    if (container != null) {
      synchronized (container) {
        container.setItemCount(event.getItemCount());
        container.setFirstItemIndex(event.getFromIndex());
        container.setLastItemIndex(event.getToIndex());
        container.onScroll();
      }
    }
  }

  private final static Object ACCESSIBILITY_EVENT_LOCK = new Object();

  private void handleAccessibilityEvent (AccessibilityEvent event) {
    int type = event.getEventType();
    AccessibilityNodeInfo source = event.getSource();

    switch (type) {
      case AccessibilityEvent.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED:
        break;

      case AccessibilityEvent.TYPE_VIEW_SELECTED:
        handleViewSelected(event, source);
        break;

      default:
        if (source != null) {
          logEventComponent(source, "source");

          switch (type) {
            case AccessibilityEvent.TYPE_VIEW_ACCESSIBILITY_FOCUSED:
              handleViewAccessibilityFocused(event, source);
              break;

            case AccessibilityEvent.TYPE_VIEW_FOCUSED:
              handleViewInputFocused(event, source);
              break;

            default: {
              AccessibilityNodeInfo node = ScreenUtilities.findCurrentNode(source);

              switch (type) {
                case AccessibilityEvent.TYPE_VIEW_SCROLLED:
                  handleViewScrolled(event, source);
                  break;

                case AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED: {
                  int cursor = event.getFromIndex() + event.getAddedCount();
                  getHostEndpoint().onTextSelectionChange(source, cursor, cursor);
                  break;
                }

                case AccessibilityEvent.TYPE_VIEW_TEXT_SELECTION_CHANGED:
                  getHostEndpoint().onTextSelectionChange(
                    source, event.getFromIndex(), event.getToIndex()
                  );
                  break;
              }

              if (node != null) {
                logEventComponent(node, "node");

                switch (type) {
                  case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED:
                    write(node, true, event);
                    break;

                  default:
                    write(node, false, event);
                    break;
                }

                node.recycle();
              } else {
                logMissingEventComponent("node");
              }

              break;
            }
          }

          source.recycle();
        } else {
          logMissingEventComponent("source");

          switch (type) {
            case AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED:
              showText(event.getText());
              break;

            default:
              break;
          }
        }
        break;
    }
  }

  @Override
  public void onAccessibilityEvent (final AccessibilityEvent event) {
    synchronized (ACCESSIBILITY_EVENT_LOCK) {
      if (ApplicationSettings.LOG_UPDATES) {
        Log.d(LOG_TAG, "accessibility event starting: " + event.toString());
      //say(event);
      }

      Crash.runComponent(
        "accessibility event", event.toString(),
        new Runnable() {
          @Override
          public void run () {
            handleAccessibilityEvent(event);
          }
        }
      );

      if (ApplicationSettings.LOG_UPDATES) {
        Log.d(LOG_TAG, "accessibility event finished");
      }
    }
  }

  @Override
  public void onInterrupt () {
  }
}
