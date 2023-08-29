# Manual de Execução

## Requisitos

Certifique-se de ter os seguintes requisitos instalados:

- [Java JDK](https://www.oracle.com/java/technologies/javase-downloads.html)
- [Maven](https://maven.apache.org/)
- [Git](https://git-scm.com/downloads)

## Setup do ambiente.
  Configure a variavel de ambiente path do seu ambiente antes de tentar compilar e rodar  
  Vai ser necessario adicionar os caminhos do maven, git e java 11.  
  Use os seguintes comandos no cmd:  
  set PATH=%PATH%;D:\Ferramentas\git\bin  
  set PATH=%PATH%;D:\Ferramentas\Maven\apache-maven-3.6.3-bin\apache-maven-3.6.3\bin   
  set PATH=%PATH%;C:\java\openjdk-11.0.2\bin  
  Obs: Os caminhos informados são referente a minha maquina de desenvolvimento.  
  

## Passos

1. Clone o repositório para o seu computador:
2. Navegue para o diretório do projeto:
3. Compile o projeto: mvn clean install
4. Execute a aplicação: java -jar mvteste-0.0.1-SNAPSHOT.jar

![image](https://github.com/fgirardi/desafioMV/assets/20515071/c4600a62-39d0-49cd-9e77-0cb197d053fc)


## Documentacao dos servicos:
http://localhost:8080/swagger-ui

## Acesso ao banco H2
http://localhost:8080/h2  
Usuario: admin  
senha: 123  

![image](https://github.com/fgirardi/desafioMV/assets/20515071/8e2dfd5c-2dfa-408a-b21b-38857473b3a8)

...

## Problemas Conhecidos
Nenhum reportado.
...

## Suporte
E-mail: fabiano.girardi@hotmail.com
