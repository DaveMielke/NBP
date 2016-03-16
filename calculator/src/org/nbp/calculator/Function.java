package org.nbp.calculator;

import static org.nbp.common.CommonActivity.getActivity;

import android.view.View;
import android.widget.CheckBox;

public abstract class Function {
  protected abstract double evaluate (double argument);

  protected double preprocessArgument (double argument) {
    return argument;
  }

  protected double postprocessResult (double result) {
    return result;
  }

  public final double call (double argument) {
    return postprocessResult(evaluate(preprocessArgument(argument)));
  }

  protected final static View findView (int resource) {
    return getActivity().findViewById(resource);
  }

  protected final static boolean isChecked (int resource) {
    return ((CheckBox)findView(resource)).isChecked();
  }

  public Function () {
  }
}
