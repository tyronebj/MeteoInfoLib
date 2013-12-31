/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.meteoinfo.legend;

import org.meteoinfo.global.GenericFileFilter;
import org.meteoinfo.shape.ShapeTypes;
import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

/**
 *
 * @author yaqiang
 */
public class FrmLegendSet extends javax.swing.JDialog {

    private LegendScheme _legendScheme = null;
    private boolean _isOK = false;
    
    /**
     * Creates new form FrmLegendSet
     */
    public FrmLegendSet(java.awt.Dialog parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }
    
    /**
     * Creates new form FrmLegendSet
     */
    public FrmLegendSet(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jToolBar1 = new javax.swing.JToolBar();
        jButton_ImportLegend = new javax.swing.JButton();
        jButton_ExportLegend = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JToolBar.Separator();
        jButton_AddBreak = new javax.swing.JButton();
        jButton_RemoveBreak = new javax.swing.JButton();
        jButton_RemoveAllBreaks = new javax.swing.JButton();
        jSeparator2 = new javax.swing.JToolBar.Separator();
        jButton_MoveBreakUp = new javax.swing.JButton();
        jButton_MoveBreakDown = new javax.swing.JButton();
        jButton_ReverseBreaks = new javax.swing.JButton();
        jSeparator3 = new javax.swing.JToolBar.Separator();
        jButton_MakeBreaks = new javax.swing.JButton();
        legendView1 = new org.meteoinfo.legend.LegendView();
        jButton_OK = new javax.swing.JButton();
        jButton_Cancel = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jToolBar1.setRollover(true);

        jButton_ImportLegend.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/meteoinfo/resources/TSB_Open.Image.png"))); // NOI18N
        jButton_ImportLegend.setToolTipText("Import Legend");
        jButton_ImportLegend.setFocusable(false);
        jButton_ImportLegend.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton_ImportLegend.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton_ImportLegend.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_ImportLegendActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton_ImportLegend);

