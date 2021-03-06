import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

class Transformer{
    
    public static void main(String args[]) throws IOException{
        //System.out.println(args.length);
        
        //remember to pass the cnf filename as argument
        Transformer cnf = new Transformer("test.cnf");

        cnf.enumeratePrimalImplicantsV2();
        
        //System.out.println(cnf.getClauses());

        //cnf.printDimacs();
        System.out.print(cnf.getDimacs());
        

    }




    private int varCount=0,originalVarCount=0,clauseCount=0,originalClauseCount=0;
    private Map<Integer,ArrayList<Integer>> varPositionMap=null;
    //the varPositionMap is to find the clauses it participates in
    
    ArrayList<ArrayList<Integer>> clauses=null; 
    ArrayList<ArrayList<Integer>> outputClauses=null;
    //clauses are arrays of arrays..


    Map<Integer,Integer> clauseUnicityVar=null;


    //public Transformer(){}
    public Transformer(String filename) throws IOException{
        
        
        varCount=readVarCount(filename);
        originalVarCount=varCount;//originalVarCount will be there when we are lost..
        
        
        String content = readClauses(filename);
        
        
        setClauses(content);//set the clauses array of arrays 
        
        clauseCount=clauses.size();
        originalClauseCount=clauseCount;
        
    }    
    


    public void setClauses(String content){
        
        String[] clauseTokens= content.split(" 0");
        
        clauses = new ArrayList<ArrayList<Integer>>();
        for(String clauseText : clauseTokens){
            
            StringTokenizer clauseVariables=(new StringTokenizer(clauseText," "));
            ArrayList<Integer> clause = new ArrayList<Integer>();
            while(clauseVariables.hasMoreTokens())
                    clause.add(Integer.parseInt(clauseVariables.nextToken().toString()));
            
            if(clause.size()!=0)
                clauses.add(clause);
                clauseCount++;
            
        }
        
        setVarPositionMap();    //maps literals & clauses they belong to..
        
        
    }   


    public void setVarPositionMap(){
        varPositionMap = new HashMap<Integer,ArrayList<Integer>>();
        
        for(Integer literal=1;literal<=varCount;literal++){
            ArrayList<Integer> locations= new ArrayList<Integer>();
            for (ArrayList<Integer> clauser : clauses) {
                if(clauser.contains(literal)||clauser.contains(-literal))
                    locations.add(clauses.indexOf(clauser));
            }
            varPositionMap.put(literal,locations);
        }
        
    }


    public void printDimacs(){
        System.out.println("p cnf "+varCount+" "+clauseCount);
        
        for (ArrayList<Integer> cls : outputClauses) {
            for (Integer number : cls) {
                System.out.print(number+" ");
            }
            
            System.out.println("0");
        }
    }

    public String getDimacs(){
        String output = "p cnf "+varCount+" "+clauseCount+" \n";

        for (ArrayList<Integer> cls : outputClauses) {
            for (Integer number : cls) {
                output+=number+" ";
            }
            
            output+= "0 \n";
        }



        return output;

    }




    public void enumeratePrimalImplicantsV1(){  //TODO: still didn't make the OR between PROHIBITIONS..
        varCount=originalVarCount*2;//
        clauseCount=0;
        outputClauses=new ArrayList<>();
        for(Integer literal=1;literal<=originalVarCount;literal++){
            Integer currentIndex=2*(literal)-1;//for the positive, +1 for the negative
            
            ArrayList<Integer> anotherlittleClause = new ArrayList<Integer>();
            anotherlittleClause.add(-currentIndex);  //!Xp or !X!p
            anotherlittleClause.add(-(currentIndex+1));
            outputClauses.add(anotherlittleClause);
            clauseCount++;
            
            ArrayList<Integer> locations = varPositionMap.get(literal);

            ArrayList<Integer> positiveClause = new ArrayList<Integer>();
            ArrayList<Integer> negativeClause = new ArrayList<Integer>();
            positiveClause.add(-currentIndex);
            negativeClause.add(-(currentIndex+1));
            
            // System.out.println("starting for "+ literal);
            // System.out.println("searching in "+ locations);
            // boolean fullPositive=false,fullNegative=false;
            for(int clauseIndex : locations ){
                if(clauses.get(clauseIndex).contains(literal)){
                    if(clauses.get(clauseIndex).size()==1){
                        positiveClause.add(currentIndex);
                    }
                    else
                        positiveClause.add(TseitinParam(clauses.get(clauseIndex),literal));
                }

                if(clauses.get(clauseIndex).contains(-literal)){
                    
                    if(clauses.get(clauseIndex).size()==1){
                        negativeClause.add(currentIndex+1);
                    }   
                    else
                        negativeClause.add(TseitinParam(clauses.get(clauseIndex),-literal));
                    
                }
            }
            // System.out.println("finished for "+ literal + " with "+positiveClause+negativeClause);


            
            outputClauses.add(positiveClause);
            clauseCount++;
            outputClauses.add(negativeClause);
            clauseCount++;
            
        }
        //now for the ancient clauses
        for(int i = 0;i<originalClauseCount;i++){   //using
            ArrayList<Integer> littleClause = new ArrayList<Integer>();
            
            for (Integer literal : clauses.get(i)) {
                
                littleClause.add(((literal>0)?literal*2-1:-2*literal)); 
                // System.out.println(littleClause);
            }
            if(littleClause.size()>0){
                
                outputClauses.add(littleClause);
                clauseCount++;
            }
        }
    }


