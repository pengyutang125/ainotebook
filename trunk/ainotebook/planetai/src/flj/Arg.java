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
 * An argument used in a property that may have undergone shrinking following falsification.
 *
 * @version 2.20<br>
 *          <ul>
 *          <li>$LastChangedRevision: 122 $</li>
 *          <li>$LastChangedDate: 2009-04-25 08:24:38 +1000 (Sat, 25 Apr 2009) $</li>
 *          <li>$LastChangedBy: runarorama $</li>
 *          </ul>
 */
public final class Arg<T> {
  private final T value;
  private final int shrinks;

  private Arg(final T value, final int shrinks) {
    this.value = value;
    this.shrinks = shrinks;
  }

  /**
   * Construct a property argument with the given value and number of shrinks.
   *
   * @param value   The value to construct an argument with.
   * @param shrinks The number of shrinks to construct an argument with.
   * @return A new argument.
   */
  public static <T> Arg<T> arg(final T value, final int shrinks) {
    return new Arg<T>(value, shrinks);
  }

  /**
   * Returns the argument's value.
   *
   * @return The argument's value.
   */
  public Object value() {
    return value;
  }

  /**
   * Returns the argument's number of shrinks following falsification.
   *
   * @return The argument's number of shrinks following falsification.
   */
  public int shrinks() {
    return shrinks;
  }

  /**
   * The rendering of an argument (uses {@link Object#toString()} for the argument value).
   */
  public static final Show<Arg<?>> argShow = Show.showS(new F<Arg<?>, String>() {
    public String f(final Arg<?> arg) {
      return Show.anyShow().showS(arg.value) +
          (arg.shrinks > 0 ? " (" + arg.shrinks + " shrink" + (arg.shrinks == 1 ? "" : 's') + ')' : "");
    }
  });
}
