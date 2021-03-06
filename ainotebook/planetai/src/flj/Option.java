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


import java.util.Collection;
import java.util.Iterator;

/**
 * An optional value that may be none (no value) or some (a value). This type is a replacement for
 * the use of <code>null</code> with better type checks.
 *
 * @version 2.20<br>
 *          <ul>
 *          <li>$LastChangedRevision: 167 $</li>
 *          <li>$LastChangedDate: 2009-06-04 17:25:22 +1000 (Thu, 04 Jun 2009) $</li>
 *          </ul>
 */
public abstract class Option<A> implements Iterable<A> {
  private Option() {

  }

  /**
   * Returns an iterator for this optional value. This method exists to permit the use in a <code>for</code>-each loop.
   *
   * @return A iterator for this optional value.
   */
  public Iterator<A> iterator() {
    return toCollection().iterator();
  }

  /**
   * Returns the value from this optional value, or fails if there is no value.
   *
   * @return The value from this optional value, or fails if there is no value.
   */
  public abstract A some();

  /**
   * Returns <code>true</code> if this optional value has a value, <code>false</code> otherwise.
   *
   * @return <code>true</code> if this optional value has a value, <code>false</code> otherwise.
   */
  public boolean isSome() {
    return this instanceof Some;
  }

  /**
   * Returns <code>false</code> if this optional value has a value, <code>true</code> otherwise.
   *
   * @return <code>false</code> if this optional value has a value, <code>true</code> otherwise.
   */
  public boolean isNone() {
    return this instanceof None;
  }

  /**
   * A first-class version of the isSome method.
   *
   * @return A function that returns true if a given optional value has a value, otherwise false.
   */
  public static <A> F<Option<A>, Boolean> isSome_() {
    return new F<Option<A>, Boolean>() {
      public Boolean f(final Option<A> a) {
        return a.isSome();
      }
    };
  }

  /**
   * A first-class version of the isNone method.
   *
   * @return A function that returns false if a given optional value has a value, otherwise true.
   */
  public static <A> F<Option<A>, Boolean> isNone_() {
    return new F<Option<A>, Boolean>() {
      public Boolean f(final Option<A> a) {
        return a.isNone();
      }
    };
  }

  /**
   * Performs a reduction on this optional value using the given arguments.
   *
   * @param b The value to return if this optional value has no value.
   * @param f The function to apply to the value of this optional value.
   * @return A reduction on this optional value.
   */
  public <B> B option(final B b, final F<A, B> f) {
    return isSome() ? f.f(some()) : b;
  }

  /**
   * Returns the length of this optional value; 1 is there is a value, 0 otherwise.
   *
   * @return The length of this optional value; 1 is there is a value, 0 otherwise.
   */
  public int length() {
    return isSome() ? 1 : 0;
  }

  /**
   * Returns the value of this optional value or the given argument.
   *
   * @param a The argument to return if this optiona value has no value.
   * @return The value of this optional value or the given argument.
   */
  public A orSome(final P1<A> a) {
    return isSome() ? some() : a._1();
  }

  /**
   * Returns the value of this optional value or the given argument.
   *
   * @param a The argument to return if this optiona value has no value.
   * @return The value of this optional value or the given argument.
   */
  public A orSome(final A a) {
    return isSome() ? some() : a;
  }

  /**
   * Maps the given function across this optional value.
   *
   * @param f The function to map across this optional value.
   * @return A new optional value after the given function has been applied to its element.
   */
  public <B> Option<B> map(final F<A, B> f) {
    return isSome() ? some(f.f(some())) : Option.<B>none();
  }

  /**
   * A first-class map function.
   *
   * @return A function that maps a given function across a given optional value.
   */
  public static <A, B> F<F<A, B>, F<Option<A>, Option<B>>> map() {
    return Function.curry(new F2<F<A, B>, Option<A>, Option<B>>() {
      public Option<B> f(final F<A, B> abf, final Option<A> option) {
        return option.map(abf);
      }
    });
  }

  /**
   * Performs a side-effect for the value of this optional value.
   *
   * @param f The side-effect to perform for the given element.
   * @return The unit value.
   */
  public Unit foreach(final F<A, Unit> f) {
    return isSome() ? f.f(some()) : Unit.unit();
  }

