/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package binpacking;

/**
 *
 * @author Naty
 */
public class SimpleExponential implements IAlgorithm { 
    
   int steps;
    
   public int getSteps() {
       return steps;
   }
    public int[] findPacking(int[] objects, int capacity, int bins) {
        steps = 0;
        int[] assignment = new int[objects.length];
        for (int i : assignment) {
            i = 0;
        }
        int oIndex = objects.length - 1;
        boolean indexChange = false;
        while (true) {
            steps++;
          if (checkPacking(objects, assignment, capacity, bins)) {
              return assignment;
          }
          assignment[oIndex]++;
          for (int i = oIndex + 1; i < objects.length; i++) {
              assignment[i] = 0;
          }
          if (indexChange) {
              oIndex = objects.length - 1;
              indexChange = false;
          }
          while (assignment[oIndex] == Math.min(oIndex,bins - 1)) { //no point putting first object into container 2 instead of 1
              oIndex--;
              indexChange = true;
              if (oIndex < 0) {
              return new int[]{-1}; //no solution
          }
          
        }
    }
    }
    
    private boolean checkPacking(int[] objects, int[] assignment, int capacity, int bins) {
        int[] binF = new int[bins];
        for (int i = 0; i < objects.length; i++) {
            binF[assignment[i]] += objects[i];
        }
        for (int j = 0; j < bins; j++) {
            if (binF[j] > capacity) {
                return false;
            }
        }
        return true;
    }
}
