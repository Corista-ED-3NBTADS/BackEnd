package com.coral.ui;
import com.coral.util.DB;
import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class LoginFrame extends JFrame {
    private JTextField tfUser = new JTextField(15);
    private JPasswordField pfPass = new JPasswordField(15);
    public LoginFrame() {
        setTitle("Login - Coral");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        JPanel p = new JPanel(new GridBagLayout());
        p.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(6,6,6,6);
        c.gridx=0; c.gridy=0; c.anchor=GridBagConstraints.EAST;
        p.add(new JLabel("Usuário:"), c);
        c.gridx=1; c.anchor=GridBagConstraints.WEST;
        p.add(tfUser, c);
        c.gridx=0; c.gridy=1; c.anchor=GridBagConstraints.EAST;
        p.add(new JLabel("Senha:"), c);
        c.gridx=1; c.anchor=GridBagConstraints.WEST;
        p.add(pfPass, c);
        JButton btnLogin = new JButton("Entrar");
        c.gridx=0; c.gridy=2; c.gridwidth=2;
        c.anchor=GridBagConstraints.CENTER;
        p.add(btnLogin, c);
        add(p, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(null);

        btnLogin.addActionListener(e -> {
            try { if(authenticate(tfUser.getText(), new String(pfPass.getPassword()))){
                    SwingUtilities.invokeLater(() -> {
                        new MainFrame().setVisible(true);
                        dispose();
                    });
                } else {
                    JOptionPane.showMessageDialog(this, "Usuário ou senha inválidos");
                }
            } catch (Exception ex) { ex.printStackTrace(); JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage()); }
        });
    }

    private boolean authenticate(String user, String pass) throws SQLException {
        String sql = "SELECT COUNT(*) FROM usuario WHERE username=? AND password=?";
        try (Connection c = DB.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, user);
            ps.setString(2, pass);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1) > 0;
            }
        }
        return false;
    }
}
