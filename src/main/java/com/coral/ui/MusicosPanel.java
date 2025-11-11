package com.coral.ui;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import com.coral.dao.MusicoDAO;
import com.coral.model.Musico;
import java.awt.*;
import java.util.List;

public class MusicosPanel extends JPanel {
    private JTable table;
    private MusicoDAO dao = new MusicoDAO();

    public MusicosPanel(){
        setLayout(new BorderLayout());
        table = new JTable();
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel south = new JPanel();
        JButton btnRefresh = new JButton("Atualizar");
        south.add(btnRefresh);
        add(south, BorderLayout.SOUTH);

        btnRefresh.addActionListener(e -> loadData());
        loadData();
    }

    private void loadData(){
        try {
            List<Musico> list = dao.findAll();
            DefaultTableModel m = new DefaultTableModel(new Object[]{"ID","Nome","Instrumento","Ativo"},0){
                public boolean isCellEditable(int r,int c){return false;}
            };
            for(Musico c: list) m.addRow(new Object[]{c.getId(), c.getNome(), c.getInstrumento(), c.isAtivo()});
            table.setModel(m);
        } catch (Exception e){ e.printStackTrace(); JOptionPane.showMessageDialog(this,"Erro: "+e.getMessage()); }
    }
}
