package com.coral.ui;
import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    public MainFrame() {
        setTitle("Sistema Coral - Principal");
        setSize(1000,650);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        JTabbedPane tabs = new JTabbedPane();

        tabs.add("Coristas", new CoristasPanel());
        tabs.add("Músicos", new MusicosPanel());
        tabs.add("Agenda", new AgendaPanel());
        tabs.add("Presenças", new PresencasPanel());

        add(tabs, BorderLayout.CENTER);
        setLocationRelativeTo(null);
    }
}
