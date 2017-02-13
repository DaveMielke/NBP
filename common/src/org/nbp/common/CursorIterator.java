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
  public final String get () {
    StringBuilder sb = new StringBuilder();

    sb.append('[');
    sb.append(cursor.getPosition());
    sb.append(',');
    sb.append(columnNumber);
    sb.append(']');

    sb.append(' ');
    sb.append(columnNames[columnNumber]);
    sb.append(' ');
    int type = cursor.getType(columnNumber);

    switch (type) {
      case Cursor.FIELD_TYPE_NULL:
        sb.append("null");
        break;

      case Cursor.FIELD_TYPE_STRING:
        sb.append('"');
        sb.append(cursor.getString(columnNumber));
        sb.append('"');
        break;

      case Cursor.FIELD_TYPE_INTEGER:
        sb.append(cursor.getInt(columnNumber));
        break;

      case Cursor.FIELD_TYPE_FLOAT:
        sb.append(cursor.getFloat(columnNumber));
        break;

      case Cursor.FIELD_TYPE_BLOB: {
        byte[] blob = cursor.getBlob(columnNumber);
        int count = blob.length;

        for (int index=0; index<count; index+=1) {
          if (index > 0) sb.append(' ');
          sb.append(String.format("%02X", (blob[index] & 0XFF)));
        }

        break;
      }

      default:
        sb.append('?');
        sb.append(type);
        break;
    }

    return sb.toString();
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
