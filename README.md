# Instruções de Execução

## Requisitos

Certifique-se de ter os seguintes requisitos instalados:

- [Java JDK](https://www.oracle.com/java/technologies/javase-downloads.html)
- [Maven](https://maven.apache.org/)
- [Git](https://git-scm.com/downloads)

## Setup do ambiente.
  Configure a variavel de ambiente path do seu ambiente antes de tentar compilar e rodar
  Vai ser necessario adicionar os caminhos do maven, git e java 11, para isso faça:
  echo %PATH:;=&echo.%
  

## Passos

1. Clone o repositório para o seu computador:
2. Navegue para o diretório do projeto:
3. Compile o projeto: mvn clean install
4. Execute a aplicação: java -jar target/nome-do-arquivo.jar

## Documentacao dos servicos:
http://localhost:8080/swagger-ui

## Acesso ao banco H2
http://localhost:8080/h2  
Usuario: admin  
senha: 123  


...

## Problemas Conhecidos
Talvez seja necessario configurar a variavel ambiente PATH do seu ambiente, aqui tem um exemplo conforme o ambiente no qual o programa foi desenvolvido.  

set PATH=%PATH%;D:\Ferramentas\git\bin  
set PATH=%PATH%;D:\Ferramentas\Maven\apache-maven-3.6.3-bin\apache-maven-3.6.3\bin   
set PATH=%PATH%;C:\java\openjdk-11.0.2\bin  
...

## Suporte
E-mail: fabiano.girardi@hotmail.com
