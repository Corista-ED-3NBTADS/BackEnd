package com.coral.ui;
import javax.swing.*;
import com.coral.dao.AgendaDAO;
import com.coral.dao.CoristaDAO;
import com.coral.dao.PresencaDAO;
import com.coral.model.Agenda;
import com.coral.model.Corista;
import java.awt.*;
import java.util.List;

public class PresencasPanel extends JPanel {
    private JComboBox<Agenda> cbAgenda = new JComboBox<>();
    private JPanel listPanel = new JPanel();
    private PresencaDAO dao = new PresencaDAO();
    private CoristaDAO coristaDAO = new CoristaDAO();
    private AgendaDAO agendaDAO = new AgendaDAO();

    public PresencasPanel(){
        setLayout(new BorderLayout());
        JPanel top = new JPanel();
        top.add(new JLabel("Selecione Agenda:")); top.add(cbAgenda);
        JButton btnLoad = new JButton("Carregar Coristas");
        top.add(btnLoad);
        add(top, BorderLayout.NORTH);

        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        add(new JScrollPane(listPanel), BorderLayout.CENTER);

        JPanel bot = new JPanel();
        JButton btnSave = new JButton("Salvar Presenças");
        bot.add(btnSave);
        add(bot, BorderLayout.SOUTH);

        btnLoad.addActionListener(e -> loadAgendasAndCoristas());
        btnSave.addActionListener(e -> savePresencas());
        loadAgendas();
    }

    private void loadAgendas(){
        try {
            List<Agenda> agendas = agendaDAO.findAll();
            cbAgenda.removeAllItems();
            for(Agenda a: agendas) cbAgenda.addItem(a);
        } catch (Exception e){ e.printStackTrace(); JOptionPane.showMessageDialog(this,"Erro: "+e.getMessage()); }
    }

    private void loadAgendasAndCoristas(){
        listPanel.removeAll();
        try {
            List<Corista> coristas = coristaDAO.findAll();
            for(Corista c: coristas){
                JCheckBox cb = new JCheckBox(c.getNome());
                cb.setName(String.valueOf(c.getId()));
                cb.setSelected(false);
                listPanel.add(cb);
            }
            listPanel.revalidate();
            listPanel.repaint();
        } catch (Exception e){ e.printStackTrace(); JOptionPane.showMessageDialog(this,"Erro: "+e.getMessage()); }
    }

    private void savePresencas(){
        Agenda sel = (Agenda) cbAgenda.getSelectedItem();
        if(sel==null){ JOptionPane.showMessageDialog(this,"Selecione uma agenda"); return; }
        Component[] comps = listPanel.getComponents();
        try {
            for(Component comp: comps){
                if(comp instanceof JCheckBox){
                    JCheckBox cb = (JCheckBox) comp;
                    int idCor = Integer.parseInt(cb.getName());
                    dao.marcarPresenca(idCor, sel.getId(), cb.isSelected());
                }
            }
            JOptionPane.showMessageDialog(this,"Presenças salvas com sucesso");
        } catch (Exception e){ e.printStackTrace(); JOptionPane.showMessageDialog(this,"Erro: "+e.getMessage()); }
    }
}
