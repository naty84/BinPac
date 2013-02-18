/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package binpacking;

/**
 *
 * @author Naty
 */
public interface IAlgorithm {
     
    public int[] findPacking(int[] objects, int capacity, int bins);
    
    public int getSteps();
}
