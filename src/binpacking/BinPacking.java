/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package binpacking;
import java.util.*;

/**
 *
 * @author Naty
 */
public class BinPacking {
   
    private DataParameters dp;
    private int[] volumes = new int[10];
    private IAlgorithm alg = new SimpleExponential();
    private Screen screen;
    private OneRun currentRun;
    private OneRun currentRepeatRun;
    private RepeatRun repeatRun;
    private ArrayList<IAlgorithm> algList = new ArrayList<IAlgorithm>(); //seznam vsech algoritmu
    private String filling;
    private int steps;
    
    public void startRun(DataParameters dp){
        if (currentRepeatRun == null || currentRepeatRun.isDone()) {
            currentRepeatRun = new OneRun(this,dp,true);
            currentRepeatRun.execute();
        } 
        else {
            System.out.println("One algorithm running.");
         
        }
    
    }
    public void startRun(){
        if (currentRun == null || currentRun.isDone()) {
            dp.setData(volumes, true);
            currentRun = new OneRun(this,dp,false);
            currentRun.execute();
        } 
        else {
            System.out.println("One algorithm running.");
         
        }
    
    }
    
    public void startRepeatRun(RepeatRun rRun){
        if (repeatRun == null || repeatRun.isDone()) {
           repeatRun = rRun; 
           repeatRun.execute();
        }
    }
    
    public OneRun getCurrentRepeatRun(){
        return currentRepeatRun;
    }
    
    public void stopRepeat(){
        repeatRun.stopRun();
    }
    public void stop() {
        if (currentRun == null || currentRun.isDone()) {
            System.out.println("No algorithm running");
        }
        else {
       currentRun.cancel(true);
       screen.getResPanel().setResults(new int[]{-3});
       screen.getResPanel().repaint();
       screen.getGPanel().setResults(new int[]{-3});
       screen.getGPanel().repaint();
        }
    }
    
    public void stopCurrentRepeat(){
        if (currentRepeatRun == null || currentRepeatRun.isDone()) {
            System.out.println("No algorithm running");
        }
        else {
       currentRepeatRun.cancel(true);
    }
    } 
    public void setDp(DataParameters dp){
        this.dp = dp;
    }
    public DataParameters getDp(){
        return dp;
    }
   
    
    public void setVolumes(int[] vol) {
        volumes = new int[vol.length];
        for (int i = 0; i < vol.length; i++) {
            volumes[i] = vol[i];
        }
    }
    
    public int[] getVolumes() {
        return volumes;
    }
    
    public Screen getScreen(){
        return screen;
    }
    
    public String[] sendResults(){
        return new String[]{filling, Integer.toString(steps)};
    }
    
    public IAlgorithm chooseAlg(int selected){
        IAlgorithm algo = algList.get(selected);
        return algo;
    }
    
    public void setAlg(IAlgorithm alg) {
        this.alg = alg;
    }
    /*public String getAlgName(){
        return alg.getClass().getName().substring(11);
    }*/
    public String getAlgName(int index) {
        return algList.get(index).getClass().getName().substring(11);
    }
    public int countAlgs(){
        return algList.size();
    }
    
    public void generateData(DataParameters dp) {
        int[] data = generateOneData(dp);
        while (countFreeCapacity(data,dp) < 0) {
            data = generateOneData(dp);
        }
        Arrays.sort(data);
        setVolumes(data);   //uplne zrusit a vracet data
        dp.setData(data,false);
        screen.getResPanel().updateSize();
        screen.getResPanel().repaint();
        screen.getGPanel().updateSize();
        screen.getGPanel().repaint();
    }
     public void generateDataW(DataParameters dp) {
        int[] data = generateOneData(dp);
        while (countFreeCapacity(data,dp) < 0) {
            data = generateOneData(dp);
        }
        Arrays.sort(data);
        //setVolumes(data);   //zrusit
        dp.setData(data,false);
    }
    
    
    public int[] generateOneData(DataParameters dp) {
        int[] data = new int[dp.objects];
        Random generator = new Random();
        for (int i = 0; i < dp.objects; i++) {
            data[i] = generator.nextInt(dp.maxSize) + 1;
        }
       return data;
    }
    
    public int countFreeCapacity(int[] data, DataParameters dp) {
        int cap = dp.capacity * dp.containers;
        for (int i: data) {
            cap -= i;
        }
        return cap;
    }
    
    public void printResults(int[] results) {
        System.out.println(alg.getClass().getName());
        System.out.println(Arrays.toString(volumes));
        //System.out.println("free capacity " + countFreeCapacity(volumes));
        System.out.println(Arrays.toString(results));
        System.out.println(alg.getSteps());
    }
    
    public void setResults(int[] results) {
        screen.getResPanel().setResults(results);
        screen.getResPanel().repaint();
        screen.getGPanel().setResults(results);
        screen.getGPanel().repaint();
        filling = screen.getResPanel().getFilling(results,dp);
      
        steps = currentRun.getSteps();
          System.out.println(filling + " " + steps);
    }
    public void setResultsW(int[] results) {
       filling = screen.getResPanel().getFilling(results, currentRepeatRun.getDp());
       steps = currentRepeatRun.getSteps(); 
       System.out.println(filling + " " + steps);
    }
    public void eraseResults(){
        screen.getResPanel().setResults(new int[]{-2});
        screen.getGPanel().setResults(new int[]{-2});
    }
   
    public static void main(String[] args) {
        BinPacking bp = new BinPacking();
        bp.algList.add(new SimpleExponential());
        bp.algList.add(new SubExponential());
        bp.screen = new Screen(bp);
    }
}
