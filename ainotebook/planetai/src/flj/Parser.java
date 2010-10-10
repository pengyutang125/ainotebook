/**
    $LastChangedRevision: 108 $
    $LastChangedDate: 2009-03-25 16:26:56 +1000 (Wed, 25 Mar 2009) $
    $LastChangedBy: tonymorris $
    
    Functional Java included in Default Package
    Functional Java: 2.20
    
    Copyright (c) 2008-2009, Tony Morris, Runar Bjarnason, Tom Adams, Brad Clow, Ricky Clarkson
    All rights reserved.
    
    Redistribution and use in source and binary forms, with or without
    modification, are permitted provided that the following conditions
    are met:
    1. Redistributions of source code must retain the above copyright
       notice, this list of conditions and the following disclaimer.
    2. Redistributions in binary form must reproduce the above copyright
       notice, this list of conditions and the following disclaimer in the
       documentation and/or other materials provided with the distribution.
    3. The name of the author may not be used to endorse or promote products
       derived from this software without specific prior written permission.
    
    THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR
    IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
    OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
    IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT,
    INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
    NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
    DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
    THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
    (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
    THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

 
 */
package flj;


/**
 * A parser is a function that takes some input (I) and produces either an error (E) or a parse ParserResult (A) and the
 * remainder of the input.
 *
 * @version 2.20<br>
 *          <ul>
 *          <li>$LastChangedRevision: 122 $</li>
 *          <li>$LastChangedDate: 2009-04-25 08:24:38 +1000 (Sat, 25 Apr 2009) $</li>
 *          </ul>
 */
public class Parser<I, A, E> {
  private final F<I, Validation<E, ParserResult<I, A>>> f;

  private Parser(final F<I, Validation<E, ParserResult<I, A>>> f) {
    this.f = f;
  }

  /**
   * Parses the input to produce a ParserResult or error.
   *
   * @param i The input to parse.
   * @return A parse ParserResult with the remaining input or an error.
   */
  public Validation<E, ParserResult<I, A>> parse(final I i) {
    return f.f(i);
  }

  /**
   * Maps the parse input type through an invariant functor.
   *
   * @param f The function to covariant map.
   * @param g The function to contra-variant map.
   * @return A parser with the new input type.
   */
  public <Z> Parser<Z, A, E> xmap(final F<I, Z> f, final F<Z, I> g) {
    return parser(new F<Z, Validation<E, ParserResult<Z, A>>>() {
      public Validation<E, ParserResult<Z, A>> f(final Z z) {
        return parse(g.f(z)).map(new F<ParserResult<I, A>, ParserResult<Z, A>>() {
          public ParserResult<Z, A> f(final ParserResult<I, A> r) {
            return r.mapRest(f);
          }
        });
      }
    });
  }

  /**
   * Maps the given ParserResult type across this parser.
   *
   * @param f The function to map.
   * @return A parser with the new ParserResult type.
   */
  public <B> Parser<I, B, E> map(final F<A, B> f) {
    return parser(new F<I, Validation<E, ParserResult<I, B>>>() {
      public Validation<E, ParserResult<I, B>> f(final I i) {
        return parse(i).map(new F<ParserResult<I, A>, ParserResult<I, B>>() {
          public ParserResult<I, B> f(final ParserResult<I, A> r) {
            return r.mapValue(f);
          }
        });
      }
    });
  }

  /**
   * Returns a parser that fails with the given error if the ParserResult value does not meet the given predicate.
   *
   * @param f The predicate to filter on.
   * @param e The error to in the event that the predicate is not met.
   * @return A parser that fails with the given error if the ParserResult value does not meet the given predicate.
   */
  public Parser<I, A, E> filter(final F<A, Boolean> f, final E e) {
    return parser(new F<I, Validation<E, ParserResult<I, A>>>() {
      public Validation<E, ParserResult<I, A>> f(final I i) {
        return parse(i).bind(new F<ParserResult<I, A>, Validation<E, ParserResult<I, A>>>() {
          public Validation<E, ParserResult<I, A>> f(final ParserResult<I, A> r) {
            final A v = r.value();
            return f.f(v) ?
                Validation.<E, ParserResult<I, A>>success(ParserResult.result(r.rest(), v)) :
                Validation.<E, ParserResult<I, A>>fail(e);
          }
        });
      }
    });
  }

  /**
   * Binds the given function across the parser with a final join.
   *
   * @param f The function to apply to the element of this parser.
   * @return A new parser after performing the map, then final join.
   */
  public <B> Parser<I, B, E> bind(final F<A, Parser<I, B, E>> f) {
    return parser(new F<I, Validation<E, ParserResult<I, B>>>() {
      public Validation<E, ParserResult<I, B>> f(final I i) {
        return parse(i).bind(new F<ParserResult<I, A>, Validation<E, ParserResult<I, B>>>() {
          public Validation<E, ParserResult<I, B>> f(final ParserResult<I, A> r) {
            return f.f(r.value()).parse(r.rest());
          }
        });
      }
    });
  }

  /**
   * Binds the given function across the parsers with a final join.
   *
   * @param f  The function to apply to the element of the parsers.
   * @param pb A given parser to bind the given function with.
   * @return A new parser after performing the map, then final join.
   */
  public <B, C> Parser<I, C, E> bind(final Parser<I, B, E> pb, final F<A, F<B, C>> f) {
    return pb.apply(map(f));
  }