  /**
   * Performs a side-effect for the value of this optional value.
   *
   * @param f The side-effect to perform for the given element.
   */
  public void foreach(final Effect<A> f) {
    if (isSome())
      f.e(some());
  }

  /**
   * Filters elements from this optional value by returning only elements which produce
   * <code>true</code> when the given function is applied to them.
   *
   * @param f The predicate function to filter on.
   * @return A new optional value whose value matches the given predicate if it has one.
   */
  public Option<A> filter(final F<A, Boolean> f) {
    return isSome() ? f.f(some()) ? this : Option.<A>none() : Option.<A>none();
  }

  /**
   * Binds the given function across the element of this optional value with a final join.
   *
   * @param f The function to apply to the element of this optional value.
   * @return A new optional value after performing the map, then final join.
   */
  public <B> Option<B> bind(final F<A, Option<B>> f) {
    return isSome() ? f.f(some()) : Option.<B>none();
  }

  /**
   * Binds the given function across the element of this optional value and the given optional value
   * with a final join.
   *
   * @param ob A given optional value to bind the given function with.
   * @param f  The function to apply to the element of this optional value and the given optional
   *           value.
   * @return A new optional value after performing the map, then final join.
   */
  public <B, C> Option<C> bind(final Option<B> ob, final F<A, F<B, C>> f) {
    return ob.apply(map(f));
  }

  /**
   * Binds the given function across the element of this optional value and the given optional value
   * with a final join.
   *
   * @param ob A given optional value to bind the given function with.
   * @param oc A given optional value to bind the given function with.
   * @param f  The function to apply to the element of this optional value and the given optional
   *           value.
   * @return A new optional value after performing the map, then final join.
   */
  public <B, C, D> Option<D> bind(final Option<B> ob, final Option<C> oc, final F<A, F<B, F<C, D>>> f) {
    return oc.apply(bind(ob, f));
  }

  /**
   * Binds the given function across the element of this optional value and the given optional value
   * with a final join.
   *
   * @param ob A given optional value to bind the given function with.
   * @param oc A given optional value to bind the given function with.
   * @param od A given optional value to bind the given function with.
   * @param f  The function to apply to the element of this optional value and the given optional
   *           value.
   * @return A new optional value after performing the map, then final join.
   */
  public <B, C, D, E> Option<E> bind(final Option<B> ob, final Option<C> oc, final Option<D> od,
                                     final F<A, F<B, F<C, F<D, E>>>> f) {
    return od.apply(bind(ob, oc, f));
  }

  /**
   * Binds the given function across the element of this optional value and the given optional value
   * with a final join.
   *
   * @param ob A given optional value to bind the given function with.
   * @param oc A given optional value to bind the given function with.
   * @param od A given optional value to bind the given function with.
   * @param oe A given optional value to bind the given function with.
   * @param f  The function to apply to the element of this optional value and the given optional
   *           value.
   * @return A new optional value after performing the map, then final join.
   */
  public <B, C, D, E, F$> Option<F$> bind(final Option<B> ob, final Option<C> oc, final Option<D> od,
                                          final Option<E> oe, final F<A, F<B, F<C, F<D, F<E, F$>>>>> f) {
    return oe.apply(bind(ob, oc, od, f));
  }

  /**
   * Binds the given function across the element of this optional value and the given optional value
   * with a final join.
   *
   * @param ob A given optional value to bind the given function with.
   * @param oc A given optional value to bind the given function with.
   * @param od A given optional value to bind the given function with.
   * @param oe A given optional value to bind the given function with.
   * @param of A given optional value to bind the given function with.
   * @param f  The function to apply to the element of this optional value and the given optional
   *           value.
   * @return A new optional value after performing the map, then final join.
   */
  public <B, C, D, E, F$, G> Option<G> bind(final Option<B> ob, final Option<C> oc, final Option<D> od,
                                            final Option<E> oe, final Option<F$> of,
                                            final F<A, F<B, F<C, F<D, F<E, F<F$, G>>>>>> f) {
    return of.apply(bind(ob, oc, od, oe, f));
  }

