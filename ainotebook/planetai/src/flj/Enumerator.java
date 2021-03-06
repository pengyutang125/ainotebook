package flj;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Abstracts over a type that may have a successor and/or predecessor value. This implies ordering for that type. A user
 * may construct an enumerator with an optimised version for <code>plus</code>, otherwise a default is implemented using
 * the given successor/predecessor implementations.
 * <p/>
 * For any enumerator e, the following laws must satisfy:
 * <ul>
 * <li>forall a. e.successor(a).forall(\t -> e.predecessor(t).forall(\z -> z == a))</li>
 * <li>forall a. e.predecessor(a).forall(\t -> e.successor(t).forall(\z -> z == a))</li>
 * <li>e.max().forall(\t -> e.successor(t).isNone)</li>
 * <li>e.min().forall(\t -> e.predecessor(t).isNone)</li>
 * <li>forall a n. e.plus(a, 0) == Option.some(a)</li>
 * <li>forall a n | n > 0. e.plus(a, n) == e.plus(a, n - 1)</li>
 * <li>forall a n | n < 0. e.plus(a, n) == e.plus(a, n + 1)</li>
 * </ul>
 *
 * @version 2.20<br>
 *          <ul>
 *          <li>$LastChangedRevision: 161 $</li>
 *          <li>$LastChangedDate: 2009-06-01 17:14:38 +1000 (Mon, 01 Jun 2009) $</li>
 *          </ul>
 */
public final class Enumerator<A> {
  private final F<A, Option<A>> successor;
  private final F<A, Option<A>> predecessor;
  private final Option<A> max;
  private final Option<A> min;
  private final Ord<A> order;
  private final F<A, F<Long, Option<A>>> plus;

  private Enumerator(final F<A, Option<A>> successor, final F<A, Option<A>> predecessor, final Option<A> max,
                     final Option<A> min, final Ord<A> order, final F<A, F<Long, Option<A>>> plus) {
    this.successor = successor;
    this.predecessor = predecessor;
    this.max = max;
    this.min = min;
    this.order = order;
    this.plus = plus;
  }

  /**
   * Returns the potential successor of a value for this enumerator in curried form.
   *
   * @return The potential successor of a value for this enumerator in curried form.
   */
  public F<A, Option<A>> successor() {
    return successor;
  }

  /**
   * Returns the potential successor of a value for this enumerator.
   *
   * @param a The value to return the successor of.
   * @return The potential successor of a value for this enumerator.
   */
  public Option<A> successor(final A a) {
    return successor.f(a);
  }

  /**
   * Returns the potential predecessor of a value for this enumerator in curried form.
   *
   * @return The potential predecessor of a value for this enumerator in curried form.
   */
  public F<A, Option<A>> predecessor() {
    return predecessor;
  }

  /**
   * Returns the potential predecessor of a value for this enumerator.
   *
   * @param a The value to return the predecessor of.
   * @return The potential predecessor of a value for this enumerator.
   */
  public Option<A> predecessor(final A a) {
    return predecessor.f(a);
  }

  /**
   * Returns the maximum value for this enumerator if there is one.
   *
   * @return The maximum value for this enumerator if there is one.
   */
  public Option<A> max() {
    return max;
  }

  /**
   * Returns the minimum value for this enumerator if there is one.
   *
   * @return The minimum value for this enumerator if there is one.
   */
  public Option<A> min() {
    return min;
  }

  /**
   * Returns a function that moves a value along the enumerator a given number of times.
   *
   * @return A function that moves a value along the enumerator a given number of times.
   */
  public F<A, F<Long, Option<A>>> plus() {
    return plus;
  }

  /**
   * Returns a function that moves a value along the enumerator a given number of times.
   *
   * @param a The value to begin moving along from.
   * @return A function that moves a value along the enumerator a given number of times.
   */
  public F<Long, Option<A>> plus(final A a) {
    return plus.f(a);
  }

  /**
   * Returns a function that moves a value along the enumerator a given number of times.
   *
   * @param l The number of times to move along the enumerator.
   * @return A function that moves a value along the enumerator a given number of times.
   */
  public F<A, Option<A>> plus(final long l) {
    return Function.flip(plus).f(l);
  }