  /**
   * Binds the given function across the parsers with a final join.
   *
   * @param f  The function to apply to the element of the parsers.
   * @param pb A given parser to bind the given function with.
   * @param pc A given parser to bind the given function with.
   * @return A new parser after performing the map, then final join.
   */
  public <B, C, D> Parser<I, D, E> bind(final Parser<I, B, E> pb, final Parser<I, C, E> pc,
                                        final F<A, F<B, F<C, D>>> f) {
    return pc.apply(bind(pb, f));
  }

  /**
   * Binds the given function across the parsers with a final join.
   *
   * @param f  The function to apply to the element of the parsers.
   * @param pb A given parser to bind the given function with.
   * @param pc A given parser to bind the given function with.
   * @param pd A given parser to bind the given function with.
   * @return A new parser after performing the map, then final join.
   */
  public <B, C, D, E$> Parser<I, E$, E> bind(final Parser<I, B, E> pb, final Parser<I, C, E> pc,
                                             final Parser<I, D, E> pd, final F<A, F<B, F<C, F<D, E$>>>> f) {
    return pd.apply(bind(pb, pc, f));
  }

  /**
   * Binds the given function across the parsers with a final join.
   *
   * @param f  The function to apply to the element of the parsers.
   * @param pb A given parser to bind the given function with.
   * @param pc A given parser to bind the given function with.
   * @param pd A given parser to bind the given function with.
   * @param pe A given parser to bind the given function with.
   * @return A new parser after performing the map, then final join.
   */
  public <B, C, D, E$, F$> Parser<I, F$, E> bind(final Parser<I, B, E> pb, final Parser<I, C, E> pc,
                                                 final Parser<I, D, E> pd, final Parser<I, E$, E> pe,
                                                 final F<A, F<B, F<C, F<D, F<E$, F$>>>>> f) {
    return pe.apply(bind(pb, pc, pd, f));
  }

  /**
   * Binds the given function across the parsers with a final join.
   *
   * @param f  The function to apply to the element of the parsers.
   * @param pb A given parser to bind the given function with.
   * @param pc A given parser to bind the given function with.
   * @param pd A given parser to bind the given function with.
   * @param pe A given parser to bind the given function with.
   * @param pf A given parser to bind the given function with.
   * @return A new parser after performing the map, then final join.
   */
  public <B, C, D, E$, F$, G> Parser<I, G, E> bind(final Parser<I, B, E> pb, final Parser<I, C, E> pc,
                                                   final Parser<I, D, E> pd, final Parser<I, E$, E> pe,
                                                   final Parser<I, F$, E> pf,
                                                   final F<A, F<B, F<C, F<D, F<E$, F<F$, G>>>>>> f) {
    return pf.apply(bind(pb, pc, pd, pe, f));
  }

  /**
   * Binds the given function across the parsers with a final join.
   *
   * @param f  The function to apply to the element of the parsers.
   * @param pb A given parser to bind the given function with.
   * @param pc A given parser to bind the given function with.
   * @param pd A given parser to bind the given function with.
   * @param pe A given parser to bind the given function with.
   * @param pf A given parser to bind the given function with.
   * @param pg A given parser to bind the given function with.
   * @return A new parser after performing the map, then final join.
   */
  public <B, C, D, E$, F$, G, H> Parser<I, H, E> bind(final Parser<I, B, E> pb, final Parser<I, C, E> pc,
                                                      final Parser<I, D, E> pd, final Parser<I, E$, E> pe,
                                                      final Parser<I, F$, E> pf, final Parser<I, G, E> pg,
                                                      final F<A, F<B, F<C, F<D, F<E$, F<F$, F<G, H>>>>>>> f) {
    return pg.apply(bind(pb, pc, pd, pe, pf, f));
  }

  /**
   * Binds the given function across the parsers with a final join.
   *
   * @param f  The function to apply to the element of the parsers.
   * @param pb A given parser to bind the given function with.
   * @param pc A given parser to bind the given function with.
   * @param pd A given parser to bind the given function with.
   * @param pe A given parser to bind the given function with.
   * @param pf A given parser to bind the given function with.
   * @param pg A given parser to bind the given function with.
   * @param ph A given parser to bind the given function with.
   * @return A new parser after performing the map, then final join.
   */
  public <B, C, D, E$, F$, G, H, I$> Parser<I, I$, E> bind(final Parser<I, B, E> pb, final Parser<I, C, E> pc,
                                                           final Parser<I, D, E> pd, final Parser<I, E$, E> pe,
                                                           final Parser<I, F$, E> pf, final Parser<I, G, E> pg,
                                                           final Parser<I, H, E> ph,
                                                           final F<A, F<B, F<C, F<D, F<E$, F<F$, F<G, F<H, I$>>>>>>>> f) {
    return ph.apply(bind(pb, pc, pd, pe, pf, pg, f));
  }

  /**
   * Binds anonymously, ignoring the ParserResult value.
   *
   * @param p The parser to bind with.
   * @return A parser after binding anonymously.
   */
  public <B> Parser<I, B, E> sequence(final Parser<I, B, E> p) {
    return bind(new F<A, Parser<I, B, E>>() {
      public Parser<I, B, E> f(final A a) {
        return p;
      }
    });
  }

  /**
   * Performs function application within a parser.
   *
   * @param p The parser returning a function value.
   * @return A new parser after function application.
   */
  public <B> Parser<I, B, E> apply(final Parser<I, F<A, B>, E> p) {
    return p.bind(new F<F<A, B>, Parser<I, B, E>>() {
      public Parser<I, B, E> f(final F<A, B> f) {
        return map(f);
      }
    });
  }

