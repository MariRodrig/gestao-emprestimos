# Gestão de Empréstimos de Equipamentos

API REST desenvolvida com Java e Spring Boot para gerenciar empréstimos de equipamentos para colaboradores de uma empresa.

O projeto foi desenvolvido durante um bootcamp para praticar desenvolvimento backend, banco de dados, segurança e testes automatizados.

## Funcionalidades

- Cadastro, consulta, atualização e exclusão de equipamentos.
- Cadastro e consulta de colaboradores.
- Registro de empréstimos e devoluções.
- Controle de disponibilidade dos equipamentos.
- Verificação de empréstimos atrasados.
- Validações e tratamento de exceções.
- Autenticação com JWT e controle de acesso por perfil.

## Tecnologias utilizadas

- Java 17 e Spring Boot
- Spring Data JPA / Hibernate
- PostgreSQL
- Spring Security e JWT
- Maven
- JUnit e MockMvc
- Swagger / OpenAPI
- Docker
- Git e GitHub
- GitHub Actions
- Render

## Segurança

A API utiliza Spring Security e JWT para autenticação.

Existem dois perfis de acesso:

- **ADMIN:** pode gerenciar equipamentos, colaboradores e empréstimos.
- **COLABORADOR:** pode consultar equipamentos e realizar empréstimos e devoluções.

As senhas são protegidas com BCrypt.

## Testes automatizados

O projeto possui 22 testes automatizados para verificar funcionalidades, regras de negócio e segurança.

Os testes também são executados pelo GitHub Actions a cada push na `main` e em Pull Requests direcionados a ela.

Para executar os testes localmente:

```bash
./mvnw verify
```

No Windows, utilize `.\mvnw.cmd verify`.

## Como executar localmente

1. Ter Java 17 e PostgreSQL instalados.
2. Criar o banco `gestao_emprestimos`.
3. Configurar as variáveis de ambiente necessárias.
4. Executar a aplicação Spring Boot.

A API será iniciada na porta `8081`.

## Deploy e documentação

A API foi publicada no Render utilizando Docker e PostgreSQL na nuvem.

O Swagger permite visualizar e testar os endpoints diretamente pelo navegador.

**[Acessar Swagger da API](https://gestao-emprestimos-x3we.onrender.com/swagger-ui/index.html)**

Para testar os endpoints protegidos, é necessário fazer login e informar o token JWT no botão **Authorize**.

**Observação:** a aplicação utiliza serviços gratuitos do Render e pode demorar para responder ao primeiro acesso. O banco gratuito atual tem prazo de expiração.

---

Projeto desenvolvido para praticar os conhecimentos adquiridos durante meus estudos em desenvolvimento backend Java.