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

import java.io.StringWriter;
import java.io.PrintWriter;

/**
 * An enumeration of the possible results after checking a property. A <code>CheckResult</code> may
 * be in one of six states:
 * <ol>
 * <li>Passed</li>
 * <li>Proven</li>
 * <li>Falsified</li>
 * <li>Exhausted</li>
 * <li>Exception executing the property</li>
 * <li>Exception generating values to check the property</li>
 * </ol>
 *
 * @version 2.20<br>
 *          <ul>
 *          <li>$LastChangedRevision: 127 $</li>
 *          <li>$LastChangedDate: 2009-05-10 01:46:32 +1000 (Sun, 10 May 2009) $</li>
 *          <li>$LastChangedBy: runarorama $</li>
 *          </ul>
 */
public final class CheckResult {
  private final R r;
  private final Option<List<Arg<?>>> args;
  private final Option<Throwable> ex;
  private final int succeeded;
  private final int discarded;

  private enum R {
    Passed, Proven, Falsified, Exhausted, PropException, GenException
  }

  private CheckResult(final R r, final Option<List<Arg<?>>> args, final Option<Throwable> ex, final int succeeded,
                      final int discarded) {
    this.r = r;
    this.args = args;
    this.ex = ex;
    this.succeeded = succeeded;
    this.discarded = discarded;
  }

  /**
   * Returns a result that the property has passed.
   *
   * @param succeeded The number of checks that succeeded.
   * @param discarded The number of checks that were discarded.
   * @return A result that the property has passed.
   */
  public static CheckResult passed(final int succeeded, final int discarded) {
    return new CheckResult(R.Passed, Option.<List<Arg<?>>>none(), Option.<Throwable>none(), succeeded, discarded);
  }

  /**
   * Returns a result that the property has been proven.
   *
   * @param args      The arguments used to prove the property.
   * @param succeeded The number of checks that succeeded.
   * @param discarded The number of checks that were discarded.
   * @return A result that the property has been proven.
   */
  public static CheckResult proven(final List<Arg<?>> args, final int succeeded, final int discarded) {
    return new CheckResult(R.Proven, Option.some(args), Option.<Throwable>none(), succeeded, discarded);
  }

  /**
   * Returns a result that the property has been falsified.
   *
   * @param args      The arguments used to falsify the property.
   * @param succeeded The number of checks that succeeded.
   * @param discarded The number of checks that were discarded.
   * @return A result that the property has been falsified.
   */
  public static CheckResult falsified(final List<Arg<?>> args, final int succeeded, final int discarded) {
    return new CheckResult(R.Falsified, Option.some(args), Option.<Throwable>none(), succeeded, discarded);
  }

  /**
   * Returns a result that the property been exhausted in checking.
   *
   * @param succeeded The number of checks that succeeded.
   * @param discarded The number of checks that were discarded.
   * @return A result that the property has been exhausted in checking.
   */
  public static CheckResult exhausted(final int succeeded, final int discarded) {
    return new CheckResult(R.Exhausted, Option.<List<Arg<?>>>none(), Option.<Throwable>none(), succeeded, discarded);
  }

  /**
   * Returns a result that checking the property threw an exception.
   *
   * @param args      The arguments used when the exception was thrown.
   * @param ex        The exception that was thrown.
   * @param succeeded The number of checks that succeeded.
   * @param discarded The number of checks that were discarded.
   * @return A result that checking the property threw an exception.
   */
  public static CheckResult propException(final List<Arg<?>> args, final Throwable ex, final int succeeded,
                                          final int discarded) {
    return new CheckResult(R.PropException, Option.some(args), Option.some(ex), succeeded, discarded);
  }


  /**
   * Returns a result that generating values to check the property threw an exception.
   *
   * @param ex        The exception that was thrown.
   * @param succeeded The number of checks that succeeded.
   * @param discarded The number of checks that were discarded.
   * @return A result that generating values to check the property threw an exception.
   */
  public static CheckResult genException(final Throwable ex, final int succeeded, final int discarded) {
    return new CheckResult(R.GenException, Option.<List<Arg<?>>>none(), Option.some(ex), succeeded, discarded);
  }

  /**
   * Returns <code>true</code> if this result is passed, <code>false</code> otherwise.
   *
   * @return <code>true</code> if this result is passed, <code>false</code> otherwise.
   */
  public boolean isPassed() {
    return r == R.Passed;
  }

  /**
   * Returns <code>true</code> if this result is proven, <code>false</code> otherwise.
   *
   * @return <code>true</code> if this result is proven, <code>false</code> otherwise.
   */
  public boolean isProven() {
    return r == R.Proven;
  }

  /**
   * Returns <code>true</code> if this result is falsified, <code>false</code> otherwise.
   *
   * @return <code>true</code> if this result is falsified, <code>false</code> otherwise.
   */
  public boolean isFalsified() {
    return r == R.Falsified;
  }

