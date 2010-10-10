

/**
 * Find the largest palindrome made from the product of two 3-digit numbers.
 */
public class DemoProblem4 {
  public static void main(final String[] a) {
      
    final Stream<Integer> tdl = Stream.iterate(Function.flip(Integers.subtract).f(1), 999).takeWhile(Ord.intOrd.isGreaterThan(99));
    Show.intShow.println(tdl.tails().bind(tdl.zipWith(Integers.multiply)).filter(new F<Integer, Boolean>() {
      public Boolean f(final Integer i) {
        final Stream<Character> s = Show.intShow.show(i);
        return Equal.streamEqual(Equal.charEqual).eq(s.reverse().take(3), s.take(3));
      }
      
    }).foldLeft1(Ord.intOrd.max));
  }
}
