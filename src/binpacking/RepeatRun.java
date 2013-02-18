/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package binpacking;
import java.io.*;
import javax.swing.*;
/**
 *
 * @author Naty
 */
public class RepeatRun extends SwingWorker<Object, Object>{
    private static int run = 0;
    private JProgressBar progressBar;
    private JLabel taskProgress;
    private JTable table;
    private BinPacking bp;
    private DataParameters dp;
    private BufferedWriter bw;
    private int[] minSteps = new int[10];
    private int[] maxSteps = new int[10];
    private int[] avgSteps = new int[10];
    private boolean cancelled = false;
    //Thread t;                     //ukladat pocatecni nastaveni parametru ulohy
    
    public RepeatRun(BinPacking bp, DataParameters dp, JProgressBar pb, JTable table){
        this.bp = bp;
        this.dp = dp;
        this.progressBar = pb;
        this.table = table;
        run++;
        try{
        bw = new BufferedWriter(new FileWriter(new File("res" + run + ".txt")));
        bw.write("Input; Free Capacity; Algorithm; Steps");
        for (int i = 0; i < dp.containers; i++) {
            bw.write("; Bin" + (i+1));
        }
        bw.newLine();
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    public void setTaskLabel(JLabel label){
        taskProgress = label;
    }
    @Override
    public Object doInBackground(){
        for (int i = 0; i<10; i++) {
            minSteps[i] = Integer.MAX_VALUE;
            maxSteps[i] = 0;
        }
        boolean res = repeatAlg();
        return res;
    }
    
    public void done(){
        bp.getScreen().enableRepeatButton();
        if (!isCancelled()) {
          bp.getScreen().setCyclesLabel("Done!");
        }
       
    }
    
    public void stopRun(){
        try {
        bw.close();
        }
        catch(Exception e) {
            System.out.println(e.getMessage());
        }
        cancel(true);
        cancelled = true;
        progressBar.setString(progressBar.getString() + " Cancelled.");
        
    }
 
    public boolean repeatAlg(){
        progressBar.setValue(0);
        progressBar.setString(null);
        for (int i = 0; i < dp.cycles;i++) {
            if (cancelled) return false;
            bp.eraseResults();
            bp.generateDataW(dp);
            for (int k = 0; k<bp.countAlgs();k++) {
                if (!dp.algorithms[k]) continue;
              dp.algorithmIndex = k; 
            String s = Integer.toString(dp.getData()[0]);
            for (int j = 1; j < dp.objects; j++) {
                s = s.concat(" " + Integer.toString(dp.getData()[j]));
            }
            s = s.concat("; " + dp.freeCapacity + "; ");
            //bp.setAlg(k);
            s = s.concat(bp.getAlgName(k) + ";");
            try {
            bw.write(s);   //zapiseme input data
            }
            catch(Exception e) {}
            //bp.setDp(dp);   //zrusit
            bp.startRun(dp);
            long startTime = System.currentTimeMillis();
            float runTime = 0;
            while(!bp.getCurrentRepeatRun().isDone()) { 
               if (cancelled) continue; 
                if ((System.currentTimeMillis() - startTime)/(float)1000 - runTime > 0.1)
                    runTime = (System.currentTimeMillis() - startTime)/(float)1000;
                taskProgress.setText("Task " + (progressBar.getValue() + 1) + "/" + progressBar.getMaximum() + ": " + runTime + "s");
            }
            if (!bp.getCurrentRepeatRun().isCancelled()) {
            saveData2(bp.sendResults(), k);
            }
            else {  //upravit tak, aby se veslo do tabulky
                saveData2(new String[]{"Cancelled",Integer.toString(bp.getCurrentRepeatRun().getSteps())},k);
            }
            progressBar.setValue(progressBar.getValue()+1);
            }
        }
            
        
        int row = -1;
        for (int a = 0; a < bp.countAlgs(); a++) {
            if (!dp.algorithms[a]) continue;
            row++;
            table.setValueAt(bp.getAlgName(a), row, 0);
            avgSteps[a] = avgSteps[a]/dp.cycles;
            table.setValueAt(avgSteps[a],row,1);
            table.setValueAt(minSteps[a],row,2);
            table.setValueAt(maxSteps[a],row,3);
        }
        try{
            bw.close();
        }
        catch (Exception e) {
            System.out.println(e.getMessage());}
        return true;
    }
    
    public void saveData2(String[] s, int algorithm) { //pocitat vyresene a nevyresene ulohy
        int steps = Integer.parseInt(s[1]);
        if (steps < minSteps[algorithm]) {
            minSteps[algorithm] = steps;             
        }
            if (steps > maxSteps[algorithm]) {
                maxSteps[algorithm] = steps;
            }
        
        avgSteps[algorithm] += steps;
        try {
           bw.write(s[1] + ";" + s[0]);
           bw.newLine();
        }
        catch(Exception e) {}
    }
    
}

