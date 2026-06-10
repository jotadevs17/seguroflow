# SeguroFlow

SeguroFlow e um projeto de estudo criado para praticar uma stack comum em aplicacoes reais do mercado segurador: frontend em React/TypeScript, API principal em Java/Spring Boot, microservico em Python/FastAPI, SQL Server e ambiente local com Docker.

O projeto foi pensado para uma demonstracao tecnica curta. A ideia nao e construir um sistema completo de seguradora, mas mostrar dominio de arquitetura em camadas, APIs REST, persistencia relacional, integracao entre servicos, tratamento de erros e clareza para explicar decisoes tecnicas.

## Objetivo

Simular um fluxo simples de gestao de seguros:

- cadastrar clientes;
- cadastrar apolices vinculadas a clientes;
- registrar sinistros vinculados a apolices;
- calcular o nivel de risco do sinistro com apoio de um servico separado;
- exibir os dados principais em uma interface web.

## Contexto de estudo

Este repositorio foi montado para consolidar conhecimentos importantes para vagas com foco em backend Java, APIs REST, SQL Server, Python/FastAPI, React/TypeScript, Docker e cloud.

O escopo foi mantido pequeno de proposito. Assim, a demonstracao consegue focar em qualidade de organizacao, separacao de responsabilidades e capacidade de evoluir o projeto, em vez de tentar cobrir todas as funcionalidades de um sistema real de seguros.

## Tecnologias

- Java 21
- Spring Boot
- Spring Web MVC
- Spring Data JPA
- Bean Validation
- SQL Server
- Python
- FastAPI
- React
- TypeScript
- Vite
- Docker Compose
- OpenAPI/Swagger UI

## Arquitetura

```text
Usuario
  |
  v
React/TypeScript frontend
  |
  v
Spring Boot policy-api
  |                 |
  |                 v
  |          FastAPI risk-api
  |
  v
SQL Server
```

### frontend

Aplicacao web em React com TypeScript.

Responsabilidades:

- apresentar o dashboard de sinistros;
- exibir indicadores resumidos;
- permitir cadastro de sinistros pela interface;
- consumir a API Java por meio de services TypeScript.

### policy-api

API principal do sistema, desenvolvida em Java com Spring Boot.

Responsabilidades:

- expor endpoints REST de clientes, apolices e sinistros;
- concentrar regras principais do dominio;
- persistir dados no SQL Server usando JPA;
- validar entradas com Bean Validation;
- padronizar respostas de erro;
- chamar a `risk-api` ao criar sinistros;
- disponibilizar contrato OpenAPI em `/v3/api-docs` e Swagger UI em `/swagger-ui/index.html`.

### risk-api

Microservico em Python com FastAPI.

Responsabilidades:

- receber dados de analise de risco;
- aplicar regras simples de classificacao;
- retornar `BAIXO`, `MEDIO` ou `ALTO` com os motivos da decisao.

### SQL Server

Banco relacional usado para persistir clientes, apolices e sinistros.

No ambiente local, o SQL Server sobe via Docker Compose na porta `1433`.

## Estrutura do repositorio

```text
seguroflow/
  frontend/
  policy-api/
  risk-api/
  docker-compose.yml
  README.md
  .gitignore
```

## Como rodar localmente

### 1. Subir o SQL Server

Pre-requisito: Docker Desktop instalado e em execucao.

Na raiz do projeto:

```powershell
docker compose up -d
```

Verifique o container:

```powershell
docker compose ps
```

O banco usado pela `policy-api` e `SeguroFlow`, com conexao local em:

```text
jdbc:sqlserver://localhost:1433;databaseName=SeguroFlow;encrypt=true;trustServerCertificate=true
```

### 2. Rodar a risk-api

No diretorio `risk-api`:

```powershell
python -m venv .venv
.\.venv\Scripts\Activate.ps1
pip install -r requirements.txt
uvicorn app.main:app --reload --port 8000
```

Health check:

```text
GET http://localhost:8000/
```

### 3. Rodar a policy-api

No diretorio `policy-api`:

```powershell
.\mvnw.cmd spring-boot:run
```

A API sobe em:

```text
http://localhost:8080
```

Swagger UI:

```text
http://localhost:8080/swagger-ui/index.html
```

