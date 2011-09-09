#!/bin/sh

# Log plot on Y

OUTPUT_IMG_FILE=plot_bond_count.png

grep 'plotBondCount' 'logs/cell_simulation.log' > data_plotBondCount.dat

echo "set terminal pngcairo size 1024,768
set output '${OUTPUT_IMG_FILE}'
set title 'Plot Bond Count By Iterations (# bonds incr over time)'
set size 1,1
set key left top
set autoscale
set xlabel 'Iteration'
set ylabel 'TotalBonds'
set style fill pattern
set style histogram clustered
set xtic rotate by -45 scale 0.8   
set x2tics
set grid
set log y  
plot  \\
 'data_plotBondCount.dat' u 2:3 t 'Bonds' w linespoints,  \\
 '' u 2:4 t 'NoBonds' w linespoints
" > gnuplot_tmp_cmd.tmp
gnuplot gnuplot_tmp_cmd.tmp > /dev/null
