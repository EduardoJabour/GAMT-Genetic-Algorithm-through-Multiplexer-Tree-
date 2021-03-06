package ferramentas;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

/*
 * SimulatorOutputFrame.java
 *
 * Created on 6 de Abril de 2007, 18:56
 */
/**
 *
 * @author jabour
 */
public class SimulatorOutputFrame extends javax.swing.JFrame
        implements JTextAreaInterface {

    /**
     * Creates new form SimulatorOutputFrame
     */
    public SimulatorOutputFrame(String simulationID) {
        initComponents();
        jTextField1.setText(simulationID);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Saída de dados");

        jLabel1.setText("Resolvendo o ");

        jTextField1.setEditable(false);

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane1.setViewportView(jTextArea1);

        jButton1.setMnemonic('C');
        jButton1.setText("Cancel");
        jButton1.setToolTipText("Close this frame. Threads' stop not implemented yet.");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setMnemonic('s');
        jButton2.setText("Save");
        jButton2.setToolTipText("Save this log to file");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField1, javax.swing.GroupLayout.DEFAULT_SIZE, 758, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(560, 560, 560)
                .addComponent(jButton2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1))
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 840, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 336, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton2)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

   private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
       JFileChooser mArqSalvDialogo = new JFileChooser();
       mArqSalvDialogo.setFileSelectionMode(JFileChooser.FILES_ONLY);
       mArqSalvDialogo.setDialogTitle(this.getTitle() + " : Salvar");
       int result = mArqSalvDialogo.showSaveDialog(this);
       if (mArqSalvDialogo.getSelectedFile() != null && result
               != JFileChooser.CANCEL_OPTION) {
           String nomeArq = mArqSalvDialogo.getSelectedFile().toString();
           File arquivo = new File(nomeArq);
           if (arquivo.exists()) {
               result = JOptionPane.showOptionDialog(this, "Arquivo "
                       + "já existe. Sobrescrever?", "Cuidado!",
                       JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE,
                       null, null, null);
           }
           if (result == JOptionPane.YES_OPTION) {
               FileWriter escreveArquivo;
               try {
                   escreveArquivo = new FileWriter(arquivo);
                   escreveArquivo.write(jTextArea1.getText());
                   escreveArquivo.close();
                   this.setTitle("Edição do arquivo: " + nomeArq);
               } catch (IOException ex) {
                   JOptionPane.showMessageDialog(this, "Erro de entrada e saída");
               }
           }
       }
   }//GEN-LAST:event_jButton2ActionPerformed

    public void salvaAutomatico() {

        String nomeArq = jTextField1.getText().replace(" ","") + ".txt";
        File arquivo = new File(nomeArq);
        /* if (arquivo.exists()) {
               result = JOptionPane.showOptionDialog(this, "Arquivo "
                       + "já existe. Sobrescrever?", "Cuidado!",
                       JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE,
                       null, null, null);
           }
           if (result == JOptionPane.YES_OPTION) {*/
        FileWriter escreveArquivo;
        try {
            escreveArquivo = new FileWriter(arquivo);
            escreveArquivo.write(jTextArea1.getText());
            escreveArquivo.close();
            //this.setTitle("Edição do arquivo: " + nomeArq);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Erro de entrada e saída");
        }
    }


    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        this.dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

    @Override
    public void jTextAreaAppend(String string) {
        jTextArea1.append(string);
        jTextArea1.setCaretPosition(jTextArea1.getText().length());
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables

}
