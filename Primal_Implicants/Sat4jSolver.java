import org.sat4j.reader.Reader;
import java.util.ArrayList;
import java.util.StringTokenizer;

import org.sat4j.core.VecInt;
import org.sat4j.maxsat.SolverFactory;
import org.sat4j.reader.DimacsReader;
import org.sat4j.specs.ContradictionException;
import org.sat4j.specs.IProblem;
import org.sat4j.specs.ISolver;

public class Sat4jSolver implements Solver,ObservableSolver,ExhaustListener {
    
    ArrayList<SolutionObserver> observers = new ArrayList<SolutionObserver> ();

    int myVarCount=0;

    ArrayList<ArrayList<Integer>> myOutputClauses=null;

    public void resolveCNF(int varCount,ArrayList<ArrayList<Integer>> outputClauses){

        myVarCount=varCount;
        myOutputClauses=new ArrayList<ArrayList<Integer>> ();
        myOutputClauses.addAll(outputClauses);

//System.out.println(System.currentTimeMillis());
        executeResolution();

        

        //return null;
    }

    private void executeResolution(){
        ISolver solver = SolverFactory.newDefault(); // SAT solver
        solver.setTimeout(3600); // 1 hour timeout
        solver.newVar(myVarCount);
        int clauseCount=0;

        for (ArrayList<Integer> clause : myOutputClauses) {
            
            VecInt cls = new VecInt(clause.stream().mapToInt(i->i).toArray());
            try {
                // System.out.println(cls);
                if(cls.size()>0){
                    solver.addClause(cls);
                    // System.out.println(cls);
                    clauseCount++;
                }

            } catch (ContradictionException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();

                System.out.println(cls);
                System.exit(0);
                



            }

        }

        solver.setExpectedNumberOfClauses(clauseCount);

        IProblem problem = solver;
        
        Reader reader = new DimacsReader(solver); // needed to decode the obtained result

        try {
            if (problem.isSatisfiable()) {
                // System.out.println("Satisfiable !");

                long time = System.currentTimeMillis();

                String output=reader.decode(problem.model());
                
                time= System.currentTimeMillis() - time;

                notifySolution(output,time);
                
                // System.out.println(reader.decode(problem.model())); // Print a model satisfiying the problem
                
            } else {
                notifySolution(null,0);
            }
        } catch (org.sat4j.specs.TimeoutException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }    
    }

    @Override
    public void notifySolution(String solution,long time ) {
        // TODO Auto-generated method stub
        for (SolutionObserver solutionObserver : observers) {
            solutionObserver.handleSolution(solution,time);
            //System.out.println("x");
        }
        
    }

    @Override
    public void addListener(SolutionObserver observer) {
        // TODO Auto-generated method stub
        observers.add(observer);

        
    }

    @Override
    public void eaveDrop(String solution) {
        
        ArrayList<Integer> clause= new ArrayList<>();
        
        StringTokenizer st= new StringTokenizer(solution," ");
        
        while (st.hasMoreTokens()) {
            clause.add(Integer.parseInt(st.nextToken().toString()));
        }
        
        //myVarCount++;
        myOutputClauses.add(clause);

        executeResolution();
        
    }

    












}
