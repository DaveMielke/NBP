package org.nbp.compass;

import android.util.Log;
import android.location.Address;

public abstract class LocationUtilities {
  private final static String LOG_TAG = LocationUtilities.class.getName();

  private LocationUtilities () {
  }

  private final static void append (StringBuilder sb, boolean haveCoordinate, double coordinate) {
    if (haveCoordinate) {
      sb.append(String.format("%.7f", coordinate));
    } else {
      sb.append('?');
    }
  }

  private final static void append (StringBuilder sb, String label, String string) {
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
    append(sb, address.hasLatitude(), address.getLatitude());
    sb.append(", ");
    append(sb, address.hasLongitude(), address.getLongitude());
    sb.append(']');

    append(sb, "CC", address.getCountryCode());
    append(sb, "CN", address.getCountryName());
    append(sb, "Adm", address.getAdminArea());
    append(sb, "SubAdm", address.getSubAdminArea());
    append(sb, "Loc", address.getLocality());
    append(sb, "SubLoc", address.getSubLocality());
    append(sb, "Ftr", address.getFeatureName());
    append(sb, "Thor", address.getThoroughfare());
    append(sb, "SubThor", address.getSubThoroughfare());
    append(sb, "PC", address.getPostalCode());
    append(sb, "Prem", address.getPremises());
    append(sb, "Phone", address.getPhone());
    append(sb, "URL", address.getUrl());

    {
      int last = address.getMaxAddressLineIndex();
      int current = 0;

      while (current <= last) {
        append(sb, ("Line" + current), address.getAddressLine(current++));
      }
    }

    return sb.toString();
  }

  public final static void log (Address address) {
    if (ApplicationParameters.LOG_ADDRESSES) {
      Log.d(LOG_TAG, ("address: " + toString(address)));
    }
  }

  private interface NameMaker {
    public String makeName (Address address);
  }

  private final static NameMaker[] nameMakers = new NameMaker[] {
    // premises
    new NameMaker() {
      @Override
      public String makeName (Address address) {
        return address.getPremises();
      }
    },

    // thoroughfare - [address] street
    new NameMaker() {
      @Override
      public String makeName (Address address) {
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
      public String makeName (Address address) {
        return address.getFeatureName();
      }
    },

    // locality - [neighborhood,] city
    new NameMaker() {
      @Override
      public String makeName (Address address) {
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
      public String makeName (Address address) {
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
      public String makeName (Address address) {
        String name = address.getCountryName();
        if (name == null) return null;

        String suffix = address.getPostalCode();
        if (suffix != null) name = name + ", " + suffix;

        return name;
      }
    }
  };

  public final static String getName (Address address) {
    for (NameMaker nameMaker : nameMakers) {
      String name = nameMaker.makeName(address);
      if (name != null) return name;
    }

    Log.w(LOG_TAG, ("no name for address: " + toString(address)));
    return "";
  }
}
