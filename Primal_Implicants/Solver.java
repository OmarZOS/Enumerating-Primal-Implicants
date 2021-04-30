import java.util.ArrayList;


public interface Solver {

    void resolveCNF(int vars,ArrayList<ArrayList<Integer>> outputClauses) ;

    void addListener(SolutionObserver observer);
    
}
