import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

public class parser{

    public static void main(String args[]) throws IOException{

        BufferedReader dataFile;
        String testDir1="test3/";
        String testDir2="test5/";

        
        File testDirectoryV1 = new File(testDir1);
        File[] filesV1 = testDirectoryV1.listFiles();

        File testDirectoryV2 = new File(testDir2);
        File[] filesV2 = testDirectoryV2.listFiles();

        Map<Integer,MyFile> instances = new HashMap<Integer,MyFile>();


        for(File file : filesV1 ){ 
            
            dataFile = new BufferedReader(new FileReader(testDirectoryV1+"/"+file.getName()));
            
            MyFile myfile  = new MyFile();

            String curLine=dataFile.readLine();
            while(curLine!=null){    
                StringTokenizer st= new StringTokenizer(curLine);
                

                String x= st.nextToken();
                // System.out.println(x);

                switch(x){
                    case "OriginalVariableCount" :
                        myfile.varCount= Integer.parseInt(st.nextToken());
                        break; 
                    case "OriginalClauseCount" :
                        myfile.clauseCount= Integer.parseInt(st.nextToken());
                        break; 
                    case "ModifiedVariableCountV1" :
                        myfile.varCountV1= Integer.parseInt(st.nextToken());
                        break; 
                    case "ModifiedClauseCountV1" :
                        myfile.clauseCountV1= Integer.parseInt(st.nextToken());
                        break; 
                    case "sol" :
                        myfile.sol1= Integer.parseInt(st.nextToken());
                        break; 
                    case "real" :
                        myfile.time1= Float.parseFloat(st.nextToken().replace(",", "."));
                        break; 
                    }
                    
                    curLine=dataFile.readLine();
            }
            instances.put(myfile.varCount,myfile);
        }

        for(File file : filesV2 ){ 
            
            dataFile = new BufferedReader(new FileReader(testDirectoryV2+"/"+file.getName()));
            
            MyFile myfile  = new MyFile();

            String curLine=dataFile.readLine();
            while(curLine!=null){    
                StringTokenizer st= new StringTokenizer(curLine);



                switch(st.nextToken()){
                    case "OriginalVariableCount" :
                        myfile.varCount= Integer.parseInt(st.nextToken());
                        break; 
                    case "OriginalClauseCount" :
                        myfile.clauseCount= Integer.parseInt(st.nextToken());
                        break; 
                    case "ModifiedVariableCountV2" :
                        myfile.varCountV2= Integer.parseInt(st.nextToken());
                        break; 
                    case "ModifiedClauseCountV2" :
                        myfile.clauseCountV2= Integer.parseInt(st.nextToken());
                        break; 
                    case "sol" :
                        myfile.sol2= Integer.parseInt(st.nextToken());
                        break; 
                    case "real" :
                        myfile.time2= Float.parseFloat(st.nextToken().replace(",", "."));
                        break; 
                    }
                    
                    curLine=dataFile.readLine();
            }
            instances.get(myfile.varCount).varCountV2=myfile.varCountV2;
            instances.get(myfile.varCount).clauseCountV2=myfile.clauseCountV2;
            instances.get(myfile.varCount).sol2=myfile.sol2;
            instances.get(myfile.varCount).time2=myfile.time2;
        }
        
int count=0;
        for (MyFile file : instances.values()) {
            file.display();
            count++;
        }

// System.out.println("instance size "+instances.size()+"  "+count);


    }


