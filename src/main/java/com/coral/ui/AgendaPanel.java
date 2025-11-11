package com.coral.ui;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import com.coral.dao.AgendaDAO;
import com.coral.model.Agenda;
import java.awt.*;
import java.util.List;
import java.text.SimpleDateFormat;

public class AgendaPanel extends JPanel {
    private JTable table;
    private AgendaDAO dao = new AgendaDAO();
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    public AgendaPanel(){
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
            AgendaFormDialog dlg = new AgendaFormDialog(SwingUtilities.getWindowAncestor(this));
            dlg.setVisible(true);
            if (dlg.isSaved()) loadData();
        });
        btnDelete.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row==-1) { JOptionPane.showMessageDialog(this,"Selecione um registro"); return; }
            int id = (int)table.getModel().getValueAt(row,0);
            int confirm = JOptionPane.showConfirmDialog(this, "Confirma exclusão?", "Excluir", JOptionPane.YES_NO_OPTION);
            if (confirm==JOptionPane.YES_OPTION) {
                try {
                    dao.delete(id);
                    loadData();
                } catch (Exception ex){ ex.printStackTrace(); JOptionPane.showMessageDialog(this,"Erro: "+ex.getMessage()); }
            }
        });
        loadData();
    }

    private void loadData(){
        try {
            List<Agenda> list = dao.findAll();
            DefaultTableModel m = new DefaultTableModel(new Object[]{"ID","Data","Local","Descrição"},0){
                public boolean isCellEditable(int r,int c){return false;}
            };
            for(Agenda a: list) {
                String d = a.getData()!=null ? sdf.format(a.getData()) : "";
                m.addRow(new Object[]{a.getId(), d, a.getLocal(), a.getDescricao()});
            }
            table.setModel(m);
        } catch (Exception e){ e.printStackTrace(); JOptionPane.showMessageDialog(this,"Erro: "+e.getMessage()); }
    }
}
