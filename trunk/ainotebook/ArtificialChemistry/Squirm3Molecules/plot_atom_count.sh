#!/bin/sh

# Log plot on Y

OUTPUT_IMG_FILE=plot_atom_state_line.png

grep 'plotAtomStateLine' 'logs/cell_simulation.log' > data_plotStateLine.dat

echo "set terminal pngcairo size 1024,768
set output '${OUTPUT_IMG_FILE}'
set title 'Plot Atom State By Iterations'
set size 1,1
set key left top
set autoscale
set xlabel 'Iteration'
set ylabel 'NumberAtAtomState'
set style fill pattern
set style histogram clustered
set xtic rotate by -45 scale 0.8   
set x2tics
set grid
set log y  
plot  \\
 'data_plotStateLine.dat' u 2:3 t 'e0' w linespoints,  \\
 '' u 2:4 t 'e3' w linespoints,  \\
 '' u 2:8 t 'e8' w linespoints,  \\
 '' u 2:13 t 'a0' w linespoints \\
" > gnuplot_tmp_cmd.tmp
gnuplot gnuplot_tmp_cmd.tmp > /dev/null
