# E-Commerce-MQ

Projeto criado com o intuito de praticar conhecimentos em mensageria com RabbitMQ

## Diagrama
![diagrama de arquitetura de mensagens](Diagrama%20EcommerceMQ.png)

## Instruções

### 1. Configure o ambiente

**Na raíz do projeto** execute:
    
Para criar a imagem
* `docker build -t ecommercemq-rabbitmq -f Dockerfile.rabbitmq .` 

Para criar e iniciar o container

* `docker run -d --name ecommercemq -p 5672:5672 -p 15672:15672 ecommercemq-rabbitmq`

### 2. Inicie a Aplicação

**Dentro do diretório EcommerceMQ** execute:

* `mvn spring-boot:run` 


