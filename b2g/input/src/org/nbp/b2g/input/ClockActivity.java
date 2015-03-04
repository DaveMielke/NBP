package org.nbp.b2g.input;

import java.util.Timer;
import java.util.TimerTask;

import java.util.Date;
import java.text.SimpleDateFormat;

import android.os.Bundle;
import android.app.Activity;
import android.widget.TextView;

public class ClockActivity extends Activity {
  public TextView timeView;
  SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

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
          synchronized (ClockActivity.this) {
            if (isClockUpdateScheduled()) {
              Date date = new Date();
              timeView.setText(dateFormatter.format(date));
              scheduleClockUpdate(1000 - (date.getTime() % 1000));
            }
          }
        }
      });
    }
  }

  ClockUpdateTask clockUpdateTask = null;

  private boolean isClockUpdateScheduled () {
    return clockUpdateTask != null;
  }

  private void cancelClockUpdate () {
    synchronized (this) {
      if (isClockUpdateScheduled()) {
        clockUpdateTask.stop();
        clockUpdateTask = null;
      }
    }
  }

  private void scheduleClockUpdate (long delay) {
    synchronized (this) {
      cancelClockUpdate();
      clockUpdateTask = new ClockUpdateTask(delay);
    }
  }

  @Override
  public void onCreate (Bundle state) {
    super.onCreate(state);
    setContentView(R.layout.clock);
    timeView = (TextView)findViewById(R.id.clock_time);
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
