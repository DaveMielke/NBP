package org.nbp.editor;

import java.util.Collections;
import java.util.Collection;

import java.util.Set;
import java.util.TreeSet;

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;

public class OwnerProfile {
  private final Collection<String> namesCollection;

  public OwnerProfile (Context context) {
    Set<String> names = new TreeSet<String>();

    String nameField = "display_name";

    String[] projection = new String[] {
      nameField
    };

    Cursor cursor = context.getApplicationContext()
                           .getContentResolver()
                           .query(ContactsContract.Profile.CONTENT_URI,
                                  projection, null, null, null);

    if (cursor != null) {
      if (cursor.moveToFirst()) {
        int nameColumn = cursor.getColumnIndex(nameField);

        do {
          String name = cursor.getString(nameColumn);
          if (name == null) continue;

          name = name.trim();
          if (name.isEmpty()) continue;

          names.add(name);
        } while (cursor.moveToNext());
      }

      cursor.close();
    }

    namesCollection = Collections.unmodifiableCollection(names);
  }

  public final Collection<String> getNames () {
    return namesCollection;
  }
}
