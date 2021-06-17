#!/bin/bash


for file in *.wcnf
do
	#echo $file
	
	awk ' {if($1=="p") {print ($5)}} '  $file > tmp
	
	awk ' {if($1=="p") { $5="" ; print $0}} '  $file > filtered/$file.cnf
	
	read val < tmp

	#echo "hello $val"

	
	awk -v var=$val  '{if($1 == var ) { $1=""  ;print $0 }} '  $file  >> filtered/$file.cnf 


		


done
