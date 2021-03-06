package flj;

/**
 * A product-8.
 *
 * @version 2.20<br>
 *          <ul>
 *          <li>$LastChangedRevision: 5 $</li>
 *          <li>$LastChangedDate: 2008-12-06 16:49:43 +1000 (Sat, 06 Dec 2008) $</li>
 *          </ul>
 */
@SuppressWarnings({"UnnecessaryFullyQualifiedName"})
public abstract class P8<A, B, C, D, E, Z, G, H> {
  /**
   * Access the first element of the product.
   *
   * @return The first element of the product.
   */
  public abstract A _1();

  /**
   * Access the second element of the product.
   *
   * @return The second element of the product.
   */
  public abstract B _2();

  /**
   * Access the third element of the product.
   *
   * @return The third element of the product.
   */
  public abstract C _3();

  /**
   * Access the fourth element of the product.
   *
   * @return The fourth element of the product.
   */
  public abstract D _4();

  /**
   * Access the fifth element of the product.
   *
   * @return The fifth element of the product.
   */
  public abstract E _5();

  /**
   * Access the sixth element of the product.
   *
   * @return The sixth element of the product.
   */
  public abstract Z _6();

  /**
   * Access the seventh element of the product.
   *
   * @return The seventh element of the product.
   */
  public abstract G _7();

  /**
   * Access the eighth element of the product.
   *
   * @return The eighth element of the product.
   */
  public abstract H _8();

  /**
   * Map the first element of the product.
   *
   * @param f The function to map with.
   * @return A product with the given function applied.
   */
  public <X> P8<X, B, C, D, E, Z, G, H> map1(final F<A, X> f) {
    return new P8<X, B, C, D, E, Z, G, H>() {
      public X _1() {
        return f.f(P8.this._1());
      }

      public B _2() {
        return P8.this._2();
      }

      public C _3() {
        return P8.this._3();
      }

      public D _4() {
        return P8.this._4();
      }

      public E _5() {
        return P8.this._5();
      }

      public Z _6() {
        return P8.this._6();
      }

      public G _7() {
        return P8.this._7();
      }

      public H _8() {
        return P8.this._8();
      }
    };
  }

  /**
   * Map the second element of the product.
   *
   * @param f The function to map with.
   * @return A product with the given function applied.
   */
  public <X> P8<A, X, C, D, E, Z, G, H> map2(final F<B, X> f) {
    return new P8<A, X, C, D, E, Z, G, H>() {
      public A _1() {
        return P8.this._1();
      }

      public X _2() {
        return f.f(P8.this._2());
      }

      public C _3() {
        return P8.this._3();
      }

      public D _4() {
        return P8.this._4();
      }

      public E _5() {
        return P8.this._5();
      }

      public Z _6() {
        return P8.this._6();
      }

      public G _7() {
        return P8.this._7();
      }

      public H _8() {
        return P8.this._8();
      }
    };
  }

  /**
   * Map the third element of the product.
   *
   * @param f The function to map with.
   * @return A product with the given function applied.
   */
  public <X> P8<A, B, X, D, E, Z, G, H> map3(final F<C, X> f) {
    return new P8<A, B, X, D, E, Z, G, H>() {
      public A _1() {
        return P8.this._1();
      }

      public B _2() {
        return P8.this._2();
      }

      public X _3() {
        return f.f(P8.this._3());
      }

      public D _4() {
        return P8.this._4();
      }

      public E _5() {
        return P8.this._5();
      }

      public Z _6() {
        return P8.this._6();
      }

      public G _7() {
        return P8.this._7();
      }

      public H _8() {
        return P8.this._8();
      }
    };
  }

  /**
   * Map the fourth element of the product.
   *
   * @param f The function to map with.
   * @return A product with the given function applied.
   */
  public <X> P8<A, B, C, X, E, Z, G, H> map4(final F<D, X> f) {
    return new P8<A, B, C, X, E, Z, G, H>() {
      public A _1() {
        return P8.this._1();
      }

      public B _2() {
        return P8.this._2();
      }

      public C _3() {
        return P8.this._3();
      }

      public X _4() {
        return f.f(P8.this._4());
      }

      public E _5() {
        return P8.this._5();
      }

      public Z _6() {
        return P8.this._6();
      }

      public G _7() {
        return P8.this._7();
      }

      public H _8() {
        return P8.this._8();
      }
    };
  }

  /**
   * Map the fifth element of the product.
   *
   * @param f The function to map with.
   * @return A product with the given function applied.
   */
  public <X> P8<A, B, C, D, X, Z, G, H> map5(final F<E, X> f) {
    return new P8<A, B, C, D, X, Z, G, H>() {
      public A _1() {
        return P8.this._1();
      }

      public B _2() {
        return P8.this._2();
      }

      public C _3() {
        return P8.this._3();
      }

      public D _4() {
        return P8.this._4();
      }

      public X _5() {
        return f.f(P8.this._5());
      }

      public Z _6() {
        return P8.this._6();
      }

      public G _7() {
        return P8.this._7();
      }

      public H _8() {
        return P8.this._8();
      }
    };
  }