  /**
   * Returns a parser that tries this parser and if it fails, then tries the given parser.
   *
   * @param alt The parser to try if this parser fails.
   * @return A parser that tries this parser and if it fails, then tries the given parser.
   */
  public Parser<I, A, E> or(final P1<Parser<I, A, E>> alt) {
    return parser(new F<I, Validation<E, ParserResult<I, A>>>() {
      public Validation<E, ParserResult<I, A>> f(final I i) {
        return parse(i).f().sequence(alt._1().parse(i));
      }
    });
  }

  /**
   * Returns a parser that tries this parser and if it fails, then tries the given parser.
   *
   * @param alt The parser to try if this parser fails.
   * @return A parser that tries this parser and if it fails, then tries the given parser.
   */
  public Parser<I, A, E> or(final Parser<I, A, E> alt) {
    return or(P.p(alt));
  }

  /**
   * Returns a parser that tries this parser and if it fails, then tries the given parser. If both parsers fail, then
   * append their errors with the given semigroup.
   *
   * @param alt The parser to try if this parser fails.
   * @param s   The semigroup to append error messages if both parsers fail.
   * @return A parser that tries this parser and if it fails, then tries the given parser.
   */
  public Parser<I, A, E> or(final P1<Parser<I, A, E>> alt, final Semigroup<E> s) {
    return parser(new F<I, Validation<E, ParserResult<I, A>>>() {
      public Validation<E, ParserResult<I, A>> f(final I i) {
        return parse(i).f().bind(new F<E, Validation<E, ParserResult<I, A>>>() {
          public Validation<E, ParserResult<I, A>> f(final E e) {
            return alt._1().parse(i).f().map(s.sum(e));
          }
        });
      }
    });
  }

  /**
   * Returns a parser that tries this parser and if it fails, then tries the given parser. If both parsers fail, then
   * append their errors with the given semigroup.
   *
   * @param alt The parser to try if this parser fails.
   * @param s   The semigroup to append error messages if both parsers fail.
   * @return A parser that tries this parser and if it fails, then tries the given parser.
   */
  public Parser<I, A, E> or(final Parser<I, A, E> alt, final Semigroup<E> s) {
    return or(P.p(alt), s);
  }

  /**
   * Returns a parser that negates this parser. If this parser succeeds, then the returned parser fails and vice versa.
   *
   * @param e The error message to fail with if this parser succeeds.
   * @return A parser that negates this parser.
   */
  public Parser<I, Unit, E> not(final P1<E> e) {
    return parser(new F<I, Validation<E, ParserResult<I, Unit>>>() {
      public Validation<E, ParserResult<I, Unit>> f(final I i) {
        return parse(i).isFail() ?
            Validation.<E, ParserResult<I, Unit>>success(ParserResult.result(i, Unit.unit())) :
            Validation.<E, ParserResult<I, Unit>>fail(e._1());
      }
    });
  }

  /**
   * Returns a parser that negates this parser. If this parser succeeds, then the returned parser fails and vice versa.
   *
   * @param e The error message to fail with if this parser succeeds.
   * @return A parser that negates this parser.
   */
  public Parser<I, Unit, E> not(final E e) {
    return not(P.p(e));
  }

  /**
   * Returns a parser that repeats application of this parser zero or many times.
   *
   * @return A parser that repeats application of this parser zero or many times.
   */
  public Parser<I, Stream<A>, E> repeat() {
    return repeat1().or(new P1<Parser<I, Stream<A>, E>>() {
      public Parser<I, Stream<A>, E> _1() {
        return value(Stream.<A>nil());
      }
    });
  }

  /**
   * Returns a parser that repeats application of this parser one or many times.
   *
   * @return A parser that repeats application of this parser one or many times.
   */
  public Parser<I, Stream<A>, E> repeat1() {
    return bind(repeat(), Stream.<A>cons_());
  }

  /**
   * Maps the given function across this parser's error.
   *
   * @param f The function to map this parser's error with.
   * @return A new parser with a new error type.
   */
  public <K> Parser<I, A, K> mapError(final F<E, K> f) {
    return parser(new F<I, Validation<K, ParserResult<I, A>>>() {
      public Validation<K, ParserResult<I, A>> f(final I i) {
        return Parser.this.f.f(i).f().map(f);
      }
    });
  }

  /**
   * Returns a parser that computes using the given function.
   *
   * @param f The function to construct the parser with.
   * @return A parser that computes using the given function.
   */
  public static <I, A, E> Parser<I, A, E> parser(final F<I, Validation<E, ParserResult<I, A>>> f) {
    return new Parser<I, A, E>(f);
  }

  /**
   * Constructs a parser that always returns the given value. The unital for a parser.
   *
   * @param a The value to consistently return from a parser.
   * @return A parser that always returns the given value.
   */
  public static <I, A, E> Parser<I, A, E> value(final A a) {
    return parser(new F<I, Validation<E, ParserResult<I, A>>>() {
      public Validation<E, ParserResult<I, A>> f(final I i) {
        return Validation.success(ParserResult.result(i, a));
      }
    });
  }

  /**
   * Returns a parser that always fails with the given error.
   *
   * @param e The error to fail with.
   * @return A parser that always fails with the given error.
   */
  public static <I, A, E> Parser<I, A, E> fail(final E e) {
    return parser(new F<I, Validation<E, ParserResult<I, A>>>() {
      public Validation<E, ParserResult<I, A>> f(final I i) {
        return Validation.fail(e);
      }
    });
  }

  /**
   * Sequence the list of parsers through {@link #bind}.
   *
   * @param ps The parsers to sequence.
   * @return A parser after sequencing.
   */
  public static <I, A, E> Parser<I, List<A>, E> sequence(final List<Parser<I, A, E>> ps) {
    return ps.isEmpty() ?
        Parser.<I, List<A>, E>value(List.<A>nil()) :
        ps.head().bind(new F<A, Parser<I, List<A>, E>>() {
          public Parser<I, List<A>, E> f(final A a) {
            return sequence(ps.tail()).map(List.cons_(a));
          }
        });
  }

