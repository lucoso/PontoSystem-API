# PontoSystem-API

## Restful API desenvolvida para gerenciar o registro de ponto eletrônico dos funcionários de uma determinada empresa.

Essa API foi desenvolvida através de um projeto Spring Boot usando a IDE Spring Tool Suite 4 e os seguintes starters e frameworks:

- Spring Boot Devtools
- Validation
- Spring Data JPA
- Flyway Migration
- PostgreSQL Driver
- Spring Web
- Spring HATEOAS

A persistência foi feita em um banco de dados PostgreSQL e usado JPA para criação das tabelas automaticamente. 

Basicamente a API gerencia o cadastro e informações dos funcionários de uma determinada empresa, afim de cadastrar os pontos eletrónicos registrados por cada funcionário. Além dos pontos de horário de trabalho normal, é possível deixar registrado pontos de horas extras, principalmente para casos onde os funcionários fazem plantões após a jornada normal de trabalho.

A API também registra as faltas dos funcionários, caso nenhum ponto seja batido no dia. Ao final é possível extrair um relatório completo para cada funcionário em um determinado período, com as informações mais importantes, como horas extras efetuadas, faltas e seus motivos, total de atrasos e etc.

Por ser uma API RESTFUL, temos as requisições e respostas dos dados utilizando o formato JSON e nas respostas temos os Hypermedia para navegação entre os recursos, desenvolvida usando o Spring HATEOAS, como informado anteriomente. Isso deixa essa API no nível 3 do modelo de maturidade Richardson, ou seja, no mais conhecido "Glória do REST".

Obviamente, muito ainda pode e deve ser melhorado nessa API e, por isso, deixo esse projeto aberto para melhorias e sugestões.


