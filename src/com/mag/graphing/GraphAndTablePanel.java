package com.mag.graphing;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Collection;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import com.mag.parsing.CalculationParser;
import com.mag.parsing.Expression;
import com.mag.parsing.ParseException;

/**
 *
 * @author michael
 */
public class GraphAndTablePanel extends javax.swing.JPanel {
    private GraphPanel graph;
    private PlotTableModel table;
    private ArrayList<Expression> functions = new ArrayList<Expression>();

    private double y1, x1; /* Mouse initially pressed positions. */

    public static void main(String[] args) throws ParseException {
        String plotinput = JOptionPane.showInputDialog("Give function(s), seperated by \";\".");
        String[] plots = plotinput.split("[ ]*;[ ]*");
        ArrayList<Expression> expressions = new ArrayList<Expression>();

        for (String plot : plots)
            expressions.add(CalculationParser.interpretExpressionFromString(plot));

        JFrame jf = new JFrame("Stuff.");
        jf.setSize(500, 500);
        jf.add(new GraphAndTablePanel(expressions, -10, 10, 1));
        jf.show();
    }

    /**
     * Creates new form GraphAndTablePanel
     */
    public GraphAndTablePanel(Collection<? extends Expression> c, double deflow, double defhigh, double defdx) {
        functions.addAll(c);
        table = new PlotTableModel(functions, deflow, defdx, (int)(Math.abs(deflow - defhigh) / (defdx * 1.0)) + 1);
        graph = new GraphPanel();
        graph.setGraphBoundaries(deflow, defhigh, defdx / 100.0);
        graph.setFunctionsAndRedraw(c);
        initComponents();
        tLowest.setText(deflow + "");
        tHighest.setText(defhigh + "");
        tDeltaX.setText(defdx + "");
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel3 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        jPanel4 = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel3 = new javax.swing.JPanel();
        jSlider1 = new javax.swing.JSlider();
        jPanel1 = graph;
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        tDeltaX = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        tLowest = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        tHighest = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();

        jLabel3.setText("Delta-x:");

        jTextField3.setText("jTextField1");

        jTabbedPane1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jTabbedPane1.setFocusable(false);

        jSlider1.setFocusable(false);
        jSlider1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jSlider1MouseReleased(evt);
            }
        });
        jSlider1.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSlider1StateChanged(evt);
            }
        });

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jPanel1KeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jPanel1KeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 372, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 186, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                      .addContainerGap()
                      .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jSlider1, javax.swing.GroupLayout.DEFAULT_SIZE, 376, Short.MAX_VALUE))
                      .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                      .addContainerGap()
                      .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                      .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                      .addComponent(jSlider1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                      .addContainerGap())
        );

        jTabbedPane1.addTab("Plot", jPanel3);

        jTable1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jTable1.setModel(table);
        jScrollPane1.setViewportView(jTable1);

        tDeltaX.setText("jTextField1");

        jLabel1.setText("Delta-x:");

        jLabel2.setText("Lowest:");

        tLowest.setText("jTextField1");

        jLabel4.setText("Highest:");

        tHighest.setText("jTextField1");

        jButton1.setText("Re-Apply");
        jButton1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton1MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                      .addContainerGap()
                      .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 376, Short.MAX_VALUE)
                                .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, 376, Short.MAX_VALUE)
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                                          .addComponent(jLabel1)
                                          .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                          .addComponent(tDeltaX, javax.swing.GroupLayout.DEFAULT_SIZE, 333, Short.MAX_VALUE))
                                .addGroup(jPanel2Layout.createSequentialGroup()
                                          .addComponent(jLabel2)
                                          .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                          .addComponent(tLowest, javax.swing.GroupLayout.DEFAULT_SIZE, 334, Short.MAX_VALUE))
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                                          .addComponent(jLabel4)
                                          .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                          .addComponent(tHighest, javax.swing.GroupLayout.DEFAULT_SIZE, 332, Short.MAX_VALUE)))
                      .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                      .addContainerGap()
                      .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 112, Short.MAX_VALUE)
                      .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                      .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(tDeltaX, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel1))
                      .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                      .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(tLowest, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel2))
                      .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                      .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(tHighest, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel4))
                      .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                      .addComponent(jButton1)
                      .addContainerGap())
        );

        jTabbedPane1.addTab("Table", jPanel2);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                      .addContainerGap()
                      .addComponent(jTabbedPane1)
                      .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                      .addContainerGap()
                      .addComponent(jTabbedPane1)
                      .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton1MouseClicked
        double low = 0, scale = 0;
        int len = 0;

        try {
            low = Double.parseDouble(tLowest.getText());
            scale = Double.parseDouble(tDeltaX.getText());
            double high = Double.parseDouble(tHighest.getText());
            len = (int)(Math.abs(high - low) / scale + 1);
        } catch (NumberFormatException n) {
            //TODO: handle this.
        }

        table.recalculate(low, scale, len);
    }//GEN-LAST:event_jButton1MouseClicked

    private void jSlider1StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSlider1StateChanged

    }//GEN-LAST:event_jSlider1StateChanged

    private void jSlider1MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jSlider1MouseReleased
        int s = jSlider1.getValue();

        if (s == 50)
            return;

        double percentChange = 50.0 / s;
        graph.zoomPercent(percentChange);
        jSlider1.setValue(50);
    }//GEN-LAST:event_jSlider1MouseReleased

    private void jPanel1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jPanel1KeyPressed
        keyPressed(evt);
    }//GEN-LAST:event_jPanel1KeyPressed

    private void jPanel1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jPanel1KeyReleased

    }//GEN-LAST:event_jPanel1KeyReleased

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSlider jSlider1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField tDeltaX;
    private javax.swing.JTextField tHighest;
    private javax.swing.JTextField tLowest;
    // End of variables declaration//GEN-END:variables

    private void keyPressed(KeyEvent evt) {
        switch (evt.getKeyCode()) {
        case KeyEvent.VK_LEFT: {
            graph.transformBoundaries(-100, 0);
        }
        break;

        case KeyEvent.VK_RIGHT: {
            graph.transformBoundaries(100, 0);
        }
        break;

        case KeyEvent.VK_UP: {
            if ((KeyEvent.CTRL_MASK & evt.getModifiers()) != 0)
                graph.zoomPercent(40 / 50.0);
            else
                graph.transformBoundaries(0, -100);
        }
        break;

        case KeyEvent.VK_DOWN: {
            if ((KeyEvent.CTRL_MASK & evt.getModifiers()) != 0)
                graph.zoomPercent(50 / 40.0);
            else
                graph.transformBoundaries(0, 100);
        }
        break;
        }
    }
}