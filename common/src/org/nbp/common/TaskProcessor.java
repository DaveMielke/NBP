package org.nbp.common;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

import android.util.Log;

public class TaskProcessor {
  private final static String LOG_TAG = TaskProcessor.class.getName();

  private final BlockingDeque taskQueue = new LinkedBlockingDeque();
  private final Thread taskThread = new Thread() {
    @Override
    public void run () {
      String name = taskThread.getName();

      try {
        Log.d(LOG_TAG, (name + " thread starting"));

        try {
          Runnable runnable;

          while ((runnable = (Runnable)taskQueue.take()) != null) {
            runnable.run();
          }
        } catch (InterruptedException exception) {
          Log.w(LOG_TAG, (name + " wait interrupted"));
        }
      } finally {
        Log.d(LOG_TAG, (name + " thread stopping"));
      }
    }
  };

  public TaskProcessor (String name) {
    taskThread.setName(name);
    taskThread.start();
  }

  public final void enqueue (Runnable runnable) {
    taskQueue.offer(runnable);
  }
}
