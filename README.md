# To-Do Application

## Descrição

A To-Do Application é uma aplicação Java 17 desenvolvida com o uso do framework Spring Boot. A aplicação permite que os usuários criem e gerenciem suas listas de tarefas pendentes. Cada usuário pode criar novas tarefas, visualizar suas tarefas, alterar o status e excluir tarefas que lhe pertençam. A autenticação é realizada usando um Bearer Token JWT.

## Requisitos

- Java 17
- Maven
- Banco de dados H2

## Configuração

1. Clone o repositório da aplicação.
2. Navegue até o diretório raiz da aplicação.

## Banco de Dados

A aplicação utiliza o banco de dados H2. Para configurar o H2, adicione as seguintes configurações no arquivo `application.properties`:

spring.datasource.url=jdbc:h2:file:/dados/tarefas
spring.datasource.username=seu-jvictorss
spring.datasource.password=sua-marialuiza
spring.datasource.driver-class-name=org.h2.Driver
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.jpa.hibernate.ddl-auto=update
spring.h2.console.enabled=true
spring.h2.console.path=/h2


## Instalação e execução

1. Execute o seguinte comando para compilar e empacotar a aplicação:

- `mvn clean package`

2. Após a conclusão do comando acima, execute o seguinte comando para iniciar a aplicação:

- `java -jar target/todo-application.jar`


