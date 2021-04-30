
public class GuiImplementation implements GuiService,SolutionObserver{

    @Override
    public void handleSolution(String solution,long time) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void EnumeratePrimalImplicants(String filename) {
        Transformer.startEnumerationProcess(filename);
    }
    
    
}
