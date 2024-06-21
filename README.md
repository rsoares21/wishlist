# wishlist

- CLONANDO E RODANDO O PROJETO COM DOCKER COMPOSE

1 - Clonar o projeto do github https://github.com/rsoares21/wishlist

  execute: git clone https://github.com/rsoares21/wishlist

2 - Entrar na pasta do projeto que fez o download do github

  execute: cd wishlist

3 - Subir o container Docker com Mongo, Redis, Keycloak, Elasticsearch e Kibana

  execute: docker compose up -d

4 - Buildar o projeto usando o maven e gerar o arquivo /target/wishlist-0.0.1-SNAPSHOT.war

  execute: mvn clean install 

  Se quiser pular os testes unitários basta adicionar -DskipTests

  execute: mvn clean install -DskipTests

5 - Executar o projeto Spring Boot wishlist localmente

  execute: java -jar ./target/wishlist-0.0.1-SNAPSHOT.war



- CONFIGUÇÃO POSTMAN

Para chamar as APIS, importe os arquivos Wishlist.postman_collection.json e Wishlist.postman_environment.json no Postman.

Use a requisição "Token Keycloak" para obter o token para autenticação nas requisições. O Token expira em 5 minutos, caso precise, rode novamente para "renovar".











Keycloak Client Info
{
  "realm": "lab",
  "auth-server-url": "http://localhost:8081/",
  "ssl-required": "external",
  "resource": "lab-wishilist",
  "credentials": {
    "secret": "w22lBPaO48QLUTOV2xSYlWjPFTQO3Wpv"
  },
  "confidential-port": 0
}


WARN
Swagger UI is not working

TO-DO
Verificar se o usuário logado é o dono da wishlist em todas as transações;
Swagger