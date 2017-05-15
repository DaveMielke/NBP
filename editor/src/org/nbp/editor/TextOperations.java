package org.nbp.editor;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import java.io.Reader;
import java.io.InputStreamReader;
import java.io.BufferedReader;

import java.io.Writer;
import java.io.OutputStreamWriter;

import android.text.Spanned;
import android.text.SpannedString;
import android.text.Editable;

public class TextOperations extends ContentOperations {
  private final static String LOG_TAG = TextOperations.class.getName();

  protected final Spanned asSpanned (CharSequence string) {
    if (string instanceof Spanned) return (Spanned)string;
    return new SpannedString(string);
  }

  protected void postProcessInput (Editable content) {
  }

  protected String preprocessOutput (CharSequence content) {
    return content.toString();
  }

  @Override
  public final void read (InputStream stream, final Editable content) throws IOException {
    BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

    try {
      for (String line; ((line = reader.readLine()) != null); ) {
        content.append(line);
        content.append('\n');
      }

      postProcessInput(content);
    } finally {
      reader.close();
    }
  }

  @Override
  public final void write (OutputStream stream, CharSequence content) throws IOException {
    Writer writer = new OutputStreamWriter(stream);

    try {
      writer.write(preprocessOutput(content));
      writer.write('\n');
    } finally {
      writer.close();
    }
  }

  public TextOperations () {
    super(false);
  }
}
