import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.StringTokenizer;


public class Exhaust implements SolutionObserver{
    public static void main(String args[]) throws IOException, InterruptedException{
        
        Exhaust ex = new Exhaust(5);
        
        ex.enumerate();
        
    }
    
    // Solver solver = new Sat4jSolver();

    // ArrayList<ArrayList<Integer>> myClauses= new ArrayList<ArrayList<Integer>>();

    int SizeOfInput=0,varCount=0;

    ArrayList<ExhaustListener> listeners = new ArrayList<ExhaustListener>(); 


    public Exhaust(int count){
        SizeOfInput=count*2;
        varCount=count;
    }

    public void enumerate() throws IOException{
        Scanner scanner = new Scanner(System.in);
        String buf = scanner.nextLine();
        StringTokenizer st = new StringTokenizer(buf," \t");
        String cmd="",comment="c ";
        int count=0;
        while(buf!=""&&count<SizeOfInput){

            while(st.hasMoreTokens()&&count<SizeOfInput){
                String token=st.nextToken().toString();
                cmd+=(-Integer.parseInt(token))+" ";
                if(Integer.parseInt(token)>0){
                    if(Integer.parseInt(token)%2==1)
                        comment+=Integer.parseInt(token)/2+1 +" ";   
                    else
                        comment+=-Integer.parseInt(token)/2 +" ";   
                }
                count++;
            }
            buf=scanner.nextLine();
            st = new StringTokenizer(buf," \t");
            if(st.nextToken().toString().startsWith("c"))
                break;
        }

        scanner.close();
        cmd+=" 0\n";
        comment+=" 0\n";

        FileOutputStream fos = new FileOutputStream("tset2.cnf", true);
        FileOutputStream impl = new FileOutputStream("implicants.cnf",true);
        fos.write(cmd.getBytes());
        impl.write(comment.getBytes());
        
        fos.close();
        impl.close();

    }

    public void subscribe(Solver solver){
        ((Solver) solver).addListener((SolutionObserver)this);
        // System.out.println("x");
    }

    public void addListener(ExhaustListener exhaustListener){
        listeners.add(exhaustListener);
    }


    @Override
    public void handleSolution(String solution,long time) {
        // TODO Auto-generated method stub
        
        //System.out.println(solution);

        if(solution!=null){

            String cmd="";
            StringTokenizer st = new StringTokenizer(solution," \t");
            int count=0;
            int value=0;
            ArrayList<Integer> clause= new ArrayList<Integer>();

            while(st.hasMoreTokens()&&count<SizeOfInput){
                String token=st.nextToken().toString();
                value=Integer.parseInt(token);
                cmd+=-value+" ";
                clause.add(-value);
                count++;
            }

            for (ExhaustListener listener  : listeners) {//should be the solver..
                System.out.println(cmd);
                listener.eaveDrop(cmd);
            
            
            
            }
                //cmd+=" 0";

            // myClauses.add(clause);
            // solver.resolveCNF(++varCount,myClauses);

        }
        
    }

    

    // public void resolveExhaustively(int vars, ArrayList<ArrayList<Integer>> clauses){

    //     myClauses.addAll(clauses);
    //     varCount=vars;
    //     //solver.addListener(this);

    //     //solver.resolveCNF(vars,clauses);

    // }







}