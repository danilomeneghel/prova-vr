# Prova VR

Avaliação técnica de uma API de Mini Autorizador, desenvolvido em Java com Spring-Boot.

## Características

- CRUD
- API RESTful
- Validation
- Enum
- MockMVC

## Requisitos

- Java JDK 17
- Apache Maven >= 3.8.6 (Opcional)
- Docker

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

## Instalação

```
$ git clone https://github.com/danilomeneghel/prova-vr.git

$ cd prova-vr
```

## Docker

Para rodar o projeto via Docker-Compose, bastar executar o seguinte comando:

```
$ cd docker
$ docker-compose up
```

Aguarde baixar as dependências e carregar todo o projeto, esse processo é demorado. <br>
Caso conclua e não rode pela primeira vez, tente novamente executando o mesmo comando. <br>

Para encerrar tudo digite:

```
$ docker-compose down
```

## Maven

Para carregar o projeto diretamente com Maven será necessário mudar a configuração do banco. <br>
Onde está escrito "jdbc:mysql://mysql" mude para "jdbc:mysql://localhost", para acessar o MySql Local. <br>
Após feito isso, digite no terminal:

```
$ cd miniautorizador
$ ./mvnw clean spring-boot:run
```

Aguarde carregar todo o serviço web. <br>
Após concluído, digite o endereço abaixo em seu navegador, nele será listado os cartões cadastrados na API. <br>

http://localhost:8080/cartoes

## Swagger 

Documentação da API RESTful: <br>

http://localhost:8080/swagger-ui.html

## SonarQube

Para verificar a cobertura de testes, primeiro acesse o seguinte endereço: <br>
http://localhost:9000

Depois efetue o login preenchendo "admin" no usuário e senha (login padrão). <br>
Ao se logar crie um novo projeto e gere o token.

Feito isso, entre no container criado da api:

```
$ docker exec -it api bash
```

Execute o seguinte comando do sonar:

```
$ ./mvnw clean verify sonar:sonar -Dsonar.projectKey=NOME_PROJETO_GERADO -Dsonar.host.url=http://sonarqube:9000 -Dsonar.login=TOKEN_GERADO
```

Após executado, acesse o seguinte endereço: <br>

http://localhost:9000/dashboard?id=prova-vr

## Testes

Para realizar os testes, execute o seguinte comando no terminal:

```
$ cd miniautorizador
$ ./mvnw test
```

## Licença

Projeto licenciado sob <a href="LICENSE">The MIT License (MIT)</a>.<br><br>


Desenvolvido por<br>
Danilo Meneghel<br>
danilo.meneghel@gmail.com<br>
http://danilomeneghel.github.io/<br>