  /**
   * Binds the given function across the element of this optional value and the given optional value
   * with a final join.
   *
   * @param ob A given optional value to bind the given function with.
   * @param oc A given optional value to bind the given function with.
   * @param od A given optional value to bind the given function with.
   * @param oe A given optional value to bind the given function with.
   * @param of A given optional value to bind the given function with.
   * @param og A given optional value to bind the given function with.
   * @param f  The function to apply to the element of this optional value and the given optional
   *           value.
   * @return A new optional value after performing the map, then final join.
   */
  public <B, C, D, E, F$, G, H> Option<H> bind(final Option<B> ob, final Option<C> oc, final Option<D> od,
                                               final Option<E> oe, final Option<F$> of, final Option<G> og,
                                               final F<A, F<B, F<C, F<D, F<E, F<F$, F<G, H>>>>>>> f) {
    return og.apply(bind(ob, oc, od, oe, of, f));
  }

  /**
   * Binds the given function across the element of this optional value and the given optional value
   * with a final join.
   *
   * @param ob A given optional value to bind the given function with.
   * @param oc A given optional value to bind the given function with.
   * @param od A given optional value to bind the given function with.
   * @param oe A given optional value to bind the given function with.
   * @param of A given optional value to bind the given function with.
   * @param og A given optional value to bind the given function with.
   * @param oh A given optional value to bind the given function with.
   * @param f  The function to apply to the element of this optional value and the given optional
   *           value.
   * @return A new optional value after performing the map, then final join.
   */
  public <B, C, D, E, F$, G, H, I> Option<I> bind(final Option<B> ob, final Option<C> oc, final Option<D> od,
                                                  final Option<E> oe, final Option<F$> of, final Option<G> og,
                                                  final Option<H> oh,
                                                  final F<A, F<B, F<C, F<D, F<E, F<F$, F<G, F<H, I>>>>>>>> f) {
    return oh.apply(bind(ob, oc, od, oe, of, og, f));
  }

  /**
   * Performs a bind across the optional value, but ignores the element value in the function.
   *
   * @param o The optional value to apply in the final join.
   * @return A new optional value after the final join.
   */
  public <B> Option<B> sequence(final Option<B> o) {
    final F<A, Option<B>> c = Function.constant(o);
    return bind(c);
  }

  /**
   * Performs function application within an optional value (applicative functor pattern).
   *
   * @param of The optional value of functions to apply.
   * @return A new optional value after applying the given optional value of functions through this
   *         optional value.
   */
  public <B> Option<B> apply(final Option<F<A, B>> of) {
    return of.bind(new F<F<A, B>, Option<B>>() {
      public Option<B> f(final F<A, B> f) {
        return map(new F<A, B>() {
          public B f(final A a) {
            return f.f(a);
          }
        });
      }
    });
  }

  /**
   * Returns this optional value if there is one, otherwise, returns the argument optional value.
   *
   * @param o The optional value to return if this optional value has no value.
   * @return This optional value if there is one, otherwise, returns the argument optional value.
   */
  public Option<A> orElse(final P1<Option<A>> o) {
    return isSome() ? this : o._1();
  }

  /**
   * Returns this optional value if there is one, otherwise, returns the argument optional value.
   *
   * @param o The optional value to return if this optional value has no value.
   * @return This optional value if there is one, otherwise, returns the argument optional value.
   */
  public Option<A> orElse(final Option<A> o) {
    return isSome() ? this : o;
  }

  /**
   * Returns an either projection of this optional value; the given argument in <code>Left</code> if
   * no value, or the value in <code>Right</code>.
   *
   * @param x The value to return in left if this optional value has no value.
   * @return An either projection of this optional value.
   */
  public <X> Either<X, A> toEither(final P1<X> x) {
    return isSome() ? Either.<X, A>right(some()) : Either.<X, A>left(x._1());
  }

  /**
   * Returns an either projection of this optional value; the given argument in <code>Left</code> if
   * no value, or the value in <code>Right</code>.
   *
   * @param x The value to return in left if this optional value has no value.
   * @return An either projection of this optional value.
   */
  public <X> Either<X, A> toEither(final X x) {
    return isSome() ? Either.<X, A>right(some()) : Either.<X, A>left(x);
  }

  /**
   * A first-class version of the toEither method.
   *
   * @return A function that returns an either projection of a given optional value, given a value to
   *         return in left.
   */
  public static <A, X> F<Option<A>, F<X, Either<X, A>>> toEither() {
    return Function.curry(new F2<Option<A>, X, Either<X, A>>() {
      public Either<X, A> f(final Option<A> a, final X x) {
        return a.toEither(x);
      }
    });
  }

