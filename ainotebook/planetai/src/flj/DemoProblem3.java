

/**
 * Find the largest prime factor of a composite number.
 */
public class DemoProblem3 {
    
  // An infinite stream of all the primes.
  public static final Stream<Natural> primes = Stream.cons(Natural.natural(2).some(), new P1<Stream<Natural>>() {
    public Stream<Natural> _1() {
      return Stream.forever(Enumerator.naturalEnumerator, Natural.natural(3).some(), 2).filter(new F<Natural, Boolean>() {
        public Boolean f(final Natural n) {return primeFactors(n).length() == 1;}
      });
    }
  });

  //Finds factors of a given number.
  public static Stream<Natural> factor(final Natural n, final Natural p, final P1<Stream<Natural>> ps) {
    Stream<Natural> ns = Stream.cons(p, ps);
    Stream<Natural> ret = Stream.nil();
    while (ns.isNotEmpty() && ret.isEmpty()) {
      final Natural h = ns.head();
      final P1<Stream<Natural>> t = ns.tail();
      if (Ord.naturalOrd.isGreaterThan(h.multiply(h), n))
        ret = Stream.single(n);
      else {
        final V2<Natural> dm = n.divmod(h);
        if (Ord.naturalOrd.eq(dm._2(), Natural.ZERO))
          ret = Stream.cons(h, new P1<Stream<Natural>>() {
            public Stream<Natural> _1() {return factor(dm._1(), h, t);}
          });
        else ns = ns.tail()._1();
      }
    }
    return ret;
  }

  // Finds the prime factors of a given number.
  public static Stream<Natural> primeFactors(final Natural n) {return factor(n, Natural.natural(2).some(), primes.tail());}

  public static void main(final String[] args) {
    Show.naturalShow.println(primeFactors(Natural.natural(600851475143L).some()).last());
  }
}