  /**
   * Moves a value along the enumerator a given number of times.
   *
   * @param a The value to begin moving along from.
   * @param l The number of times to move along the enumerator.
   * @return A potential value after having moved the given number of times.
   */
  public Option<A> plus(final A a, final long l) {
    return plus.f(a).f(l);
  }

  /**
   * Returns the ordering for the enumerator.
   *
   * @return The ordering for the enumerator.
   */
  public Ord<A> order() {
    return order;
  }

  /**
   * Invariant functor map over this enumerator.
   *
   * @param f The covariant map.
   * @param g The contra-variant map.
   * @return An enumerator after the given functions are applied.
   */
  public <B> Enumerator<B> xmap(final F<A, B> f, final F<B, A> g) {
    final F<Option<A>, Option<B>> of = new F<Option<A>, Option<B>>() {
      public Option<B> f(final Option<A> o) {
        return o.map(f);
      }
    };
    return enumerator(Function.compose(Function.compose(of, successor), g),
                      Function.compose(Function.compose(of, predecessor), g),
                      max.map(f),
                      min.map(f),
                      order.comap(g),
                      Function.compose(Function.compose(Function.<Long, Option<A>, Option<B>>compose().f(of), plus), g));
  }

  /**
   * Returns a stream of the values from this enumerator, starting at the given value, counting up.
   *
   * @param a A value at which to begin the stream.
   * @return a stream of the values from this enumerator, starting at the given value, counting up.
   */
  public Stream<A> toStream(final A a) {
    final F<A, A> id = Function.identity();
    return Stream.fromFunction(this, id, a);
  }

  /**
   * Create a new enumerator with the given minimum value.
   *
   * @param min A minimum value.
   * @return A new enumerator identical to this one, but with the given minimum value.
   */
  public Enumerator<A> setMin(final Option<A> min) {
    return enumerator(successor, predecessor, max, min, order, plus);
  }

  /**
   * Create a new enumerator with the given maximum value.
   *
   * @param max A maximum value.
   * @return A new enumerator identical to this one, but with the given maximum value.
   */
  public Enumerator<A> setMax(final Option<A> max) {
    return enumerator(successor, predecessor, max, min, order, plus);
  }

  /**
   * Construct an enumerator.    `
   *
   * @param successor   The successor function.
   * @param predecessor The predecessor function.
   * @param max         The potential maximum value.
   * @param min         The potential minimum value.
   * @param order       The ordering for the type.
   * @param plus        The function to move the enumeration a given number of times. This may be supplied for a performance
   *                    enhancement for certain types.
   * @return An enumerator with the given values.
   */
  public static <A> Enumerator<A> enumerator(final F<A, Option<A>> successor, final F<A, Option<A>> predecessor,
                                             final Option<A> max, final Option<A> min, final Ord<A> order,
                                             final F<A, F<Long, Option<A>>> plus) {
    return new Enumerator<A>(successor, predecessor, max, min, order, plus);
  }

  /**
   * Construct an enumerator. The <code>plus</code> function is derived from the <code>successor</code> and
   * <code>predecessor</code>.
   *
   * @param successor   The successor function.
   * @param predecessor The predecessor function.
   * @param max         The potential maximum value.
   * @param min         The potential minimum value.
   * @param order       The ordering for the type.
   * @return An enumerator with the given values.
   */
  public static <A> Enumerator<A> enumerator(final F<A, Option<A>> successor, final F<A, Option<A>> predecessor,
                                             final Option<A> max, final Option<A> min, final Ord<A> order) {
    return new Enumerator<A>(successor, predecessor, max, min, order, Function.curry(new F2<A, Long, Option<A>>() {
      public Option<A> f(final A a, final Long l) {
        if (l == 0L)
          return Option.some(a);
        else if (l < 0L) {
          A aa = a;
          for (long x = l; x < 0; x++) {
            final Option<A> s = predecessor.f(aa);
            if (s.isNone())
              return Option.none();
            else
              aa = s.some();
          }
          return Option.some(aa);
        } else {
          A aa = a;
          for (long x = l; x > 0; x--) {
            final Option<A> s = successor.f(aa);
            if (s.isNone())
              return Option.none();
            else
              aa = s.some();
          }
          return Option.some(aa);
        }
      }
    }));
  }

