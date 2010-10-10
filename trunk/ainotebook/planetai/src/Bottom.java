

/**
 * Represents the bottom _|_ value.
 *
 * @version 2.20<br>
 *          <ul>
 *          <li>$LastChangedRevision: 127 $</li>
 *          <li>$LastChangedDate: 2009-05-10 01:46:32 +1000 (Sun, 10 May 2009) $</li>
 *          </ul>
 */
public final class Bottom {
  private Bottom() {
    throw new UnsupportedOperationException();
  }

  /**
   * Returns an error to represent undefinedness in a computation.
   *
   * @return An error to represent undefinedness in a computation.
   */
  public static Error undefined() {
    return error("undefined");
  }

  /**
   * Returns an error to represent undefinedness in a computation with early failure using the given
   * message.
   *
   * @param s The message to fail with.
   * @return An error to represent undefinedness in a computation with early failure using the given
   *         message.
   */
  public static Error error(final String s) {
    throw new Error(s);
  }

  /**
   * Represents a deconstruction failure that was non-exhaustive.
   *
   * @param a  The value being deconstructed.
   * @param sa The rendering for the value being deconstructed.
   * @return A deconstruction failure that was non-exhaustive.
   */
  public static <A> Error decons(final A a, final Show<A> sa) {
    return error("Deconstruction failure on type " + a.getClass() + " with value " + sa.show(a).toString());
  }

  /**
   * Represents a deconstruction failure that was non-exhaustive.
   *
   * @param c The type being deconstructed.
   * @return A deconstruction failure that was non-exhaustive.
   */
  public static <A> Error decons(final java.lang.Class<A> c) {
    return error("Deconstruction failure on type " + c);
  }

  /**
   * A function that returns the <code>toString</code> for a throwable.
   *
   * @return A function that returns the <code>toString</code> for a throwable.
   */
  public static <T extends Throwable> F<T, String> eToString() {
    return new F<T, String>() {
      public String f(final Throwable t) {
        return t.toString();
      }
    };
  }

  /**
   * A function that returns the <code>getMessage</code> for a throwable.
   *
   * @return A function that returns the <code>getMessage</code> for a throwable.
   */
  public static <T extends Throwable> F<T, String> eMessage() {
    return new F<T, String>() {
      public String f(final Throwable t) {
        return t.getMessage();
      }
    };
  }
}
