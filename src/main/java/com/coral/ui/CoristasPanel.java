package com.coral.ui;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import com.coral.dao.CoristaDAO;
import com.coral.model.Corista;
import java.awt.*;
import java.util.List;

public class CoristasPanel extends JPanel {
    private JTable table;
    private CoristaDAO dao = new CoristaDAO();

    public CoristasPanel(){
        setLayout(new BorderLayout());
        table = new JTable();
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel south = new JPanel();
        JButton btnRefresh = new JButton("Atualizar");
        JButton btnAdd = new JButton("Adicionar");
        JButton btnDelete = new JButton("Excluir");
        south.add(btnRefresh); south.add(btnAdd); south.add(btnDelete);
        add(south, BorderLayout.SOUTH);

        btnRefresh.addActionListener(e -> loadData());
        btnAdd.addActionListener(e -> {
            CoristaFormDialog dlg = new CoristaFormDialog(SwingUtilities.getWindowAncestor(this));
            dlg.setVisible(true);
            if (dlg.isSaved()) loadData();
        });
        btnDelete.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row==-1) { JOptionPane.showMessageDialog(this,"Selecione um registro"); return; }
            int id = (int)table.getModel().getValueAt(row,0);
            try {
                dao.delete(id);
                loadData();
            } catch (Exception ex){ ex.printStackTrace(); JOptionPane.showMessageDialog(this,"Erro: "+ex.getMessage()); }
        });
        loadData();
    }

    private void loadData(){
        try {
            List<Corista> list = dao.findAll();
            DefaultTableModel m = new DefaultTableModel(new Object[]{"ID","Nome","Tipo Voz","Ativo"},0){
                public boolean isCellEditable(int r,int c){return false;}
            };
            for(Corista c: list) m.addRow(new Object[]{c.getId(), c.getNome(), c.getTipoVoz(), c.isAtivo()});
            table.setModel(m);
        } catch (Exception e){ e.printStackTrace(); JOptionPane.showMessageDialog(this,"Erro: "+e.getMessage()); }
    }
}