  /**
   * Map the sixth element of the product.
   *
   * @param f The function to map with.
   * @return A product with the given function applied.
   */
  public <X> P8<A, B, C, D, E, X, G, H> map6(final F<Z, X> f) {
    return new P8<A, B, C, D, E, X, G, H>() {
      public A _1() {
        return P8.this._1();
      }

      public B _2() {
        return P8.this._2();
      }

      public C _3() {
        return P8.this._3();
      }

      public D _4() {
        return P8.this._4();
      }

      public E _5() {
        return P8.this._5();
      }

      public X _6() {
        return f.f(P8.this._6());
      }

      public G _7() {
        return P8.this._7();
      }

      public H _8() {
        return P8.this._8();
      }
    };
  }

  /**
   * Map the seventh element of the product.
   *
   * @param f The function to map with.
   * @return A product with the given function applied.
   */
  public <X> P8<A, B, C, D, E, Z, X, H> map7(final F<G, X> f) {
    return new P8<A, B, C, D, E, Z, X, H>() {
      public A _1() {
        return P8.this._1();
      }

      public B _2() {
        return P8.this._2();
      }

      public C _3() {
        return P8.this._3();
      }

      public D _4() {
        return P8.this._4();
      }

      public E _5() {
        return P8.this._5();
      }

      public Z _6() {
        return P8.this._6();
      }

      public X _7() {
        return f.f(P8.this._7());
      }

      public H _8() {
        return P8.this._8();
      }
    };
  }

  /**
   * Map the eighth element of the product.
   *
   * @param f The function to map with.
   * @return A product with the given function applied.
   */
  public <X> P8<A, B, C, D, E, Z, G, X> map8(final F<H, X> f) {
    return new P8<A, B, C, D, E, Z, G, X>() {
      public A _1() {
        return P8.this._1();
      }

      public B _2() {
        return P8.this._2();
      }

      public C _3() {
        return P8.this._3();
      }

      public D _4() {
        return P8.this._4();
      }

      public E _5() {
        return P8.this._5();
      }

      public Z _6() {
        return P8.this._6();
      }

      public G _7() {
        return P8.this._7();
      }

      public X _8() {
        return f.f(P8.this._8());
      }
    };
  }

  /**
   * Returns a function that returns the first element of a product.
   *
   * @return A function that returns the first element of a product.
   */
  public static <A, B, C, D, E, F$, G, H> F<P8<A, B, C, D, E, F$, G, H>, A> __1() {
    return new F<P8<A, B, C, D, E, F$, G, H>, A>() {
      public A f(final P8<A, B, C, D, E, F$, G, H> p) {
        return p._1();
      }
    };
  }

  /**
   * Returns a function that returns the second element of a product.
   *
   * @return A function that returns the second element of a product.
   */
  public static <A, B, C, D, E, F$, G, H> F<P8<A, B, C, D, E, F$, G, H>, B> __2() {
    return new F<P8<A, B, C, D, E, F$, G, H>, B>() {
      public B f(final P8<A, B, C, D, E, F$, G, H> p) {
        return p._2();
      }
    };
  }

  /**
   * Returns a function that returns the third element of a product.
   *
   * @return A function that returns the third element of a product.
   */
  public static <A, B, C, D, E, F$, G, H> F<P8<A, B, C, D, E, F$, G, H>, C> __3() {
    return new F<P8<A, B, C, D, E, F$, G, H>, C>() {
      public C f(final P8<A, B, C, D, E, F$, G, H> p) {
        return p._3();
      }
    };
  }

  /**
   * Returns a function that returns the fourth element of a product.
   *
   * @return A function that returns the fourth element of a product.
   */
  public static <A, B, C, D, E, F$, G, H> F<P8<A, B, C, D, E, F$, G, H>, D> __4() {
    return new F<P8<A, B, C, D, E, F$, G, H>, D>() {
      public D f(final P8<A, B, C, D, E, F$, G, H> p) {
        return p._4();
      }
    };
  }

  /**
   * Returns a function that returns the fifth element of a product.
   *
   * @return A function that returns the fifth element of a product.
   */
  public static <A, B, C, D, E, F$, G, H> F<P8<A, B, C, D, E, F$, G, H>, E> __5() {
    return new F<P8<A, B, C, D, E, F$, G, H>, E>() {
      public E f(final P8<A, B, C, D, E, F$, G, H> p) {
        return p._5();
      }
    };
  }

  /**
   * Returns a function that returns the sixth element of a product.
   *
   * @return A function that returns the sixth element of a product.
   */
  public static <A, B, C, D, E, F$, G, H> F<P8<A, B, C, D, E, F$, G, H>, F$> __6() {
    return new F<P8<A, B, C, D, E, F$, G, H>, F$>() {
      public F$ f(final P8<A, B, C, D, E, F$, G, H> p) {
        return p._6();
      }
    };
  }

  /**
   * Returns a function that returns the seventh element of a product.
   *
   * @return A function that returns the seventh element of a product.
   */
  public static <A, B, C, D, E, F$, G, H> F<P8<A, B, C, D, E, F$, G, H>, G> __7() {
    return new F<P8<A, B, C, D, E, F$, G, H>, G>() {
      public G f(final P8<A, B, C, D, E, F$, G, H> p) {
        return p._7();
      }
    };
  }

  /**
   * Returns a function that returns the eighth element of a product.
   *
   * @return A function that returns the eighth element of a product.
   */
  public static <A, B, C, D, E, F$, G, H> F<P8<A, B, C, D, E, F$, G, H>, H> __8() {
    return new F<P8<A, B, C, D, E, F$, G, H>, H>() {
      public H f(final P8<A, B, C, D, E, F$, G, H> p) {
        return p._8();
      }
    };
  }
}
