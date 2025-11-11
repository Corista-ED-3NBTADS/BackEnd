package com.coral.dao;
import com.coral.model.Musico;
import com.coral.util.DB;
import java.sql.*;
import java.util.*;

public class MusicoDAO {
    public List<Musico> findAll() throws SQLException {
        List<Musico> list = new ArrayList<>();
        String sql = "SELECT id,nome,instrumento,ativo FROM musicos";
        try (Connection c = DB.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Musico m = new Musico();
                m.setId(rs.getInt("id"));
                m.setNome(rs.getString("nome"));
                m.setInstrumento(rs.getString("instrumento"));
                m.setAtivo(rs.getBoolean("ativo"));
                list.add(m);
            }
        }
        return list;
    }
}
