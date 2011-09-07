/**
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *  
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Copyright Tim Hutton and Berlin Brown <berlin dot brown at gmail.com> 2011
 *  
 * Tim Hutton is the original author, but a license not provided in source,
 * GPL was used for similar projects.  If Tim or anyone else has questions, please contact Berlin Brown.
 *
 * http://www.sq3.org.uk/Evolution/Squirm3/
 * 
 * Keywords: artificial chemistry simulation in java
 */
package org.squirm.chem;

import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;

import org.apache.log4j.Logger;

/**
 * Collect statistics from the squirm cell grid.
 */
public class Statistics {

  private static final String NL = System.getProperty("line.separator");
  private static final Logger LOGGER = Logger.getLogger(Statistics.class);

  private final SquirmGrid bidirectionalGridObjectRef;

  int numberOfAtoms = 0;
  final Total totalAtomsWithBonds = new Total();

  private final Map<String, Integer> moleculeSet = new HashMap<String, Integer>();
  private final Set<SquirmCell> atomsWithBondsSet = new HashSet<SquirmCell>();

  /**
   * Constructor.
   * 
   * @param bidirectionalGridObjectRef
   */
  public Statistics(final SquirmGrid bidirectionalGridObjectRef) {
    this.bidirectionalGridObjectRef = bidirectionalGridObjectRef;
  } // End of the statistics //

  public void logStats() {

    // Visit each element and perform delegation
    numberOfAtoms = this.bidirectionalGridObjectRef.getCellList().size();

    for (final SquirmCell cell : bidirectionalGridObjectRef.getCellList()) {
      delegateTotalAtomsWithBonds(cell, totalAtomsWithBonds, numberOfAtoms);
      delegateFindMoleculeString(cell);
    } // End of for //

    // Iterate through the bonds that have connections
    for (final SquirmCell cellWithBond : atomsWithBondsSet) {
      final Set<SquirmCell> visited = new HashSet<SquirmCell>();
      final Mutable<Integer> visits = new Mutable<Integer>(0);
      findMoleculeString(cellWithBond, 0, visited, visits);
      System.out.println("    Visits : " + visits);
      final StringBuffer buf = new StringBuffer();
      for (final SquirmCell nodeAfterFindMol : visited) {
        buf.append(nodeAfterFindMol.getStringType());
      }
      LOGGER.info("     After visiting : molecule string=" + buf.toString());
      touchMapEntry(buf.toString(), this.moleculeSet);
    } // End of the for //

    final StringBuffer statsBuf = new StringBuffer();
    statsBuf.append("<ArtificialChemistryStatistics>");
    statsBuf.append(NL);

    statsBuf.append(" counter=").append(bidirectionalGridObjectRef.getCount()).append(NL);
    statsBuf.append(" numberOfAtoms=").append(numberOfAtoms).append(NL);
    statsBuf.append(" totalAtomsWithBonds=").append(totalAtomsWithBonds).append(NL);

    int icountForMolecules = 0;
    final int tailMaxMolecules = 8;
    final List<Map.Entry<String, Integer>> listForSort = sortMap(this.moleculeSet);
    for (final Map.Entry<String, Integer> e : listForSort) {
      statsBuf.append("  molecule=").append(e).append(NL);
      icountForMolecules++;
      if (icountForMolecules >= tailMaxMolecules) {
        break;
      }
    } // End of for through molecule map //
    statsBuf.append("  numberMoleculesSet=").append(this.moleculeSet.size()).append(NL);
    statsBuf.append("</ArtificialChemistryStatistics>");
    LOGGER.info(statsBuf.toString());
  }

  protected void findMoleculeString(final SquirmCell cell, final int level, final Set<SquirmCell> visited,
      final Mutable<Integer> visits) {
    if (cell.getBonds().size() == 0) {
      return;
    }
    if (level >= 30) {
      return;
    }
    visited.add(cell);
    final Random rand = new Random();
    for (final SquirmCell node : cell.getBonds()) {
      final double rv = rand.nextDouble();
      if (rv >= 0.2) {
        if (visited.contains(node)) {
          continue;
        }
      }
      visited.add(node);
      visits.set(visits.get() + 1);
      findMoleculeString(node, level + 1, visited, visits);
    }
  }

  /**
   * Total all of the atoms that have bonds greater zero.
   * 
   * @return
   */
  public void delegateTotalAtomsWithBonds(final SquirmCell cell, final Total totals, final int outOfTotal) {
    if (cell.getBonds().size() > 0) {
      atomsWithBondsSet.add(cell);
      totals.total++;
    }
    totals.dtotal = 100.0 * (totals.total / (double) outOfTotal);
    totals.stotal = String.format("%d/%.3f%%", totals.total, totals.dtotal);
  }

  /**
   * Search this cell's bonds to build a molecule string.
   */
  public void delegateFindMoleculeString(final SquirmCell cell) {
    if (cell.getBonds().size() == 0) {
      return;
    }
    final StringBuffer buf = new StringBuffer();
    buf.append(cell.getStringType());

    for (final SquirmCell node : cell.getBonds()) {
      buf.append(node.getStringType());
    } // End of the for //
  }

  public void touchMapEntry(final String key, final Map<String, Integer> map) {
    if (map.get(key) == null) {
      map.put(key, 1);
    } else {
      map.put(key, map.get(key) + 1);
    }
  }

  public class Total implements Serializable {
    private static final long serialVersionUID = 1L;
    private int total = 0;
    private double dtotal = 0;
    private String stotal = "";

    public String toString() {
      return stotal;
    }
  }

  /**
   * Simple class to modify one element. This can be used to modify primitives
   * to methods have final arguments.
   * 
   * @param <T>
   */
  public static final class Mutable<T> {
    private T mutable;

    /**
     * Constructor.
     * 
     * @param m
     */
    public Mutable(final T m) {
      this.mutable = m;
    }

    /**
     * Set the mutable.
     * 
     * @param val
     * @return
     */
    public synchronized T set(final T val) {
      this.mutable = val;
      return mutable;
    }

    public synchronized T get() {
      return mutable;
    }

    @Override
    public String toString() {
      return String.valueOf(mutable);
    }
  } // End of class //

  protected <K, V extends Comparable<? super V>> List<Entry<K, V>> sortMap(final Map<K, V> map) {
    List<Map.Entry<K, V>> list = new LinkedList<Map.Entry<K, V>>(map.entrySet());
    Collections.sort(list, new Comparator<Map.Entry<K, V>>() {
      public int compare(Map.Entry<K, V> o1, Map.Entry<K, V> o2) {
        return -1 * o1.getValue().compareTo(o2.getValue());
      }
    });
    return list;
  }

} // End of the statistics.
