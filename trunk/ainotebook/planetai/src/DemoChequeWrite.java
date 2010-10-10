public final class DemoChequeWrite {
    
  private DemoChequeWrite() {}

  static List<Integer> toZero(final int from) {
    return List.unfold(new F<Integer, Option<P2<Integer, Integer>>>() {
      public Option<P2<Integer, Integer>> f(final Integer i) {
        return i < 0 ? Option.<P2<Integer, Integer>>none() : Option.some(P.p(i, i - 1));
      }
    }, from);
  }

  static int signum(final int i) {
    return i == 0 ? 0 : i < 0 ? -1 : 1;
  }

  static List<Character> show(final char c) {
    return Show.stringShow
        .show(List.list("zero", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine").index(c - '0'))
        .toList();
  }

  static List<Character> show(final List<Character> cs) {
    if (cs.isEmpty())
      return List.nil();
    else {
      final char d1 = cs.head();
      final List<Character> d1r = cs.tail();

      if (d1r.isEmpty())
        return show(d1);
      else {
        final char d2 = d1r.head();
        final List<Character> d2r = d1r.tail();

        return d2r.isEmpty()
               ? d1 == '0'
                 ? show(d2)
                 : d1 == '1'
                   ? Show.stringShow.showl(
                     List.list("ten", "eleven", "twelve", "thirteen", "fourteen", "fifteen", "sixteen", "seventeen",
                          "eighteen", "nineteen").index(d2 - '0'))
                   : Show.stringShow.showl(
                       List.list("twenty", "thirty", "forty", "fifty", "sixty", "seventy", "eighty", "ninety").index(
                           d1 - '0' - 2))
                       .append(d2 == '0' ? List.<Character>nil() : show(d2).cons('-'))
               : d1 == '0' && d2 == '0' && d2r.head() == '0'
                 ? List.<Character>nil()
                 : d1 == '0'
                   ? show(List.list(d2, d2r.head()))
                   : d2 == '0' && d2r.head() == '0'
                     ? show(d1).append(Show.stringShow.showl(" hundred"))
                     : show(d1).append(Show.stringShow.showl(" hundred and ")).append(show(List.list(d2, d2r.head())));
      }
    }
  }

  static <A> List<P2<List<A>, Integer>> split(final List<A> as) {
    final int len = as.length();

    final List<List<A>> ds = as.zip(toZero(len - 1)).foldRight(new F2<P2<A, Integer>, List<List<A>>, List<List<A>>>() {
      public List<List<A>> f(final P2<A, Integer> ki, final List<List<A>> z) {
        return ki._2() % 3 == 0 ? z.conss(List.single(ki._1())) : z.tail().conss(z.head().cons(ki._1()));
      }
    }, List.<List<A>>nil());

    return ds.zip(toZero(len / 3 + signum(len % 3) - 1));
  }

  static List<Character> illion(final int i) {
    return Show.stringShow.show(
        Stream.stream("thousand", "million", "billion", "trillion", "quadrillion", "quintillion", "sextillion", "septillion",
               "octillion", "nonillion", "decillion", "undecillion", "duodecillion", "tredecillion",
               "quattuordecillion", "quindecillion", "sexdecillion", "septendecillion", "octodecillion",
               "novemdecillion", "vigintillion")
            .append(Stream.repeat("<unsupported ?illion>")).index(i)).toList();
  }

  static boolean and(final List<Character> cs) {
    return cs.length() == 3 && cs.head() == '0' && (cs.tail().head() != '0' || cs.tail().tail().head() != '0');
  }

  static boolean existsNotZero(final List<Character> cs) {
    return cs.exists(new F<Character, Boolean>() {
      public Boolean f(final Character c) {
        return c != '0';
      }
    });
  }

  static boolean eq(final List<Character> a, final List<Character> b) {
    return Equal.listEqual(Equal.charEqual).eq(a, b);
  }

  static final F<List<Character>, List<Character>> dollars = new F<List<Character>, List<Character>>() {
    public List<Character> f(final List<Character> cs) {
      if (cs.isEmpty())
        return List.fromString("zero dollars");
      else {
        final List.Buffer<List<Character>> x = new List.Buffer<List<Character>>();

        final List<P2<List<Character>, Integer>> k = split(cs);
        final int c = k.head()._2();

        k.foreach(new Effect<P2<List<Character>, Integer>>() {
          public void e(final P2<List<Character>, Integer> z) {
            final List<Character> w = z._1();
            final int i = z._2();

            if (i == 0 && c > 0 && and(w))
              x.snoc(List.fromString("and"));

            if (existsNotZero(w)) {
              x.snoc(show(w));
              if (i != 0)
                x.snoc(illion(i - 1));
            }
          }
        });

        x.snoc(List.fromString(eq(cs, List.list('1')) ? "dollar" : "dollars"));

        return List.fromString(" ").intercalate(x.toList());
      }
    }
  };

  static final F<List<Character>, List<Character>> cents = new F<List<Character>, List<Character>>() {
    public List<Character> f(final List<Character> a) {
      final int n = a.length();
      return n == 0
             ? List.fromString("zero cents")
             : show(List.list(a.head(), n == 1 ? '0' : a.tail().head()))
                 .append(List.fromString(eq(a, List.list('0', '1')) ? " cent" : " cents"));
    }
  };

  public static List<Character> write(final List<Character> cs) {
    final F<List<Character>, List<Character>> dropNonDigit = new F<List<Character>, List<Character>>() {
      public List<Character> f(final List<Character> cs) {
        return cs.filter(Characters.isDigit);
      }
    };
    final P2<List<Character>, List<Character>> x =
        cs.dropWhile(Equal.charEqual.eq('0')).breakk(Equal.charEqual.eq('.')).map1(dropNonDigit).map1(dollars).map2(dropNonDigit)
            .map2(List.<Character>take().f(2)).map2(cents);
    return x._1().append(List.fromString(" and ")).append(x._2());
  }

  public static void main(final String[] args) {
    if (args.length == 1) {
      tests();
    } else if (args.length == 0) {
        final String [] testargs = new String [] {
                "0",
                "1", 
                "1.",
                "0.", 
                "0.0", 
                "1.0", 
                "a1a", 
                "a1a.a0.7b",
                "100", 
                "100.45", 
                "100.07",    
        };  
        for (final String a : testargs)
            System.out.println(List.asString(write(List.fromString(a))));        
    } else
      for (final String a : args)
        System.out.println(List.asString(write(List.fromString(a))));
  }

  @SuppressWarnings("unchecked")
public static void tests() {
    // show
    for (final P2<String, String> t : List.list(
        P.p("1", "one"),
        P.p("10", "ten"),
        P.p("15", "fifteen"),
        P.p("40", "forty"),
        P.p("45", "forty-five"))) {
      assert eq(show(List.fromString(t._1())), List.fromString(t._2()));
    }

    // write
    for (final P2<String, String> t : List.list(
        P.p("0", "zero dollars and zero cents"),
        P.p("1", "one dollar and zero cents"),
        P.p("1.", "one dollar and zero cents"),
        P.p("0.", "zero dollars and zero cents"),
        P.p("0.0", "zero dollars and zero cents"),
        P.p("1.0", "one dollar and zero cents"),
        P.p("a1a", "one dollar and zero cents"),
        P.p("a1a.a0.7b", "one dollar and seven cents"),
        P.p("100", "one hundred dollars and zero cents"),
        P.p("100.45", "one hundred dollars and forty-five cents"),
        P.p("100.07", "one hundred dollars and seven cents"),
        P.p("9abc9def9ghi.jkl9mno", "nine hundred and ninety-nine dollars and ninety cents"),
        P.p("12345.67", "twelve thousand three hundred and forty-five dollars and sixty-seven cents"),
        P.p("456789123456789012345678901234567890123456789012345678901234567890.12",
          "four hundred and fifty-six vigintillion seven hundred and eighty-nine novemdecillion one hundred and twenty-three octodecillion four hundred and fifty-six septendecillion seven hundred and eighty-nine sexdecillion twelve quindecillion three hundred and forty-five quattuordecillion six hundred and seventy-eight tredecillion nine hundred and one duodecillion two hundred and thirty-four undecillion five hundred and sixty-seven decillion eight hundred and ninety nonillion one hundred and twenty-three octillion four hundred and fifty-six septillion seven hundred and eighty-nine sextillion twelve quintillion three hundred and forty-five quadrillion six hundred and seventy-eight trillion nine hundred and one billion two hundred and thirty-four million five hundred and sixty-seven thousand eight hundred and ninety dollars and twelve cents"))) {
      assert eq(write(List.fromString(t._1())), List.fromString(t._2()));
    }
  }
}