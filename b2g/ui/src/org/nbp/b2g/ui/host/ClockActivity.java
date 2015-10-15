package org.nbp.b2g.ui.host;
import org.nbp.b2g.ui.*;

import java.util.Timer;
import java.util.TimerTask;

import java.util.Date;
import java.text.SimpleDateFormat;

import android.os.Bundle;
import android.app.Activity;
import android.widget.TextView;

public class ClockActivity extends Activity {
  public TextView timeView;
  SimpleDateFormat dateFormatter = new SimpleDateFormat(ApplicationParameters.CLOCK_TIME_FORMAT);

  private class ClockUpdateTask extends TimerTask {
    private final Timer timer = new Timer();

    public void stop () {
      timer.cancel();
    }

    public ClockUpdateTask (long delay) {
      super();
      timer.schedule(this, delay);
    }

    @Override
    public void run () {
      runOnUiThread(new Runnable() {
        @Override
        public void run () {
          synchronized (getClockUpdateSynchronizationObject()) {
            if (isClockUpdateScheduled()) {
              Date date = new Date();
              timeView.setText(dateFormatter.format(date));

              long interval = ApplicationParameters.CLOCK_UPDATE_INTERVAL;
              scheduleClockUpdate(interval - (date.getTime() % interval));
            }
          }
        }
      });
    }
  }

  ClockUpdateTask clockUpdateTask = null;

  private Object getClockUpdateSynchronizationObject () {
    return this;
  }

  private boolean isClockUpdateScheduled () {
    return clockUpdateTask != null;
  }

  private void cancelClockUpdate () {
    synchronized (getClockUpdateSynchronizationObject()) {
      if (isClockUpdateScheduled()) {
        clockUpdateTask.stop();
        clockUpdateTask = null;
      }
    }
  }

  private void scheduleClockUpdate (long delay) {
    synchronized (getClockUpdateSynchronizationObject()) {
      cancelClockUpdate();
      clockUpdateTask = new ClockUpdateTask(delay);
    }
  }

  @Override
  public void onCreate (Bundle state) {
    super.onCreate(state);
    ApplicationContext.setContext(this);
    setContentView(R.layout.clock);
    timeView = (TextView)findViewById(R.id.clock_time);
    timeView.setKeepScreenOn(true);
  }

  @Override
  public void onResume () {
    super.onResume();
    scheduleClockUpdate(0);
  }

  @Override
  public void onPause () {
    super.onPause();
    cancelClockUpdate();
  }
}
