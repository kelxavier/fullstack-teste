# Projeto-fullstack
O projeto consistem em uma aplicação web com um backend em Java, utilizando Spring Boot e JPA para comunicação com um banco de dados MySQL. A interface do usuário foi desenvolvida utilizando o framework React com bootstrap. 
# Tecnologias Utilizadas 
- Java
- Spring Boot
- JPA
- MySQL
- React 
- Bootstrap
# Configurações do Banco de Dados
Para configurar o banco de dados MySQL, é necessário criar um banco com o nome "frela" e adicionar as credenciais de acesso ao arquivo "application.properties" localizada na pasta "src/main/resources" do backend.

spring.datasource.url=mysql://localhost::3306//frela
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha

# Executando o Backend 
Para executar o backend, é necessário ter o Maven instalado. Navegue até a pasta "backend" do projeto e execute o seguinte comando:

mvn spring boot::run

O backedn estará rodando na porta 8080. 

# Executando o Frontend 
Para executar o frontend, é necessário ter o Node.js e o NPM instalados. Navegue até a pasta "frontend" do projeto e execute os seguintes comandos:

npm install
npm start 

O frontend estará rodando na porta 3000.

# Considerações Finais 
Este é um projeto desenvolvido por Kelly Ramos de Albuquerque Xavier. O código-fonte está disponível no GitHub e pode ser utilizado como base para estudos e desenvolvimento de novos projetos. 