  /**
   * An enumerator for <code>boolean</code>.
   */
  public static final Enumerator<Boolean> booleanEnumerator = enumerator(new F<Boolean, Option<Boolean>>() {
    public Option<Boolean> f(final Boolean b) {
      return b ? Option.<Boolean>none() : Option.some(true);
    }
  }, new F<Boolean, Option<Boolean>>() {
    public Option<Boolean> f(final Boolean b) {
      return b ? Option.some(false) : Option.<Boolean>none();
    }
  }, Option.some(true), Option.some(false), Ord.booleanOrd);

  /**
   * An enumerator for <code>byte</code>.
   */
  public static final Enumerator<Byte> byteEnumerator = enumerator(new F<Byte, Option<Byte>>() {
    public Option<Byte> f(final Byte b) {
      return b == Byte.MAX_VALUE ? Option.<Byte>none() : Option.some((byte) (b + 1));
    }
  }, new F<Byte, Option<Byte>>() {
    public Option<Byte> f(final Byte b) {
      return b == Byte.MIN_VALUE ? Option.<Byte>none() : Option.some((byte) (b - 1));
    }
  }, Option.some(Byte.MAX_VALUE), Option.some(Byte.MIN_VALUE), Ord.byteOrd);

  /**
   * An enumerator for <code>char</code>.
   */
  public static final Enumerator<Character> charEnumerator = enumerator(new F<Character, Option<Character>>() {
    public Option<Character> f(final Character c) {
      return c == Character.MAX_VALUE ? Option.<Character>none() : Option.some((char) (c + 1));
    }
  }, new F<Character, Option<Character>>() {
    public Option<Character> f(final Character c) {
      return c == Character.MIN_VALUE ? Option.<Character>none() : Option.some((char) (c - 1));
    }
  }, Option.some(Character.MAX_VALUE), Option.some(Character.MIN_VALUE), Ord.charOrd);

  /**
   * An enumerator for <code>double</code>.
   */
  public static final Enumerator<Double> doubleEnumerator = enumerator(new F<Double, Option<Double>>() {
    public Option<Double> f(final Double d) {
      return d == Double.MAX_VALUE ? Option.<Double>none() : Option.some(d + 1D);
    }
  }, new F<Double, Option<Double>>() {
    public Option<Double> f(final Double d) {
      return d == Double.MIN_VALUE ? Option.<Double>none() : Option.some(d - 1D);
    }
  }, Option.some(Double.MAX_VALUE), Option.some(Double.MIN_VALUE), Ord.doubleOrd);

  /**
   * An enumerator for <code>float</code>.
   */
  public static final Enumerator<Float> floatEnumerator = enumerator(new F<Float, Option<Float>>() {
    public Option<Float> f(final Float f) {
      return f == Float.MAX_VALUE ? Option.<Float>none() : Option.some(f + 1F);
    }
  }, new F<Float, Option<Float>>() {
    public Option<Float> f(final Float f) {
      return f == Float.MIN_VALUE ? Option.<Float>none() : Option.some(f - 1F);
    }
  }, Option.some(Float.MAX_VALUE), Option.some(Float.MIN_VALUE), Ord.floatOrd);

  /**
   * An enumerator for <code>int</code>.
   */
  public static final Enumerator<Integer> intEnumerator = enumerator(new F<Integer, Option<Integer>>() {
    public Option<Integer> f(final Integer i) {
      return i == Integer.MAX_VALUE ? Option.<Integer>none() : Option.some(i + 1);
    }
  }, new F<Integer, Option<Integer>>() {
    public Option<Integer> f(final Integer i) {
      return i == Integer.MIN_VALUE ? Option.<Integer>none() : Option.some(i - 1);
    }
  }, Option.some(Integer.MAX_VALUE), Option.some(Integer.MIN_VALUE), Ord.intOrd);

  /**
   * An enumerator for <code>BigInteger</code>.
   */
  public static final Enumerator<BigInteger> bigintEnumerator = enumerator(new F<BigInteger, Option<BigInteger>>() {
    public Option<BigInteger> f(final BigInteger i) {
      return Option.some(i.add(BigInteger.ONE));
    }
  }, new F<BigInteger, Option<BigInteger>>() {
    public Option<BigInteger> f(final BigInteger i) {
      return Option.some(i.subtract(BigInteger.ONE));
    }
  }, Option.<BigInteger>none(), Option.<BigInteger>none(), Ord.bigintOrd, Function.curry(
      new F2<BigInteger, Long, Option<BigInteger>>() {
        public Option<BigInteger> f(final BigInteger i, final Long l) {
          return Option.some(i.add(BigInteger.valueOf(l)));
        }
      }));

