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
public class SubExponential implements IAlgorithm {
        private int steps;
        
    private class PartialSolution {
        public int[][] residuals;
        public int[] assignment;
                
       public PartialSolution(int[][] residuals, int[] assignment){
           this.residuals = residuals;
           this.assignment = assignment;
       }
       
        @Override
       public int hashCode(){
          int sum = 0;
          for (int i = 0; i < residuals.length; i++) {
              sum += residuals[i][0];
          }
          return sum;
       }
       
        @Override
       public boolean equals(Object sol){
           if (this == sol) return true;
           if (sol instanceof PartialSolution == false)
               return false;
           PartialSolution solp = (PartialSolution)sol;
           for (int i = 0; i < residuals.length; i++) {
               if (residuals[i][0] != solp.residuals[i][0]) {
                   return false;
               }
           }
           return true;
       }
       
       private void printResiduals() {
           for (int i = 0; i < residuals.length; i++) {
               System.out.print(residuals[i][0] +  " bin " + residuals[i][1] + " ");
           }
       }
       
        @Override
       public String toString() {
           StringBuilder sb = new StringBuilder();
           for (int i = 0; i < residuals.length; i++) {
               sb.append(residuals[i][0] +  " bin " + residuals[i][1] + " ");
           }
           return sb.toString();
       }
        private PartialSolution update(int bin, int value, int index){
            int[][] newSolution = new int[residuals.length][2];
            for (int i = 0; i < bin; i++) {
                newSolution[i][0] = residuals[i][0];
                newSolution[i][1] = residuals[i][1];
            }
            int[] newAssign = new int[assignment.length];
            System.arraycopy(assignment, 0, newAssign, 0, assignment.length);
            newAssign[assignment.length - 1 - index] = residuals[bin][1];
            int i = bin + 1;
            int newRes = residuals[bin][0] - value;
            while (i < residuals.length && residuals[i][0] > newRes) {
                newSolution[i-1][0] = residuals[i][0];
                newSolution[i-1][1] = residuals[i][1];
                i++;
            }
            newSolution[i-1][0] = newRes;
            newSolution[i-1][1] = residuals[bin][1];
            for (int j = i; j < residuals.length; j++) {
                newSolution[j][0] = residuals[j][0];
                newSolution[j][1] = residuals[j][1];
            }
            PartialSolution solved = new PartialSolution(newSolution, newAssign);
            //solved.printResiduals();
            //System.out.println(Arrays.toString(newAssign));
            return solved;
    }
    }
    
    @Override
    public int[] findPacking(int[] objects, int capacity, int bins) {
        int remainingVol = 0;
        steps = 0;
        int[] firstAssign = new int[objects.length];
        for (int i = 0; i < objects.length; i++) {
            remainingVol += objects[i];
            firstAssign[i] = 0;
            }
        int[][] firstSolution = new int[bins][2];
        for (int i = 0; i < bins; i++) {
            firstSolution[i][0] = capacity;
            firstSolution[i][1] = i;
        }
        //Arrays.sort(objects);
        HashSet<PartialSolution> pool = new HashSet<PartialSolution>();
        pool.add(new PartialSolution(firstSolution, firstAssign));
        int nextSize;
        HashSet<PartialSolution> newList = new HashSet<PartialSolution>();
        for (int i = 0; i < objects.length; i++) {
           nextSize = objects[objects.length - i - 1]; 
           newList.clear();
           for (PartialSolution bintuple : pool) {
               if (bintuple.residuals[0][0] < nextSize) {
                   continue;
               }
               else if (bintuple.residuals[0][0] >= remainingVol){
                   if (bintuple.residuals[0][1] == 0) {
                       return bintuple.assignment;
                   }
                   else {
                       for (int j = i; j < objects.length; j++) {
                           bintuple.assignment[objects.length - 1 - j] = bintuple.residuals[0][1];
                       }
                       return bintuple.assignment;
                   }
               }
               else {
                   for (int j = 0; j < bins; j++) {
                       if (bintuple.residuals[j][0] >= nextSize) {
                           newList.add(bintuple.update(j, nextSize,i));
                           steps++;
                       }
                   }
               }
           }
           pool.clear();
        pool.addAll(newList);
        //System.out.println(Arrays.toString(pool.toArray(new PartialSolution[0])));
        remainingVol -= nextSize;
        }
        return new int[]{-1};
    }
    
    public int getSteps(){
        return steps;
    }
}
