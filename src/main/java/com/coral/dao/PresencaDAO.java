package com.coral.dao;
import com.coral.model.ParticipanteStatus;
import com.coral.model.Presenca;
import com.coral.util.DB;
import java.sql.*;
import java.util.*;

public class PresencaDAO {

    public void marcarPresenca(int idParticipante, String tipoParticipante, int idAgenda, boolean presente) throws SQLException {

        String idCol = tipoParticipante.equals("CORISTA") ? "id_corista" : "id_musico";
        String otherIdCol = tipoParticipante.equals("CORISTA") ? "id_musico" : "id_corista";

        String sel = "SELECT id FROM presencas WHERE " + idCol + "=? AND id_agenda=?";

        try (Connection c = DB.getConnection();
             PreparedStatement ps = c.prepareStatement(sel)) {

            ps.setInt(1, idParticipante);
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
                    String ins = "INSERT INTO presencas (" + idCol + ", id_agenda, presente, " + otherIdCol + ") VALUES (?,?,?,NULL)";
                    try (PreparedStatement pi = c.prepareStatement(ins)) {
                        pi.setInt(1, idParticipante);
                        pi.setInt(2, idAgenda);
                        pi.setBoolean(3, presente);
                        pi.executeUpdate();
                    }
                }
            }
        }
    }

    public List<ParticipanteStatus> findParticipantesChecklist(int idAgenda) throws SQLException {
        List<ParticipanteStatus> lista = new ArrayList<>();

        String sql =

                "(SELECT c.id, c.nome, 'CORISTA' AS tipo, COALESCE(p.presente, 0) AS presente_status " +
                        " FROM participantes_evento pe JOIN coristas c ON pe.id_corista = c.id " +
                        " LEFT JOIN presencas p ON c.id = p.id_corista AND p.id_agenda = pe.id_agenda " +
                        " WHERE pe.id_agenda = ? AND c.ativo = 1 AND pe.id_corista IS NOT NULL) " +
                        "UNION " +
                        "(SELECT m.id, m.nome, 'MUSICO' AS tipo, 0 AS presente_status " +
                        " FROM participantes_evento pe JOIN musicos m ON pe.id_musico = m.id " +
                        " WHERE pe.id_agenda = ? AND m.ativo = 1 AND pe.id_musico IS NOT NULL) " +
                        "ORDER BY tipo, nome";

        try (Connection conn = DB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idAgenda);
            ps.setInt(2, idAgenda);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(new ParticipanteStatus(
                            rs.getInt("id"),
                            rs.getString("nome"),
                            rs.getString("tipo"),
                            rs.getBoolean("presente_status")
                    ));
                }
            }
        }
        return lista;
    }
}
