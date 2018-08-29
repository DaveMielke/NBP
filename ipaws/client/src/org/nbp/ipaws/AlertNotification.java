package org.nbp.ipaws;

import android.app.Notification;
import android.app.NotificationManager;

import android.content.Context;
import android.content.Intent;
import android.app.PendingIntent;
import android.app.Activity;
import android.app.Service;

public abstract class AlertNotification extends ApplicationComponent {
  private final static String LOG_TAG = AlertNotification.class.getName();

  private AlertNotification () {
    super();
  }

  private final static Integer NOTIFICATION_IDENTIFIER = 1;
  private static NotificationManager notificationManager = null;
  private static Notification.Builder notificationBuilder = null;

  private static NotificationManager getManager () {
    if (notificationManager == null) {
      notificationManager = (NotificationManager)
                            getContext()
                           .getSystemService(Context.NOTIFICATION_SERVICE);
    }

    return notificationManager;
  }

  private static boolean isActive () {
    return notificationBuilder != null;
  }

  private static PendingIntent newPendingIntent (Class<? extends Activity> activityClass) {
    Context context = getContext();
    Intent intent = new Intent(context, activityClass);

    intent.addFlags(
      Intent.FLAG_ACTIVITY_CLEAR_TASK |
      Intent.FLAG_ACTIVITY_NEW_TASK
    );

    return PendingIntent.getActivity(context, 0, intent, 0);
  }

  private static void makeBuilder () {
    Context context = getContext();

    notificationBuilder = new Notification.Builder(context)
      .setPriority(Notification.PRIORITY_HIGH)
      .setOngoing(true)
      .setOnlyAlertOnce(true)
      .setContentIntent(newPendingIntent(MainActivity.class))
      ;
  }

  private static Notification buildNotification () {
    return notificationBuilder.build();
  }

  private static void updateNotification () {
    getManager().notify(NOTIFICATION_IDENTIFIER, buildNotification());
  }

  public static void create (Service service) {
    synchronized (NOTIFICATION_IDENTIFIER) {
      if (isActive()) {
        throw new IllegalStateException("already active");
      }

      makeBuilder();
      service.startForeground(NOTIFICATION_IDENTIFIER, buildNotification());
    }
  }

  public static void destroy () {
    synchronized (NOTIFICATION_IDENTIFIER) {
      if (!isActive()) {
        throw new IllegalStateException("not active");
      }

      notificationBuilder = null;
      getManager().cancel(NOTIFICATION_IDENTIFIER);
    }
  }
}
