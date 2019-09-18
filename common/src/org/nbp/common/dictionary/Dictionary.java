package org.nbp.common.dictionary;

import android.util.Log;

import java.util.Set;
import java.util.HashSet;

public abstract class Dictionary {
  private final static String LOG_TAG = Dictionary.class.getName();

  private Dictionary () {
  }

  public static void endSession () {
    new QuitCommand();
  }

  private static void logAudit (String audit) {
    Log.d("dictionary-audit", audit);
  }

  private static void logAudit (String format, Object... arguments) {
    logAudit(String.format(format, arguments));
  }

  private static void runAudit (String name, Runnable audit) {
    logAudit(("begin audit: " + name));

    try {
      audit.run();
    } finally {
      logAudit(("end audit: " + name));
    }
  }

  private static void auditItemNames (final ItemList items, final Set<String> names, final String type) {
    runAudit(
      (type + " names"),
      new Runnable() {
        @Override
        public void run () {
          Set<String> oldNames = new HashSet(names);
          Set<String> newNames = new HashSet();

          for (ItemEntry item : items) {
            String name = item.getName();

            if (names.contains(name)) {
              oldNames.remove(name);
            } else {
              newNames.add(name);
              logAudit("new %s: %s: %s", type, name, item.getDescription());
            }
          }

          for (String oldName : oldNames) {
            logAudit("old %s: %s", type, oldName);
          }
        }
      }
    );
  }

  public static void auditDatabaseNames () {
    final Set<String> names = new HashSet();

    for (DictionaryDatabase database : DictionaryDatabase.values()) {
      if (database == DictionaryDatabase.ALL) continue;
      if (database == DictionaryDatabase.FIRST) continue;
      names.add(database.getName());
    }

    new ShowDatabasesCommand() {
      @Override
      public void handleItems (ItemList items) {
        auditItemNames(items, names, "database");
      }
    };
  }

  public static void auditStrategyNames () {
    final Set<String> names = new HashSet();

    for (DictionaryStrategy strategy : DictionaryStrategy.values()) {
      if (strategy == DictionaryStrategy.DEFAULT) continue;
      names.add(strategy.getName());
    }

    new ShowStrategiesCommand() {
      @Override
      public void handleItems (ItemList items) {
        auditItemNames(items, names, "strategy");
      }
    };
  }
}
