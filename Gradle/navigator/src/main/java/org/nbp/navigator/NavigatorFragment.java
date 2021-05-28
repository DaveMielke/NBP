package org.nbp.navigator;

import android.util.Log;
import android.app.Fragment;
import android.os.Bundle;

import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;

public abstract class NavigatorFragment extends Fragment {
  private final static String LOG_TAG = NavigatorFragment.class.getName();

  private final int layoutResource;

  protected NavigatorFragment (int layout) {
    layoutResource = layout;
  }

  @Override
  public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedState) {
    return inflater.inflate(layoutResource, container, false);
  }
}
