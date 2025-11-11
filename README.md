# Coral Musical - Modern (FlatLaf) Java Swing + MySQL

Alterações nesta versão:
- Tema moderno FlatLaf Light (aplicado automaticamente).
- Aba Agenda com formulário de cadastro/edição e exclusão.
- Todos os SQLs incluídos no diretório db/sql.

## Como usar

1. Importe os SQLs em `db/sql/` no MySQL (schema 'coral'):
   - coral_coristas.sql
   - coral_musicos.sql
   - coral_agenda_apresentacoes.sql
   - coral_presencas.sql
   - usuario.sql (já incluso — cria usuário admin/admin123)

2. Ajuste as credenciais no arquivo `src/main/java/com/coral/util/DB.java` se necessário.

3. Build:
   - `mvn package`
   - Execute `AppMain` (pelo VSCode ou `java -cp target/coral-app-1.0-SNAPSHOT.jar com.coral.AppMain`)

