
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/cb13815122324272bbeb645b45b0985f)](https://app.codacy.com/app/augustoberwaldt/test-sicredi?utm_source=github.com&utm_medium=referral&utm_content=augustoberwaldt/test-sicredi&utm_campaign=Badge_Grade_Dashboard)
[![Build Status](https://travis-ci.org/augustoberwaldt/test-sicredi.svg?branch=master)](https://travis-ci.org/augustoberwaldt/test-sicredi)





##### Conciderações

Escolhi o spring boot para realizar teste , por ser facil de levantar servidor e ja ter bastante abstrações programação. 
Acabei não colocando as configurações no CONSUL, mas seria o ideal. Os Dados estão sendo perssitidos no H2, mas para mudar
seria so  subir um docker de outro banco e alterar application.yml.


##### Link da aplicação no Heroku

https://softdesignbrasil.herokuapp.com/swagger-ui.html


###### Interface para acessar Banco H2

localhost:8080/db

###### Docker composer
na  raiz esta docker composer para subir o rabbitmq, que e um mesange broker, assim como o kafka

###### Interface para acessar swagger

localhost:8080/swagger-ui.html

###### Desenvolvimento

Para desenvolver utilizei IDE IntellJ

##### Tecnologias Utilizadas
- Linguagem de Programação Java
- Spring Boot
- JPA Data
- Banco de dados H2
- swagger
- rabbitmq
- Junit
- Heroku Cloud