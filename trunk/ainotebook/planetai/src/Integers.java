


import static java.lang.Math.abs;

/**
 * Curried functions over Integers.
 *
 * @version 2.20<br>
 *          <ul>
 *          <li>$LastChangedRevision: 166 $</li>
 *          <li>$LastChangedDate: 2009-06-04 02:15:48 +1000 (Thu, 04 Jun 2009) $</li>
 *          </ul>
 */
public final class Integers {
  private Integers() {
    throw new UnsupportedOperationException();
  }

  /**
   * Curried Integer addition.
   */
  public static final F<Integer, F<Integer, Integer>> add = Semigroup.intAdditionSemigroup.sum();

  /**
   * Curried Integer multiplication.
   */
  public static final F<Integer, F<Integer, Integer>> multiply = Semigroup.intMultiplicationSemigroup.sum();

  /**
   * Curried Integer subtraction.
   */
  public static final F<Integer, F<Integer, Integer>> subtract = Function.curry(new F2<Integer, Integer, Integer>() {
    public Integer f(final Integer x, final Integer y) {
      return x - y;
    }
  });

  /**
   * Negation.
   */
  public static final F<Integer, Integer> negate = new F<Integer, Integer>() {
    public Integer f(final Integer x) {
      return x * -1;
    }
  };

  /**
   * Absolute value.
   */
  public static final F<Integer, Integer> abs = new F<Integer, Integer>() {
    public Integer f(final Integer x) {
      return abs(x);
    }
  };

  /**
   * Remainder.
   */
  public static final F<Integer, F<Integer, Integer>> remainder = Function.curry(new F2<Integer, Integer, Integer>() {
    public Integer f(final Integer a, final Integer b) {
      return a % b;
    }
  });

  /**
   * Power.
   */
  public static final F<Integer, F<Integer, Integer>> power = Function.curry(new F2<Integer, Integer, Integer>() {
    public Integer f(final Integer a, final Integer b) {
      return (int) StrictMath.pow(a, b);
    }
  });

  /**
   * Evenness.
   */
  public static final F<Integer, Boolean> even = new F<Integer, Boolean>() {
    public Boolean f(final Integer i) {
      return i % 2 == 0;
    }
  };

  /**
   * Sums a list of integers.
   *
   * @param ints A list of integers to sum.
   * @return The sum of the integers in the list.
   */
  public static int sum(final List<Integer> ints) {
    return Monoid.intAdditionMonoid.sumLeft(ints);
  }

  /**
   * Returns the product of a list of integers.
   *
   * @param ints A list of integers to multiply together.
   * @return The product of the integers in the list.
   */
  public static int product(final List<Integer> ints) {
    return Monoid.intMultiplicationMonoid.sumLeft(ints);
  }

  /**
   * A function that converts strings to integers.
   *
   * @return A function that converts strings to integers.
   */
  public static F<String, Option<Integer>> fromString() {
    return new F<String, Option<Integer>>() {
      public Option<Integer> f(final String s) {
        try { return Option.some(Integer.valueOf(s)); }
        catch (final NumberFormatException ignored) {
          return Option.none();
        }
      }
    };
  }

}