    public Integer TseitinParam(ArrayList<Integer> clause,Integer target){
        
        boolean keep=true;
        if(clause.contains(target)){
            
            if(clause.size()==2){
                for (Integer literal : clause) {
                    if(literal.equals(target))
                        continue;       
                    return -(literal>0?2*literal-1:-2*literal);
                }
            }
            else
                for (Integer literal : clause) {
                        if(literal.equals(target))
                            continue;

                        keep=false;
                        ArrayList<Integer> littleClause = new ArrayList<Integer>();
                        littleClause.add(-(varCount+1));
                        littleClause.add(-(literal>0?2*literal-1:-2*literal));
                        outputClauses.add(littleClause);
                        // System.out.println(littleClause);
                        clauseCount++;

                }
                if(!keep)
                    varCount++;
                return varCount;
            }
            
            System.out.println("program encountered a fatal error");
            System.exit(0);
            return 0;
            // newVars.add(varCount); //keeping this as Phi..
    }

    public int getVarCount(){
        return varCount;
    }
    
    public int getOriginalClauseCount(){
        return clauses.size();
    }

    public int getNewClauseCount(){
        return outputClauses.size();
    }

    public  ArrayList<ArrayList<Integer>> getOriginalClauses(){
        return clauses;
    }

    public ArrayList<ArrayList<Integer>> getOutputClauses(){
        return outputClauses;
    }

    public int getLiteral(int clauseIndex,int position){
        return clauses.get(clauseIndex).get(position);
    }

    public void addClause(int x, int y){    
        //the mapper isn't touched because wwe only care about mapping original literals 
        ArrayList<Integer> littleClause = new ArrayList<Integer>();
        littleClause.add(x);
        littleClause.add(y);
        outputClauses.add(littleClause);
        clauseCount++;
    }

    public void addClause(int x, int y,int z){
        //the mapper isn't touched because we only care about mapping original literals 
        ArrayList<Integer> littleClause = new ArrayList<Integer>();
        littleClause.add(x);
        littleClause.add(y);
        littleClause.add(z);
        outputClauses.add(littleClause);
        clauseCount++;
    }

    public void enumeratePrimalImplicantsV2(){  
        
        clauseUnicityVar=new HashMap<Integer,Integer>();
        outputClauses=new ArrayList<ArrayList<Integer>>();
        clauseCount=0;
        varCount=originalVarCount*2; 
        
        for(int i=1;i<=  originalVarCount;i++){   //using
            
            setPrimalityConstraintOnLiteral(i);
            setPrimalityConstraintOnLiteral(-i);
            
            ArrayList<Integer> littleClause = new ArrayList<Integer>();
            littleClause.add(-(i*2-1));
            littleClause.add(-(i*2));
            outputClauses.add(littleClause);

            
            clauseCount++;
        }
        //now for the ancient clauses
        for(int i = 0;i<originalClauseCount;i++){   //using
            ArrayList<Integer> littleClause = new ArrayList<Integer>();
            
            for (Integer literal : clauses.get(i)) {
                littleClause.add(((literal>0)?literal*2-1:-2*literal)); 
               // System.out.println(littleClause);
            }
            outputClauses.add(littleClause);
            clauseCount++;
            
        }
        
        
    }
    
