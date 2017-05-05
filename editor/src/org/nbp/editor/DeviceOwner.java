package org.nbp.editor;

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;

public class DeviceOwner {
  public DeviceOwner (Context context) {
    Cursor cursor = context.getApplicationContext()
                           .getContentResolver()
                           .query(ContactsContract.Profile.CONTENT_URI,
                                  null, null, null, null);

    cursor.moveToFirst();
    cursor.getString(cursor.getColumnIndex("display_name"));
    cursor.close();
  }
}