Contrato OpenAPI:

```text
http://localhost:8080/v3/api-docs
```

### 4. Rodar o frontend

No diretorio `frontend`:

```powershell
npm install
npm run dev
```

O Vite informara a URL local do frontend, normalmente:

```text
http://localhost:5173
```

## Fluxo principal da aplicacao

1. O usuario acessa o frontend.
2. O frontend consulta e envia dados para a `policy-api`.
3. A `policy-api` valida os dados de entrada.
4. A `policy-api` persiste clientes, apolices e sinistros no SQL Server.
5. Ao criar um sinistro, a `policy-api` chama a `risk-api`.
6. A `risk-api` calcula o nivel de risco e retorna os motivos.
7. A `policy-api` salva o sinistro com o nivel de risco recebido.
8. O frontend exibe o resultado para o usuario.

## Endpoints principais

### Clientes

| Metodo | Endpoint | Descricao |
| --- | --- | --- |
| `POST` | `/clientes` | Cria um cliente |
| `GET` | `/clientes` | Lista clientes |
| `GET` | `/clientes/{id}` | Busca cliente por id |

Exemplo de criacao:

```json
{
  "nome": "Joao Silva",
  "cpf": "12345678900",
  "email": "joao@email.com",
  "telefone": "21999999999"
}
```

### Apolices

| Metodo | Endpoint | Descricao |
| --- | --- | --- |
| `POST` | `/apolices` | Cria uma apolice |
| `GET` | `/apolices` | Lista apolices |
| `GET` | `/apolices/{id}` | Busca apolice por id |

Exemplo de criacao:

```json
{
  "numero": "AP-1001",
  "tipoSeguro": "AUTO",
  "valorSegurado": 80000,
  "dataInicio": "2026-01-01",
  "dataFim": "2026-12-31",
  "clienteId": 1
}
```

Tipos de seguro aceitos:

- `AUTO`
- `RESIDENCIAL`
- `VIDA`

### Sinistros

| Metodo | Endpoint | Descricao |
| --- | --- | --- |
| `POST` | `/sinistros` | Cria um sinistro e calcula o risco |
| `GET` | `/sinistros` | Lista sinistros |
| `GET` | `/sinistros/{id}` | Busca sinistro por id |

Exemplo de criacao:

```json
{
  "descricao": "Colisao traseira",
  "valorEstimado": 25000,
  "dataOcorrencia": "2026-06-01",
  "status": "ABERTO",
  "apoliceId": 1
}
```

Status aceitos:

- `ABERTO`
- `EM_ANALISE`
- `APROVADO`
- `NEGADO`

### Risk API

| Metodo | Endpoint | Descricao |
| --- | --- | --- |
| `GET` | `/` | Health check da API de risco |
| `POST` | `/risk-analysis` | Calcula o nivel de risco |

Exemplo de requisicao:

```json
{
  "claimAmount": 25000,
  "insuredAmount": 80000,
  "daysUntilPolicyExpiration": 120,
  "previousClaims": 0
}
```

Exemplo de resposta:

```json
{
  "riskLevel": "BAIXO",
  "reasons": [
    "Nenhum fator de risco relevante foi identificado"
  ]
}
```

## Regras de risco

A classificacao de risco e calculada na `risk-api`.

Regras atuais:

- `ALTO`: valor do sinistro maior que 70% do valor segurado;
- `ALTO`: cliente possui 3 ou mais sinistros anteriores;
- `MEDIO`: apolice vence em menos de 30 dias;
- `MEDIO`: valor do sinistro entre 40% e 70% do valor segurado;
- `BAIXO`: nenhum fator de risco relevante identificado.

A `policy-api` tambem valida se a data do sinistro esta dentro da vigencia da apolice. Caso esteja fora, a API retorna erro `400 Bad Request`.

## Tratamento de erros

A `policy-api` possui um handler global para padronizar erros.

Exemplos de situacoes tratadas:

- dados invalidos de entrada;
- recurso nao encontrado;
- conflito de dados;
- apolice inexistente ao criar sinistro;
- falha de comunicacao com a `risk-api`;
- resposta invalida da `risk-api`.

Formato esperado:

```json
{
  "timestamp": "2026-06-10T10:00:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Data do sinistro fora da vigencia da apolice",
  "path": "/sinistros",
  "fields": {}
}
```

