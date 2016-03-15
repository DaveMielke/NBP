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
        protected final double evaluate (double argument) {
          return Math.abs(argument);
        }
      }
    );

    systemFunctions.put("round",
      new Function() {
        @Override
        protected final double evaluate (double argument) {
          return Math.rint(argument);
        }
      }
    );

    systemFunctions.put("floor",
      new Function() {
        @Override
        protected final double evaluate (double argument) {
          return Math.floor(argument);
        }
      }
    );

    systemFunctions.put("ceil",
      new Function() {
        @Override
        protected final double evaluate (double argument) {
          return Math.ceil(argument);
        }
      }
    );

    systemFunctions.put("sqrt",
      new Function() {
        @Override
        protected final double evaluate (double argument) {
          return Math.sqrt(argument);
        }
      }
    );

    systemFunctions.put("cbrt",
      new Function() {
        @Override
        protected final double evaluate (double argument) {
          return Math.cbrt(argument);
        }
      }
    );

    systemFunctions.put("exp",
      new Function() {
        @Override
        protected final double evaluate (double argument) {
          return Math.exp(argument);
        }
      }
    );

    systemFunctions.put("log",
      new Function() {
        @Override
        protected final double evaluate (double argument) {
          return Math.log(argument);
        }
      }
    );

    systemFunctions.put("log10",
      new Function() {
        @Override
        protected final double evaluate (double argument) {
          return Math.log10(argument);
        }
      }
    );

    systemFunctions.put("rd2dg",
      new Function() {
        @Override
        protected final double evaluate (double argument) {
          return Math.toDegrees(argument);
        }
      }
    );

    systemFunctions.put("dg2rd",
      new Function() {
        @Override
        protected final double evaluate (double argument) {
          return Math.toRadians(argument);
        }
      }
    );

    systemFunctions.put("sin",
      new Function() {
        @Override
        protected final double evaluate (double argument) {
          return Math.sin(argument);
        }
      }
    );

    systemFunctions.put("cos",
      new Function() {
        @Override
        protected final double evaluate (double argument) {
          return Math.cos(argument);
        }
      }
    );

    systemFunctions.put("tan",
      new Function() {
        @Override
        protected final double evaluate (double argument) {
          return Math.tan(argument);
        }
      }
    );

    systemFunctions.put("asin",
      new Function() {
        @Override
        protected final double evaluate (double argument) {
          return Math.asin(argument);
        }
      }
    );

    systemFunctions.put("acos",
      new Function() {
        @Override
        protected final double evaluate (double argument) {
          return Math.acos(argument);
        }
      }
    );

    systemFunctions.put("atan",
      new Function() {
        @Override
        protected final double evaluate (double argument) {
          return Math.atan(argument);
        }
      }
    );

    systemFunctions.put("sinh",
      new Function() {
        @Override
        protected final double evaluate (double argument) {
          return Math.sinh(argument);
        }
      }
    );

    systemFunctions.put("cosh",
      new Function() {
        @Override
        protected final double evaluate (double argument) {
          return Math.cosh(argument);
        }
      }
    );

    systemFunctions.put("tanh",
      new Function() {
        @Override
        protected final double evaluate (double argument) {
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
