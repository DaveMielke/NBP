package org.nbp.ipaws;

import android.util.Log;

import android.util.Xml;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.File;
import java.io.FileWriter;
import java.io.StringReader;

import java.util.Map;
import java.util.HashMap;

import java.util.Set;
import java.util.HashSet;

public abstract class Alerts extends AlertComponent {
  private final static String LOG_TAG = Alerts.class.getName();

  private Alerts () {
    super();
  }

  public final static String PROPERTY_IDENTIFIER = "identifier";
  public final static String PROPERTY_SENT = "sent";
  public final static String PROPERTY_EFFECTIVE = "effective";
  public final static String PROPERTY_EXPIRES = "expires";
  public final static String PROPERTY_HEADLINE = "headline";
  public final static String PROPERTY_DESCRIPTION = "description";

  private final static Set<String> PROPERTIES = new HashSet<String>() {
    {
      add(PROPERTY_IDENTIFIER);
      add(PROPERTY_SENT);
      add(PROPERTY_EFFECTIVE);
      add(PROPERTY_EXPIRES);
      add(PROPERTY_HEADLINE);
      add(PROPERTY_DESCRIPTION);
    }
  };

  private static Map<String, String> getProperties (String xml) {
    XmlPullParser parser = Xml.newPullParser();
    Map<String, String> properties = new HashMap<String, String>();

    try {
      parser.setInput(new StringReader(xml));

      while (true) {
        switch (parser.next()) {
          case XmlPullParser.END_DOCUMENT:
            return properties;

          case XmlPullParser.START_TAG: {
            String name = parser.getName();

            if (PROPERTIES.contains(name)) {
              parser.next();
              parser.require(XmlPullParser.TEXT, null, null);
              String value = parser.getText();

              parser.next();
              parser.require(XmlPullParser.END_TAG, null, name);

              properties.put(name, value);
            }

            break;
          }
        }
      }
    } catch (XmlPullParserException exception) {
      Log.e(LOG_TAG, ("XML error: " + exception.getMessage()));
    } catch (IOException exception) {
      Log.e(LOG_TAG, ("I/O error: " + exception.getMessage()));
    }

    return null;
  }

  private static File getFile (String identifier) {
    return new File(getAlertsDirectory(), identifier);
  }

  public static void add (String identifier, String xml) {
    Map<String, String> properties = getProperties(xml);

    if (properties != null) {
      if (!identifier.isEmpty()) {
        properties.put(PROPERTY_IDENTIFIER, identifier);
      }

      {
        File temporaryFile = new File(getFilesDirectory(), "new-alert");

        try {
          FileWriter writer = new FileWriter(temporaryFile);
          writer.write(xml);
          writer.close();
          temporaryFile.setReadOnly();

          File permanentFile = getFile(identifier);
          temporaryFile.renameTo(permanentFile);
        } catch (IOException exception) {
          temporaryFile.delete();
          Log.e(LOG_TAG, ("alert file creation error: " + exception.getMessage()));
        }
      }
    }
  }

  public static void remove (String identifier) {
    File file = getFile(identifier);
    file.delete();
  }

  public static String[] list () {
    return getAlertsDirectory().list();
  }
}
