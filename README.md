
# 🚀 API de Gerenciamento de Requisições por Time

A **API de Gerenciamento de Requisições por Time** distribui requisições entre agentes de diferentes times, garantindo que cada agente possa atender múltiplas requisições simultaneamente. Quando o limite de atendimento de um agente é atingido, as requisições são colocadas em uma fila e automaticamente atribuídas ao próximo agente disponível.

Desenvolvida utilizando **Java 21** e **Spring WebFlux**, a API segue os princípios de **Clean Architecture**, **SOLID**, e utiliza padrões de design como **Command** e **Strategy**.

A API implementa programação reativa para melhorar o desempenho e escalabilidade, proporcionando um fluxo de dados não-bloqueante que otimiza a alocação de recursos.

A API está integrada com **CI** através do **GitHub Actions** e utiliza **SonarQube** para análise contínua da qualidade do código.

Você pode acessar o [contrato da API](src/main/resources/openapi/REQUEST-API.yaml) desenvolvido com **Swagger/OpenAPI** para obter mais detalhes sobre os endpoints e as especificações técnicas.


Você pode acessar a [collection do Postman](src/main/resources/collection_postman/auto-atendimento-service-api.postman_collection.json), desenvolvida para facilitar os testes manuais da API.



---

## 📜 Sumário

