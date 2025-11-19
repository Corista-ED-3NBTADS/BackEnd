package com.coral.dao;
import com.coral.model.Corista;
import com.coral.util.DB;
import java.sql.*;
import java.util.*;

public class CoristaDAO {

    public List<Corista> findAll() throws SQLException {
        List<Corista> list = new ArrayList<>();
        String sql = "SELECT id,nome,tipo_voz,ativo FROM coristas";
        try (Connection c = DB.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Corista cr = new Corista();
                cr.setId(rs.getInt("id"));
                cr.setNome(rs.getString("nome"));
                cr.setTipoVoz(rs.getString("tipo_voz"));
                cr.setAtivo(rs.getBoolean("ativo"));
                list.add(cr);
            }
        }
        return list;
    }

    public void insert(Corista cvo) throws SQLException {
        String sql = "INSERT INTO coristas (nome,tipo_voz,ativo) VALUES (?,?,?)";
        try (Connection c = DB.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, cvo.getNome());
            ps.setString(2, cvo.getTipoVoz());
            ps.setBoolean(3, cvo.isAtivo());
            ps.executeUpdate();
        }
    }

    public void update(Corista cvo) throws SQLException {
        String sql = "UPDATE coristas SET nome=?, tipo_voz=?, ativo=? WHERE id=?";
        try (Connection c = DB.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, cvo.getNome());
            ps.setString(2, cvo.getTipoVoz());
            ps.setBoolean(3, cvo.isAtivo());
            ps.setInt(4, cvo.getId());
            ps.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM coristas WHERE id=?";
        try (Connection c = DB.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
    public Corista findById(int id) throws SQLException {
        Corista cr = null;
        String sql = "SELECT id,nome,tipo_voz,ativo FROM coristas WHERE id = ?";

        try (Connection c = DB.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    cr = new Corista();
                    cr.setId(rs.getInt("id"));
                    cr.setNome(rs.getString("nome"));
                    cr.setTipoVoz(rs.getString("tipo_voz"));
                    cr.setAtivo(rs.getBoolean("ativo"));
                }
            }
        }
        return cr;
    }
}