  /**
   * An enumerator for <code>BigDecimal</code>.
   */
  public static final Enumerator<BigDecimal> bigdecimalEnumerator = enumerator(new F<BigDecimal, Option<BigDecimal>>() {
    public Option<BigDecimal> f(final BigDecimal i) {
      return Option.some(i.add(BigDecimal.ONE));
    }
  }, new F<BigDecimal, Option<BigDecimal>>() {
    public Option<BigDecimal> f(final BigDecimal i) {
      return Option.some(i.subtract(BigDecimal.ONE));
    }
  }, Option.<BigDecimal>none(), Option.<BigDecimal>none(), Ord.bigdecimalOrd, Function.curry(
      new F2<BigDecimal, Long, Option<BigDecimal>>() {
        public Option<BigDecimal> f(final BigDecimal d, final Long l) {
          return Option.some(d.add(BigDecimal.valueOf(l)));
        }
      }));

  /**
   * An enumerator for <code>long</code>.
   */
  public static final Enumerator<Long> longEnumerator = enumerator(new F<Long, Option<Long>>() {
    public Option<Long> f(final Long i) {
      return i == Long.MAX_VALUE ? Option.<Long>none() : Option.some(i + 1L);
    }
  }, new F<Long, Option<Long>>() {
    public Option<Long> f(final Long i) {
      return i == Long.MIN_VALUE ? Option.<Long>none() : Option.some(i - 1L);
    }
  }, Option.some(Long.MAX_VALUE), Option.some(Long.MIN_VALUE), Ord.longOrd);

  /**
   * An enumerator for <code>short</code>.
   */
  public static final Enumerator<Short> shortEnumerator = enumerator(new F<Short, Option<Short>>() {
    public Option<Short> f(final Short i) {
      return i == Short.MAX_VALUE ? Option.<Short>none() : Option.some((short) (i + 1));
    }
  }, new F<Short, Option<Short>>() {
    public Option<Short> f(final Short i) {
      return i == Short.MIN_VALUE ? Option.<Short>none() : Option.some((short) (i - 1));
    }
  }, Option.some(Short.MAX_VALUE), Option.some(Short.MIN_VALUE), Ord.shortOrd);

  /**
   * An enumerator for <code>Ordering</code>.
   */
  public static final Enumerator<Ordering> orderingEnumerator = enumerator(new F<Ordering, Option<Ordering>>() {
    public Option<Ordering> f(final Ordering o) {
      return o == Ordering.LT ? Option.some(Ordering.EQ) : o == Ordering.EQ ? Option.some(Ordering.GT) : Option.<Ordering>none();
    }
  }, new F<Ordering, Option<Ordering>>() {
    public Option<Ordering> f(final Ordering o) {
      return o == Ordering.GT ? Option.some(Ordering.EQ) : o == Ordering.EQ ? Option.some(Ordering.LT) : Option.<Ordering>none();
    }
  }, Option.some(Ordering.GT), Option.some(Ordering.LT), Ord.orderingOrd);

  /**
   * An enumerator for <code>Natural</code>
   */
  public static final Enumerator<Natural> naturalEnumerator = enumerator(new F<Natural, Option<Natural>>() {
    public Option<Natural> f(final Natural n) {
      return Option.some(n.succ());
    }
  }, new F<Natural, Option<Natural>>() {
    public Option<Natural> f(final Natural n) {
      return n.pred();
    }
  }, Option.<Natural>none(), Option.some(Natural.ZERO), Ord.naturalOrd, Function.curry(new F2<Natural, Long, Option<Natural>>() {
    public Option<Natural> f(final Natural n, final Long l) {
      return Option.some(n).apply(Natural.natural(l).map(Function.curry(new F2<Natural, Natural, Natural>() {
        public Natural f(final Natural n1, final Natural n2) {
          return n1.add(n2);
        }
      })));
    }
  }));

}
