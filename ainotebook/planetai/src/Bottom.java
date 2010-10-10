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

/**
 * Represents the bottom _|_ value.
 *
 * @version 2.20<br>
 *          <ul>
 *          <li>$LastChangedRevision: 127 $</li>
 *          <li>$LastChangedDate: 2009-05-10 01:46:32 +1000 (Sun, 10 May 2009) $</li>
 *          </ul>
 */
public final class Bottom {
  private Bottom() {
    throw new UnsupportedOperationException();
  }

  /**
   * Returns an error to represent undefinedness in a computation.
   *
   * @return An error to represent undefinedness in a computation.
   */
  public static Error undefined() {
    return error("undefined");
  }

  /**
   * Returns an error to represent undefinedness in a computation with early failure using the given
   * message.
   *
   * @param s The message to fail with.
   * @return An error to represent undefinedness in a computation with early failure using the given
   *         message.
   */
  public static Error error(final String s) {
    throw new Error(s);
  }

  /**
   * Represents a deconstruction failure that was non-exhaustive.
   *
   * @param a  The value being deconstructed.
   * @param sa The rendering for the value being deconstructed.
   * @return A deconstruction failure that was non-exhaustive.
   */
  public static <A> Error decons(final A a, final Show<A> sa) {
    return error("Deconstruction failure on type " + a.getClass() + " with value " + sa.show(a).toString());
  }

  /**
   * Represents a deconstruction failure that was non-exhaustive.
   *
   * @param c The type being deconstructed.
   * @return A deconstruction failure that was non-exhaustive.
   */
  public static <A> Error decons(final java.lang.Class<A> c) {
    return error("Deconstruction failure on type " + c);
  }

  /**
   * A function that returns the <code>toString</code> for a throwable.
   *
   * @return A function that returns the <code>toString</code> for a throwable.
   */
  public static <T extends Throwable> F<T, String> eToString() {
    return new F<T, String>() {
      public String f(final Throwable t) {
        return t.toString();
      }
    };
  }

  /**
   * A function that returns the <code>getMessage</code> for a throwable.
   *
   * @return A function that returns the <code>getMessage</code> for a throwable.
   */
  public static <T extends Throwable> F<T, String> eMessage() {
    return new F<T, String>() {
      public String f(final Throwable t) {
        return t.getMessage();
      }
    };
  }
}
