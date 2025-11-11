package com.coral.dao;
import com.coral.model.Presenca;
import com.coral.util.DB;
import java.sql.*;
import java.util.*;

public class PresencaDAO {
    public void marcarPresenca(int idCorista, int idAgenda, boolean presente) throws SQLException {
        String sel = "SELECT id FROM presencas WHERE id_corista=? AND id_agenda=?";
        try (Connection c = DB.getConnection();
             PreparedStatement ps = c.prepareStatement(sel)) {
            ps.setInt(1, idCorista);
            ps.setInt(2, idAgenda);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int id = rs.getInt("id");
                    String up = "UPDATE presencas SET presente=? WHERE id=?";
                    try (PreparedStatement pu = c.prepareStatement(up)) {
                        pu.setBoolean(1, presente);
                        pu.setInt(2, id);
                        pu.executeUpdate();
                    }
                } else {
                    String ins = "INSERT INTO presencas (id_corista,id_agenda,presente) VALUES (?,?,?)";
                    try (PreparedStatement pi = c.prepareStatement(ins)) {
                        pi.setInt(1, idCorista);
                        pi.setInt(2, idAgenda);
                        pi.setBoolean(3, presente);
                        pi.executeUpdate();
                    }
                }
            }
        }
    }
}
