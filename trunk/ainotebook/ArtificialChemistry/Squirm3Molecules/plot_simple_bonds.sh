#!/bin/sh

# Log plot on Y

OUTPUT_IMG_FILE=plot_simple_bonds.png

grep 'plotSimpleBonds' 'logs/cell_simulation.log' > data_simple_bonds.dat

echo "set terminal pngcairo size 1024,768
set output '${OUTPUT_IMG_FILE}'
set title 'Plot Common Bonds By Iterations (note some bonds incr)'
set size 1,1
set key left top
set autoscale
set xlabel 'Iteration'
set ylabel 'Total Common Bonds'
set style fill pattern
set style histogram clustered
set xtic rotate by -45 scale 0.8   
set x2tics
set grid
set log y  
plot  \\
 'data_simple_bonds.dat' \\
    u 2:3  t 'e2e2' w linespoints, \\
 '' u 2:4  t 'c1f1' w linespoints, \\
 '' u 2:5  t 'b1c1' w linespoints, \\
 '' u 2:6  t 'a2e2' w linespoints, \\
 '' u 2:7  t 'e2e3' w linespoints, \\
 '' u 2:8  t 'a1b1' w linespoints, \\
 '' u 2:9  t 'e2a5' w linespoints, \\
 '' u 2:10 t 'e8a1' w linespoints, \\
 '' u 2:11 t 'b9c1' w linespoints  \\
" > gnuplot_tmp_cmd.tmp
gnuplot gnuplot_tmp_cmd.tmp > /dev/null
