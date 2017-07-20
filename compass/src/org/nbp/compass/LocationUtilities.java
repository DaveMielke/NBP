package org.nbp.compass;

import android.util.Log;
import android.location.Address;

public abstract class LocationUtilities {
  private final static String LOG_TAG = LocationUtilities.class.getName();

  private LocationUtilities () {
  }

  private final static void log (StringBuilder sb, String label, String value) {
    if ((value != null) && !value.isEmpty()) {
      sb.append(' ');
      sb.append(label);
      sb.append(':');
      sb.append(value);
    }
  }

  private final static void log (StringBuilder sb, String label, double value) {
    log(sb, label, Double.toString(value));
  }

  public final static void log (Address address) {
    if (ApplicationParameters.LOG_ADDRESSES) {
      StringBuilder sb = new StringBuilder();
      sb.append("address:");

      log(sb, "CC", address.getCountryCode());
      log(sb, "CN", address.getCountryName());
      log(sb, "Adm", address.getAdminArea());
      log(sb, "SubAdm", address.getSubAdminArea());
      log(sb, "Loc", address.getLocality());
      log(sb, "SubLoc", address.getSubLocality());
      log(sb, "Ftr", address.getFeatureName());
      log(sb, "Thor", address.getThoroughfare());
      log(sb, "SubThor", address.getSubThoroughfare());
      log(sb, "PC", address.getPostalCode());
      log(sb, "Prem", address.getPremises());
      log(sb, "Phone", address.getPhone());
      log(sb, "URL", address.getUrl());

      if (address.hasLatitude()) log(sb, "Lat", address.getLatitude());
      if (address.hasLongitude()) log(sb, "Lng", address.getLongitude());

      {
        int last = address.getMaxAddressLineIndex();
        int current = 0;

        while (current <= last) {
          log(sb, ("Line" + current), address.getAddressLine(current++));
        }
      }

      Log.d(LOG_TAG, sb.toString());
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

    // country
    new NameMaker() {
      @Override
      public String makeName (Address address) {
        return address.getCountryName();
      }
    }
  };

  public final static String getName (Address address) {
    for (NameMaker nameMaker : nameMakers) {
      String name = nameMaker.makeName(address);
      if (name != null) return name;
    }

    return "";
  }
}