 /*


15tree901p & 38016 & 153542 & 6.94 & 5.8 & 10.98 & 4.87 \\
15tree201posib & 38599 & 151681 & 6.72 & 5.71 & 10.65 & 4.83 \\
10tree315p & 6666 & 25459 & 6.76 & 5.45 & 10.58 & 4.88 \\
10tree515p & 6738 & 25699 & 6.69 & 5.41 & 10.5 & 4.85 \\
15tree801posib & 39079 & 151394 & 6.57 & 5.65 & 10.45 & 4.79 \\
normalized-ii16b1 & 3456 & 26520 & 6.72 & 10.17 & 14.35 & 4.01 \\
normalized-fir01 area opers & 194 & 292 & 3.63 & 6.84 & 5.13 & 5.76 \\
normalized-fir09 area partials & 5060 & 20188 & 6.11 & 21.53 & 10.1 & 4.72 \\
normalized-ii16b2 & 2152 & 17197 & 7.28 & 10.64 & 15.24 & 4.09 \\
normalized-par32-4-c & 2666 & 6659 & 7.57 & 6.66 & 10.07 & 6.17 \\
normalized-fir10 area partials & 2091 & 7288 & 5.68 & 16.96 & 9.17 & 4.92 \\
normalized-fir05 area partials & 556 & 1561 & 4.97 & 26.82 & 7.77 & 5.13 \\
normalized-par32-2-c & 2606 & 6509 & 7.56 & 6.65 & 10.06 & 6.17 \\
normalized-f20c10b 003 area delay & 7343 & 22903 & 4.4 & 8.49 & 7.52 & 4.42 \\
normalized-f20c10b 025 area delay & 16199 & 117024 & 16.92 & 9.45 & 24.15 & 5.36 \\
normalized-f20c10b 020 area delay & 4427 & 12661 & 4.33 & 7.26 & 7.19 & 4.57 \\
normalized-f20c10b 014 area delay & 5256 & 15614 & 4.41 & 7.39 & 7.38 & 4.56 \\
normalized-ii32b2 & 522 & 2819 & 16.29 & 82.89 & 21.69 & 10.04 \\
normalized-ii32e5 & 1044 & 12158 & 28.98 & 72.29 & 40.62 & 8.8 \\
normalized-ii32e3 & 660 & 5350 & 24.6 & 87.08 & 32.71 & 10.26 \\
normalized-s3-3-3-2pb & 1252 & 3211 & 8.29 & 7.17 & 10.86 & 6.45 \\
normalized-m100 100 90 90.r & 100 & 100 & 92.0 & 8014.0 & 37.5 & 106.5 \\
normalized-m50 100 70 70.r & 100 & 50 & 37.0 & 4837.0 & 37.5 & 213.0 \\
normalized-s3-3-3-3pb & 1124 & 2875 & 8.25 & 7.16 & 10.81 & 6.44 \\
normalized-max1024.pi & 1317 & 978 & 6.0 & 43.41 & 6.74 & 18.04 \\
normalized-fir04 area delay & 741 & 2072 & 4.6 & 8.94 & 7.32 & 4.86 \\
normalized-ssa7552-159 & 2726 & 4395 & 2.77 & 3.9 & 4.39 & 4.53 \\
normalized-hanoi4 & 1436 & 5652 & 6.95 & 4.43 & 10.88 & 4.45 \\
splitedReads 158.matrix & 16983 & 41604 & 9.34 & 8.22 & 11.79 & 7.22 \\
splitedReads 0.matrix & 12962 & 41160 & 11.52 & 7.94 & 14.7 & 6.94 \\
SU3 simp-genos.haps.72 & 6988 & 128910 & 30.77 & 4.6 & 49.22 & 4.26 \\
simp-ibd 50.07 & 2917 & 45610 & 27.42 & 5.37 & 43.05 & 4.41 \\
simp-ibd 50.02 & 2763 & 41115 & 25.37 & 5.1 & 40.25 & 4.34 \\
simp-test chr10 JPT 75 & 474 & 3069 & 12.58 & 5.5 & 19.06 & 4.75 \\
simp-ibd 50.04 & 3065 & 51677 & 29.13 & 5.13 & 45.99 & 4.36 \\




    inst & 3456 & 26520 & 6.7222223 & 10.177677 & 14.356482 & 4.014178 \\
    inst & 38016 & 153542 & 6.9484954 & 5.8034153 & 10.987373 & 4.870876 \\
    inst & 194 & 292 & 3.6340206 & 6.842466 & 5.1391754 & 5.767123 \\
    inst & 5060 & 20188 & 6.114427 & 21.53933 & 10.10415 & 4.7200813 \\
    inst & 16199 & 117024 & 16.929193 & 9.456445 & 24.151367 & 5.366395 \\
    inst & 38599 & 151681 & 6.7239046 & 5.717539 & 10.653566 & 4.832075 \\
    inst & 5256 & 15614 & 4.4179983 & 7.391956 & 7.3886986 & 4.5619955 \\
    inst & 522 & 2819 & 16.293104 & 82.89287 & 21.693487 & 10.045052 \\
    inst & 6666 & 25459 & 6.768527 & 5.45752 & 10.587759 & 4.8834596 \\
    inst & 2763 & 41115 & 25.378574 & 5.1056304 & 40.25914 & 4.3473916 \\
    inst & 4427 & 12661 & 4.336119 & 7.2629333 & 7.1960697 & 4.5785484 \\
    inst & 6988 & 128910 & 30.777046 & 4.6059113 & 49.224384 & 4.2690096 \\
    inst & 6738 & 25699 & 6.69442 & 5.41309 & 10.508459 & 4.8560643 \\
    inst & 1044 & 12158 & 28.98276 & 72.29726 & 40.628353 & 8.80556 \\
    inst & 660 & 5350 & 24.60909 & 87.08598 & 32.715153 & 10.262804 \\
    inst & 16983 & 41604 & 9.349232 & 8.224618 & 11.798975 & 7.224618 \\
    inst & 474 & 3069 & 12.586498 & 5.508309 & 19.061182 & 4.75334 \\
    inst & 1436 & 5652 & 6.9512534 & 4.4373674 & 10.883008 & 4.4566526 \\
    inst & 12962 & 41160 & 11.526308 & 7.944752 & 14.701743 & 6.944752 \\
    inst & 1252 & 3211 & 8.295527 & 7.1737776 & 10.860224 & 6.4512615 \\
    inst & 100 & 100 & 92.0 & 8014.0 & 37.5 & 106.5 \\
    inst & 1124 & 2875 & 8.258007 & 7.1606956 & 10.815836 & 6.4427824 \\
    inst & 2917 & 45610 & 27.423037 & 5.372177 & 43.058964 & 4.414427 \\
    inst & 1317 & 978 & 6.0 & 43.416157 & 6.7425966 & 18.046013 \\
    inst & 741 & 2072 & 4.605938 & 8.9483595 & 7.326586 & 4.868726 \\
    inst & 2726 & 4395 & 2.7791636 & 3.90967 & 4.391416 & 4.534926 \\
    inst & 39079 & 151394 & 6.5780597 & 5.6533217 & 10.45211 & 4.79879 \\
    inst & 2152 & 17197 & 7.288104 & 10.640054 & 15.247677 & 4.0919347 \\
    inst & 2666 & 6659 & 7.5746436 & 6.6648145 & 10.072393 & 6.1769032 \\
    inst & 2091 & 7288 & 5.6881876 & 16.962816 & 9.173601 & 4.921652 \\
    inst & 556 & 1561 & 4.971223 & 26.82319 & 7.778777 & 5.13517 \\
    inst & 2606 & 6509 & 7.5648503 & 6.65709 & 10.062548 & 6.171762 \\
    inst & 7343 & 22903 & 4.406237 & 8.493734 & 7.5247173 & 4.424093 \\
    inst & 3065 & 51677 & 29.13 & 5.13 & 45.99 & 4.36 \\







*/











}