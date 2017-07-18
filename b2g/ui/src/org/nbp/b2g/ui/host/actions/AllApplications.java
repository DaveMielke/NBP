package org.nbp.b2g.ui.host.actions;
import org.nbp.b2g.ui.host.*;
import org.nbp.b2g.ui.*;

import java.util.Map;
import java.util.TreeMap;

import org.nbp.common.LaunchUtilities;

import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.ActivityInfo;

public class AllApplications extends Action {
  @Override
  public boolean performAction () {
    PackageManager pm = ApplicationContext.getContext().getPackageManager();
    Map<String, ActivityInfo> map = new TreeMap<String, ActivityInfo>();

    for (ResolveInfo resolve : LaunchUtilities.getLaunchableActivities(pm)) {
      ActivityInfo activity = resolve.activityInfo;
      String label = activity.loadLabel(pm).toString();
      if (label == null) continue;
      map.put(label, activity);
    }

    int count = map.size();
    final ActivityInfo[] array = new ActivityInfo[count];

    StringBuilder sb = new StringBuilder();
    sb.append(ApplicationContext.getString(R.string.launch_message_select));

    {
      int index = 0;

      for (String label : map.keySet()) {
        sb.append('\n');
        sb.append(label);
        array[index++] = map.get(label);
      }
    }

    Endpoints.setPopupEndpoint(sb.toString(),
      new PopupClickHandler () {
        @Override
        public final boolean handleClick (int index) {
          if ((index -= 1) < 0) return true;
          ActivityInfo activity = array[index];

          LaunchUtilities.launchActivity(activity);
          Endpoints.setHostEndpoint();
          return true;
        }
      }
    );

    return true;
  }

  public AllApplications (Endpoint endpoint) {
    super(endpoint, false);
  }
}
