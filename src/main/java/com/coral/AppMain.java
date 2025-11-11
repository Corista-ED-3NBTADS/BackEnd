package com.coral;

import com.coral.ui.LoginFrame;
import javax.swing.*;
import com.formdev.flatlaf.FlatLightLaf;

public class AppMain {
    public static void main(String[] args) {
        try {
            FlatLightLaf.setup();
        } catch (Exception e) {
            System.err.println("Falha ao aplicar FlatLaf, usando L&F do sistema.");
        }
        SwingUtilities.invokeLater(() -> {
            new LoginFrame().setVisible(true);
        });
    }
}