  /**
   * Parsers that accept {@link Stream} input.
   */
  public static final class StreamParser {
    private StreamParser() {

    }

    /**
     * Returns a parser that produces an element from the stream if it is available and fails otherwise.
     *
     * @param e The error to fail with if no element is available.
     * @return A parser that produces an element from the stream if it is available and fails otherwise.
     */
    public static <I, E> Parser<Stream<I>, I, E> element(final P1<E> e) {
      return parser(new F<Stream<I>, Validation<E, ParserResult<Stream<I>, I>>>() {
        public Validation<E, ParserResult<Stream<I>, I>> f(final Stream<I> is) {
          return is.isEmpty() ?
              Validation.<E, ParserResult<Stream<I>, I>>fail(e._1()) :
              Validation.<E, ParserResult<Stream<I>, I>>success(ParserResult.result(is.tail()._1(), is.head()));
        }
      });
    }

    /**
     * Returns a parser that produces an element from the stream if it is available and fails otherwise.
     *
     * @param e The error to fail with if no element is available.
     * @return A parser that produces an element from the stream if it is available and fails otherwise.
     */
    public static <I, E> Parser<Stream<I>, I, E> element(final E e) {
      return element(P.p(e));
    }

    /**
     * Returns a parser that produces an element from the stream that satisfies the given predicate, or fails.
     *
     * @param missing The error if no element is available.
     * @param sat     The error if the element does not satisfy the predicate.
     * @param f       The predicate that the element should satisfy.
     * @return A parser that produces an element from the stream that satisfies the given predicate, or fails.
     */
    public static <I, E> Parser<Stream<I>, I, E> satisfy(final P1<E> missing, final F<I, E> sat,
                                                         final F<I, Boolean> f) {
      return StreamParser.<I, E>element(missing).bind(new F<I, Parser<Stream<I>, I, E>>() {
        public Parser<Stream<I>, I, E> f(final I x) {
          return f.f(x) ?
              Parser.<Stream<I>, I, E>value(x) :
              Parser.<Stream<I>, I, E>fail(sat.f(x));
        }
      });
    }

    /**
     * Returns a parser that produces an element from the stream that satisfies the given predicate, or fails.
     *
     * @param missing The error if no element is available.
     * @param sat     The error if the element does not satisfy the predicate.
     * @param f       The predicate that the element should satisfy.
     * @return A parser that produces an element from the stream that satisfies the given predicate, or fails.
     */
    public static <I, E> Parser<Stream<I>, I, E> satisfy(final E missing, final F<I, E> sat, final F<I, Boolean> f) {
      return satisfy(P.p(missing), sat, f);
    }
  }

  /**
   * Parsers that accept {@link Stream Stream&lt;Character&gt;} input.
   */
  public static final class CharsParser {
    private CharsParser() {

    }

    /**
     * Returns a parser that produces a character if one is available or fails with the given error.
     *
     * @param e The error to fail with if a character is unavailable.
     * @return A parser that produces a character if one is available or fails with the given error.
     */
    public static <E> Parser<Stream<Character>, Character, E> character(final P1<E> e) {
      return StreamParser.element(e);
    }

    /**
     * Returns a parser that produces a character if one is available or fails with the given error.
     *
     * @param e The error to fail with if a character is unavailable.
     * @return A parser that produces a character if one is available or fails with the given error.
     */
    public static <E> Parser<Stream<Character>, Character, E> character(final E e) {
      return character(P.p(e));
    }

    /**
     * Returns a parser that produces the given character or fails otherwise.
     *
     * @param missing The error if no character is available.
     * @param sat     The error if the produced character is not the one given.
     * @param c       The character to produce in the parser.
     * @return A parser that produces the given character or fails otherwise.
     */
    public static <E> Parser<Stream<Character>, Character, E> character(final P1<E> missing, final F<Character, E> sat,
                                                                        final char c) {
      return StreamParser.satisfy(missing, sat, new F<Character, Boolean>() {
        public Boolean f(final Character x) {
          return x == c;
        }
      });
    }

    /**
     * Returns a parser that produces the given character or fails otherwise.
     *
     * @param missing The error if no character is available.
     * @param sat     The error if the produced character is not the one given.
     * @param c       The character to produce in the parser.
     * @return A parser that produces the given character or fails otherwise.
     */
    public static <E> Parser<Stream<Character>, Character, E> character(final E missing, final F<Character, E> sat,
                                                                        final char c) {
      return character(P.p(missing), sat, c);
    }

    /**
     * Returns a parser that produces the given number of characters, or fails with the given error.
     *
     * @param missing The error if the given number of characters is unavailable.
     * @param n       The number of characters to produce in the parse ParserResult.
     * @return A parser that produces the given number of characters, or fails with the given error.
     */
    public static <E> Parser<Stream<Character>, Stream<Character>, E> characters(final P1<E> missing, final int n) {
      return n <= 0 ?
          Parser.<Stream<Character>, Stream<Character>, E>value(Stream.<Character>nil()) :
          CharsParser.character(missing).bind(CharsParser.characters(missing, n - 1), Stream.<Character>cons_());
    }

    /**
     * Returns a parser that produces the given number of characters, or fails with the given error.
     *
     * @param missing The error if the given number of characters is unavailable.
     * @param n       The number of characters to produce in the parse ParserResult.
     * @return A parser that produces the given number of characters, or fails with the given error.
     */
    public static <E> Parser<Stream<Character>, Stream<Character>, E> characters(final E missing, final int n) {
      return characters(P.p(missing), n);
    }

