// NOVO ARQUIVO: com.coral.dao.ParticipanteEventoDAO.java
package com.coral.dao;

import com.coral.model.ParticipanteEvento;
import com.coral.model.ParticipanteStatus;
import com.coral.model.ParticipanteStatus;
import com.coral.util.DB;
import java.sql.*;
import java.util.*;

public class ParticipanteEventoDAO {

    public List<ParticipanteStatus> findAtivosParaSelecao() throws SQLException {
        List<ParticipanteStatus> lista = new ArrayList<>();
        String sql =
                "(SELECT id, nome, 'CORISTA' AS tipo FROM coristas WHERE ativo = 1) " +
                        "UNION ALL " +
                        "(SELECT id, nome, 'MUSICO' AS tipo FROM musicos WHERE ativo = 1) " +
                        "ORDER BY nome";

        try (Connection c = DB.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(new ParticipanteStatus(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getString("tipo"),
                        false
                ));
            }
        }
        return lista;
    }

    public void deleteByAgenda(int idAgenda) throws SQLException {
        String sql = "DELETE FROM participantes_evento WHERE id_agenda=?";
        try (Connection c = DB.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, idAgenda);
            ps.executeUpdate();
        }
    }


    public void insert(ParticipanteEvento pe) throws SQLException {
        String sql = "INSERT INTO participantes_evento (id_agenda, id_corista, id_musico) VALUES (?, ?, ?)";
        try (Connection c = DB.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, pe.getIdAgenda());

            if (pe.getIdCorista() != null) ps.setInt(2, pe.getIdCorista()); else ps.setNull(2, Types.INTEGER);
            if (pe.getIdMusico() != null) ps.setInt(3, pe.getIdMusico()); else ps.setNull(3, Types.INTEGER);

            ps.executeUpdate();
        }
    }
}