3. A aplicação estará disponível em [http://localhost:8080/todoapp](http://localhost:8080/todoapp).

## API

A aplicação fornece uma API RESTful para interação com as funcionalidades. Abaixo estão detalhadas as principais operações disponíveis.

### Usuário

#### Criar um novo usuário

**URL:** `/todoapp/usuario/salvar`
**Método:** `POST`
**Descrição:** Cria um novo usuário.
**Parâmetros:**
- `usuarioDTO` (corpo da requisição): Objeto JSON contendo os dados do usuário a ser criado.


**Respostas:**

- Caso a senha siga o padrão de conter letras maiúsculas, minúsculas, números e pelo menos 1 caractere especial (ex.: !@#$%&), a resposta será o HttpStatus 200 (OK) e o usuário poderá logar pelo endpoint `/todoapp/usuario/entrar`.
- Caso haja uma nova tentativa de salvar o mesmo e-mail, a resposta será um HttpStatus 400 (Bad request) em formato JSON como o seguinte:
~~~
{
  "codigoStatus": 400,
  "status": "Bad Request",
  "message": "Não foi possível salvar. Já existe um usuário com o e-mail cadastrado."
}
~~~

#### Fazer login e obter token JWT

**URL:** `/todoapp/usuario/entrar`
**Método:** `POST`
**Descrição:** Autentica um usuário e retorna um token JWT para uso nas demais operações.
**Parâmetros:**
- `loginRequest` (corpo da requisição): Objeto JSON contendo o e-mail e a senha do usuário para autenticação.
  **Resposta:**
- Ao logar corretamente, o usuário recebe como resposta o Bearer Token para autenticar as demais chamadas. O token tem validade de 15 minutos.
- Em ferramentas de suporte à documentação das requisições feitas por API, como o Postman, é necessário incluir o BearerToken na camada Headers de cada endpoint a ser utilizado das Tarefas abaixo, criando uma Key de nome "Authorization" e o Value com "Bearer + 'token recebido'".

### Tarefas

#### Salvar uma nova tarefa

**URL:** `/todoapp/todo/salvar`
**Método:** `POST`
**Descrição:** Salva uma nova tarefa para o usuário autenticado.
**Parâmetros:**
- `todoDTO` (corpo da requisição): Objeto JSON contendo os dados da tarefa a ser criada.
~~~
{
    "descricao": "Descrição",
    "prioridade": "alta",
    "concluido": false
}
~~~
- `Bearer Token` (token de autorização de login) para permitir o uso do endpoint e setar o valor do id do Usuário ao salvar a tarefa.
-
**Resposta:**
Se 200 (OK):
~~~
{
  "id": Long,
  "descricao": "string",
  "prioridade": "string",
  "concluido": true,
  "idUsuario": Long,
  "criado": "2023-05-25T14:02:34.932Z",
  "atualizado": "2023-05-25T14:02:34.932Z"
}
~~~

#### Atualizar uma tarefa existente

**URL:** `/todoapp/todo/atualizar`
**Método:** `POST`
**Descrição:** Atualiza uma tarefa existente.
**Parâmetros:**
- `todoDTO` (corpo da requisição): Objeto JSON contendo os dados atualizados da tarefa. Exemplo:
~~~
{
    "id": 1,
    "descricao": "teste atualizar prioridade",
    "prioridade": "alta"
}
~~~
- `Bearer Token` (token de autorização de login) para permitir o uso do endpoint e verificar se a tarefa pertence ao Usuário através do seu id e atualizar a tarefa.
  **Respostas:**
  Se 200: OK (No content).
  Se 404 (Not found):
~~~
"Desculpe. Não foi possível encontrar a tarefa especificada."
~~~

#### Deletar uma tarefa

**URL:** `/todoapp/todo/delete/{id}`
**Método:** `DELETE`
**Descrição:** Deleta uma tarefa com base no seu ID.
**Parâmetros:**
- `id` (path): ID da tarefa a ser deletada.
- `Bearer Token` (token de autorização de login) para permitir o uso do endpoint e verificar se a tarefa pertence ao Usuário através do seu id e deletar a tarefa.
  ** Respostas: **
  Se 200: OK (No content).
  Se 404 (Not found):
~~~
"Desculpe. Não foi possível encontrar a tarefa especificada."
~~~

#### Alterar o status de uma tarefa não concluída

**URL:** `/todoapp/todo/alterar-status`
**Método:** `POST`
**Descrição:** Altera o status de uma tarefa que ainda não foi concluída.
**Parâmetros:**
- `todoDTO` (corpo da requisição): Objeto JSON contendo o ID da tarefa e o novo status.
~~~
{ 
    "id": 3,
    "concluido": false
}
~~~
- `Bearer Token` (token de autorização de login) para permitir o uso do endpoint e verificar se a tarefa pertence ao Usuário através do seu id e alterar o status a tarefa.
- `Body`:
~~~
{
  "id": Long,
  "descricao": "string",
  "prioridade": "string",
  "concluido": false,
}
~~~

**Respostas:**
Se 200: OK (No content).
Se 404 (Not found):
~~~
"Desculpe. Não foi possível encontrar a tarefa especificada."
~~~


#### Listar tarefas pendentes por prioridade

**URL:** `/todoapp/todo/listar-tarefas/{prioridade}`
**Método:** `GET`
**Descrição:** Lista as tarefas pendentes do usuário autenticado com a prioridade especificada.
**Parâmetros:**
- `prioridade` (path): Prioridade das tarefas a serem listadas (alta, média, baixa). Required.
- `Bearer Token` (token de autorização de login) para permitir o uso do endpoint.
  **Respostas:**
  Se 200 (OK): Lista com as tarefas com a prioridade definida que não estão concluídas. Exemplo:
~~~
[
    {
        "id": 11,
        "descricao": "Maria Luiza",
        "prioridade": "alta",
        "concluido": false,
        "idUsuario": 2,
        "criado": "2023-05-25T19:24:18.703752",
        "atualizado": "2023-05-25T19:24:18.703752"
    },
    {
        "id": 12,
        "descricao": "Maria",
        "prioridade": "alta",
        "concluido": false,
        "idUsuario": 2,
        "criado": "2023-05-25T19:24:22.430657",
        "atualizado": "2023-05-25T19:24:22.430657"
    }
]
~~~

#### Listar tarefas pendentes

**URL:** `/todoapp/todo/listar-tarefas-pendentes`
**Método:** `GET`
**Descrição:** Lista todas as tarefas pendentes do usuário autenticado.
**Parâmetros:**
- `prioridade` (parâmetro opcional): Filtra as tarefas pendentes pela prioridade especificada. Caso não seja fornecido, todas as tarefas pendentes serão retornadas.
- `Bearer Token` (token de autorização de login) para permitir o uso do endpoint.
  **Resposta:**
  Se 200 (OK):
~~~
[
    {
        "id": 11,
        "descricao": "Maria Luiza",
        "prioridade": "alta",
        "concluido": false,
        "idUsuario": 2,
        "criado": "2023-05-25T19:24:18.703752",
        "atualizado": "2023-05-25T19:24:18.703752"
    },
    {
        "id": 12,
        "descricao": "Maria",
        "prioridade": "alta",
        "concluido": false,
        "idUsuario": 2,
        "criado": "2023-05-25T19:24:22.430657",
        "atualizado": "2023-05-25T19:24:22.430657"
    },
    {
        "id": 13,
        "descricao": "Maria",
        "prioridade": "media",
        "concluido": false,
        "idUsuario": 2,
        "criado": "2023-05-25T19:24:28.59922",
        "atualizado": "2023-05-25T19:24:28.59922"
    },
    {
        "id": 10,
        "descricao": "Maria Luiza",
        "prioridade": "baixa",
        "concluido": false,
        "idUsuario": 2,
        "criado": "2023-05-25T19:24:14.983815",
        "atualizado": "2023-05-25T19:24:14.983815"
    }
]
~~~
Se 404 (Not found):
~~~
"Desculpe. Não foi possível encontrar a tarefa especificada."
~~~

## Swagger

A documentação da API pode ser acessada pelo Swagger UI em [http://localhost:8080/todoapp/swagger-ui.html](http://localhost:8080/todoapp/swagger-ui.html).

