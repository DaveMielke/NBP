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
    systemFunctions.put("abs",
      new Function() {
        @Override
        public double call (double argument) {
          return Math.abs(argument);
        }
      }
    );

    systemFunctions.put("round",
      new Function() {
        @Override
        public double call (double argument) {
          return Math.rint(argument);
        }
      }
    );

    systemFunctions.put("floor",
      new Function() {
        @Override
        public double call (double argument) {
          return Math.floor(argument);
        }
      }
    );

    systemFunctions.put("ceil",
      new Function() {
        @Override
        public double call (double argument) {
          return Math.ceil(argument);
        }
      }
    );

    systemFunctions.put("sqrt",
      new Function() {
        @Override
        public double call (double argument) {
          return Math.sqrt(argument);
        }
      }
    );

    systemFunctions.put("cbrt",
      new Function() {
        @Override
        public double call (double argument) {
          return Math.cbrt(argument);
        }
      }
    );

    systemFunctions.put("r2d",
      new Function() {
        @Override
        public double call (double argument) {
          return Math.toDegrees(argument);
        }
      }
    );

    systemFunctions.put("d2r",
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

    systemFunctions.put("asin",
      new Function() {
        @Override
        public double call (double argument) {
          return Math.asin(argument);
        }
      }
    );

    systemFunctions.put("acos",
      new Function() {
        @Override
        public double call (double argument) {
          return Math.acos(argument);
        }
      }
    );

    systemFunctions.put("atan",
      new Function() {
        @Override
        public double call (double argument) {
          return Math.atan(argument);
        }
      }
    );

    systemFunctions.put("sinh",
      new Function() {
        @Override
        public double call (double argument) {
          return Math.sinh(argument);
        }
      }
    );

    systemFunctions.put("cosh",
      new Function() {
        @Override
        public double call (double argument) {
          return Math.cosh(argument);
        }
      }
    );

    systemFunctions.put("tanh",
      new Function() {
        @Override
        public double call (double argument) {
          return Math.tanh(argument);
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
