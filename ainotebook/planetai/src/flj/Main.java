package flj;


/**
 * Checks the properties of a class using a standard random generator, standard check parameters and
 * the given categories. The class name and categories are passed as command line arguments. 
 *
 * @version 2.20<br>
 *          <ul>
 *          <li>$LastChangedRevision: 5 $</li>
 *          <li>$LastChangedDate: 2008-12-06 16:49:43 +1000 (Sat, 06 Dec 2008) $</li>
 *          <li>$LastChangedBy: tonymorris $</li>
 *          </ul>
 */
public final class Main {
  private Main() {
    throw new UnsupportedOperationException();
  }

  /**
   * Check the given class and categories. At least one command line argument (the class name) must be
   * passed or an error message results.
   *
   * @param args The class name as the first argument, then zero or more categories.
   */
  public static void main(final String... args) {
    if(args.length == 0) {
      System.err.println("<class> [category]*");
      System.exit(441);
    } else {
      try {
        Check.check(Class.forName(args[0]), Array.array(args).toList().tail()).foreach(new Effect<P2<String, CheckResult>>() {
          public void e(final P2<String, CheckResult> r) {
            CheckResult.summary.print(r._2());
            System.out.println(" (" + r._1() + ')');
          }
        });
      } catch(ClassNotFoundException e) {
        System.err.println(e);        
        System.exit(144);
      }
    }
  }
}
