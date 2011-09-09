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
  final Total totalAtomsNoBonds = new Total();

  private final Map<String, Integer> moleculeSet = new HashMap<String, Integer>();
  private final Set<SquirmCell> atomsWithBondsSet = new HashSet<SquirmCell>();
  private final Map<String, Integer> atomStateSet = new HashMap<String, Integer>();
  private final Map<String, Integer> reactionSet = new HashMap<String, Integer>();
  private final Map<String, Integer> simpleBondSet = new HashMap<String, Integer>();
  
  private final static String plotSpacer = "    ";
  
  private String [] investigateAtomTypes = { "e", "f", "a", "b", "c", "d" };
  private String [] investigateStates = { "0", "3", "8", "9", "2" };
  private String [] investigateSimpleBonds = { "e2e2", "c1f1", "b1c1", "a2e2", "e2e3", "a1b1", "e2a5", "e8a1", "b9c1" };
  private String [] investigateReactions = { "x3/y6->x2", "x5/x0->x7", "e8/e0->e4" }; 
     
  private final static String PLOT_FOR_TOT_BOND_COUNT = "plotBondCount";
  private final static String PLOT_FOR_ATOM_STATE_LINE = "plotAtomStateLine";
  private final static String PLOT_FOR_SIMPLE_BONDS = "plotSimpleBonds";
  private final static String PLOT_FOR_REACTIONS = "plotReactions";  
  
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
      this.delegateTotalAtomsWithBonds(cell, totalAtomsWithBonds, totalAtomsNoBonds, numberOfAtoms);
      this.delegateFindMoleculeString(cell);
      this.delegateReactions(cell);
      this.touchAtomState(cell);
    } // End of for //

    // Iterate through the bonds that have connections
    int totalVisitsNotForConnections = 0;
    for (final SquirmCell cellWithBond : atomsWithBondsSet) {
      final Set<SquirmCell> visited = new HashSet<SquirmCell>();
      final Mutable<Integer> visits = new Mutable<Integer>(0);
      findMoleculeString(cellWithBond, 0, visited, visits);
      totalVisitsNotForConnections += visits.get();      
      final StringBuffer buf = new StringBuffer();
      for (final SquirmCell nodeAfterFindMol : visited) {
        buf.append(nodeAfterFindMol.getStringType());
      }      
      touchMapEntry(buf.toString(), this.moleculeSet);
    } // End of the for //
    LOGGER.info("    totalVisitsNotForConnections=" + totalVisitsNotForConnections);    
    final StringBuffer statsBuf = new StringBuffer();
    statsBuf.append("<ArtificialChemistryStatistics>");
    statsBuf.append(NL);

    final int simCounter = bidirectionalGridObjectRef.getCount();
    statsBuf.append(" counter=").append(bidirectionalGridObjectRef.getCount()).append(NL);
    statsBuf.append(" numberOfAtoms=").append(numberOfAtoms).append(NL);
    statsBuf.append(" totalAtomsWithBonds=").append(totalAtomsWithBonds).append(NL);
    statsBuf.append(" totalAtomsWithoutBonds=").append(totalAtomsNoBonds).append(NL);          
    {
      int icountForMolecules = 0; final int tailMaxMolecules = 12;
      final List<Map.Entry<String, Integer>> listForSort = sortMap(this.moleculeSet);
      for (final Map.Entry<String, Integer> e : listForSort) {
        statsBuf.append("  molecule=").append(e).append(NL);
        icountForMolecules++;
        if (icountForMolecules >= tailMaxMolecules) {
          break;
        }
      } // End of for through molecule map //
    } // End of section for molecule set    
    {
      int icount = 0; final int tailMax = 12;
      final List<Map.Entry<String, Integer>> listForSort = sortMap(this.atomStateSet);
      for (final Map.Entry<String, Integer> e : listForSort) {
        statsBuf.append("  atomStateSet=").append(e).append(NL);
        icount++;
        if (icount >= tailMax) {
          break;
        }
      } // End of for through molecule map //
    } // End of section for atom state set
    
    {
      int icount = 0; final int tailMax = 10;
      final List<Map.Entry<String, Integer>> listForSort = sortMap(this.reactionSet);
      for (final Map.Entry<String, Integer> e : listForSort) {
        statsBuf.append("  reactionSet=").append(e).append(NL);
        icount++;
        if (icount >= tailMax) {
          break;
        }
      } // End of for through molecule map //
    } // End of section for reaction set
    
    {
      int icount = 0; final int tailMax = 14;
      final List<Map.Entry<String, Integer>> listForSort = sortMap(this.simpleBondSet);
      for (final Map.Entry<String, Integer> e : listForSort) {
        statsBuf.append("  simpleBondSet=").append(e).append(NL);
        icount++;
        if (icount >= tailMax) {
          break;
        }
      } // End of for through molecule map //
    } // End of section for bond set
    
    statsBuf.append("  numberMoleculesSet=").append(this.moleculeSet.size()).append(NL);
    statsBuf.append("  numberAtomStates=").append(this.atomStateSet.size()).append(NL);
    statsBuf.append("  numberReactions=").append(this.reactionSet.size()).append(NL);
    statsBuf.append("</ArtificialChemistryStatistics>");
    
    LOGGER.info(statsBuf.toString());
    LOGGER.info(NL);        
    LOGGER.info(NL + PLOT_FOR_TOT_BOND_COUNT + plotSpacer + simCounter + plotSpacer + totalAtomsWithBonds.total + plotSpacer + totalAtomsNoBonds.total);
    LOGGER.info(NL);      
    
    LOGGER.info(NL+this.logPlotAtomState());    
    LOGGER.info(NL+this.logPlotSimpleBonds());
    LOGGER.info(NL+this.logPlotReactions());
  }

  protected void findMoleculeString(final SquirmCell cell, final int level, final Set<SquirmCell> visited,
      final Mutable<Integer> visits) {
    if (cell.getBonds().size() == 0) {
      return;
    }
    final int recurseLoopLevel = 30;
    if (level >= recurseLoopLevel) {
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
  public void delegateReactions(final SquirmCell cell) {
    if (cell.getBonds().size() > 0) {
      if (cell.getLastReaction() != null && cell.getLastReaction().length() > 0) {
        this.touchMapEntry(cell.getLastReaction(), this.reactionSet);    
      }
    }    
  }
  
  /**
   * Total all of the atoms that have bonds greater zero.
   * 
   * @return
   */
  public void delegateTotalAtomsWithBonds(final SquirmCell cell, final Total totals, final Total totalsWithout, final int outOfTotal) {
    if (cell.getBonds().size() > 0) {
      atomsWithBondsSet.add(cell);
      totals.total++;
    } else {
      totalsWithout.total++;
    }
    totals.dtotal = 100.0 * (totals.total / (double) outOfTotal);
    totals.stotal = String.format("%d/%.3f%%", totals.total, totals.dtotal);    
    totalsWithout.dtotal = 100.0 * (totalsWithout.total / (double) outOfTotal);
    totalsWithout.stotal = String.format("%d/%.3f%%", totalsWithout.total, totalsWithout.dtotal);    
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

    String simpleBondKey = cell.getStringType()+cell.getState();
    for (final SquirmCell node : cell.getBonds()) {
      buf.append(node.getStringType());
      simpleBondKey += node.getStringType()+node.getState();
            
      this.touchMapEntry(simpleBondKey, this.simpleBondSet);
      
      // Reset the bond key:
      simpleBondKey = cell.getStringType()+cell.getState();
    } // End of the for //
  }
  
  public void touchMapEntry(final String key, final Map<String, Integer> map) {
    if (map.get(key) == null) {
      map.put(key, 1);
    } else {
      map.put(key, map.get(key) + 1);
    }
  }
  
  /**
   * Determine count of atom states.
   * 
   * @param cell
   */
  public void touchAtomState(final SquirmCell cell) {
    final String type = cell.getStringType() + cell.getState();
    for (final String atomType : investigateAtomTypes) {
      for (final String state : investigateStates) {
        final String chk = atomType + state;
        if (chk.equalsIgnoreCase(type)) {
          this.touchMapEntry(chk, atomStateSet);
        }
      }
    } // End of the for //
  }
  
  protected String logPlotSimpleBonds() {
    final StringBuffer buf = new StringBuffer();   
    buf.append("##" + plotSpacer + plotSpacer + plotSpacer + "counter" + plotSpacer);
    for (final String bondName : investigateSimpleBonds) {
      buf.append(bondName+"  ");
    }    
    final int simCounter = bidirectionalGridObjectRef.getCount();
    buf.append(NL + PLOT_FOR_SIMPLE_BONDS + plotSpacer);
    buf.append(simCounter + plotSpacer);
        
    for (final String bondName : investigateSimpleBonds) {
      buf.append(( simpleBondSet.get(bondName) == null ? 0 : simpleBondSet.get(bondName)  ) +plotSpacer);
    }
    
    return buf.toString();
  }  
  
  protected String logPlotReactions() {
    final StringBuffer buf = new StringBuffer();   
    buf.append("##" + plotSpacer + plotSpacer + plotSpacer + "counter" + plotSpacer);
    for (final String name : investigateReactions) {
      buf.append(name+"  ");
    }    
    final int simCounter = bidirectionalGridObjectRef.getCount();
    buf.append(NL + PLOT_FOR_REACTIONS + plotSpacer);
    buf.append(simCounter + plotSpacer);
        
    for (final String name : investigateReactions) {
      buf.append(( reactionSet.get(name) == null ? 0 : reactionSet.get(name)  ) +plotSpacer);
    }
    
    return buf.toString();
  }  
  
  protected String logPlotAtomState() {
    final StringBuffer buf = new StringBuffer();    
    buf.append("##" + plotSpacer + plotSpacer + plotSpacer + "counter" + plotSpacer);
    for (final String atomType : investigateAtomTypes) {      
      for (final String state : investigateStates) {
        final String chk = atomType + state;
        buf.append(chk + "  ");        
      }
    } // End of the for //
    final int simCounter = bidirectionalGridObjectRef.getCount();
    buf.append(NL + PLOT_FOR_ATOM_STATE_LINE + plotSpacer);
    buf.append(simCounter + plotSpacer);
    
    for (final String atomType : investigateAtomTypes) {
      for (final String state : investigateStates) {
        final String chk = atomType + state;
        if (atomStateSet.get(chk) == null) {
          buf.append("0" + plotSpacer);
        } else {
          buf.append(atomStateSet.get(chk) + plotSpacer);
        }
      }
    } // End of the for //
    return buf.toString();
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