    /**
     * Returns a parser that produces the given stream of characters or fails otherwise.
     *
     * @param missing The error if the producing stream could not supply more characters.
     * @param sat     The error if a character was produced that is not the given stream of characters.
     * @param cs      The stream of characters to produce.
     * @return A parser that produces the given stream of characters or fails otherwise.
     */
    public static <E> Parser<Stream<Character>, Stream<Character>, E> characters(final P1<E> missing,
                                                                                 final F<Character, E> sat,
                                                                                 final Stream<Character> cs) {
      return cs.isEmpty() ?
          Parser.<Stream<Character>, Stream<Character>, E>value(Stream.<Character>nil()) :
          character(missing, sat, cs.head()).bind(characters(missing, sat, cs.tail()._1()), Stream.<Character>cons_());
    }

    /**
     * Returns a parser that produces the given stream of characters or fails otherwise.
     *
     * @param missing The error if the producing stream could not supply more characters.
     * @param sat     The error if a character was produced that is not the given stream of characters.
     * @param cs      The stream of characters to produce.
     * @return A parser that produces the given stream of characters or fails otherwise.
     */
    public static <E> Parser<Stream<Character>, Stream<Character>, E> characters(final E missing,
                                                                                 final F<Character, E> sat,
                                                                                 final Stream<Character> cs) {
      return characters(P.p(missing), sat, cs);
    }

    /**
     * Returns a parser that produces the given string or fails otherwise.
     *
     * @param missing The error if the producing stream could not supply more characters.
     * @param sat     The error if a character was produced that is not the given string.
     * @param s       The string to produce.
     * @return A parser that produces the given string or fails otherwise.
     */
    public static <E> Parser<Stream<Character>, String, E> string(final P1<E> missing, final F<Character, E> sat,
                                                                  final String s) {
      return characters(missing, sat, List.fromString(s).toStream()).map(new F<Stream<Character>, String>() {
        public String f(final Stream<Character> cs) {
          return List.asString(cs.toList());
        }
      });
    }

    /**
     * Returns a parser that produces the given string or fails otherwise.
     *
     * @param missing The error if the producing stream could not supply more characters.
     * @param sat     The error if a character was produced that is not the given string.
     * @param s       The string to produce.
     * @return A parser that produces the given string or fails otherwise.
     */
    public static <E> Parser<Stream<Character>, String, E> string(final E missing, final F<Character, E> sat,
                                                                  final String s) {
      return string(P.p(missing), sat, s);
    }

    /**
     * Returns a parser that produces a digit (0 to 9).
     *
     * @param missing The error if there is no character on the stream to produce a digit with.
     * @param sat     The error if the produced character is not a digit.
     * @return A parser that produces a digit (0 to 9).
     */
    public static <E> Parser<Stream<Character>, Digit, E> digit(final P1<E> missing, final F<Character, E> sat) {
      return StreamParser.satisfy(missing, sat, new F<Character, Boolean>() {
        public Boolean f(final Character c) {
          return Character.isDigit(c);
        }
      }).map(new F<Character, Digit>() {
        public Digit f(final Character c) {
          return Digit.fromChar(c).some();
        }
      });
    }

    /**
     * Returns a parser that produces a digit (0 to 9).
     *
     * @param missing The error if there is no character on the stream to produce a digit with.
     * @param sat     The error if the produced character is not a digit.
     * @return A parser that produces a digit (0 to 9).
     */
    public static <E> Parser<Stream<Character>, Digit, E> digit(final E missing, final F<Character, E> sat) {
      return digit(P.p(missing), sat);
    }

    /**
     * Returns a parser that produces a lower-case character.
     *
     * @param missing The error if there is no character on the stream to produce a lower-case character with.
     * @param sat     The error if the produced character is not a lower-case character.
     * @return A parser that produces a lower-case character.
     * @see Character#isLowerCase(char)
     */
    public static <E> Parser<Stream<Character>, Character, E> lower(final P1<E> missing, final F<Character, E> sat) {
      return StreamParser.satisfy(missing, sat, new F<Character, Boolean>() {
        public Boolean f(final Character c) {
          return Character.isLowerCase(c);
        }
      });
    }

    /**
     * Returns a parser that produces a lower-case character.
     *
     * @param missing The error if there is no character on the stream to produce a lower-case character with.
     * @param sat     The error if the produced character is not a lower-case character.
     * @return A parser that produces a lower-case character.
     * @see Character#isLowerCase(char)
     */
    public static <E> Parser<Stream<Character>, Character, E> lower(final E missing, final F<Character, E> sat) {
      return lower(P.p(missing), sat);
    }

    /**
     * Returns a parser that produces a upper-case character.
     *
     * @param missing The error if there is no character on the stream to produce a upper-case character with.
     * @param sat     The error if the produced character is not a upper-case character.
     * @return A parser that produces a upper-case character.
     * @see Character#isUpperCase(char)
     */
    public static <E> Parser<Stream<Character>, Character, E> upper(final P1<E> missing, final F<Character, E> sat) {
      return StreamParser.satisfy(missing, sat, new F<Character, Boolean>() {
        public Boolean f(final Character c) {
          return Character.isUpperCase(c);
        }
      });
    }

    /**
     * Returns a parser that produces a upper-case character.
     *
     * @param missing The error if there is no character on the stream to produce a upper-case character with.
     * @param sat     The error if the produced character is not a upper-case character.
     * @return A parser that produces a upper-case character.
     * @see Character#isUpperCase(char)
     */
    public static <E> Parser<Stream<Character>, Character, E> upper(final E missing, final F<Character, E> sat) {
      return upper(P.p(missing), sat);
    }

