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

import java.math.BigInteger;

/**
 * Curried functions over Integers.
 *
 * @version 2.20<br>
 *          <ul>
 *          <li>$LastChangedRevision: 122 $</li>
 *          <li>$LastChangedDate: 2009-04-25 08:24:38 +1000 (Sat, 25 Apr 2009) $</li>
 *          </ul>
 */
public final class BigIntegers {
  private BigIntegers() {
    throw new UnsupportedOperationException();
  }

  /**
   * Curried Integer addition.
   */
  public static final F<BigInteger, F<BigInteger, BigInteger>> add =
      Function.curry(new F2<BigInteger, BigInteger, BigInteger>() {
        public BigInteger f(final BigInteger a1, final BigInteger a2) {
          return a1.add(a2);
        }
      });

  /**
   * Curried Integer multiplication.
   */
  public static final F<BigInteger, F<BigInteger, BigInteger>> multiply =
      Function.curry(new F2<BigInteger, BigInteger, BigInteger>() {
        public BigInteger f(final BigInteger a1, final BigInteger a2) {
          return a1.multiply(a2);
        }
      });

  /**
   * Curried Integer subtraction.
   */
  public static final F<BigInteger, F<BigInteger, BigInteger>> subtract =
      Function.curry(new F2<BigInteger, BigInteger, BigInteger>() {
        public BigInteger f(final BigInteger a1, final BigInteger a2) {
          return a1.subtract(a2);
        }
      });

  /**
   * Negation.
   */
  public static final F<BigInteger, BigInteger> negate = new F<BigInteger, BigInteger>() {
    public BigInteger f(final BigInteger i) {
      return i.negate();
    }
  };

  /**
   * Absolute value.
   */
  public static final F<BigInteger, BigInteger> abs = new F<BigInteger, BigInteger>() {
    public BigInteger f(final BigInteger i) {
      return i.abs();
    }
  };

  /**
   * Remainder.
   */
  public static final F<BigInteger, F<BigInteger, BigInteger>> remainder =
      Function.curry(new F2<BigInteger, BigInteger, BigInteger>() {
        public BigInteger f(final BigInteger a1, final BigInteger a2) {
          return a1.remainder(a2);
        }
      });

  /**
   * Power.
   */
  public static final F<BigInteger, F<Integer, BigInteger>> power = Function.curry(new F2<BigInteger, Integer, BigInteger>() {
    public BigInteger f(final BigInteger a1, final Integer a2) {
      return a1.pow(a2);
    }
  });

  /**
   * Sums a list of big integers.
   *
   * @param ints A list of big integers to sum.
   * @return The sum of the big integers in the list.
   */
  public static BigInteger sum(final List<BigInteger> ints) {
    return Monoid.bigintAdditionMonoid.sumLeft(ints);
  }

  /**
   * Returns the product of a list of big integers.
   *
   * @param ints A list of big integers to multiply together.
   * @return The product of the big integers in the list.
   */
  public static BigInteger product(final List<BigInteger> ints) {
    return Monoid.bigintMultiplicationMonoid.sumLeft(ints);
  }
}