  /**
   * Returns a list projection of this optional value.
   *
   * @return A list projection of this optional value.
   */
  public List<A> toList() {
    return isSome() ? List.cons(some(), List.<A>nil()) : List.<A>nil();
  }

  /**
   * Returns a stream projection of this optional value.
   *
   * @return A stream projection of this optional value.
   */
  public Stream<A> toStream() {
    return isSome() ? Stream.<A>nil().cons(some()) : Stream.<A>nil();
  }

  /**
   * Returns an array projection of this optional value.
   *
   * @return An array projection of this optional value.
   */
  @SuppressWarnings({"unchecked"})
  public Array<A> toArray() {
    return isSome() ? Array.array(some()) : Array.<A>empty();
  }

  /**
   * Returns an array projection of this optional value.
   *
   * @param c The class type of the array to return.
   * @return An array projection of this optional value.
   */
  @SuppressWarnings({"unchecked"})
  public Array<A> toArray(final Class<A[]> c) {
    if (isSome()) {
      final A[] a = (A[]) java.lang.reflect.Array.newInstance(c.getComponentType(), 1);
      a[0] = some();
      return Array.array(a);
    } else
      return Array.array((A[]) java.lang.reflect.Array.newInstance(c.getComponentType(), 0));
  }

  /**
   * Returns <code>true</code> if this optional value has no value, or the predicate holds for the
   * given predicate function, <code>false</code> otherwise.
   *
   * @param f the predicate function to test on the value of this optional value.
   * @return <code>true</code> if this optional value has no value, or the predicate holds for the
   *         given predicate function, <code>false</code> otherwise.
   */
  public boolean forall(final F<A, Boolean> f) {
    return isNone() || f.f(some());
  }

  /**
   * Returns <code>true</code> is this optional value has a value and the given predicate function
   * holds on that value, <code>false</code> otherwise.
   *
   * @param f the predicate function to test on the value of this optional value.
   * @return <code>true</code> is this optional value has a value and the given predicate function
   *         holds on that value, <code>false</code> otherwise.
   */
  public boolean exists(final F<A, Boolean> f) {
    return isSome() && f.f(some());
  }

  /**
   * Projects an immutable collection of this optional value.
   *
   * @return An immutable collection of this optional value.
   */
  public Collection<A> toCollection() {
    return toList().toCollection();
  }

  private static final class None<A> extends Option<A> {
    public A some() {
      throw Bottom.error("some on None");
    }
  }

  private static final class Some<A> extends Option<A> {
    private final A a;

    Some(final A a) {
      this.a = a;
    }

    public A some() {
      return a;
    }
  }

  public static <T> F<T, Option<T>> some_() {
    return new F<T, Option<T>>() {
      public Option<T> f(final T t) {
        return some(t);
      }
    };
  }

  /**
   * Constructs an optional value that has a value of the given argument.
   *
   * @param t The value for the returned optional value.
   * @return An optional value that has a value of the given argument.
   */
  public static <T> Option<T> some(final T t) {
    return new Some<T>(t);
  }

  /**
   * Constructs an optional value that has no value.
   *
   * @return An optional value that has no value.
   */
  public static <T> Option<T> none() {
    return new None<T>();
  }

  /**
   * Turns an unsafe nullable value into a safe optional value. If <code>t == null</code> then
   * return none, otherwise, return the given value in some.
   *
   * @param t The unsafe nullable value.
   * @return If <code>t == null</code> then return it in some, otherwise, return none.
   */
  public static <T> Option<T> fromNull(final T t) {
    return t == null ? Option.<T>none() : some(t);
  }

  /**
   * Joins the given optional value of optional value using a bind operation.
   *
   * @param o The optional value of optional value to join.
   * @return A new optional value that is the join of the given optional value.
   */
  public static <A> Option<A> join(final Option<Option<A>> o) {
    final F<Option<A>, Option<A>> id = Function.identity();
    return o.bind(id);
  }

  /**
   * Sequence through the option monad.
   *
   * @param a The list of option to sequence.
   * @return The option of list after sequencing.
   */
  public static <A> Option<List<A>> sequence(final List<Option<A>> a) {
    return a.isEmpty() ?
           some(List.<A>nil()) :
           a.head().bind(new F<A, Option<List<A>>>() {
             public Option<List<A>> f(final A aa) {
               return sequence(a.tail()).map(List.cons_(aa));
             }
           });
  }

