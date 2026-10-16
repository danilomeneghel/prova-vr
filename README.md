# Prova VR

Avaliação técnica de uma API de Mini Autorizador, desenvolvido em Java com Spring-Boot.


## Características

- CRUD
- API RESTful
- Basic Auth
- Validation
- Enum
- Exception
- MockMVC


## Requisitos

- Java JDK 21
- Apache Maven >= 3.9.8
- MySql 5.7
- Docker (Opcional)


## Tecnologias

- Java
- JPA
- Maven
- Spring
- Lombok
- Swagger
- H2
- JUnit
- SonarQube
- Docker


## Arquitetura

O projeto adota uma **Arquitetura em Camadas (Layered Architecture)**,
com a aplicação organizada em uma **API REST**. Cada pacote representa uma camada com
responsabilidade bem definida:

- **Controller (`api.controller`)**: camada de apresentação/entrada. Expõe os
  endpoints REST (`CartaoController`, `TransacaoController`), recebe e valida as
  requisições e devolve as respostas HTTP.
- **Service (`api.service`)**: camada de negócio. Concentra as regras de
  autorização e orquestra o acesso aos dados (`CartaoService`,
  `TransacaoService`).
- **Repository (`api.repository`)**: camada de persistência. Abstrai o acesso ao
  banco via Spring Data JPA (`CartaoRepository`, `SaldoRepository`,
  `TransacaoRepository`).
- **Entity (`api.entity`)**: modelo de domínio/persistência mapeado em JPA
  (`CartaoEntity`, `SaldoEntity`, `TransacaoEntity`).
- **Model (`api.model`)**: objetos de transferência (DTOs) que definem o
  contrato de entrada e saída da API, desacoplados das entidades.
- **Config (`api.config`)**: configurações transversais de segurança e
  documentação.
- **Exception (`api.exception`)**: tratamento centralizado de exceções.

O fluxo de uma requisição percorre as camadas em sentido único
(`Controller → Service → Repository → Banco de Dados`) e retorna pelo caminho
inverso, o que favorece a separação de responsabilidades, a testabilidade
e a manutenção. A segurança e a documentação atuam como camadas transversais
através dos filtros do Spring Security e do Springdoc/OpenAPI.


## Design Patterns

- **MVC (Model-View-Controller)**: separação em `controller` (entrada HTTP),
  `service` (regras de negócio), `repository` (persistência) e `entity`/`model`
  (dados).
- **Front Controller**: o `DispatcherServlet` do Spring centraliza a entrada das
  requisições e o roteamento para os `@RestController`.
- **Dependency Injection / Inversion of Control**: uso de `@Service`,
  `@Repository`, `@Configuration` e `@Bean`, com injeção via `@Autowired`.
- **Singleton**: beans gerenciados pelo Spring possuem escopo singleton por
  padrão (serviços, repositórios e configurações).
- **Repository Pattern**: acesso a dados abstraído pelas interfaces que estendem
  `CrudRepository` (`CartaoRepository`, `SaldoRepository`,
  `TransacaoRepository`), com queries derivadas pelo Spring Data JPA.
- **DTO / Transfer Object**: os `model` (`CriaCartaoModel`, `CartaoModel`,
  `CartaoRespostaModel`, `CriaTransacaoModel`, `TransacaoModel`) isolam o
  contrato da API das entidades JPA.
- **Builder**: construção fluente de objetos com `@Builder` do Lombok.
- **Mapper / Assembler**: conversão entre entidades e DTOs concentrada em
  métodos privados `toModel`, mantendo os controllers e serviços limpos.
- **Facade**: os serviços expõem uma interface simplificada que orquestra um ou
  mais repositórios.
- **Abstract Base Class / Template**: `BaseModelExceptionHandler` (abstrata)
  concentra o tratamento de exceções e `ModelExceptionHandler`
  (`@RestControllerAdvice`) apenas a concretiza.
- **Chain of Responsibility**: cadeia de filtros do Spring Security
  (`SecurityFilterChain`) e o `OncePerRequestFilter`
  (`SwaggerAuthenticationFilter`) que processa a requisição antes de repassá-la
  ao próximo elo.
- **Value Object / Constant Object**: mensagens de erro encapsuladas em objetos
  imutáveis de valor (`ErrorModel`, `CartaoErrors`, `CommonErrors`,
  `TransacaoErrors`).


## Boas Práticas

- **Arquitetura em camadas**: responsabilidades bem definidas entre controller,
  service, repository e model, reduzindo acoplamento.
- **API RESTful**: uso correto dos verbos HTTP e dos status codes (`200`, `201`,
  `401`, `404`, `422`), conforme o contrato do enunciado.
- **Validação de entrada**: Bean Validation (`@Valid`, `@NotNull`) nos modelos e
  entidades, com `@Validated` nos controllers.
- **Tratamento de erros centralizado**: `@RestControllerAdvice` com exceções
  dedicadas (`ModelException`, `TransacaoException`, `CartaoJaExisteException`),
  evitando `try/catch` repetidos.
