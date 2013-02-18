/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package binpacking;

/**
 *
 * @author Naty
 */
public class DataParameters {
    public int objects;
    public int maxSize;
    public int capacity;
    public int freeCapacity;
    public int containers;
    public int cycles;
    private int[] data;
    public int algorithmIndex;
    public boolean[] algorithms = new boolean[10];
    
    public DataParameters(){
        algorithms[0] = true;
        algorithmIndex = 0;
    }
    
    public int[] getData(){
        return data;
    }
    public void setData(int[] data, boolean controlSize) {
        if (controlSize && data.length != objects) {
            System.out.println("Wrong number of objects");
        }
        else {
        this.data = data;
        objects = data.length;
        freeCapacity = countFreeCapacity();
        }
    }
     public int countFreeCapacity() {
        int cap = capacity * containers;
        for (int i: data) {
            cap -= i;
        }
        return cap;
    }
}
