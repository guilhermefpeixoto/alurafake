# 🚀 Alura Fake

## 📝 Sobre

Este repositório contém uma aplicação desenvolvida com **Spring Boot** e gerenciada com o **Maven**.  
O projeto é parte do processo seletivo da Alura. Trata-se de uma API que é uma versão simplificada do sistema da Alura. É possível criar usuários, cursos e atividades.

## 📁 Estrutura do Projeto

- `src/main/java/` – Código-fonte principal da aplicação
- `src/test/java/` – Testes unitários e de integração
- `pom.xml` – Arquivo de configuração do Maven com as dependências
- `application.properties` – Configurações da aplicação

## ⚙️ Tecnologias e Bibliotecas Utilizadas

- Java 21
- Spring Boot 3.3.3
- **Spring Boot Starters:**
  - `spring-boot-starter-web`
  - `spring-boot-starter-data-jpa`
  - `spring-boot-starter-validation`
  - `spring-boot-starter-security`
- **Validação:** `jakarta.validation-api
- **Persistência e Banco de Dados:**
  - MySQL (via `mysql-connector-j`)
  - Flyway (`flyway-core`, `flyway-mysql`)
- **Testes:**
  - `spring-boot-starter-test`
  - `spring-security-test`

---

## 📦 Como Instalar as Dependências

As dependências são gerenciadas automaticamente pelo Maven.

No terminal, execute na raiz do projeto:

```bash
./mvnw clean install -DskipTests
# ou, se o Maven estiver instalado no sistema:
mvn clean install -DskipTests
```

## ▶️ Como Rodar a Aplicação
É necessário subir um banco de dados MySQL na porta 3306.
É recomendado que seja feito via Docker, por meio do `docker-compose.yaml` existente na raiz desse repositório.
Para isso, preencha o `.env` disponibilizado, abra seu console e rode o comando abaixo:
```bash
docker compose up -d
```

Com o banco de dados rodando, é necessário definir as configurações de nosso projeto no `application.properties`.

💡 Lembre-se de passar as configurações de conexão ao banco de dados corretamente, de acordo com as variáveis criadas no `.env` 

Ao garantir que as condições foram atendidas, utilize o comando abaixo para inicializar a aplicação:
```bash
mvn spring-boot:run
```

## 🧪 Como Rodar os Testes
Para executar os testes automatizados, utilize:
```bash
mvn test
```