- **Transacionalidade**: operações de escrita anotadas com `@Transactional`,
  garantindo atomicidade no débito do saldo e no registro da transação.
- **Controle de concorrência**: uso de `@Lock(PESSIMISTIC_WRITE)` na busca do
  cartão para evitar saldo negativo em acessos simultâneos.
- **Precisão monetária**: valores tratados com `BigDecimal` (nunca `double`).
- **Segurança**: HTTP Basic com credenciais codificadas via `BCryptPasswordEncoder`.
- **Auditoria**: campos `createdAt`/`updatedAt` preenchidos automaticamente com
  `@CreationTimestamp` e `@UpdateTimestamp`.
- **Configuração por ambiente**: profiles `dev`, `docker` e `test`, permitindo
  trocar banco (MySQL/H2) e parâmetros sem alterar o código.
- **Documentação da API**: OpenAPI/Swagger via Springdoc, com autenticação
  configurada na UI.
- **Qualidade de código**: Lombok para reduzir boilerplate, SonarQube para
  análise estática e JaCoCo para cobertura.
- **Testes automatizados**: JUnit 5 + Mockito + MockMvc cobrindo controllers,
  serviços, repositórios, entidades, enums e modelos, com H2 em memória no
  perfil de teste.
- **Containerização**: `docker-compose` para subir API e banco de forma
  reproduzível.
- **Constantes em vez de magic strings**: mensagens e códigos de erro
  centralizados em classes dedicadas.
- **Sem `if`/`break`/`continue` na autorização**: fluxo de decisão encadeado com
  exceções específicas, atendendo ao desafio opcional proposto.


## Instalação

```
$ git clone https://github.com/danilomeneghel/prova-vr.git

$ cd prova-vr
```

## Maven

Para rodar o projeto com Maven, é necessário ter a versão 3.9.8 ou superior instalada.<br>
Além disso, é preciso ter o Java 21 e o MySql 5.7 instalado.<br>
Importe o arquivo SQL disponível na pasta `dump-sql` para sua instância do MySql.

Tendo tudo instalado e rodando localmente, basta executar o seguinte comando:

```
$ cd miniautorizador
$ mvn clean install -U
$ mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

Caso não tenha o Maven instalado ou tenha outra versão, pode usar o comando ./mvnw (no Linux) ou mvnw.cmd (no Windows).


## Docker (Opcional)

O arquivo `docker/docker-compose.yml` sobe o MySQL e a aplicação em containers. A
declaração do banco selecionado (MySQL) foi mantida exatamente como disponibilizada no
desafio e o MongoDB permanece comentado (serviço opcional, não faz parte do compose).

Para subir o banco de dados e a aplicação (a partir da raiz do projeto):

```
$ docker compose -f docker/docker-compose.yml up
```

O serviço `mysql` ficará disponível em `localhost:3306` (banco `miniautorizador`,
usuário `root` sem senha) e o serviço `api` em `http://localhost:8080`. O container da
aplicação aguarda o MySQL ficar disponível e então executa a aplicação com o perfil
`docker`, que já aponta para o host `mysql`:

```
$ docker compose -f docker/docker-compose.yml logs -f api
```

O primeiro `up` pode demorar alguns minutos, pois o Maven baixa as dependências dentro do container.

Também é possível rodar apenas o banco em Docker e a aplicação localmente via Maven:

```
$ docker compose -f docker/docker-compose.yml up -d mysql
$ cd miniautorizador
$ mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

Para encerrar tudo:

```
$ docker compose -f docker/docker-compose.yml down
```


## Swagger 

Documentação da API RESTful: <br>

http://localhost:8080/swagger-ui.html

Login:
  - Usuário: username
  - Senha: password


## SonarQube (Opcional)

A análise de qualidade e cobertura via SonarQube é **opcional** e **não faz parte do
`docker-compose.yml`**.
Caso queira utilizá-la, suba uma instância própria do SonarQube (por exemplo, com
`docker run -p 9000:9000 sonarqube:9.9.7-community`), gere um token e execute:

```
$ cd miniautorizador
$ mvn clean verify sonar:sonar \
  -Dsonar.projectKey=NOME_PROJETO_GERADO \
  -Dsonar.host.url=http://localhost:9000 \
  -Dsonar.login=TOKEN_GERADO
