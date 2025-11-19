package com.coral;

import com.coral.dao.*;
import com.coral.model.*;
import com.coral.util.DB;
import com.google.gson.Gson;
import io.javalin.Javalin;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import io.javalin.json.JavalinGson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class ApiServer {

    private static final int PORT = 7000;
    private static final Gson GSON = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd")
            .create();

    private static final CoristaDAO CORISTA_DAO = new CoristaDAO();
    private static final MusicoDAO MUSICO_DAO = new MusicoDAO();
    private static final AgendaDAO AGENDA_DAO = new AgendaDAO();
    private static final PresencaDAO PRESENCA_DAO = new PresencaDAO();
    private static final ParticipanteEventoDAO PARTICIPANTE_EVENTO_DAO = new ParticipanteEventoDAO();


    private static class LoginRequest {
        public String username;
        public String password;
    }

    private static class MarcacaoPresencaRequest {
        public int idParticipante;
        public String tipo;
        public int idAgenda;
        public boolean presente;
    }

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
            config.jsonMapper(new JavalinGson());
            config.bundledPlugins.enableCors(cors -> {
                cors.addRule(it -> {
                    it.anyHost();
                });
            });

        }).start(PORT);

        app.exception(SQLException.class, (e, ctx) -> {
            ctx.status(500).json("{\"error\": \"Erro de Banco de Dados: " + e.getMessage() + "\"}");
        });

        app.exception(JsonSyntaxException.class, (e, ctx) -> {
            ctx.status(400).json("{\"error\": \"Dados inválidos no corpo da requisição (JSON)\"}");
        });


        app.post("/api/login", ctx -> {
            LoginRequest req = GSON.fromJson(ctx.body(), LoginRequest.class);
            if (authenticate(req.username, req.password)) {
                ctx.json("{\"success\": true, \"message\": \"Login OK\"}");
            } else {
                ctx.status(401).json("{\"success\": false, \"message\": \"Usuário ou senha inválidos\"}");
            }
        });

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
            Corista corista = CORISTA_DAO.findById(id);

            if (corista != null) {
                ctx.json(corista);
            } else {
                ctx.status(404).result("Corista não encontrado");
            }
        });

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
        app.get("/api/musicos/{id}", ctx -> {
            int id = Integer.parseInt(ctx.pathParam("id"));
            Musico musico = MUSICO_DAO.findById(id);

            if (musico != null) {
                ctx.json(musico);
            } else {
                ctx.status(404).result("Musica não encontrado");
            }
        });


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
        app.put("/api/agenda/{id}", ctx -> {
            int id = Integer.parseInt(ctx.pathParam("id"));
            Agenda a = GSON.fromJson(ctx.body(), Agenda.class);
            a.setId(id);

            AGENDA_DAO.update(a);
            ctx.status(200).result("Evento atualizado com sucesso");
        });

        app.get("/api/participantes/ativos", ctx -> {
            ctx.json(PARTICIPANTE_EVENTO_DAO.findAtivosParaSelecao());
        });


        app.post("/api/agenda/{idAgenda}/participantes", ctx -> {
            int idAgenda = Integer.parseInt(ctx.pathParam("idAgenda"));

            Type listType = new TypeToken<List<ParticipanteEvento>>(){}.getType();
            List<ParticipanteEvento> lista = GSON.fromJson(ctx.body(), listType);

            PARTICIPANTE_EVENTO_DAO.deleteByAgenda(idAgenda);


            for (ParticipanteEvento pe : lista) {
                pe.setIdAgenda(idAgenda);
                PARTICIPANTE_EVENTO_DAO.insert(pe);
            }

            ctx.status(200).result("Lista de participantes envolvidos atualizada.");
        });


        app.post("/api/presencas/marcar", ctx -> {

            MarcacaoPresencaRequest req = GSON.fromJson(ctx.body(), MarcacaoPresencaRequest.class);

            PRESENCA_DAO.marcarPresenca(
                    req.idParticipante,
                    req.tipo.toUpperCase(),
                    req.idAgenda,
                    req.presente
            );
            ctx.result("Presença marcada/atualizada com sucesso!");
        });

        app.get("/api/presencas/checklist/{idAgenda}", ctx -> {
            int idAgenda = Integer.parseInt(ctx.pathParam("idAgenda"));
            ctx.json(PRESENCA_DAO.findParticipantesChecklist(idAgenda));
        });



        System.out.println("Servidor Javalin iniciado na porta: " + PORT);
    }
}