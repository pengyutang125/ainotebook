
package flj;
import static java.lang.System.out;

/**
 * Find the sum of all the even-valued terms in the Fibonacci sequence which do not exceed four million.
 */
public class DemoProblem2 {
    
  public static void main(final String[] args) {
      
    final Stream<Integer> fibs = new F2<Integer, Integer, Stream<Integer>>() {
        
      public Stream<Integer> f(final Integer a, final Integer b) {
        return Stream.cons(a, FW.$(Function.curry(this).f(b)).lazy().f(a + b));
      }
      
    }.f(1, 2);
    out.println(Integers.sum(fibs.filter(Integers.even).takeWhile(Ord.intOrd.isLessThan(4000001)).toList()));
  }
}
