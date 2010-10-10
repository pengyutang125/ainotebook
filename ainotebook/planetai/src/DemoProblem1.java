
import static java.lang.System.out;

/**
 * Add all the natural numbers below one thousand that are multiples of 3 or 5.
 */
public class DemoProblem1 {
  public static void main(final String[] args) {
    out.println(Integers.sum(List.range(0, 1000).filter(new F<Integer, Boolean>() {
      public Boolean f(final Integer a) { return a % 3 == 0 || a % 5 == 0;}
    })));
  }
}
