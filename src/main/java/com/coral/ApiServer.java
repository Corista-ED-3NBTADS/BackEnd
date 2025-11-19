package com.coral;

import com.coral.dao.*;
import com.coral.model.*;
import com.coral.util.DB;
import com.google.gson.Gson;
import io.javalin.Javalin;
import io.javalin.http.Handler; // Importação correta (existe)

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import io.javalin.json.JavalinGson;
import com.google.gson.JsonSyntaxException; // Adicionando import específico

public class ApiServer {

    private static final int PORT = 7000;
    private static final Gson GSON = new Gson();


    // Instâncias dos DAOs
    private static final CoristaDAO CORISTA_DAO = new CoristaDAO();
    private static final MusicoDAO MUSICO_DAO = new MusicoDAO();
    private static final AgendaDAO AGENDA_DAO = new AgendaDAO();
    private static final PresencaDAO PRESENCA_DAO = new PresencaDAO();

    // Classes auxiliares (mantidas)
    private static class LoginRequest {
        public String username;
        public String password;
    }
    private static class MarcacaoPresencaRequest {
        public int idCorista;
        public int idAgenda;
        public boolean presente;
    }

    // Lógica de autenticação (mantida)
    private static boolean authenticate(String user, String pass) throws SQLException {
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

    public static void main(String[] args) {
        Javalin app = Javalin.create(config -> {

            // ===============================================
            // 🚩 NOVO CÓDIGO AQUI: CONFIGURAÇÃO DO JSON MAPPER
            // ===============================================
            config.jsonMapper(new JavalinGson());

            // Configuração CORS (Corrigida anteriormente)
            config.bundledPlugins.enableCors(cors -> {
                cors.addRule(it -> {
                    it.anyHost();
                });
            });

        }).start(PORT);

        // =========================================================
        // 🚨 TRATAMENTO DE EXCEÇÕES (MOVIMENTADO PARA AQUI)
        // =========================================================
        // Exceção de Banco de Dados
        app.exception(SQLException.class, (e, ctx) -> {
            // Agora 'ctx.status' e 'e.getMessage' devem ser resolvidos
            ctx.status(500).json("{\"error\": \"Erro de Banco de Dados: " + e.getMessage() + "\"}");
        });

        // Exceção de JSON Inválido
        app.exception(JsonSyntaxException.class, (e, ctx) -> {
            ctx.status(400).json("{\"error\": \"Dados inválidos no corpo da requisição (JSON)\"}");
        });


        // =========================================================
        // 🔑 ROTA DE AUTENTICAÇÃO (MANTIDA)
        // =========================================================
        app.post("/api/login", ctx -> {
            LoginRequest req = GSON.fromJson(ctx.body(), LoginRequest.class);
            if (authenticate(req.username, req.password)) {
                ctx.json("{\"success\": true, \"message\": \"Login OK\"}");
            } else {
                ctx.status(401).json("{\"success\": false, \"message\": \"Usuário ou senha inválidos\"}");
            }
        });

        // =========================================================
        // 🎼 ROTAS DE CORISTAS (MANTIDAS)
        // =========================================================
        app.get("/api/coristas", ctx -> {
            ctx.json(CORISTA_DAO.findAll());
        });
        app.post("/api/coristas", ctx -> {
            Corista novoCorista = GSON.fromJson(ctx.body(), Corista.class);
            CORISTA_DAO.insert(novoCorista);
            ctx.status(201).result("Corista adicionado com sucesso");
        });
        app.delete("/api/coristas/{id}", ctx -> {
            int id = Integer.parseInt(ctx.pathParam("id"));
            CORISTA_DAO.delete(id);
            ctx.status(204);
        });
        app.get("/api/coristas/{id}", ctx -> {
            int id = Integer.parseInt(ctx.pathParam("id"));
            Corista corista = CORISTA_DAO.findById(id); // <--- CHAMA O NOVO MÉTODO

            if (corista != null) {
                ctx.json(corista); // Retorna o objeto Corista em JSON
            } else {
                ctx.status(404).result("Corista não encontrado"); // Retorna 404 se o ID não existir
            }
        });

        // =========================================================
        // 🎶 ROTAS DE MÚSICOS (MANTIDAS)
        // =========================================================
        app.get("/api/musicos", ctx -> {
            ctx.json(MUSICO_DAO.findAll());
        });
        app.post("/api/musicos", ctx -> {
            Musico novoMusico = GSON.fromJson(ctx.body(), Musico.class);
            MUSICO_DAO.insert(novoMusico);
            ctx.status(201).result("Músico adicionado com sucesso");
        });
        app.delete("/api/musicos/{id}", ctx -> {
            int id = Integer.parseInt(ctx.pathParam("id"));
            MUSICO_DAO.delete(id);
            ctx.status(204);
        });

        // =========================================================
        // 📅 ROTAS DE AGENDA (MANTIDAS)
        // =========================================================
        app.get("/api/agenda", ctx -> {
            ctx.json(AGENDA_DAO.findAll());
        });
        app.post("/api/agenda", ctx -> {
            Agenda novaAgenda = GSON.fromJson(ctx.body(), Agenda.class);
            AGENDA_DAO.insert(novaAgenda);
            ctx.status(201).result("Agenda adicionada com sucesso");
        });
        app.delete("/api/agenda/{id}", ctx -> {
            int id = Integer.parseInt(ctx.pathParam("id"));
            AGENDA_DAO.delete(id);
            ctx.status(204);
        });

        // =========================================================
        // ✔️ ROTAS DE PRESENÇAS (MANTIDAS)
        // =========================================================
        app.post("/api/presencas/marcar", ctx -> {
            MarcacaoPresencaRequest req = GSON.fromJson(ctx.body(), MarcacaoPresencaRequest.class);
            PRESENCA_DAO.marcarPresenca(req.idCorista, req.idAgenda, req.presente);
            ctx.result("Presença marcada/atualizada com sucesso!");
        });


        System.out.println("Servidor Javalin iniciado na porta: " + PORT);
    }
}