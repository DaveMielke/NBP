package org.nbp.common;

import java.util.Map;
import java.util.HashMap;

import android.app.Fragment;
import android.app.Activity;
import android.os.Bundle;

import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;

import android.widget.TextView;

public class CommonSettingsFragment extends Fragment {
  public CommonSettingsFragment () {
    super();
  }

  private String titleText = null;

  public CommonSettingsFragment (String title) {
    this();
    titleText = title;
  }

  private final static String STATE_TITLE_TEXT = "title-text";

  @Override
  public void onSaveInstanceState (Bundle state) {
    state.putString(STATE_TITLE_TEXT, titleText);
  }

  private final static Map<String, CommonSettingsFragment> fragments =
               new HashMap<String, CommonSettingsFragment>();

  @Override
  public void onCreate (Bundle state) {
    super.onCreate(state);

    if (titleText == null) titleText = state.getString(STATE_TITLE_TEXT, null);
    fragments.put(titleText, this);
  }

  private View fragmentView = null;
  private TextView titleView = null;
  private View focusedView = null;

  @Override
  public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle state) {
    return fragmentView;
  }

  @Override
  public void onResume () {
    super.onResume();

    if (titleView == null) {
      Activity activity = getActivity();
      titleView = (TextView)activity.findViewById(R.id.settings_fragment_title);
    }

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
      focusedView = null;

      if (fragmentView instanceof ViewGroup) {
        ViewGroup group = (ViewGroup)fragmentView;
        View view = group.findFocus();
        if (view != null) focusedView = view;
      }
    } finally {
      super.onPause();
    }
  }

  public static CommonSettingsFragment get (View view, String title) {
    CommonSettingsFragment fragment = fragments.get(title);
    if (fragment == null) fragment =  new CommonSettingsFragment(title);

    fragment.fragmentView = view;
    return fragment;
  }
}