        jButton_ExportLegend.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/meteoinfo/resources/TSB_Save.Image.png"))); // NOI18N
        jButton_ExportLegend.setToolTipText("Export Legend");
        jButton_ExportLegend.setFocusable(false);
        jButton_ExportLegend.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton_ExportLegend.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton_ExportLegend.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_ExportLegendActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton_ExportLegend);
        jToolBar1.add(jSeparator1);

        jButton_AddBreak.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/meteoinfo/resources/TSB_Add.Image.png"))); // NOI18N
        jButton_AddBreak.setToolTipText("Add Break");
        jButton_AddBreak.setFocusable(false);
        jButton_AddBreak.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton_AddBreak.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton_AddBreak.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_AddBreakActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton_AddBreak);

        jButton_RemoveBreak.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/meteoinfo/resources/TSB_Del.Image.png"))); // NOI18N
        jButton_RemoveBreak.setToolTipText("Remove Break");
        jButton_RemoveBreak.setFocusable(false);
        jButton_RemoveBreak.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton_RemoveBreak.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton_RemoveBreak.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_RemoveBreakActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton_RemoveBreak);

        jButton_RemoveAllBreaks.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/meteoinfo/resources/TSB_DelAll.Image.png"))); // NOI18N
        jButton_RemoveAllBreaks.setToolTipText("Remove All Breaks");
        jButton_RemoveAllBreaks.setFocusable(false);
        jButton_RemoveAllBreaks.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton_RemoveAllBreaks.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton_RemoveAllBreaks.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_RemoveAllBreaksActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton_RemoveAllBreaks);
        jToolBar1.add(jSeparator2);

        jButton_MoveBreakUp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/meteoinfo/resources/TSB_Up.Image.png"))); // NOI18N
        jButton_MoveBreakUp.setToolTipText("Move Break Up");
        jButton_MoveBreakUp.setFocusable(false);
        jButton_MoveBreakUp.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton_MoveBreakUp.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton_MoveBreakUp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_MoveBreakUpActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton_MoveBreakUp);

        jButton_MoveBreakDown.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/meteoinfo/resources/TSB_Down.Image.png"))); // NOI18N
        jButton_MoveBreakDown.setToolTipText("Move Break Down");
        jButton_MoveBreakDown.setFocusable(false);
        jButton_MoveBreakDown.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton_MoveBreakDown.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton_MoveBreakDown.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_MoveBreakDownActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton_MoveBreakDown);

        jButton_ReverseBreaks.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/meteoinfo/resources/TSB_Reverse.Image.png"))); // NOI18N
        jButton_ReverseBreaks.setToolTipText("Reverse Breaks");
        jButton_ReverseBreaks.setFocusable(false);
        jButton_ReverseBreaks.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton_ReverseBreaks.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton_ReverseBreaks.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_ReverseBreaksActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton_ReverseBreaks);
        jToolBar1.add(jSeparator3);

        jButton_MakeBreaks.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/meteoinfo/resources/TSB_MakeBreaks.Image.png"))); // NOI18N
        jButton_MakeBreaks.setFocusable(false);
        jButton_MakeBreaks.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton_MakeBreaks.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton_MakeBreaks.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_MakeBreaksActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton_MakeBreaks);

        javax.swing.GroupLayout legendView1Layout = new javax.swing.GroupLayout(legendView1);
        legendView1.setLayout(legendView1Layout);
        legendView1Layout.setHorizontalGroup(
            legendView1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 382, Short.MAX_VALUE)
        );
        legendView1Layout.setVerticalGroup(
            legendView1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 342, Short.MAX_VALUE)
        );

        jButton_OK.setText("OK");
        jButton_OK.setPreferredSize(new java.awt.Dimension(70, 27));
        jButton_OK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_OKActionPerformed(evt);
            }
        });

        jButton_Cancel.setText("Cancel");
        jButton_Cancel.setPreferredSize(new java.awt.Dimension(70, 27));
        jButton_Cancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_CancelActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, 382, Short.MAX_VALUE)
            .addComponent(legendView1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 382, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGap(68, 68, 68)
                .addComponent(jButton_OK, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(69, 69, 69)
                .addComponent(jButton_Cancel, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(legendView1, javax.swing.GroupLayout.DEFAULT_SIZE, 342, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE, false)
                    .addComponent(jButton_OK, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton_Cancel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton_ImportLegendActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_ImportLegendActionPerformed
        // TODO add your handling code here:
        JFileChooser aDlg = new JFileChooser();
        aDlg.setCurrentDirectory(new File(System.getProperty("user.dir")));
        String[] fileExts = new String[]{"lgs", "pal"};
        GenericFileFilter mapFileFilter = new GenericFileFilter(fileExts, "LegendScheme file (*.lgs,*.pal)");
        aDlg.setFileFilter(mapFileFilter);
        if (JFileChooser.APPROVE_OPTION == aDlg.showOpenDialog(this)) {
            File aFile = aDlg.getSelectedFile();
            System.setProperty("user.dir", aFile.getParent());

            String fext = GenericFileFilter.getExtension(aFile);
            if (fext.equals("pal")) {
                _legendScheme.importFromPaletteFile_Unique(aFile.getAbsolutePath());
            } else if (fext.equals("lgs")) {
                try {
                    _legendScheme.importFromXMLFile(aFile.getAbsolutePath());
                } catch (ParserConfigurationException ex) {
                    Logger.getLogger(FrmLegendSet.class.getName()).log(Level.SEVERE, null, ex);
                } catch (SAXException ex) {
                    Logger.getLogger(FrmLegendSet.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(FrmLegendSet.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            legendView1.update(_legendScheme);
        }
    }//GEN-LAST:event_jButton_ImportLegendActionPerformed

    private void jButton_ExportLegendActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_ExportLegendActionPerformed
        // TODO add your handling code here:
        JFileChooser aDlg = new JFileChooser();
        String[] fileExts = new String[]{"lgs"};
        GenericFileFilter mapFileFilter = new GenericFileFilter(fileExts, "LegendScheme file (*.lgs)");
        aDlg.setFileFilter(mapFileFilter);
        String path = System.getProperty("user.dir");
        aDlg.setCurrentDirectory(new File(path));
        if (JFileChooser.APPROVE_OPTION == aDlg.showSaveDialog(this)) {
            File aFile = aDlg.getSelectedFile();
            String filePath = aFile.getAbsolutePath();
            if (!filePath.substring(filePath.length() - 4).equals(".lgs")) {
                filePath = filePath + ".lgs";
            }
            try {
                _legendScheme.exportToXMLFile(filePath);
            } catch (ParserConfigurationException ex) {
                Logger.getLogger(FrmLegendSet.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_jButton_ExportLegendActionPerformed

    private void jButton_AddBreakActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_AddBreakActionPerformed
        // TODO add your handling code here:
        switch (_legendScheme.getShapeType()) {
            case Polyline:
            case PolylineZ:
            PolylineBreak aPLB = new PolylineBreak();
            aPLB.setDrawPolyline(true);
            aPLB.setSize(0.1F);
            aPLB.setColor(Color.red);
            aPLB.setStartValue(0);
            aPLB.setEndValue(0);
            aPLB.setCaption("");
            _legendScheme.getLegendBreaks().add(aPLB);
            break;
            case Point:
            PointBreak aPB = new PointBreak();
            aPB.setDrawShape(true);
            aPB.setDrawFill(true);
            aPB.setSize(5);
            aPB.setColor(Color.red);
            aPB.setStartValue(0);
            aPB.setEndValue(0);
            aPB.setCaption("");
            _legendScheme.getLegendBreaks().add(aPB);
            break;
            case Polygon:
            PolygonBreak aPGB = new PolygonBreak();
            aPGB.setDrawShape(true);
            aPGB.setDrawFill(true);
            aPGB.setColor(Color.red);
            aPGB.setStartValue(0);
            aPGB.setEndValue(0);
            aPGB.setCaption("");
            _legendScheme.getLegendBreaks().add(aPGB);
            break;
            case Image:
            ColorBreak aCB = new ColorBreak();
            aCB.setColor(Color.red);
            aCB.setStartValue(0);
            aCB.setEndValue(0);
            aCB.setCaption("");
            _legendScheme.getLegendBreaks().add(aCB);
            break;
        }
        legendView1.update(_legendScheme);
    }//GEN-LAST:event_jButton_AddBreakActionPerformed

    private void jButton_RemoveBreakActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_RemoveBreakActionPerformed
        // TODO add your handling code here:
        int i, rowIdx;
        for (i = 0; i < legendView1.getSelectedRows().size(); i++) {
            rowIdx = legendView1.getSelectedRows().get(i);
            _legendScheme.getLegendBreaks().remove(rowIdx);
        }
        legendView1.update(_legendScheme);
    }//GEN-LAST:event_jButton_RemoveBreakActionPerformed

    private void jButton_RemoveAllBreaksActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_RemoveAllBreaksActionPerformed
        // TODO add your handling code here:
        if (JOptionPane.showConfirmDialog(null, "If remove all breaks?", "Alarm", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            _legendScheme.getLegendBreaks().clear();
            legendView1.update(_legendScheme);
        }
    }//GEN-LAST:event_jButton_RemoveAllBreaksActionPerformed

    private void jButton_MoveBreakUpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_MoveBreakUpActionPerformed
        // TODO add your handling code here:
        int oldIdx, newIdx;
        oldIdx = legendView1.getSelectedRows().get(0);
        if (oldIdx > 0) {
            if (_legendScheme.getShapeType() == ShapeTypes.Point) {
                PointBreak aPB = (PointBreak) _legendScheme.getLegendBreaks().get(oldIdx);
                if (aPB.isNoData()) {
                    return;
                }
            }
            newIdx = oldIdx - 1;
            _legendScheme.getLegendBreaks().add(newIdx, _legendScheme.getLegendBreaks().get(oldIdx));
            _legendScheme.getLegendBreaks().remove(oldIdx + 1);
            legendView1.getSelectedRows().clear();
            legendView1.getSelectedRows().add(newIdx);
            legendView1.update(_legendScheme);
        }
    }//GEN-LAST:event_jButton_MoveBreakUpActionPerformed

    private void jButton_MoveBreakDownActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_MoveBreakDownActionPerformed
        // TODO add your handling code here:
        int oldIdx, newIdx, endIdx;
        oldIdx = legendView1.getSelectedRows().get(0);
        endIdx = legendView1.getLegendScheme().getBreakNum() - 1;
        if (_legendScheme.getShapeType() == ShapeTypes.Point) {
            PointBreak aPB = (PointBreak) _legendScheme.getLegendBreaks().get(oldIdx);
            if (aPB.isNoData()) {
                endIdx = endIdx - 1;
            }
        }
        if (oldIdx < endIdx) {
            newIdx = oldIdx + 2;
            _legendScheme.getLegendBreaks().add(newIdx, _legendScheme.getLegendBreaks().get(oldIdx));
            _legendScheme.getLegendBreaks().remove(oldIdx);
            legendView1.getSelectedRows().clear();
            legendView1.getSelectedRows().add(newIdx - 1);
            legendView1.update(_legendScheme);
        }
    }//GEN-LAST:event_jButton_MoveBreakDownActionPerformed

    private void jButton_ReverseBreaksActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_ReverseBreaksActionPerformed
        // TODO add your handling code here:
        Collections.reverse(_legendScheme.getLegendBreaks());
        if (_legendScheme.getHasNoData()) {
            _legendScheme.getLegendBreaks().add(_legendScheme.getLegendBreaks().get(0));
            _legendScheme.getLegendBreaks().remove(0);
        }
        legendView1.update(_legendScheme);
    }//GEN-LAST:event_jButton_ReverseBreaksActionPerformed

    private void jButton_MakeBreaksActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_MakeBreaksActionPerformed
        // TODO add your handling code here:
        boolean isUniqueValue = false;
        if (_legendScheme.getLegendType() == LegendType.UniqueValue) {
            isUniqueValue = true;
        }

        FrmLegendBreaks frmLB = new FrmLegendBreaks(this, false, isUniqueValue);
        frmLB.setLegendScheme(_legendScheme);
        frmLB.setLocationRelativeTo(this);
        frmLB.setVisible(true);
    }//GEN-LAST:event_jButton_MakeBreaksActionPerformed

    private void jButton_OKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_OKActionPerformed
        // TODO add your handling code here:
        _isOK = true;
        this.dispose();
    }//GEN-LAST:event_jButton_OKActionPerformed

    private void jButton_CancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_CancelActionPerformed
        // TODO add your handling code here:        
        this.dispose();
    }//GEN-LAST:event_jButton_CancelActionPerformed

    /**
     * Get if OK
     * @return Boolean
     */
    public boolean isOK(){
        return _isOK;
    }
    
     /**
     * Set legend scheme
     * @param aLS Legend scheme
     */
    public void setLegendScheme(LegendScheme aLS){
        _legendScheme = (LegendScheme)aLS.clone();
        legendView1.setLegendScheme(_legendScheme);
        legendView1.update(aLS);
    }
    
    /**
     * Get legend scheme
     * @return Legend scheme
     */
    public LegendScheme getLegendScheme(){
        return _legendScheme;
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(FrmLegendSet.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FrmLegendSet.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FrmLegendSet.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FrmLegendSet.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                FrmLegendSet dialog = new FrmLegendSet(new javax.swing.JDialog(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton_AddBreak;
    private javax.swing.JButton jButton_Cancel;
    private javax.swing.JButton jButton_ExportLegend;
    private javax.swing.JButton jButton_ImportLegend;
    private javax.swing.JButton jButton_MakeBreaks;
    private javax.swing.JButton jButton_MoveBreakDown;
    private javax.swing.JButton jButton_MoveBreakUp;
    private javax.swing.JButton jButton_OK;
    private javax.swing.JButton jButton_RemoveAllBreaks;
    private javax.swing.JButton jButton_RemoveBreak;
    private javax.swing.JButton jButton_ReverseBreaks;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JToolBar.Separator jSeparator2;
    private javax.swing.JToolBar.Separator jSeparator3;
    private javax.swing.JToolBar jToolBar1;
    private org.meteoinfo.legend.LegendView legendView1;
    // End of variables declaration//GEN-END:variables
}
