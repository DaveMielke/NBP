package org.nbp.editor.operations.aspose;
import org.nbp.editor.*;

import android.util.Log;
import android.content.Context;

import com.aspose.words.*;
import java.io.IOException;

public class WordsLicense {
  private final static String LOG_TAG = WordsLicense.class.getName();

  private License licenseObject = null;
  private Throwable licenseProblem = null;

  private WordsLicense () {
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
              licenseObject.setLicense(context.getAssets().open(ApplicationParameters.ASPOSE_WORDS_LICENSE_FILE));
              Log.d(LOG_TAG, "license activated");
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

  private static Object SINGLETON_LOCK = new Object();
  private static WordsLicense singletonObject = null;

  public final static WordsLicense singleton () {
    synchronized (SINGLETON_LOCK) {
      if (singletonObject == null) singletonObject = new WordsLicense();
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
