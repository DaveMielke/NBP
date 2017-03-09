package org.nbp.calculator;

import android.app.Activity;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.TypedValue;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class CalculatorConfigurator {
  private final static float BUTTON_TEXT_SIZE = 14.0f;
  private final static int CONTROL_EMS = 4;

  private final Activity activity;
  private final Resources resources;
  private final DisplayMetrics metrics;

  public CalculatorConfigurator (Activity activity) {
    this.activity = activity;
    resources = activity.getResources();
    metrics = resources.getDisplayMetrics();
  }

  private final float toPixels (int unit, float size) {
    return TypedValue.applyDimension(unit, size, metrics);
  }

  private float DP (float dp) {
    return toPixels(TypedValue.COMPLEX_UNIT_DIP, dp);
  }

  private float SP (float sp) {
    return toPixels(TypedValue.COMPLEX_UNIT_SP, sp);
  }

  private interface ViewConfigurer {
    public void configureView (View view);
  }

  private void configureViews (View view, ViewConfigurer configurer) {
    if (view != null) {
      if (view instanceof ViewGroup) {
        ViewGroup group = (ViewGroup)view;
        final int count = group.getChildCount();

        for (int index=0; index<count; index+=1) {
          configureViews(group.getChildAt(index), configurer);
        }
      } else {
        configurer.configureView(view);
      }
    }
  }

  private void configureViews (int container, ViewConfigurer configurer) {
    configureViews(activity.findViewById(container), configurer);
  }

  public void configureCalculator () {
    final float buttonTextSize = SP(BUTTON_TEXT_SIZE);

    configureViews(R.id.control,
      new ViewConfigurer() {
        @Override
        public void configureView (View view) {
          if (view instanceof Button) {
            Button button = (Button)view;
            button.setTextSize(buttonTextSize);
            button.setEms(CONTROL_EMS);
          }
        }
      }
    );

    configureViews(R.id.memory,
      new ViewConfigurer() {
        @Override
        public void configureView (View view) {
          if (view instanceof Button) {
            Button button = (Button)view;
            button.setTextSize(buttonTextSize);
          }
        }
      }
    );

    configureViews(R.id.navigation,
      new ViewConfigurer() {
        @Override
        public void configureView (View view) {
          if (view instanceof Button) {
            Button button = (Button)view;
            button.setTextSize(buttonTextSize);
            button.setEms(CONTROL_EMS);
          }
        }
      }
    );
  }
}
