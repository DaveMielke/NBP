package org.nbp.common;

import android.app.Fragment;
import android.app.Activity;
import android.os.Bundle;

import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;

import android.widget.TextView;

public class CommonSettingsFragment extends Fragment {
  private View fragmentView = null;
  private String titleText = null;

  public CommonSettingsFragment (View fragment, String title) {
    super();
    fragmentView = fragment;
    titleText = title;
  }

  private TextView titleView = null;
  private View focusedView = null;

  @Override
  public void onCreate (Bundle state) {
    super.onCreate(state);

    Activity activity = getActivity();
    titleView = (TextView)activity.findViewById(R.id.settings_fragment_title);
  }

  @Override
  public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle state) {
    return fragmentView;
  }

  @Override
  public void onResume () {
    super.onResume();
    titleView.setText(titleText);

    if (focusedView != null) {
      focusedView.requestFocus();
    } else {
      fragmentView.requestFocus();
    }
  }

  @Override
  public void onPause () {
    try {
      if (fragmentView instanceof ViewGroup) {
        ViewGroup group = (ViewGroup)fragmentView;
        View view = group.findFocus();
        if (view != null) focusedView = view;
      }
    } finally {
      super.onPause();
    }
  }
}
