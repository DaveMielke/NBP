package org.nbp.common;

import android.database.Cursor;

public class CursorIterator implements Logger.Iterator {
  private final Cursor cursor;
  private final int columnCount;
  private final String[] columnNames;

  public CursorIterator (Cursor cursor) {
    this.cursor = cursor;
    columnCount = cursor.getColumnCount();
    columnNames = cursor.getColumnNames();
  }

  private boolean hasStarted = false;
  private int columnNumber;

  @Override
  public final String getText () {
    StringBuilder text = new StringBuilder();

    text.append('[');
    text.append(cursor.getPosition());
    text.append(',');
    text.append(columnNumber);
    text.append(']');

    text.append(' ');
    text.append(columnNames[columnNumber]);
    text.append(' ');
    int type = cursor.getType(columnNumber);

    switch (type) {
      case Cursor.FIELD_TYPE_NULL:
        text.append("null");
        break;

      case Cursor.FIELD_TYPE_STRING:
        text.append('"');
        text.append(cursor.getString(columnNumber));
        text.append('"');
        break;

      case Cursor.FIELD_TYPE_INTEGER:
        text.append(cursor.getInt(columnNumber));
        break;

      case Cursor.FIELD_TYPE_FLOAT:
        text.append(cursor.getFloat(columnNumber));
        break;

      case Cursor.FIELD_TYPE_BLOB: {
        byte[] blob = cursor.getBlob(columnNumber);
        int count = blob.length;

        for (int index=0; index<count; index+=1) {
          if (index > 0) text.append(' ');
          text.append(String.format("%02X", (blob[index] & 0XFF)));
        }

        break;
      }

      default:
        text.append('?');
        text.append(type);
        break;
    }

    return text.toString();
  }

  public final boolean next () {
    if (!hasStarted) {
      if (!cursor.moveToFirst()) return false;
      hasStarted = true;
    } else if (++columnNumber < columnCount) {
      return true;
    } else if (!cursor.moveToNext()) {
      return false;
    }

    columnNumber = 0;
    return true;
  }
}
