# Gestão de Empréstimos de Equipamentos

API REST desenvolvida com Java e Spring Boot para gerenciar o empréstimo de equipamentos para colaboradores de uma empresa.

O projeto foi desenvolvido durante um bootcamp, com o objetivo de praticar desenvolvimento backend, banco de dados, testes automatizados e segurança de APIs.

## Funcionalidades

- Cadastro, consulta, atualização e exclusão de equipamentos.
- Cadastro e consulta de colaboradores.
- Registro e devolução de equipamentos emprestados.
- Controle da disponibilidade dos equipamentos.
- Verificação de empréstimos atrasados.
- Validações para evitar empréstimos indevidos.
- Tratamento de erros com mensagens padronizadas.
- Login com JWT e controle de acesso por perfis ADMIN e COLABORADOR.

## Tecnologias utilizadas

- Java 17
- Spring Boot
- Spring Data JPA / Hibernate
- PostgreSQL
- Spring Security e JWT
- Maven
- JUnit e MockMvc
- Git e GitHub
- GitHub Actions

## Segurança

A API utiliza Spring Security com JWT para autenticação.

Existem dois perfis de acesso:

- **ADMIN:** pode gerenciar equipamentos e colaboradores, além de realizar empréstimos e devoluções.
- **COLABORADOR:** pode consultar equipamentos, realizar empréstimos e devoluções.

As senhas são protegidas com BCrypt.

## Testes automatizados

O projeto possui testes unitários e de integração para verificar as funcionalidades e as regras de negócio.

Também foram criados testes de segurança para verificar acessos permitidos e bloqueados.

Com o **GitHub Actions**, os testes são executados automaticamente a cada push na branch `main` e nos Pull Requests direcionados a ela.

## Como executar

1. Ter Java 17 e PostgreSQL instalados.
2. Criar os bancos de dados e configurar as variáveis de ambiente.
3. Executar a aplicação Spring Boot.
4. Utilizar o Postman para testar os endpoints.

A aplicação é executada localmente na porta `8081`.

---

Projeto desenvolvido para colocar em prática os conhecimentos adquiridos durante meus estudos em desenvolvimento backend Java.