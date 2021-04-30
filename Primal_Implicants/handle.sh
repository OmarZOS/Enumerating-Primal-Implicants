#!/bin/bash


# echo "5 " > temp1
cadical tset2.cnf | grep ^v | sed 's/v//' > temp1
echo ' k' >> temp1
# cat temp1 | grep ^c | wc -l > commentCount
# cat temp1
java Exhaust < temp1
cp tset2.cnf tmp/tset2.cnf
awk '{if($1=="p") print $1,$2,$3,$4+1; else print $0}' tmp/tset2.cnf > tset2.cnf


