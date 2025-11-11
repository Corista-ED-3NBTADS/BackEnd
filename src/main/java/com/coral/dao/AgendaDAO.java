package com.coral.dao;
import com.coral.model.Agenda;
import com.coral.util.DB;
import java.sql.*;
import java.util.*;

public class AgendaDAO {
    public List<Agenda> findAll() throws SQLException {
        List<Agenda> list = new ArrayList<>();
        String sql = "SELECT id,data,local,descricao FROM agenda_apresentacoes";
        try (Connection c = DB.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Agenda a = new Agenda();
                a.setId(rs.getInt("id"));
                a.setData(rs.getDate("data"));
                a.setLocal(rs.getString("local"));
                a.setDescricao(rs.getString("descricao"));
                list.add(a);
            }
        }
        return list;
    }

    public void insert(Agenda a) throws SQLException {
        String sql = "INSERT INTO agenda_apresentacoes (`data`, `local`, `descricao`) VALUES (?,?,?)";
        try (Connection c = DB.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setDate(1, a.getData());
            ps.setString(2, a.getLocal());
            ps.setString(3, a.getDescricao());
            ps.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM agenda_apresentacoes WHERE id=?";
        try (Connection c = DB.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
}
