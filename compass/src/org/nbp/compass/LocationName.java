package org.nbp.compass;

import android.util.Log;
import android.location.Address;

public class LocationName {
  private final static String LOG_TAG = LocationName.class.getName();

  private final void log (StringBuilder sb, String label, String value) {
    if ((value != null) && !value.isEmpty()) {
      sb.append(' ');
      sb.append(label);
      sb.append(':');
      sb.append(value);
    }
  }

  private final void log (StringBuilder sb, String label, double value) {
    log(sb, label, Double.toString(value));
  }

  private final void log (Address address) {
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

  private final Address locationAddress;
  private String locationName = null;

  public LocationName (Address address) {
    log(address);
    locationAddress = address;
  }

  public final Address getAddress () {
    return locationAddress;
  }

  private interface NameMaker {
    public String makeName (Address address);
  }

  private final NameMaker[] nameMakers = new NameMaker[] {
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

    // locality - [district,] city
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
    },

    // this one must be last
    new NameMaker() {
      @Override
      public String makeName (Address address) {
        return "";
      }
    }
  };

  public final String getName () {
    synchronized (this) {
      if (locationName == null) {
        for (NameMaker nameMaker : nameMakers) {
          String name = nameMaker.makeName(locationAddress);

          if (name != null) {
            locationName = name;
            break;
          }
        }
      }
    }

    return locationName;
  }
}