  /**
   * Returns an optional value that has a value of the given argument, if the given predicate holds
   * on that argument, otherwise, returns no value.
   *
   * @param f The predicate to test on the given argument.
   * @param a The argument to test the predicate on and potentially use as the value of the returned
   *          optional value.
   * @return an optional value that has a value of the given argument, if the given predicate holds
   *         on that argument, otherwise, returns no value.
   */
  public static <A> Option<A> iif(final F<A, Boolean> f, final A a) {
    return f.f(a) ? some(a) : Option.<A>none();
  }

  /**
   * First-class version of the iif function.
   *
   * @return a function that returns an optional value that has a value of the given argument, if the given predicate
   *         holds on that argument, or no value otherwise.
   */
  public static <A> F2<F<A, Boolean>, A, Option<A>> iif() {
    return new F2<F<A, Boolean>, A, Option<A>>() {
      public Option<A> f(final F<A, Boolean> p, final A a) {
        return iif(p, a);
      }
    };
  }

  /**
   * Returns all the values in the given list.
   *
   * @param as The list of potential values to get actual values from.
   * @return All the values in the given list.
   */
  public static <A> List<A> somes(final List<Option<A>> as) {
    return as.filter(Option.<A>isSome_()).map(new F<Option<A>, A>() {
      public A f(final Option<A> o) {
        return o.some();
      }
    });
  }


  /**
   * Returns all the values in the given stream.
   *
   * @param as The stream of potential values to get actual values from.
   * @return All the values in the given stream.
   */
  public static <A> Stream<A> somes(final Stream<Option<A>> as) {
    return as.filter(Option.<A>isSome_()).map(new F<Option<A>, A>() {
      public A f(final Option<A> o) {
        return o.some();
      }
    });
  }

  /**
   * Returns an optional non-empty string, or no value if the given string is empty.
   *
   * @param s A string to turn into an optional non-empty string.
   * @return an optional non-empty string, or no value if the given string is empty.
   */
  public static Option<String> fromString(final String s) {
    return fromNull(s).bind(new F<String, Option<String>>() {
      public Option<String> f(final String s) {
        final Option<String> none = none();
        return s.length() == 0 ? none : some(s);
      }
    });
  }

  /**
   * Returns a function that transforms a string to an optional non-empty string,
   * or no value if the string is empty.
   *
   * @return a function that transforms a string to an optional non-empty string,
   *         or no value if the string is empty.
   */
  public static F<String, Option<String>> fromString() {
    return new F<String, Option<String>>() {
      public Option<String> f(final String s) {
        return fromString(s);
      }
    };
  }

  /**
   * Returns a function that takes an optional value to a value or errors if there is no value.
   *
   * @return A function that takes an optional value to a value or errors if there is no value.
   */
  public static <A> F<Option<A>, A> fromSome() {
    return new F<Option<A>, A>() {
      public A f(final Option<A> option) {
        return option.some();
      }
    };
  }

  /**
   * Promotes a function of arity-2 so that it operates over options.
   *
   * @param f A function to promote.
   * @return The given function promoted to operate on options.
   */
  public static <A, B, C> F<Option<A>, F<Option<B>, Option<C>>> liftM2(final F<A, F<B, C>> f) {
    return Function.curry(new F2<Option<A>, Option<B>, Option<C>>() {
      public Option<C> f(final Option<A> a, final Option<B> b) {
        return a.bind(b, f);
      }
    });
  }

  /**
   * First-class bind function.
   *
   * @return A function that binds a given function across an option with a final join.
   */
  public static <A, B> F<F<A, Option<B>>, F<Option<A>, Option<B>>> bind() {
    return Function.curry(new F2<F<A, Option<B>>, Option<A>, Option<B>>() {
      public Option<B> f(final F<A, Option<B>> f, final Option<A> a) {
        return a.bind(f);
      }
    });
  }

  /**
   * First-class join function.
   *
   * @return A function that joins an Option of an Option to make a single Option.
   */
  public static <A> F<Option<Option<A>>, Option<A>> join() {
    return new F<Option<Option<A>>, Option<A>>() {
      public Option<A> f(final Option<Option<A>> option) {
        return join(option);
      }
    };
  }

}
