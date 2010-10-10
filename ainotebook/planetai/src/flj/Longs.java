package flj;
import static java.lang.Math.abs;

/**
 * Curried functions over Longs.
 *
 * @version 2.20<br>
 *          <ul>
 *          <li>$LastChangedRevision: 122 $</li>
 *          <li>$LastChangedDate: 2009-04-25 08:24:38 +1000 (Sat, 25 Apr 2009) $</li>
 *          </ul>
 */
public final class Longs {
  private Longs() {
    throw new UnsupportedOperationException();
  }

  /**
   * Curried Long addition.
   */
  public static final F<Long, F<Long, Long>> add = Semigroup.longAdditionSemigroup.sum();

  /**
   * Curried Long multiplication.
   */
  public static final F<Long, F<Long, Long>> multiply = Semigroup.longMultiplicationSemigroup.sum();

  /**
   * Curried Long subtraction.
   */
  public static final F<Long, F<Long, Long>> subtract = Function.curry(new F2<Long, Long, Long>() {
    public Long f(final Long x, final Long y) {
      return x - y;
    }
  });

  /**
   * Negation.
   */
  public static final F<Long, Long> negate = new F<Long, Long>() {
    public Long f(final Long x) {
      return x * -1L;
    }
  };

  /**
   * Absolute value.
   */
  public static final F<Long, Long> abs = new F<Long, Long>() {
    public Long f(final Long x) {
      return abs(x);
    }
  };

  /**
   * Remainder.
   */
  public static final F<Long, F<Long, Long>> remainder = Function.curry(new F2<Long, Long, Long>() {
    public Long f(final Long a, final Long b) {
      return a % b;
    }
  });
}