    public void conditionalAtMostOne(int clauseIndex, int condition)    //credits to: Dr. A.Boudane
    {
        int Zi=getVarCount(); // pour r??cup??rer les variables qui encodent la contrainte de cardinalit??
        int nbrElements=clauses.get(clauseIndex).size();
        if(nbrElements>1)
        {
            if(nbrElements==2){
                addClause(-condition,-((clauses.get(clauseIndex).get(0)>0)?2*clauses.get(clauseIndex).get(0)-1:-2*clauses.get(clauseIndex).get(0)),-((clauses.get(clauseIndex).get(1)>0)?2*clauses.get(clauseIndex).get(1)-1:-2*clauses.get(clauseIndex).get(1)));
            }
            else{
                addClause(Zi+1,-((clauses.get(clauseIndex).get(0)>0)?2*clauses.get(clauseIndex).get(0)-1:-2*clauses.get(clauseIndex).get(0)));
                addClause(-condition,-((clauses.get(clauseIndex).get(nbrElements-1)>0)?2*clauses.get(clauseIndex).get(nbrElements-1)-1:-2*clauses.get(clauseIndex).get(nbrElements-1)),-(Zi+nbrElements-1));
                for(int j=1; j<nbrElements-1; j++)
                {
                    addClause(Zi+j+1,-((clauses.get(clauseIndex).get(j)>0)?2*clauses.get(clauseIndex).get(j)-1:-2*clauses.get(clauseIndex).get(j)));
                    addClause(-(Zi+j),Zi+j+1);
                    addClause(-condition,-((clauses.get(clauseIndex).get(j)>0)?2*clauses.get(clauseIndex).get(j)-1:-2*clauses.get(clauseIndex).get(j)),-(Zi+j));
                }
                varCount+=nbrElements;
            }
        }
        
    }
    
   
    
    public void setPrimalityConstraintOnLiteral(int literal){ //can be negative..
        
        ArrayList<Integer> locations = varPositionMap.get(Math.abs(literal));
        ArrayList<Integer> littleClause = new ArrayList<Integer>();
        
        littleClause.add(-((literal>0)?literal*2-1:-literal*2));    //that's our new variable Xa or X!a..
        
        
        for(int location : locations ){
            if(clauses.get(location).contains(-literal))
            continue;//we won't need the constraint in that case..Xa works only on !a clauses..
            // if(littleClause.size()==0){
                //     //System.out.println("c "+literal+"  "+varCount);
                // }
                int condition;
                if(clauseUnicityVar.containsKey(location)){//if S is has already been used by another variable
                    condition=clauseUnicityVar.get(location);
                }
                else{
                    condition=++varCount;//this is the sum variable..
                    conditionalAtMostOne(location,condition);
                    clauseUnicityVar.put(location,condition);
                }
                littleClause.add(condition);
            }
            outputClauses.add(littleClause);
            clauseCount++;
            
        }
        
        
        public static int readVarCount(String filename) throws IOException{
            BufferedReader dataFile = new BufferedReader(new FileReader(filename));
        
            String header= dataFile.readLine();
            //getting the header
            while(header.startsWith("c")){
                
                header= dataFile.readLine();
                //getting rid of comments..
            }
        
            StringTokenizer headerTokens=new StringTokenizer(header," \t");
            headerTokens.nextToken();   //take down the "p"
            headerTokens.nextToken();   //I suppose this was "cnf".. and it's gone now..
            int ver=Integer.parseInt(headerTokens.nextToken().toString());
            dataFile.close();
            return ver;
        }


        public String readClauses(String filename) throws IOException{
            BufferedReader dataFile = new BufferedReader(new FileReader(filename));
        
            String header= dataFile.readLine();
            //getting the header
            while(header.startsWith("c")){
                
                header= dataFile.readLine();
                //getting rid of comments..
            }
        
            StringTokenizer headerTokens=new StringTokenizer(header," \t");
            headerTokens.nextToken();   //take down the "p"
            headerTokens.nextToken();   //I suppose this was "cnf".. and it's gone now..
            //int ver=Integer.parseInt(headerTokens.nextToken().toString());


            String content="";
            header = dataFile.readLine();
        
            while(header!=null){    //getting the file content..
                if(!header.startsWith("c"))
                    content+=header;
                header = dataFile.readLine();
            }
            dataFile.close();

            return content;

        }
    
    
    


    
    
    
};


