package org.nbp.calculator;

import java.util.HashMap;

public abstract class Functions {
  private static class FunctionMap extends HashMap<String, Function> {
    public FunctionMap () {
      super();
    }
  }

  private final static FunctionMap systemFunctions = new FunctionMap();

  static {
    systemFunctions.put("deg",
      new Function() {
        @Override
        public double call (double argument) {
          return Math.toDegrees(argument);
        }
      }
    );

    systemFunctions.put("rad",
      new Function() {
        @Override
        public double call (double argument) {
          return Math.toRadians(argument);
        }
      }
    );

    systemFunctions.put("sin",
      new Function() {
        @Override
        public double call (double argument) {
          return Math.sin(argument);
        }
      }
    );

    systemFunctions.put("cos",
      new Function() {
        @Override
        public double call (double argument) {
          return Math.cos(argument);
        }
      }
    );

    systemFunctions.put("tan",
      new Function() {
        @Override
        public double call (double argument) {
          return Math.tan(argument);
        }
      }
    );
  }

  public static Function get (String name) {
    return systemFunctions.get(name);
  }

  private Functions () {
  }
}
