echo off
rem assumes you are in folder D:\IJGradle\Recursion\batFiles
rem from which you invoke runDemo or any of the other bat files
rem typical invocation
rem D:\IJGradle\GPP_Demos\batFiles>runDemo MCpi RunSkelMCpi piTest 4 1024 10000
rem where %1 is the jar file to run ; its is assumed it is on a folder of the same name
rem  the csv style output will be written in the folder D:\IJGradle\gppDemos\csvFiles\%1.csv
cd ..
for %%A in (1 2 3 4 5 6 7 8 9 10 ) do (
echo run %%A of %1 with args %2 %3 %4 %5 %6 %7 %8
java -jar .\out\artifacts\%1\%1.jar  %2 %3 %4 %5 %6 %7 %8 >>  .\csvFiles\%1.csv) 2> .\csvFiles\%1.err
cd batFiles

