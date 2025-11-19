# API de Gerenciamento de Coral

![Java](https://img.shields.io/badge/Java-17-blue) ![Javalin](https://img.shields.io/badge/Javalin-6.1.3-brightgreen) ![MySQL](https://img.shields.io/badge/MySQL-8.0-orange)

API RESTful para gerenciamento de corais, desenvolvida em Java com o framework Javalin. O sistema permite o cadastro de membros, agendamento de eventos e controle de presença, fornecendo uma solução completa para a administração de um grupo de coral.

## ✨ Funcionalidades

- **Gerenciamento de Membros:** CRUD completo para coristas e músicos, com controle de status (ativo/inativo).
- **Gestão de Agenda:** Crie, edite e visualize eventos, ensaios e apresentações.
- **Controle de Presença:** Registre a presença de participantes em cada evento de forma simples e rápida.
- **Associação de Participantes:** Vincule coristas e músicos específicos a cada evento agendado.
- **Autenticação:** Endpoint de login para acesso seguro à API.

## 🚀 Tecnologias Utilizadas

- **Backend:**
  - [Java 17](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
  - [Javalin 6.1.3](https://javalin.io/) - Um framework web leve e moderno para Java/Kotlin.
  - [Maven](https://maven.apache.org/) - Gerenciador de dependências e build.
- **Banco de Dados:**
  - [MySQL 8.0](https://www.mysql.com/) - Sistema de gerenciamento de banco de dados relacional.
  - [MySQL Connector/J 8.0.33](https://dev.mysql.com/downloads/connector/j/) - Driver JDBC para conexão com o MySQL.
- **Serialização JSON:**
  - [Gson 2.10.1](https://github.com/google/gson) - Para converter objetos Java em sua representação JSON e vice-versa.

## 📋 Pré-requisitos

Antes de começar, certifique-se de ter os seguintes softwares instalados em sua máquina:

- **JDK 17** (Java Development Kit)
- **Maven**
- **MySQL Server**

## ⚙️ Instalação e Configuração

Siga os passos abaixo para configurar e executar o projeto em seu ambiente local.

1.  **Clone o repositório:**

    ```bash
    git clone <URL_DO_REPOSITORIO>
    cd BackEnd-main
    ```

2.  **Crie o banco de dados:**

    Conecte-se ao seu servidor MySQL e execute o seguinte comando para criar o banco de dados da aplicação:

    ```sql
    CREATE DATABASE coral;
    ```

3.  **Configure a conexão com o banco de dados:**

    Abra o arquivo `src/main/java/com/coral/util/DB.java` e altere as credenciais de conexão (`URL`, `USER`, `PASS`) para corresponder à sua configuração local do MySQL.

    ```java
    // src/main/java/com/coral/util/DB.java
    private static final String URL = "jdbc:mysql://localhost:3306/coral?useSSL=false&serverTimezone=UTC";
    private static final String USER = "seu_usuario"; // Altere aqui
    private static final String PASS = "sua_senha";   // Altere aqui
    ```

    **Atenção:** Para produção, é altamente recomendável usar variáveis de ambiente para armazenar essas credenciais.

4.  **Crie as tabelas no banco de dados:**

    Execute o script SQL abaixo para criar todas as tabelas necessárias.

    <details>
    <summary>Clique para ver o Script SQL</summary>

    ```sql
    CREATE TABLE `coristas` (
      `id` int NOT NULL AUTO_INCREMENT,
      `nome` varchar(255) NOT NULL,
      `tipo_voz` varchar(50) DEFAULT NULL,
      `ativo` tinyint(1) DEFAULT '1',
      PRIMARY KEY (`id`)
    );

    CREATE TABLE `musicos` (
      `id` int NOT NULL AUTO_INCREMENT,
      `nome` varchar(255) NOT NULL,
      `instrumento` varchar(100) DEFAULT NULL,
      `ativo` tinyint(1) DEFAULT '1',
      PRIMARY KEY (`id`)
    );

    CREATE TABLE `agenda_apresentacoes` (
      `id` int NOT NULL AUTO_INCREMENT,
      `data` date NOT NULL,
      `local` varchar(255) NOT NULL,
      `descricao` text,
      PRIMARY KEY (`id`)
    );

    CREATE TABLE `participantes_evento` (
      `id_agenda` int NOT NULL,
      `id_corista` int DEFAULT NULL,
      `id_musico` int DEFAULT NULL,
      KEY `id_agenda` (`id_agenda`),
      KEY `id_corista` (`id_corista`),
      KEY `id_musico` (`id_musico`),
      CONSTRAINT `participantes_evento_ibfk_1` FOREIGN KEY (`id_agenda`) REFERENCES `agenda_apresentacoes` (`id`) ON DELETE CASCADE,
      CONSTRAINT `participantes_evento_ibfk_2` FOREIGN KEY (`id_corista`) REFERENCES `coristas` (`id`) ON DELETE CASCADE,
      CONSTRAINT `participantes_evento_ibfk_3` FOREIGN KEY (`id_musico`) REFERENCES `musicos` (`id`) ON DELETE CASCADE
    );

    CREATE TABLE `presencas` (
      `id` int NOT NULL AUTO_INCREMENT,
      `id_agenda` int NOT NULL,
      `id_corista` int DEFAULT NULL,
      `id_musico` int DEFAULT NULL,
      `presente` tinyint(1) NOT NULL DEFAULT '0',
      PRIMARY KEY (`id`),
      UNIQUE KEY `idx_participante_agenda` (`id_agenda`,`id_corista`,`id_musico`),
      KEY `id_corista` (`id_corista`),
      KEY `id_musico` (`id_musico`),
      CONSTRAINT `presencas_ibfk_1` FOREIGN KEY (`id_agenda`) REFERENCES `agenda_apresentacoes` (`id`) ON DELETE CASCADE,
      CONSTRAINT `presencas_ibfk_2` FOREIGN KEY (`id_corista`) REFERENCES `coristas` (`id`) ON DELETE CASCADE,
      CONSTRAINT `presencas_ibfk_3` FOREIGN KEY (`id_musico`) REFERENCES `musicos` (`id`) ON DELETE CASCADE
    );

    CREATE TABLE `usuario` (
      `username` varchar(50) NOT NULL,
      `password` varchar(255) NOT NULL,
      PRIMARY KEY (`username`)
    );

    -- Adicionar um usuário de exemplo
    INSERT INTO `usuario` (username, password) VALUES ('admin', 'admin123');
    ```

    </details>

5.  **Compile e execute o projeto:**

    Use o Maven para compilar o projeto e instalar as dependências:

    ```bash
    mvn clean install
    ```

    Após a compilação, execute a classe principal `ApiServer.java` a partir da sua IDE de preferência (IntelliJ, Eclipse, etc.) ou via linha de comando.

    O servidor será iniciado na porta `7000`.

    ```
    Servidor Javalin iniciado na porta: 7000
    ```

## 🌐 Endpoints da API

A API está organizada em torno dos seguintes recursos:

| Recurso | Endpoints |
|---|---|
| **Autenticação** | `POST /api/login` |
| **Coristas** | `GET, POST /api/coristas` <br> `GET, PUT, DELETE /api/coristas/{id}` |
| **Músicos** | `GET, POST /api/musicos` <br> `GET, PUT, DELETE /api/musicos/{id}` |
| **Agenda** | `GET, POST /api/agenda` <br> `PUT, DELETE /api/agenda/{id}` |
| **Participantes** | `GET /api/participantes/ativos` <br> `POST /api/agenda/{idAgenda}/participantes` |
| **Presenças** | `POST /api/presencas/marcar` <br> `GET /api/presencas/checklist/{idAgenda}` |

Para uma descrição detalhada de cada endpoint, consulte a **Documentação Técnica**.
