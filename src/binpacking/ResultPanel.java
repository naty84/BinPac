/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * ResultPanel.java
 *
 * Created on 27.12.2012, 11:46:20
 */
package binpacking;
import java.awt.*;
import java.util.*;
/**
 *
 * @author Naty
 */
public class ResultPanel extends javax.swing.JPanel {
     
    private int[] oneResult = new int[]{-2};
    private boolean paintBins = false;
    private BinPacking bp;
    private final int OBJ_HEIGHT = 50;
    private final int BIN_HEIGHT = 100;
    private final int GAP_HEIGHT = 10;
    private final Color[] colList = new Color[]{Color.RED, Color.GREEN, Color.YELLOW, Color.BLUE};
    private int bWidth;
    private final int BORDER_WIDTH = 20;
    private final int ON_LINE = 10;
    private int yCor = BORDER_WIDTH;
    private int yCorN = BORDER_WIDTH;
    private final int MIN_HEIGHT = 400;
    /** Creates new form ResultPanel */
   
     public ResultPanel(BinPacking bp, boolean graphical) {
        this.bp = bp;
        paintBins = graphical;
        initComponents();
    }
      public ResultPanel() {
        //this.bp = bp;
        initComponents();
    }

    @Override
    public void paint(Graphics g) {
        
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, this.getWidth(), this.getHeight());
        g.setColor(Color.BLACK);
        if (bp == null) return;
        if (!paintBins) {
            paintNum(g);
        }
        else {
            paintBins(g);
        }
    }
    
    private void paintBins(Graphics g) {
        bWidth = (getWidth() - 2*BORDER_WIDTH)/ON_LINE;
        if (bp.getVolumes().length > 0 && bp.getVolumes()[0] != 0) {
            for (int i = 0; i < bp.getVolumes().length; i++) {
                g.setColor(colList[i % colList.length]);
                g.fillRect(BORDER_WIDTH + bWidth*(i % ON_LINE), BORDER_WIDTH + (GAP_HEIGHT + OBJ_HEIGHT) * (i/ON_LINE), bWidth, OBJ_HEIGHT);
                g.setColor(Color.WHITE);
                g.fillRect(BORDER_WIDTH + bWidth*(i % ON_LINE), BORDER_WIDTH + (OBJ_HEIGHT + GAP_HEIGHT) * (i/ON_LINE), bWidth, OBJ_HEIGHT - (int)(((double) bp.getVolumes()[i] / bp.getDp().maxSize) * OBJ_HEIGHT));
                //g.setColor(colList[i % colList.length]);
                g.setColor(Color.BLACK);
                g.drawString(Integer.toString(bp.getVolumes()[i]), BORDER_WIDTH + bWidth*(i % ON_LINE), BORDER_WIDTH + (GAP_HEIGHT + OBJ_HEIGHT) * (i/ON_LINE) + OBJ_HEIGHT - (int)(((double) bp.getVolumes()[i] / bp.getDp().maxSize) * OBJ_HEIGHT));
            }
            yCor = BORDER_WIDTH + (OBJ_HEIGHT + GAP_HEIGHT) * ((bp.getVolumes().length - 1)/ON_LINE) + (OBJ_HEIGHT + ON_LINE);
            for (int i = 0; i < bp.getDp().containers; i++) {
                    g.setColor(Color.BLACK);
                    g.drawRect(BORDER_WIDTH + bWidth * (2*i % ON_LINE), yCor + (2*i / ON_LINE) * (BIN_HEIGHT + GAP_HEIGHT), bWidth, BIN_HEIGHT);
                }
            int height = yCor + 2* bp.getDp().containers / ON_LINE;
            if (height > getPreferredSize().getHeight()) {
          this.setPreferredSize(new Dimension(getWidth(), height + 50));
            }
        if (oneResult[0] != -2) {
             if (oneResult[0] == -3) {
                         g.drawString("Computation stopped.", BORDER_WIDTH, 100);
                     }
            if (oneResult[0] == -1) {
                Font f = g.getFont();
                 g.setFont(g.getFont().deriveFont(Font.BOLD, 24));
                g.drawString("Impossible!", BORDER_WIDTH, yCor + BIN_HEIGHT);
                g.setFont(f);
            }
            else {
                showFillingG(g,fillBins(oneResult, bp.getDp().containers));
            }
        }
    }
    }
    
   private void paintNum(Graphics g) {
       if (bp != null && bp.getVolumes() != null && bp.getVolumes().length > 0 && bp.getVolumes()[0] != 0) {
        String s = "";
        g.drawString("Input: ", BORDER_WIDTH, BORDER_WIDTH);
        yCorN = BORDER_WIDTH + 20;
        int read = 0;
        FontMetrics fm = g.getFontMetrics();
        while (read < bp.getVolumes().length) {
        int sWidth = 0;
        while (sWidth < getWidth() - BORDER_WIDTH * 2 - fm.stringWidth(Integer.toString(bp.getDp().maxSize)) && read < bp.getVolumes().length) {
            s = s.concat(Integer.toString(bp.getVolumes()[read++]) + " ");
            sWidth = fm.stringWidth(s); 
        }
        g.drawString(s, BORDER_WIDTH, yCorN);
        yCorN += 20;
        s = "";
        }
        }
        if (oneResult[0] != -2) {
             if (oneResult[0] == -1) {
                 Font f = g.getFont();
                g.setFont(g.getFont().deriveFont(Font.BOLD, 24));
                g.drawString("Impossible!", BORDER_WIDTH, 100);
                g.setFont(f);
            }
                     if (oneResult[0] == -3) {
                         g.drawString("Computation stopped.", BORDER_WIDTH, 100);
                     }
            else {
            showFilling(g,fillBins(oneResult, bp.getDp().containers));
            }
        }
    }
    public void setPaintBins(boolean graphical) {
        paintBins = graphical;
    }
    public void setResults(int[] res){
        oneResult = res;
    }
    
    public ArrayList<Integer>[] fillBins(int[] result, int containers) {
        if (result[0] == -1) {
            return new ArrayList[0];
        }
        ArrayList<Integer>[] filling = new ArrayList[containers];
        
        for (int i = 0; i < filling.length; i++) {
            filling[i] = new ArrayList<Integer>();
        }
        for (int i = 0; i < result.length; i++) {
            filling[result[i]].add(i);
        }
        return filling;
    }
    
    public void showFilling(Graphics g, ArrayList<Integer>[] filling) {
        if (filling.length == 0) {
            g.drawString("No solution.", BORDER_WIDTH, yCorN);
        }
        FontMetrics fm = g.getFontMetrics();
        for (int i = 0; i < filling.length; i++) {
            int read = 0;
        String s = "Bin " + (i + 1) + ":";
        int sWidth = fm.stringWidth(s);
        while (read < filling[i].size()) {
        while (sWidth < getWidth() - 2 * BORDER_WIDTH - fm.stringWidth(Integer.toString(bp.getDp().maxSize)) && read < filling[i].size()) {
            s = s.concat(" " + bp.getVolumes()[filling[i].get(read++)]);
            sWidth = fm.stringWidth(s);
        }   
            g.drawString(s,BORDER_WIDTH, yCorN);
            yCorN += 20;
            s = "      ";
            sWidth = fm.stringWidth(s);
        }
        }
        
    }
    
    public String getFilling(int[] result, DataParameters data){
        if (result[0] == -1) {
            return "No solution.";
        }
       ArrayList<Integer>[] f = fillBins(result, data.containers);
       String s = " ";
       for (int i = 0; i < f.length; i++) {
            int read = 0;
        while (read < f[i].size()) {
       
            s = s.concat(" " + data.getData()[f[i].get(read++)]);
        }
        s = s.concat(";");
        }
       return s;
    }
    
    public void showFillingG(Graphics g, ArrayList<Integer>[] filling) {
        for (int i = 0; i < filling.length; i++) {
            int filled = 0;
                int filledCount = 0;
            for (int object: filling[i]) {
               g.setColor(Color.BLACK);
                g.drawRect(BORDER_WIDTH + bWidth * (2*i % ON_LINE), 
                        -1 + yCor + (2*i / ON_LINE) * (BIN_HEIGHT + GAP_HEIGHT) - filled + BIN_HEIGHT - (int)(((double)bp.getVolumes()[object] / bp.getDp().capacity) * BIN_HEIGHT), bWidth,
                        1 + (int)(((double)bp.getVolumes()[object] / bp.getDp().capacity) * BIN_HEIGHT));
                g.setColor(colList[object % colList.length]); 
                
                g.fillRect(BORDER_WIDTH + bWidth * (2*i % ON_LINE) + 1, 
                        yCor + (2*i / ON_LINE) * (BIN_HEIGHT + GAP_HEIGHT) - filled + BIN_HEIGHT - (int)(((double)bp.getVolumes()[object] / bp.getDp().capacity) * BIN_HEIGHT), bWidth - 1,
                        (int)(((double)bp.getVolumes()[object] / bp.getDp().capacity) * BIN_HEIGHT));
                filled += (int)(((double)bp.getVolumes()[object] / bp.getDp().capacity) * BIN_HEIGHT);
                filledCount++;
          
            }
            
            
        }
    }
    
    public void updateSize() {
        int height = BORDER_WIDTH + (bp.getVolumes().length / ON_LINE) * (OBJ_HEIGHT + GAP_HEIGHT) +
                (2 * bp.getDp().containers / ON_LINE) * (BIN_HEIGHT + GAP_HEIGHT) + 100;
        if (Math.abs(height - getPreferredSize().getHeight()) > 100) {
            getParent().invalidate();
            setPreferredSize(new Dimension (getWidth(), Math.max(MIN_HEIGHT, height)));
            getParent().validate();
        }
    }
    
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