    /**
     * Returns a parser that produces a defined character.
     *
     * @param missing The error if there is no character on the stream to produce a defined character with.
     * @param sat     The error if the produced character is not a defined character.
     * @return A parser that produces a defined character.
     * @see Character#isDefined(char)
     */
    public static <E> Parser<Stream<Character>, Character, E> defined(final P1<E> missing, final F<Character, E> sat) {
      return StreamParser.satisfy(missing, sat, new F<Character, Boolean>() {
        public Boolean f(final Character c) {
          return Character.isDefined(c);
        }
      });
    }

    /**
     * Returns a parser that produces a defined character.
     *
     * @param missing The error if there is no character on the stream to produce a defined character with.
     * @param sat     The error if the produced character is not a defined character.
     * @return A parser that produces a defined character.
     * @see Character#isDefined(char)
     */
    public static <E> Parser<Stream<Character>, Character, E> defined(final E missing, final F<Character, E> sat) {
      return defined(P.p(missing), sat);
    }

    /**
     * Returns a parser that produces a high-surrogate character.
     *
     * @param missing The error if there is no character on the stream to produce a high-surrogate character with.
     * @param sat     The error if the produced character is not a high-surrogate character.
     * @return A parser that produces a high-surrogate character.
     * @see Character#isHighSurrogate(char)
     */
    public static <E> Parser<Stream<Character>, Character, E> highSurrogate(final P1<E> missing,
                                                                            final F<Character, E> sat) {
      return StreamParser.satisfy(missing, sat, new F<Character, Boolean>() {
        public Boolean f(final Character c) {
          return Character.isHighSurrogate(c);
        }
      });
    }

    /**
     * Returns a parser that produces a high-surrogate character.
     *
     * @param missing The error if there is no character on the stream to produce a high-surrogate character with.
     * @param sat     The error if the produced character is not a high-surrogate character.
     * @return A parser that produces a high-surrogate character.
     * @see Character#isHighSurrogate(char)
     */
    public static <E> Parser<Stream<Character>, Character, E> highSurrogate(final E missing,
                                                                            final F<Character, E> sat) {
      return highSurrogate(P.p(missing), sat);
    }

    /**
     * Returns a parser that produces an identifier-ignorable character.
     *
     * @param missing The error if there is no character on the stream to produce an identifier-ignorable character with.
     * @param sat     The error if the produced character is not an identifier-ignorable character.
     * @return A parser that produces an identifier-ignorable character.
     * @see Character#isIdentifierIgnorable(char)
     */
    public static <E> Parser<Stream<Character>, Character, E> identifierIgnorable(final P1<E> missing,
                                                                                  final F<Character, E> sat) {
      return StreamParser.satisfy(missing, sat, new F<Character, Boolean>() {
        public Boolean f(final Character c) {
          return Character.isIdentifierIgnorable(c);
        }
      });
    }

    /**
     * Returns a parser that produces an identifier-ignorable character.
     *
     * @param missing The error if there is no character on the stream to produce an identifier-ignorable character with.
     * @param sat     The error if the produced character is not an identifier-ignorable character.
     * @return A parser that produces an identifier-ignorable character.
     * @see Character#isIdentifierIgnorable(char)
     */
    public static <E> Parser<Stream<Character>, Character, E> identifierIgnorable(final E missing,
                                                                                  final F<Character, E> sat) {
      return identifierIgnorable(P.p(missing), sat);
    }

    /**
     * Returns a parser that produces an ISO control character.
     *
     * @param missing The error if there is no character on the stream to produce an ISO control character with.
     * @param sat     The error if the produced character is not an ISO control character.
     * @return A parser that produces an ISO control character.
     * @see Character#isISOControl(char)
     */
    public static <E> Parser<Stream<Character>, Character, E> isoControl(final P1<E> missing,
                                                                         final F<Character, E> sat) {
      return StreamParser.satisfy(missing, sat, new F<Character, Boolean>() {
        public Boolean f(final Character c) {
          return Character.isISOControl(c);
        }
      });
    }

    /**
     * Returns a parser that produces an ISO control character.
     *
     * @param missing The error if there is no character on the stream to produce an ISO control character with.
     * @param sat     The error if the produced character is not an ISO control character.
     * @return A parser that produces an ISO control character.
     * @see Character#isISOControl(char)
     */
    public static <E> Parser<Stream<Character>, Character, E> isoControl(final E missing, final F<Character, E> sat) {
      return isoControl(P.p(missing), sat);
    }

    /**
     * Returns a parser that produces a Java identifier part character.
     *
     * @param missing The error if there is no character on the stream to produce a Java identifier part character with.
     * @param sat     The error if the produced character is not a Java identifier part character.
     * @return A parser that produces a Java identifier part character.
     * @see Character#isJavaIdentifierPart(char)
     */
    public static <E> Parser<Stream<Character>, Character, E> javaIdentifierPart(final P1<E> missing,
                                                                                 final F<Character, E> sat) {
      return StreamParser.satisfy(missing, sat, new F<Character, Boolean>() {
        public Boolean f(final Character c) {
          return Character.isJavaIdentifierPart(c);
        }
      });
    }

    /**
     * Returns a parser that produces a Java identifier part character.
     *
     * @param missing The error if there is no character on the stream to produce a Java identifier part character with.
     * @param sat     The error if the produced character is not a Java identifier part character.
     * @return A parser that produces a Java identifier part character.
     * @see Character#isJavaIdentifierPart(char)
     */
    public static <E> Parser<Stream<Character>, Character, E> javaIdentifierPart(final E missing,
                                                                                 final F<Character, E> sat) {
      return javaIdentifierPart(P.p(missing), sat);
    }

