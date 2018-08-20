package org.nbp.ipaws;

import android.util.Log;

import android.util.Xml;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
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

  private final static Set<String> TAGS = new HashSet<String>() {
    {
      add("identifier");

      add("sent");
      add("effective");
      add("expires");

      add("headline");
      add("description");
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

            if (TAGS.contains(name)) {
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

  public static void add (String identifier, String xml) {
    Map<String, String> properties = getProperties(xml);

    if (properties != null) {
      if (!identifier.isEmpty()) {
        properties.put("identifier", identifier);
      }
    }
  }

  public static void remove (String identifier) {
  }
}
