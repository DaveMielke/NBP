package org.nbp.compass;

import android.util.Log;
import android.location.Address;

public abstract class LocationUtilities {
  private final static String LOG_TAG = LocationUtilities.class.getName();

  private LocationUtilities () {
  }

  private final static void appendCoordinate (StringBuilder sb, boolean haveValue, double value) {
    if (haveValue) {
      sb.append(ApplicationUtilities.toCoordinateText(value));
    } else {
      sb.append('?');
    }
  }

  private final static void appendProperty (StringBuilder sb, String label, String string) {
    if ((string != null) && !string.isEmpty()) {
      sb.append(' ');
      sb.append(label);
      sb.append(':');

      sb.append('"');
      sb.append(string);
      sb.append('"');
    }
  }

  public final static String toString (Address address) {
    StringBuilder sb = new StringBuilder();

    sb.append('[');
    appendCoordinate(sb, address.hasLatitude(), address.getLatitude());
    sb.append(',');
    appendCoordinate(sb, address.hasLongitude(), address.getLongitude());
    sb.append(']');

    appendProperty(sb, "CC", address.getCountryCode());
    appendProperty(sb, "CN", address.getCountryName());
    appendProperty(sb, "Adm", address.getAdminArea());
    appendProperty(sb, "SubAdm", address.getSubAdminArea());
    appendProperty(sb, "Loc", address.getLocality());
    appendProperty(sb, "SubLoc", address.getSubLocality());
    appendProperty(sb, "Ftr", address.getFeatureName());
    appendProperty(sb, "Thor", address.getThoroughfare());
    appendProperty(sb, "SubThor", address.getSubThoroughfare());
    appendProperty(sb, "PC", address.getPostalCode());
    appendProperty(sb, "Prem", address.getPremises());
    appendProperty(sb, "Phone", address.getPhone());
    appendProperty(sb, "URL", address.getUrl());

    {
      int last = address.getMaxAddressLineIndex();
      int current = 0;

      while (current <= last) {
        appendProperty(sb, ("Line" + current), address.getAddressLine(current++));
      }
    }

    return sb.toString();
  }

  private interface NameMaker {
    public CharSequence makeName (Address address);
  }

  private final static NameMaker[] nameMakers = new NameMaker[] {
    // premises
    new NameMaker() {
      @Override
      public CharSequence makeName (Address address) {
        return address.getPremises();
      }
    },

    // thoroughfare - [address] street
    new NameMaker() {
      @Override
      public CharSequence makeName (Address address) {
        String name = address.getThoroughfare();
        if (name == null) return null;

        String prefix = address.getSubThoroughfare();
        if (prefix != null) name = prefix + ' ' + name;

        return name;
      }
    },

    // feature
    new NameMaker() {
      @Override
      public CharSequence makeName (Address address) {
        return address.getFeatureName();
      }
    },

    // locality - [neighborhood,] city
    new NameMaker() {
      @Override
      public CharSequence makeName (Address address) {
        String name = address.getLocality();
        if (name == null) return null;

        String prefix = address.getSubLocality();
        if (prefix != null) name = prefix + ", " + name;

        return name;
      }
    },

    // administrative area - [county,] state
    new NameMaker() {
      @Override
      public CharSequence makeName (Address address) {
        String name = address.getAdminArea();
        if (name == null) return null;

        String prefix = address.getSubAdminArea();
        if (prefix != null) name = prefix + ", " + name;

        return name;
      }
    },

    // country [, postal code]
    new NameMaker() {
      @Override
      public CharSequence makeName (Address address) {
        String name = address.getCountryName();
        if (name == null) return null;

        String suffix = address.getPostalCode();
        if (suffix != null) name = name + ", " + suffix;

        return name;
      }
    },

    // [latitude,longitude]
    new NameMaker() {
      @Override
      public CharSequence makeName (Address address) {
        if (!address.hasLatitude()) return null;
        if (!address.hasLongitude()) return null;
        return ApplicationUtilities.toCoordinatesText(
          address.getLatitude(), address.getLongitude()
        );
      }
    }
  };

  public final static CharSequence getName (Address address) {
    for (NameMaker nameMaker : nameMakers) {
      CharSequence name = nameMaker.makeName(address);
      if (name != null) return name;
    }

    Log.w(LOG_TAG, ("no name for address: " + toString(address)));
    return "";
  }
}
