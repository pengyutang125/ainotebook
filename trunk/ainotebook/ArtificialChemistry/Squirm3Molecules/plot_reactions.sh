#!/bin/sh

# Log plot on Y

OUTPUT_IMG_FILE=plot_reactions.png

grep 'plotReactions' 'logs/cell_simulation.log' > data_reactions.dat

echo "set terminal pngcairo size 1024,768
set output '${OUTPUT_IMG_FILE}'
set title 'Plot Reactions By Iterations (some reactions more active)'
set size 1,1
set key left top
set autoscale
set xlabel 'Iteration'
set ylabel 'TotalReactionsByType'
set style fill pattern
set style histogram clustered
set xtic rotate by -45 scale 0.8   
set x2tics
set grid
set log y  
plot  \\
 'data_reactions.dat' u 2:3 t 'R1:x3_y6->x2' w linespoints,  \\
 '' u 2:4 t 'R2:x5/x0->x7' w linespoints, \\
 '' u 2:5 t 'R3:e8/e0->e4' w linespoints
" > gnuplot_tmp_cmd.tmp
gnuplot gnuplot_tmp_cmd.tmp > /dev/null
