package com.coral.ui;

import javax.swing.*;
import java.awt.*;
import com.coral.model.Agenda;
import com.coral.dao.AgendaDAO;
import java.sql.Date;
import java.text.SimpleDateFormat;

public class AgendaFormDialog extends JDialog {
    private JTextField tfData = new JTextField(10);
    private JTextField tfLocal = new JTextField(20);
    private JTextArea taDesc = new JTextArea(4, 20);
    private boolean saved = false;

    public AgendaFormDialog(Window owner) {
        super(owner, "Cadastrar Apresentação", ModalityType.APPLICATION_MODAL);
        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(6, 6, 6, 6);
        c.gridx = 0;
        c.gridy = 0;
        add(new JLabel("Data (DD/MM/AAAA):"), c);
        c.gridx = 1;
        add(tfData, c);
        c.gridx = 0;
        c.gridy = 1;
        add(new JLabel("Local:"), c);
        c.gridx = 1;
        add(tfLocal, c);
        c.gridx = 0;
        c.gridy = 2;
        add(new JLabel("Descrição:"), c);
        c.gridx = 1;
        add(new JScrollPane(taDesc), c);

        JPanel p = new JPanel();
        JButton btnSave = new JButton("Salvar");
        JButton btnCancel = new JButton("Cancelar");
        p.add(btnSave);
        p.add(btnCancel);
        c.gridy = 3;
        c.gridx = 0;
        c.gridwidth = 2;
        add(p, c);
        pack();
        setLocationRelativeTo(owner);

        // ✅ Tratamento do formato brasileiro (dd/MM/yyyy)
        btnSave.addActionListener(e -> {
            try {
                String dataTxt = tfData.getText().trim();
                if (dataTxt.isEmpty()) {
                    JOptionPane.showMessageDialog(this,
                            "Por favor, informe a data no formato DD/MM/AAAA.");
                    return;
                }

                // Converte dd/MM/yyyy para java.sql.Date
                SimpleDateFormat formatoBR = new SimpleDateFormat("dd/MM/yyyy");
                java.util.Date dataUtil = formatoBR.parse(dataTxt);
                Date dataSQL = new Date(dataUtil.getTime());

                Agenda a = new Agenda();
                a.setData(dataSQL);
                a.setLocal(tfLocal.getText().trim());
                a.setDescricao(taDesc.getText().trim());

                new AgendaDAO().insert(a);
                saved = true;
                dispose();

            } catch (java.text.ParseException ex) {
                JOptionPane.showMessageDialog(this,
                        "Data inválida! Use o formato DD/MM/AAAA (ex: 08/10/2025).");
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage());
            }
        });

        btnCancel.addActionListener(e -> dispose());
    }

    public boolean isSaved() {
        return saved;
    }
}