## Como rodar testes

### policy-api

No diretorio `policy-api`:

```powershell
.\mvnw.cmd test
```

### frontend

No diretorio `frontend`:

```powershell
npm run build
```

### risk-api

A `risk-api` ainda nao possui uma suite automatizada dedicada. A validacao minima pode ser feita subindo o servico e chamando `GET /` e `POST /risk-analysis`.

## Decisoes tecnicas

### Por que Spring Boot como API principal

Spring Boot foi escolhido para concentrar o dominio principal do sistema, expor os endpoints REST e persistir dados no SQL Server. A estrutura com controllers, services, repositories, DTOs e entidades deixa clara a separacao de responsabilidades.

### Por que FastAPI ficou separado

A analise de risco foi isolada em uma API Python para simular um cenario comum em sistemas reais: a API principal orquestra o fluxo, enquanto um servico especializado executa uma regra especifica. Isso tambem permite demonstrar integracao entre stacks diferentes.

### Por que SQL Server

SQL Server foi usado para praticar uma tecnologia comum em ambientes corporativos e no mercado segurador. Mesmo quando o desenvolvedor tem mais contato com MySQL ou PostgreSQL, entender conexao, driver, JPA e ambiente local com SQL Server amplia a aderencia a projetos empresariais.

### Por que nao ha login no escopo

Autenticacao ficou fora do MVP para manter o foco em dominio, persistencia, integracao entre servicos e documentacao. Em uma evolucao real, login poderia ser adicionado com Spring Security e JWT ou integrado a um provedor externo.

### Como a falha entre servicos e tratada

Quando a `policy-api` nao consegue chamar a `risk-api`, ela registra o problema e retorna `503 Service Unavailable`. Quando a `risk-api` responde um nivel invalido, a `policy-api` retorna `502 Bad Gateway`. Assim, a falha de integracao nao fica silenciosa.

## Plano de evolucao para AWS

Este projeto ainda nao precisa de deploy real para cumprir seu objetivo de estudo, mas poderia evoluir para AWS com a seguinte arquitetura:

```text
Route 53
  |
  v
CloudFront
  |
  v
S3 static website - frontend React

API Gateway ou Application Load Balancer
  |
  v
ECS Fargate
  |-- policy-api
  |-- risk-api
  |
  v
RDS SQL Server

Secrets Manager -> credenciais e variaveis sensiveis
CloudWatch -> logs e metricas
ECR -> imagens Docker das APIs
```

Possivel divisao:

- Frontend React publicado em S3 e distribuido por CloudFront.
- `policy-api` empacotada em imagem Docker e executada em ECS Fargate.
- `risk-api` tambem em ECS Fargate, como servico separado.
- SQL Server gerenciado em Amazon RDS for SQL Server.
- Secrets de banco e URLs internas no AWS Secrets Manager.
- Logs das APIs centralizados no CloudWatch.
- Imagens das APIs armazenadas no Amazon ECR.
- Comunicacao interna entre APIs controlada por security groups.

## Aprendizados

Este projeto reforca:

- construcao de APIs REST com Spring Boot;
- modelagem simples de dominio com entidades relacionadas;
- uso de SQL Server em ambiente local;
- integracao entre Java e Python via HTTP;
- aplicacao de regras de negocio em services;
- tratamento padronizado de erros;
- criacao de contrato OpenAPI;
- consumo de API no frontend React;
- organizacao de um README pensado para avaliacao tecnica.

## Proximos passos

- adicionar testes automatizados para a `risk-api`;
- adicionar testes de controller/service na `policy-api`;
- evoluir o frontend para consumir todos os fluxos de clientes, apolices e sinistros;
- criar Dockerfiles para `policy-api`, `risk-api` e `frontend`;
- adicionar um compose completo com todos os servicos;
- preparar uma colecao Postman ou Insomnia;
- estudar uma versao de deploy em AWS com ECS, RDS e CloudWatch.

## Frase para entrevista

Eu tratei o README como parte do projeto, porque queria que qualquer pessoa tecnica conseguisse entender rapidamente a arquitetura, os endpoints, os trade-offs e como esse MVP poderia evoluir para uma arquitetura de cloud.
