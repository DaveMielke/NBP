package org.nbp.calculator;

// Cornelius Lanczos 1964 approximation of the Gamma function
//
public abstract class GammaFunction {
  private final static ComplexNumber[] coefficientArray = {
    new ComplexNumber(676.5203681218851d),
    new ComplexNumber(-1259.1392167224028d),
    new ComplexNumber(771.32342877765313d),
    new ComplexNumber(-176.61502916214059d),
    new ComplexNumber(12.507343278686905d),
    new ComplexNumber(-0.13857109526572012d),
    new ComplexNumber(9.9843695780195716e-6d),
    new ComplexNumber(1.5056327351493116e-7d)
  };

  private final static int coefficientCount = coefficientArray.length;
  private final static double EPSILON = 0.0000001d;

  public final static ComplexNumber gamma (ComplexNumber number) {
    ComplexNumber result;

    if (number.real() < 0.5d) {
      result = ComplexNumber.ONE;
    } else {
      number = number.sub(1d);
      ComplexNumber x = new ComplexNumber(0.99999999999980993d);

      for (int index=0; index<coefficientCount; index+=1) {
        x = x.add(coefficientArray[index].div(number.add(index+1)));
      }

      ComplexNumber t = number.add((double)coefficientCount - 0.5d);
      result = ComplexOperations.sqrt(ComplexNumber.PI.mul(2d))
              .mul(t.pow(number.add(0.5d)))
              .mul(t.neg().exp())
              .mul(x);
    }

    if (Math.abs(result.imag()) < EPSILON) result = new ComplexNumber(result.real());
    return result;
  }

  private GammaFunction () {
  }
}
