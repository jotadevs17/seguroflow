# SeguroFlow

SeguroFlow é um projeto de estudo criado para praticar uma stack usada em aplicações modernas do mercado segurador.

A ideia é construir, de forma incremental, uma aplicação com frontend em React/TypeScript, API principal em Java/Spring Boot, microserviço em Python/FastAPI, banco SQL Server e ambiente local com Docker Compose.

## Objetivo do projeto

O objetivo do SeguroFlow é simular partes de um sistema de seguros, permitindo praticar conceitos como:

- APIs REST
- arquitetura em camadas
- persistência com SQL Server
- integração entre serviços
- frontend com React e TypeScript
- containers com Docker
- organização de projeto com Git
- boas práticas de desenvolvimento backend e frontend

## Stack planejada

- Java 17+
- Spring Boot
- SQL Server
- Python
- FastAPI
- React
- TypeScript
- Docker
- Docker Compose
- Git

## Estrutura inicial

```text
seguroflow/
  frontend/
  policy-api/
  risk-api/
  docker-compose.yml
  README.md
  .gitignore
```

## Responsabilidade de cada parte

### frontend

Aplicacao web em React com TypeScript.

Responsabilidade futura:

- exibir telas do sistema
- consumir APIs backend
- organizar componentes, paginas, tipos e services de integracao

### policy-api

API principal do sistema, desenvolvida em Java com Spring Boot.

Responsabilidade futura:

- expor endpoints REST
- concentrar regras principais do dominio
- acessar o banco SQL Server
- organizar controllers, services, repositories, DTOs e entidades

### risk-api

Microservico em Python com FastAPI.

Responsabilidade futura:

- receber dados relacionados ao seguro
- calcular ou simular analise de risco
- retornar resultados para a API principal

### docker-compose.yml

Arquivo responsavel por declarar os servicos necessarios para o ambiente local.

Nesta etapa inicial, o Docker Compose sobe o SQL Server localmente na porta `1433`.

## Status atual

Projeto em fase inicial de setup.

Nesta etapa, a estrutura base do repositorio foi criada e o SQL Server ja pode ser executado localmente com Docker Compose.

## Como executar

Pre-requisito: Docker Desktop instalado e em execucao.

Para subir o ambiente local:

```powershell
docker compose up -d
```

Para verificar os containers:

```powershell
docker compose ps
```

Para parar o ambiente:

```powershell
docker compose down
```

## Proximas etapas

- iniciar a estrutura da `policy-api` com Spring Boot
- configurar conexao da `policy-api` com SQL Server
- iniciar a estrutura da `risk-api` com FastAPI
- iniciar a estrutura do `frontend` com React e TypeScript
- evoluir o projeto de forma incremental com APIs REST, persistencia e integracao entre servicos

## Observacao

Este projeto esta sendo desenvolvido com foco em aprendizado, clareza arquitetural e preparacao para entrevista tecnica.