    /**
     * Returns a parser that produces a Java identifier start character.
     *
     * @param missing The error if there is no character on the stream to produce a Java identifier start character with.
     * @param sat     The error if the produced character is not a Java identifier start character.
     * @return A parser that produces a Java identifier start character.
     * @see Character#isJavaIdentifierStart(char)
     */
    public static <E> Parser<Stream<Character>, Character, E> javaIdentifierStart(final P1<E> missing,
                                                                                  final F<Character, E> sat) {
      return StreamParser.satisfy(missing, sat, new F<Character, Boolean>() {
        public Boolean f(final Character c) {
          return Character.isJavaIdentifierStart(c);
        }
      });
    }

    /**
     * Returns a parser that produces a Java identifier start character.
     *
     * @param missing The error if there is no character on the stream to produce a Java identifier start character with.
     * @param sat     The error if the produced character is not a Java identifier start character.
     * @return A parser that produces a Java identifier start character.
     * @see Character#isJavaIdentifierStart(char)
     */
    public static <E> Parser<Stream<Character>, Character, E> javaIdentifierStart(final E missing,
                                                                                  final F<Character, E> sat) {
      return javaIdentifierStart(P.p(missing), sat);
    }

    /**
     * Returns a parser that produces an alpha character.
     *
     * @param missing The error if there is no character on the stream to produce an alpha character with.
     * @param sat     The error if the produced character is not an alpha character.
     * @return A parser that produces an alpha character.
     * @see Character#isLetter(char)
     */
    public static <E> Parser<Stream<Character>, Character, E> alpha(final P1<E> missing, final F<Character, E> sat) {
      return StreamParser.satisfy(missing, sat, new F<Character, Boolean>() {
        public Boolean f(final Character c) {
          return Character.isLetter(c);
        }
      });
    }

    /**
     * Returns a parser that produces an alpha character.
     *
     * @param missing The error if there is no character on the stream to produce an alpha character with.
     * @param sat     The error if the produced character is not an alpha character.
     * @return A parser that produces an alpha character.
     * @see Character#isLetter(char)
     */
    public static <E> Parser<Stream<Character>, Character, E> alpha(final E missing, final F<Character, E> sat) {
      return alpha(P.p(missing), sat);
    }

    /**
     * Returns a parser that produces an alpha-numeric character.
     *
     * @param missing The error if there is no character on the stream to produce an alpha-numeric character with.
     * @param sat     The error if the produced character is not an alpha-numeric character.
     * @return A parser that produces an alpha-numeric character.
     * @see Character#isLetterOrDigit(char)
     */
    public static <E> Parser<Stream<Character>, Character, E> alphaNum(final P1<E> missing, final F<Character, E> sat) {
      return StreamParser.satisfy(missing, sat, new F<Character, Boolean>() {
        public Boolean f(final Character c) {
          return Character.isLetterOrDigit(c);
        }
      });
    }

    /**
     * Returns a parser that produces an alpha-numeric character.
     *
     * @param missing The error if there is no character on the stream to produce an alpha-numeric character with.
     * @param sat     The error if the produced character is not an alpha-numeric character.
     * @return A parser that produces an alpha-numeric character.
     * @see Character#isLetterOrDigit(char)
     */
    public static <E> Parser<Stream<Character>, Character, E> alphaNum(final E missing, final F<Character, E> sat) {
      return alphaNum(P.p(missing), sat);
    }

    /**
     * Returns a parser that produces a low-surrogate character.
     *
     * @param missing The error if there is no character on the stream to produce a low-surrogate character with.
     * @param sat     The error if the produced character is not a low-surrogate character.
     * @return A parser that produces a low-surrogate character.
     * @see Character#isLowSurrogate(char)
     */
    public static <E> Parser<Stream<Character>, Character, E> lowSurrogate(final P1<E> missing,
                                                                           final F<Character, E> sat) {
      return StreamParser.satisfy(missing, sat, new F<Character, Boolean>() {
        public Boolean f(final Character c) {
          return Character.isLowSurrogate(c);
        }
      });
    }

    /**
     * Returns a parser that produces a low-surrogate character.
     *
     * @param missing The error if there is no character on the stream to produce a low-surrogate character with.
     * @param sat     The error if the produced character is not a low-surrogate character.
     * @return A parser that produces a low-surrogate character.
     * @see Character#isLowSurrogate(char)
     */
    public static <E> Parser<Stream<Character>, Character, E> lowSurrogate(final E missing, final F<Character, E> sat) {
      return lowSurrogate(P.p(missing), sat);
    }

    /**
     * Returns a parser that produces a mirrored character.
     *
     * @param missing The error if there is no character on the stream to produce a mirrored character with.
     * @param sat     The error if the produced character is not a mirrored character.
     * @return A parser that produces a mirrored character.
     * @see Character#isMirrored(char)
     */
    public static <E> Parser<Stream<Character>, Character, E> mirrored(final P1<E> missing, final F<Character, E> sat) {
      return StreamParser.satisfy(missing, sat, new F<Character, Boolean>() {
        public Boolean f(final Character c) {
          return Character.isMirrored(c);
        }
      });
    }

    /**
     * Returns a parser that produces a mirrored character.
     *
     * @param missing The error if there is no character on the stream to produce a mirrored character with.
     * @param sat     The error if the produced character is not a mirrored character.
     * @return A parser that produces a mirrored character.
     * @see Character#isMirrored(char)
     */
    public static <E> Parser<Stream<Character>, Character, E> mirrored(final E missing, final F<Character, E> sat) {
      return mirrored(P.p(missing), sat);
    }

