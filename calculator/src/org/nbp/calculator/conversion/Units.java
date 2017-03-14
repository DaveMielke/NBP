package org.nbp.calculator.conversion;

import java.util.Stack;

public abstract class Units {
  private Units () {
  }

  public final static double convert (double value, Unit from, Unit to) {
    Stack<Unit> stack = new Stack<Unit>();

    while (to != null) {
      stack.push(to);
      to = to.getReference();
    }

    while (!stack.contains(from)) {
      value -= from.getAdjustment();
      value /= from.getMultiplier();
      from = from.getReference();
    }

    while (stack.pop() != from);

    while (!stack.empty()) {
      to = stack.pop();
      value *= to.getMultiplier();
      value += to.getAdjustment();
    }

    return value;
  }

  public final static double convert (double value, String from, String to) {
    return convert(value, Unit.get(from), Unit.get(to));
  }
}
