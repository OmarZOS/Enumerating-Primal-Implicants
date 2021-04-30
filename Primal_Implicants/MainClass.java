import java.io.IOException;

public class MainClass {

    public static void main(String args[]) throws IOException {
        EnumeratePrimalImplicants("test.cnf");//sudoku.dimacstest.cnf
    }

    public static void EnumeratePrimalImplicants(String filename) throws IOException {

        Transformer cnf = new Transformer(filename);
        Exhaust exhaust = new Exhaust(cnf.getVarCount());
        cnf.enumeratePrimalImplicantsV1();

        //System.out.println(cnf.getDimacs());

        //  System.exit(0);

        Solver solver = new Sat4jSolver();

        // setting communication
        solver.addListener(exhaust);
        exhaust.addListener((ExhaustListener) solver);
        // TODO : exhaust.addListener((ExhaustListener) gui);

        solver.resolveCNF(cnf.getVarCount(), cnf.getOutputClauses());

    }

}
