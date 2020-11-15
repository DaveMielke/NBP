package org.nbp.editor.operations;
import org.nbp.editor.*;

import android.util.Log;
import android.content.Context;

import com.aspose.words.*;
import java.io.IOException;

public class AsposeWordsLicense {
  private final static String LOG_TAG = AsposeWordsLicense.class.getName();

  private final static AsposeWordsApplication asposeApplication = new AsposeWordsApplication();

  static {
    Context context = ApplicationContext.getContext();
    asposeApplication.loadLibs(context);
  }

  private License licenseObject = null;
  private Throwable licenseProblem = null;

  private AsposeWordsLicense () {
    final Context context = ApplicationContext.getContext();
    final Object licenseLock = this;
    final Object threadLock = new Object();

    Thread thread = new Thread() {
      @Override
      public void run () {
        synchronized (licenseLock) {
          synchronized (threadLock) {
            threadLock.notify();
          }

          if (licenseObject == null) {
            Log.d(LOG_TAG, "activating license");

            try {
              licenseObject = new License();
              licenseObject.setLicense(context.getAssets().open(ApplicationParameters.ASPOSE_LICENSE_FILE));
              Log.d(LOG_TAG, "license ready");
            } catch (Throwable problem) {
              Log.w(LOG_TAG, ("license problem: " + problem.getMessage()));
              licenseProblem = problem;
            }
          }
        }
      }
    };

    synchronized (threadLock) {
      thread.setDaemon(true);
      thread.start();

      try {
        threadLock.wait();
      } catch (InterruptedException exception) {
        Log.w(LOG_TAG, "thread wait interrupted");
      }
    }   
  }

  private static Object singletonLock = new Object();
  private static AsposeWordsLicense singletonObject = null;

  public final static AsposeWordsLicense singleton () {
    synchronized (singletonLock) {
      if (singletonObject == null) singletonObject = new AsposeWordsLicense();
    }

    return singletonObject;
  }

  public final void check () throws IOException {
    synchronized (this) {
      if (licenseProblem != null) {
        throw new IOException("license problem", licenseProblem);
      }
    }
  }
}
