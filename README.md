# WHISHLIST APP

<h3>CLONANDO E RODANDO O PROJETO COM DOCKER COMPOSE</h3>

1 - Clonar o projeto do github

<b>git clone https://github.com/rsoares21/wishlist</b>

2 - Entrar na pasta do projeto que fez o download do github

<b>cd wishlist</b>

3 - Subir o container Docker com Mongo, Redis, Keycloak, Elasticsearch e Kibana

<b>docker compose up -d</b>

4 - Buildar o projeto usando o maven e gerar o arquivo /target/wishlist-0.0.1-SNAPSHOT.war

<b>mvn clean install</b>

  Se quiser pular os testes unitários basta adicionar -DskipTests

<b>mvn clean install -DskipTests</b>

5 - Executar o projeto Spring Boot wishlist localmente

<b>java -jar ./target/wishlist-0.0.1-SNAPSHOT.war</b>



<h3>CONFIGURAÇÃO POSTMAN</h3>

Para chamar as APIS, importe os arquivos Wishlist.postman_collection.json e Wishlist.postman_environment.json no Postman.

Use a requisição "Token Keycloak" para obter o token para autenticação nas requisições. O Token expira em 5 minutos, caso precise, rode novamente para "renovar".










<br><br>
<br><br>
<br><br>
<br><br>

MEMO:<br><br>
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

<h4>TODO list</h4>
Swagger UI is not working
Verificar se o usuário logado é o dono da wishlist em todas as transações;
Swagger