    /**
     * Returns a parser that produces a space character.
     *
     * @param missing The error if there is no character on the stream to produce a space character with.
     * @param sat     The error if the produced character is not a space character.
     * @return A parser that produces a space character.
     * @see Character#isSpace(char)
     */
    public static <E> Parser<Stream<Character>, Character, E> space(final P1<E> missing, final F<Character, E> sat) {
      return StreamParser.satisfy(missing, sat, new F<Character, Boolean>() {
        public Boolean f(final Character c) {
          return Character.isSpaceChar(c);
        }
      });
    }

    /**
     * Returns a parser that produces a space character.
     *
     * @param missing The error if there is no character on the stream to produce a space character with.
     * @param sat     The error if the produced character is not a space character.
     * @return A parser that produces a space character.
     * @see Character#isSpace(char)
     */
    public static <E> Parser<Stream<Character>, Character, E> space(final E missing, final F<Character, E> sat) {
      return space(P.p(missing), sat);
    }

    /**
     * Returns a parser that produces a title-case character.
     *
     * @param missing The error if there is no character on the stream to produce a title-case character with.
     * @param sat     The error if the produced character is not a title-case character.
     * @return A parser that produces a title-case character.
     * @see Character#isTitleCase(char)
     */
    public static <E> Parser<Stream<Character>, Character, E> titleCase(final P1<E> missing,
                                                                        final F<Character, E> sat) {
      return StreamParser.satisfy(missing, sat, new F<Character, Boolean>() {
        public Boolean f(final Character c) {
          return Character.isTitleCase(c);
        }
      });
    }

    /**
     * Returns a parser that produces a title-case character.
     *
     * @param missing The error if there is no character on the stream to produce a title-case character with.
     * @param sat     The error if the produced character is not a title-case character.
     * @return A parser that produces a title-case character.
     * @see Character#isTitleCase(char)
     */
    public static <E> Parser<Stream<Character>, Character, E> titleCase(final E missing, final F<Character, E> sat) {
      return titleCase(P.p(missing), sat);
    }

    /**
     * Returns a parser that produces a unicode identifier part character.
     *
     * @param missing The error if there is no character on the stream to produce a unicode identifier part character with.
     * @param sat     The error if the produced character is not a unicode identifier part character.
     * @return A parser that produces a unicode identifier part character.
     * @see Character#isUnicodeIdentifierPart(char)
     */
    public static <E> Parser<Stream<Character>, Character, E> unicodeIdentiferPart(final P1<E> missing,
                                                                                   final F<Character, E> sat) {
      return StreamParser.satisfy(missing, sat, new F<Character, Boolean>() {
        public Boolean f(final Character c) {
          return Character.isUnicodeIdentifierPart(c);
        }
      });
    }

    /**
     * Returns a parser that produces a unicode identifier part character.
     *
     * @param missing The error if there is no character on the stream to produce a unicode identifier part character with.
     * @param sat     The error if the produced character is not a unicode identifier part character.
     * @return A parser that produces a unicode identifier part character.
     * @see Character#isUnicodeIdentifierPart(char)
     */
    public static <E> Parser<Stream<Character>, Character, E> unicodeIdentiferPart(final E missing,
                                                                                   final F<Character, E> sat) {
      return unicodeIdentiferPart(P.p(missing), sat);
    }

    /**
     * Returns a parser that produces a unicode identifier start character.
     *
     * @param missing The error if there is no character on the stream to produce a unicode identifier start character with.
     * @param sat     The error if the produced character is not a unicode identifier start character.
     * @return A parser that produces a unicode identifier start character.
     * @see Character#isUnicodeIdentifierStart(char)
     */
    public static <E> Parser<Stream<Character>, Character, E> unicodeIdentiferStart(final P1<E> missing,
                                                                                    final F<Character, E> sat) {
      return StreamParser.satisfy(missing, sat, new F<Character, Boolean>() {
        public Boolean f(final Character c) {
          return Character.isUnicodeIdentifierStart(c);
        }
      });
    }

    /**
     * Returns a parser that produces a unicode identifier start character.
     *
     * @param missing The error if there is no character on the stream to produce a unicode identifier start character with.
     * @param sat     The error if the produced character is not a unicode identifier start character.
     * @return A parser that produces a unicode identifier start character.
     * @see Character#isUnicodeIdentifierStart(char)
     */
    public static <E> Parser<Stream<Character>, Character, E> unicodeIdentiferStart(final E missing,
                                                                                    final F<Character, E> sat) {
      return unicodeIdentiferStart(P.p(missing), sat);
    }

    /**
     * Returns a parser that produces a white-space character.
     *
     * @param missing The error if there is no character on the stream to produce a white-space character with.
     * @param sat     The error if the produced character is not a white-space character.
     * @return A parser that produces a white-space character.
     * @see Character#isWhitespace(char)
     */
    public static <E> Parser<Stream<Character>, Character, E> whitespace(final P1<E> missing,
                                                                         final F<Character, E> sat) {
      return StreamParser.satisfy(missing, sat, new F<Character, Boolean>() {
        public Boolean f(final Character c) {
          return Character.isWhitespace(c);
        }
      });
    }

    /**
     * Returns a parser that produces a white-space character.
     *
     * @param missing The error if there is no character on the stream to produce a white-space character with.
     * @param sat     The error if the produced character is not a white-space character.
     * @return A parser that produces a white-space character.
     * @see Character#isWhitespace(char)
     */
    public static <E> Parser<Stream<Character>, Character, E> whitespace(final E missing, final F<Character, E> sat) {
      return whitespace(P.p(missing), sat);
    }
  }
}