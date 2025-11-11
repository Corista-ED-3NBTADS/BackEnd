package com.coral.ui;
import javax.swing.*;
import java.awt.*;
import com.coral.model.Corista;
import com.coral.dao.CoristaDAO;

public class CoristaFormDialog extends JDialog {
    private JTextField tfNome = new JTextField(20);
    private JTextField tfTipo = new JTextField(15);
    private JCheckBox cbAtivo = new JCheckBox("Ativo", true);
    private boolean saved = false;

    public CoristaFormDialog(Window owner){
        super(owner, "Adicionar Corista", ModalityType.APPLICATION_MODAL);
        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets=new Insets(6,6,6,6);
        c.gridx=0; c.gridy=0; add(new JLabel("Nome:"), c);
        c.gridx=1; add(tfNome, c);
        c.gridx=0; c.gridy=1; add(new JLabel("Tipo de voz:"), c);
        c.gridx=1; add(tfTipo, c);
        c.gridx=0; c.gridy=2; c.gridwidth=2; add(cbAtivo, c);

        JPanel p = new JPanel();
        JButton btnSave = new JButton("Salvar");
        JButton btnCancel = new JButton("Cancelar");
        p.add(btnSave); p.add(btnCancel);
        c.gridy=3; add(p,c);
        pack(); setLocationRelativeTo(owner);

        btnSave.addActionListener(e -> {
            if(tfNome.getText().trim().isEmpty()){ JOptionPane.showMessageDialog(this,"Nome obrigatório"); return; }
            try {
                Corista cvo = new Corista();
                cvo.setNome(tfNome.getText().trim());
                cvo.setTipoVoz(tfTipo.getText().trim());
                cvo.setAtivo(cbAtivo.isSelected());
                new CoristaDAO().insert(cvo);
                saved = true;
                dispose();
            } catch (Exception ex){ ex.printStackTrace(); JOptionPane.showMessageDialog(this,"Erro: "+ex.getMessage()); }
        });
        btnCancel.addActionListener(e -> dispose());
    }
    public boolean isSaved(){ return saved; }
}
