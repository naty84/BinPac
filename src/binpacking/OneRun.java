/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package binpacking;
import javax.swing.*;
/**
 *
 * @author Naty
 */
public class OneRun extends SwingWorker<int[],int[]> {
    private DataParameters dp;
    private BinPacking bp;
    private int[] result;
    private IAlgorithm alg;
    private boolean repeating;
    
    
    public OneRun(BinPacking bp, DataParameters dp, boolean repeating) {
        this.bp = bp;
        this.dp = dp;
        this.repeating = repeating;
    }  
    
    
    @Override
    public int[] doInBackground(){
        if (dp.getData().length == 0) {
            System.out.println("Data missing."); //doplnit parametr maxSize
        }
       alg = bp.chooseAlg(dp.algorithmIndex);
       result = alg.findPacking(dp.getData(), dp.capacity, dp.containers);
       if (repeating) {   //ulozit vysledky pouze zde, oznamit bp a cekat, jestli je bude chtit zobrazit
          bp.setResultsW(result); 
       }
       else {
       bp.setResults(result);
       }
       return result;
    }
    
    public int getSteps(){
        return alg.getSteps();
    }
    
    @Override
    public void done(){
        if (!repeating) {
            bp.getScreen().changeButtonsEnabled();
        }
    }
    
    public DataParameters getDp(){
        return dp;
    }
    
}
