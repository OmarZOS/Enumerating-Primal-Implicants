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
        Transformer cnf = new Transformer(args[0]);
        
        


        cnf.enumeratePrimalImplicantsV2();

        //System.out.println(cnf.getClauses());


        cnf.printDimacs();


    }




    private int varCount=0,originalVarCount=0,clauseCount=0;
    private Map<Integer,ArrayList<Integer>> varPositionMap=null;
    //the varPositionMap is to find the clauses it participates in
    
    ArrayList<ArrayList<Integer>> clauses=null; 
    //clauses are arrays of arrays..


    Map<Integer,Integer> clauseUnicityVar=null;


    //public Transformer(){}
    public Transformer(String filename) throws IOException{
        
        BufferedReader dataFile = new BufferedReader(new FileReader(filename));
        
        String header= dataFile.readLine();
        //getting the header
        while(header.startsWith("c")){
            
            header= dataFile.readLine();
            //getting rid of comments..
        }
        
        StringTokenizer headerTokens=new StringTokenizer(header," ");
        headerTokens.nextToken();   //take down the "p"
        headerTokens.nextToken();   //I suppose this was "cnf".. and it's gone now..
        varCount=Integer.parseInt(headerTokens.nextToken().toString());
        originalVarCount=varCount;//originalVarCount will be there when we are lost..
        clauseCount=Integer.parseInt(headerTokens.nextToken().toString());
        
        //System.out.println("what");
        //System.out.println(varCount);
        
        String content="";
        header = dataFile.readLine();
        
        while(header!=null){    //getting the file content..
            if(!header.startsWith("c"))
                content+=header;
            header = dataFile.readLine();
        }
        
        dataFile.close();

        
        
        setClauses(content);//set the clauses array of arrays 
        
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
        
        for (ArrayList<Integer> cls : clauses) {
            for (Integer number : cls) {
                System.out.print(number+" ");
            }
            
            System.out.println("0");
        }
    }


    @Deprecated
    public void enumeratePrimalImplicantsV1(){  //TODO: still didn't make the OR between PROHIBITIONS..
        for(Integer literal=1;literal<=varCount;literal++){
            Integer currentIndex=varCount+2*(literal)-1;//for the positive, +1 for the negative
            
            ArrayList<Integer> anotherlittleClause = new ArrayList<Integer>();
            anotherlittleClause.add(-currentIndex);  //!Xp or !X!p
            anotherlittleClause.add(-(currentIndex+1));
            clauses.add(anotherlittleClause);
            clauseCount++;
            
            
            // anotherlittleClause = new ArrayList<Integer>();
            // anotherlittleClause.add(-currentIndex);  //Xp nor X!p
            // anotherlittleClause.add(-(currentIndex+1));
            // clauses.add(anotherlittleClause);
            // clauseCount++;
            
            
            ArrayList<Integer> locations = varPositionMap.get(literal);
            for(Integer clauseIndex : locations ){
                if(clauses.get(clauseIndex).contains(-literal)){
                    for(Integer otherLiteral : clauses.get(clauseIndex)){
                        if(otherLiteral.equals(-literal))
                        continue;
                        ArrayList<Integer> littleClause = new ArrayList<Integer>();
                        littleClause.add(currentIndex);
                        littleClause.add(-otherLiteral);
                        clauses.add(littleClause);
                        clauseCount++;
                    }
                }
                else if(clauses.get(clauseIndex).contains(literal)){
                    for(Integer otherLiteral : clauses.get(clauseIndex)){
                        if(otherLiteral.equals(literal))
                        continue;
                        ArrayList<Integer> littleClause = new ArrayList<Integer>();
                        littleClause.add(currentIndex+1);
                        littleClause.add(-otherLiteral);
                        clauses.add(littleClause);
                        clauseCount++;
                    }
                }
            }
            
            
        }
    }
    
    public int getVarCount(){
        return varCount;
    }
    
    public int getClauseCount(){
        return clauseCount;
    }

    public  ArrayList<ArrayList<Integer>> getClauses(){
        return clauses;
    }


    public int getLiteral(int clauseIndex,int position){
        return clauses.get(clauseIndex).get(position);
    }

    public void addClause(int x, int y){    
        //the mapper isn't touched because wwe only care about mapping original literals 
        ArrayList<Integer> littleClause = new ArrayList<Integer>();
        littleClause.add(x);
        littleClause.add(y);
        clauses.add(littleClause);
        clauseCount++;
    }

    public void addClause(int x, int y,int z){
        //the mapper isn't touched because we only care about mapping original literals 
        ArrayList<Integer> littleClause = new ArrayList<Integer>();
        littleClause.add(x);
        littleClause.add(y);
        littleClause.add(z);
        clauses.add(littleClause);
        clauseCount++;
    }


    
    public void conditionalAtMostOne(int clauseIndex, int condition)    //credits to: Dr. A.Boudane
    {
        int Zi=getVarCount(); // pour récupérer les variables qui encodent la contrainte de cardinalité
        int nbrElements=clauses.get(clauseIndex).size();
        if(nbrElements>1)
        {
            if(nbrElements==2){
                addClause(-condition,-clauses.get(clauseIndex).get(0),-clauses.get(clauseIndex).get(1));
            }
            else{
                addClause(Zi+1,-clauses.get(clauseIndex).get(0));
                addClause(-condition,-clauses.get(clauseIndex).get(nbrElements-1),-(Zi+nbrElements-1));
                for(int j=1; j<nbrElements-1; j++)
                {
                    addClause(Zi+j+1,-clauses.get(clauseIndex).get(j));
                    addClause(-(Zi+j),Zi+j+1);
                    addClause(-condition,-clauses.get(clauseIndex).get(j),-(Zi+j));
                }

            }
        }
       
    }

    public void setPrimalityConstraintOnLiteral(int literal){ //can be negative..
        ArrayList<Integer> locations = varPositionMap.get(Math.abs(literal));
        ArrayList<Integer> littleClause = new ArrayList<Integer>();
        
        
        for(int location : locations ){
            if(clauses.get(location).contains(literal))
            continue;//we won't need the constraint in that case..Xa works only on !a clauses..
            if(littleClause.size()==0)
                littleClause.add(-(++varCount));    //that's our new variable Xa or X!a..
            int condition;
            if(clauseUnicityVar.containsKey(location)){
                condition=clauseUnicityVar.get(location);
            }   
            else{
                condition=++varCount;//this is the sum variable..
                conditionalAtMostOne(location,condition);
                clauseUnicityVar.put(location,condition);
            }
            littleClause.add(-condition);
        }
        if(littleClause.size()!=0){
            clauses.add(littleClause);
            clauseCount++;
        }
    }

    public void enumeratePrimalImplicantsV2(){

        clauseUnicityVar=new HashMap<Integer,Integer>();

        for(int i : varPositionMap.keySet()){   //using
            setPrimalityConstraintOnLiteral(i);
            setPrimalityConstraintOnLiteral(-i);
        }
    }
    
    
    
};