  /**
   * Returns <code>true</code> if this result is exhausted, <code>false</code> otherwise.
   *
   * @return <code>true</code> if this result is exhausted, <code>false</code> otherwise.
   */
  public boolean isExhausted() {
    return r == R.Exhausted;
  }


  /**
   * Returns <code>true</code> if this result is an exception during property execution,
   * <code>false</code> otherwise.
   *
   * @return <code>true</code> if this result is an exception during property execution,
   *         <code>false</code> otherwise.
   */
  public boolean isPropException() {
    return r == R.PropException;
  }

  /**
   * Returns <code>true</code> if this result is an exception during generating of values for
   * property checking, <code>false</code> otherwise.
   *
   * @return <code>true</code> if this result is an exception during generating of values for
   *         property checking, <code>false</code> otherwise.
   */
  public boolean isGenException() {
    return r == R.GenException;
  }

  /**
   * Returns the arguments if the result is one of; proven, falsified or exception during property
   * execution, otherwise, no arguments are returned.
   *
   * @return The arguments if the result is one of; proven, falsified or exception during property
   *         execution, otherwise, no arguments are returned.
   */
  public Option<List<Arg<?>>> args() {
    return args;
  }

  /**
   * Returns the execption if the result is one of; exception during property execution or exception
   * during argument value generation, otherwise, no exception are returned.
   *
   * @return The execption if the result is one of; exception during property execution or exception
   *         during argument value generation, otherwise, no exception are returned.
   */
  public Option<Throwable> exception() {
    return ex;
  }

  /**
   * Returns the number of succeeded checks of the property in this result.
   *
   * @return The number of succeeded checks of the property in this result.
   */
  public int succeeded() {
    return succeeded;
  }

  /**
   * Returns the number of discarded checks of the property in this result.
   *
   * @return The number of discarded checks of the property in this result.
   */
  public int discarded() {
    return discarded;
  }

  /**
   * A rendering of a check result that summarises in one line.
   *
   * @param sa The rendering of arguments.
   * @return A rendering of a check result that summarises in one line.
   */
  public static Show<CheckResult> summary(final Show<Arg<?>> sa) {
    return Show.showS(new F<CheckResult, String>() {
      private String test(final CheckResult r) {
        return r.succeeded() == 1 ? "test" : "tests";
      }

      private String arguments(final CheckResult r) {
        final List<Arg<?>> args = r.args().some();
        return args.length() == 1 ? "argument: " + sa.showS(args.head()) : "arguments: " + Show.listShow(sa).showS(args);
      }

      public String f(final CheckResult r) {
        if (r.isProven())
          return "OK, property proven with " + arguments(r);
        else if (r.isPassed())
          return "OK, passed " + r.succeeded() + ' ' + test(r) +
              (r.discarded() > 0 ? " (" + r.discarded() + " discarded)" : "") + '.';
        else if (r.isFalsified())
          return "Falsified after " + r.succeeded() + " passed " + test(r) + " with " + arguments(r);
        else if (r.isExhausted())
          return "Gave up after " + r.succeeded() + " passed " + test(r) + " and " + r.discarded() +
              " discarded tests.";
        else if (r.isPropException()) {
          final StringWriter sw = new StringWriter();
          final PrintWriter pw = new PrintWriter(sw);
          r.exception().some().printStackTrace(pw);
          return "Exception on property evaluation with " + arguments(r) + System.getProperty("line.separator") + sw;
        } else if (r.isGenException()) {
          final StringWriter sw = new StringWriter();
          final PrintWriter pw = new PrintWriter(sw);
          r.exception().some().printStackTrace(pw);
          return "Exception on argument generation " + System.getProperty("line.separator") + sw;
        } else
          throw Bottom.decons(r.getClass());
      }
    });
  }

  /**
   * A rendering of a check result that summarises in one line.
   */
  public static final Show<CheckResult> summary = summary(Arg.argShow);

  /**
   * A rendering of a check result that summarises in one line but throws an exception in the result
   * is a failure (falsified, property exception or generator exception).
   */
  public static final Show<CheckResult> summaryEx = summaryEx(Arg.argShow);

  /**
   * A rendering of a check result that summarises in one line but throws an exception in the result
   * is a failure (falsified, property exception or generator exception).
   *
   * @param sa The rendering of arguments.
   * @return A rendering of a check result that summarises in one line but throws an exception in
   *         the result is a failure (falsified, property exception or generator exception).
   */
  public static Show<CheckResult> summaryEx(final Show<Arg<?>> sa) {
    return Show.showS(new F<CheckResult, String>() {
      public String f(final CheckResult r) {
        final String s = summary(sa).show(r).toString();
        if (r.isProven() || r.isPassed() || r.isExhausted())
          return s;
        else if (r.isFalsified() || r.isPropException() || r.isGenException())
          throw new Error(s);
        else
          throw Bottom.decons(r.getClass());
      }
    });
  }
}