```

## Testes

Para realizar os testes, execute o seguinte comando no terminal:

```
$ cd miniautorizador
$ mvn test
```

Os testes usam JUnit 5, Mockito e MockMvc, com o banco H2 em memória no perfil `test`
(`src/test/resources/application-test.properties`).


## Contratos implementados

Os contratos do enunciado são respeitados exatamente.

### Criar cartão
- `POST /cartoes` — Basic Auth (`username` / `password`)
- Corpo: `{ "numeroCartao": "6549873025634501", "senha": "1234" }`
- `201` → `{ "senha": "1234", "numeroCartao": "6549873025634501" }`
- `422` (cartão já existe) → `{ "senha": "1234", "numeroCartao": "6549873025634501" }`
- `401` sem autenticação válida

### Obter saldo
- `GET /cartoes/{numeroCartao}` — Basic Auth
- `200` → saldo como número (ex.: `495.15`)
- `404` sem corpo quando o cartão não existe
- `401` sem autenticação válida

### Realizar transação
- `POST /transacoes` — Basic Auth
- Corpo: `{ "numeroCartao": "6549873025634501", "senhaCartao": "1234", "valor": 10.00 }`
- `201` → `OK`
- `422` → `SALDO_INSUFICIENTE` | `SENHA_INVALIDA` | `CARTAO_INEXISTENTE`
- `401` sem autenticação válida

> Observação: no corpo da transação o campo da senha é **`senhaCartao`**, exatamente
> como definido no contrato (diferente de `senha`, usado na criação do cartão).


## Decisões de projeto

- **Stack**: Java 21 + Spring Boot 3.3.5 + Maven, conforme sugerido no enunciado.
- **Banco de dados**: optou-se pelo MySQL (relacional). O MongoDB foi mantido
  comentado no `docker-compose.yml`, sem alterar a declaração do MySQL.
- **Número do cartão**: tratado como `String` (e não como número), pois os 16 dígitos
  estouram o tipo `Long` e zeros à esquerda seriam significativos.
- **Saldo inicial**: R$ 500,00, criado junto com o cartão em uma entidade `SaldoEntity`
  separada. O saldo é debitado apenas quando a transação é autorizada.
- **Senha**: armazenada e comparada como texto simples, pois o contrato exige devolver
  a senha no corpo de resposta da criação do cartão. Em um cenário real seria usado
  hash (BCrypt); a decisão prioriza a aderência literal ao contrato.
- **Persistência da transação**: as transações são persistidas para auditoria/consulta,
  ainda que o enunciado não exija isso. O débito do saldo ocorre na mesma transação de
  banco (`@Transactional`). Um cartão pode ter **várias** transações, por isso o
  relacionamento `TransacaoEntity → CartaoEntity` é `@ManyToOne` (com `@OneToOne` o
  Hibernate criava um `unique` em `id_cartao` e apenas a primeira transação do cartão
  era aceita, retornando 500 nas seguintes).
- **Autenticação**: HTTP Basic com usuário fixo `username` / `password`, conforme o
  contrato.
- **Formato dos retornos**: erros de autorização retornam `text/plain` com o token
  exato do contrato (ex.: `SALDO_INSUFICIENTE`); a criação de cartão retorna apenas
  `{ senha, numeroCartao }`; a consulta de saldo retorna o número puro e `404` sem
  corpo quando o cartão não existe.
- **Regras de autorização (desafio opcional "sem if")**: o fluxo de decisão
  (cartão existe → cartão ativo → senha correta → saldo suficiente) foi escrito sem
  `if`, `break` ou `continue`, encadeando estruturas e lançando exceções específicas
  para cada regra não atendida.
- **Concorrência (desafio opcional)**: a busca do cartão usada na autorização é feita
  com `@Lock(PESSIMISTIC_WRITE)`, o que serializa transações concorrentes sobre o
  mesmo cartão. Assim, dado um cartão com R$ 10,00 de saldo e duas transações de
  R$ 10,00 simultâneas, a primeira é autorizada e a segunda recebe
  `SALDO_INSUFICIENTE` — evitando o cenário de saldo negativo.
- **Tratamento de erros**: centralizado via `@RestControllerAdvice`, com exceções
  dedicadas (`CartaoJaExisteException` e `TransacaoException`) mapeadas para os
  status e corpos definidos no contrato.
- **Endpoints adicionais**: além dos três endpoints exigidos, a API expõe operações
  de listagem/consulta/alteração/exclusão (CRUD) de cartões e transações e documentação
  Swagger. Esses recursos extras não interferem nos contratos avaliados.
- **Testes**: cobertura alta com JUnit 5 + Mockito + MockMvc, exercitando as classes de
  serviço, repositórios, entidades, enums, models e controllers.


## Screenshots

Swagger-UI <br>
![Screenshots](screenshots/screenshot01.png) <br>
![Screenshots](screenshots/screenshot02.png) <br><br>

Postman <br>
![Screenshots](screenshots/screenshot03.png) <br><br>

Modelagem ER <br>
![Screenshots](screenshots/screenshot04.png) <br><br>

Testes Unitários <br>
![Screenshots](screenshots/screenshot05.png) <br><br>

SonarQube <br>
![Screenshots](screenshots/screenshot06.png) <br>
![Screenshots](screenshots/screenshot07.png) <br><br>


## Licença

Projeto licenciado sob <a href="LICENSE">The MIT License (MIT)</a>.<br><br>


Desenvolvido por<br>
Danilo Meneghel<br>
danilo.meneghel@gmail.com<br>
http://danilomeneghel.github.io/<br>
