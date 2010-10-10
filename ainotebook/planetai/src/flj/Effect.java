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
 * Represents a side-effect.
 *
 * @version 2.20<br>
 *          <ul>
 *          <li>$LastChangedRevision: 122 $</li>
 *          <li>$LastChangedDate: 2009-04-25 08:24:38 +1000 (Sat, 25 Apr 2009) $</li>
 *          </ul>
 */
public interface Effect<A> {
  void e(A a);

  /**
   * A projection of an effect. The methods defined on a projection may belong on an effect,
   * however, this would disallow the use of {@link Effect} to be used with Java 7 closure syntax.
   */  
  final class Projection {
    private Projection() {
      throw new UnsupportedOperationException();
    }

    /**
     * Returns an effect for the given function.
     *
     * @param f The function to produce the effort with.
     * @return The effect using the given function.
     */
    public static <A> Effect<A> f(final F<A, Unit> f) {
      return new Effect<A>() {
        public void e(final A a) {
          f.f(a);
        }
      };
    }

    /**
     * Returns a function for the given effect.
     *
     * @param e The effect to produce the function with.
     * @return The function using the given effect.
     */
    public static <A> F<A, Unit> e(final Effect<A> e) {
      return new F<A, Unit>() {
        public Unit f(final A a) {
          e.e(a);
          return Unit.unit();
        }
      };
    }

    /**
     * A contra-variant functor on effect.
     *
     * @param e The effect to map over.
     * @param f The function to map over the effect.
     * @return An effect after a contra-variant map.
     */
    public static <A, B> Effect<B> comap(final Effect<A> e, final F<B, A> f) {
      return new Effect<B>() {
        public void e(final B b) {
          e.e(f.f(b));
        }
      };
    }
  }
}
