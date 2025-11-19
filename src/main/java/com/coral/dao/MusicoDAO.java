package com.coral.dao;
import com.coral.model.Corista;
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

    public void insert(Musico mvo) throws SQLException {
        String sql = "INSERT INTO musicos (nome,instrumento,ativo) VALUES (?,?,?)";
        try (Connection c = DB.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, mvo.getNome());
            ps.setString(2, mvo.getInstrumento());
            ps.setBoolean(3, mvo.isAtivo());
            ps.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM musicos WHERE id=?";
        try (Connection c = DB.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    public Musico findById(int id) throws SQLException {
        Musico ms = null;
        String sql = "SELECT id,nome,tipo_voz,ativo FROM coristas WHERE id = ?";

        try (Connection c = DB.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    ms = new Musico();
                    ms.setId(rs.getInt("id"));
                    ms.setNome(rs.getString("nome"));
                    ms.setInstrumento(rs.getString("instrumento"));
                    ms.setAtivo(rs.getBoolean("ativo"));
                }
            }
        }
        return ms;
    }
}