- [⚡ Tecnologias](#-tecnologias)
- [📑 Funcionalidades](#-funcionalidades)
- [📦 Estrutura do Projeto](#-estrutura-do-projeto)
- [📚 Documentação](#-documentação)
- [✅ Cenários de Testes](#-cenários-de-testes)
- [🔌 Padrões de Design](#-padrões-de-design)
- [🛠️ Desenvolvimento](#️-desenvolvimento)
- [🚀 Execução e Configuração](#-execução-e-configuração)
- [🌐 Referências](#-referências)

---

## ⚡ Tecnologias

Essas são algumas das tecnologias e ferramentas utilizadas no projeto:

![SonarCloud Coverage](https://sonarcloud.io/api/project_badges/measure?project=dowglasmaia_auto-atendimento-service-api&metric=coverage)
![SonarQube](https://img.shields.io/badge/-SonarQube-4E9BCD?style=flat-square&logo=sonarqube&logoColor=white)
![Java 21](https://img.shields.io/badge/-Java%2021-007396?style=flat-square&logo=java&logoColor=white)
![Spring WebFlux](https://img.shields.io/badge/-Spring%20WebFlux-6DB33F?style=flat-square&logo=spring&logoColor=white)
![Docker](https://img.shields.io/badge/-Docker-2496ED?style=flat-square&logo=docker&logoColor=white)
![JUnit](https://img.shields.io/badge/-JUnit-25A162?style=flat-square&logo=junit5&logoColor=white)
![Mockito](https://img.shields.io/badge/-Mockito-25A162?style=flat-square&logo=mockito&logoColor=white)
![Rest-Assured](https://img.shields.io/badge/-Rest--Assured-008CBA?style=flat-square&logo=rest-assured&logoColor=white)
![GitHub Actions](https://img.shields.io/badge/-GitHub%20Actions-2088FF?style=flat-square&logo=github-actions&logoColor=white)


---

## 📑 Funcionalidades

- **Distribuição Automática de Requisições**: A API distribui requisições automaticamente para agentes disponíveis ou coloca-as em uma fila até que um agente esteja disponível.
- **Fila de Atendimento**: Requisições são enfileiradas quando todos os agentes de um time estão ocupados.
- **Encerramento de Atendimento**: Agentes podem encerrar suas requisições via API, liberando espaço para novos atendimentos.
- **Tipos de Requisição**:
  - **CARD_ISSUE**: Problemas com cartão.
  - **LOAN_REQUEST**: Solicitação de empréstimo.
  - **OTHER**: Outros tipos de requisição.

---

## 📦 Estrutura do Projeto

```plaintext
src
├── main
│   ├── java
│   │   └── com.dowglasmaia.autoatendimentoserviceapi
│   │       ├── application
│   │       │   ├── controller        
│   │       │   └── service           
│   │       ├── domain
│   │       │   ├── model             
│   │       │   ├── enums           
│   │       │   └── command           
│   │       └── infrastructure
│   │           ├── config            
│   │           └── logging           
│   │           └── repository        
│   └── resources
│       └── application.yml
│       └── openapi/REQUEST-API.yaml  <!-- Contrato da API -->        
└── test
    └── java
        └── com.dowglasmaia.autoatendimentoserviceapi
            ├── application.controller 
            └── domain                 
```

---

## 📚 Documentação

### Endpoints

#### 1. Criar uma nova requisição

- **Endpoint**: `POST /api/requests`
- **Descrição**: Cria uma nova requisição e a distribui automaticamente para um agente disponível ou a coloca em fila.

**Request Body**:
```json
{
  "id": "1",
  "type": "CARD_ISSUE",
  "customerId": "123"
}
```

**Exemplo de Requisição**:
```bash
curl -X POST http://localhost:8080/api/requests \
     -H "Content-Type: application/json" \
     -d '{"id": "1", "type": "CARD_ISSUE", "customerId": "123"}'
```

#### 2. Encerrar uma requisição

- **Endpoint**: `PUT /api/requests/{agentId}/complete`
- **Descrição**: Um agente encerra o atendimento de uma requisição. Se houver requisições na fila, a próxima será automaticamente atribuída ao agente.

**Parâmetros**:
- `agentId` (Path Variable): ID do agente que está encerrando a requisição.
- `requestId` (Query Parameter): ID da requisição que será encerrada.
- `requestType` (Query Parameter): Tipo da requisição.

**Exemplo de Requisição**:
```bash
curl -X PUT "http://localhost:8080/api/requests/agent1/complete?requestId=1&requestType=CARD_ISSUE"
```

**Resposta**:
- **200 OK**: A requisição foi encerrada com sucesso e, se disponível, outra requisição foi atribuída ao agente.
- **404 Not Found**: O agente ou a requisição não foram encontrados.
- **500 Internal Server Error**: Ocorreu um erro no processamento da requisição.

---

## ✅ Cenários de Testes

Foram implementados testes de integração e unitários para garantir o correto funcionamento dos principais fluxos da API:

### Testes de Integração (Classe: `RequestControllerIntegrationTest`)

- **Criação de Requisição de Empréstimo** (`shouldHandleLoanRequest`): Verifica se uma requisição do tipo **LOAN_REQUEST** pode ser criada com sucesso e distribuída para um agente disponível.
- **Encerramento de Requisição de Empréstimo** (`shouldCompleteLoanRequest`): Garante que um agente pode encerrar uma requisição do tipo **LOAN_REQUEST** e, caso existam requisições na fila, a próxima será automaticamente atribuída ao agente.
- **Criação de Requisição de Problema com Cartão** (`shouldHandleCardIssueRequest`): Verifica se uma requisição do tipo **CARD_ISSUE** é criada e processada corretamente.
- **Encerramento de Requisição de Problema com Cartão** (`shouldCompleteCardIssueRequest`): Garante que um agente pode encerrar uma requisição do tipo **CARD_ISSUE** e processar a próxima requisição da fila.
- **Criação de Outras Requisições** (`shouldHandleOtherRequest`): Testa a criação de requisições de outros tipos e sua distribuição.
- **Encerramento de Outras Requisições** (`shouldCompleteOtherRequest`): Valida o encerramento de uma requisição de outro tipo e o processamento da próxima requisição da fila.

### Testes Unitários (Command Pattern)

- **Classe: `CardRequestCommandTest`**
  - **Atribuir Requisição a Agente Disponível** (`shouldAssignRequestToAvailableAgent`): Verifica se a requisição é atribuída corretamente a um agente disponível.
  - **Enfileirar Requisição Quando Todos os Agentes Estão Ocupados** (`shouldQueueRequestWhenAllAgentsAreBusy`): Garante que, quando todos os agentes estão ocupados, novas requisições são corretamente enfileiradas.
  - **Atribuir Próxima Requisição da Fila ao Encerrar Atendimento** (`shouldQueueAndDequeueRequestsProperly`): Verifica se, ao encerrar um atendimento, a próxima requisição na fila é atribuída corretamente ao agente.

- **Classe: `LoanRequestCommandTest`**
  - Testes semelhantes para o fluxo de empréstimos.

- **Classe: `OtherRequestCommandTest`**
  - Testes semelhantes para o fluxo de requisições do tipo "Other".

- **Classe: `CommandInvokerTest`**
  - **Executar Múltiplos Comandos** (`shouldExecuteMultipleCommands`): Assegura que múltiplos comandos são executados na sequência correta.

---

## 🔌 Padrões de Design

### Command Pattern

O **Command Pattern** encapsula as requisições como objetos, permitindo que sejam processadas de forma padronizada e adicionadas a uma fila quando necessário.

- **Comando**: `CardRequestCommand`, `LoanRequestCommand`, `OtherRequestCommand`
- **Executor**: `CommandInvoker`

### Strategy Pattern

O **Strategy Pattern** define e encapsula diferentes estratégias de distribuição de requisições entre agentes, permitindo a troca dinâmica de estratégias conforme o tipo de requisição.

- **Contexto**: `RequestDistributionService`
- **Estratégias**: Diferentes estratégias para o tratamento de requisições de **Card Issues**, **Loan Requests**, e **Other**.

---

## 🛠️ Desenvolvimento

- **Java 21**: Linguagem principal da API.
- **Spring WebFlux**: Implementação reativa para endpoints não-bloqueantes.
- **JUnit & Mockito**: Ferramentas para testes unitários e mocks.
- **Rest-Assured**: Utilizado para testes de integração, verificando o comportamento da API através de requisições HTTP simuladas.
- **Clean Architecture**: Seguindo os princípios de separação de responsabilidades e camadas de domínio puras.

---

## 🚀 Execução e Configuração

### Pré-requisitos

- **Java 21**: Certifique-se de que você tenha o Java 21 instalado.
- **Docker**: Se preferir rodar a aplicação em containers, instale o Docker.

### Rodando a Aplicação

1. **Clone o repositório**:
   ```bash
   git clone https://github.com/seuprojeto/repo.git
   ```

2. **Rodando via Docker**:
   ```bash
   docker-compose up --build
   ```

A aplicação estará disponível em `http://localhost:8099`.

---

## 🌐 Referências

- [Spring WebFlux Documentation](https://docs.spring.io/spring-framework/reference/web/webflux.html)
- [Java 21 Documentation](https://docs.oracle.com/en/java/)
- [Command Pattern](https://refactoring.guru/design-patterns/command)
- [Strategy Pattern](https://refactoring.guru/design-patterns/strategy)
- [Rest-Assured Documentation](https://rest-assured.io/)
- [Swagger OpenAPI](https://swagger.io/specification/)
