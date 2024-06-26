### Задание

Создать базовое веб-приложение с использованием Spring Security и JWT для аутентификации и авторизации пользователей.

Полное описание требований в файле: techSpecification/Specification.txt

### Общая информация о приложении

Основная функция приложения - предоставление возможности регистрации пользователей и аутентификации/авторизации с использованием функционала, предоставляемого Spring Security и JWT Token.

Для аутентификации пользователей используется два JWT токена: 

1 ) accessToken (Токен доступа) - используется непосредственно для аутентификации и доступа к защищенным ресурсам. Имеет короткий срок жизни.

2 ) refreshToken (Токен обновления) – используется для генерации нового токена доступа. Имеет больший срок жизни чем токен доступа.

В целях повышения безопасности токен обновления сохраняется в БД (в приложении реализовано сохранение в основную БД Postgres)

Взаимодействие с приложением осуществляется через соответствующие Api:

1)	Регистрация пользователя:
   
POST    http://localhost:8080/api/v1/registration

3)	Авторизация пользователя:
   
POST    http://localhost:8080/api/v1/authentication

5)	Обновление токена доступа:
   
POST    http://localhost:8080/api/v1/refresh-token

7)	Доступ к защищенным и общедоступным ресурсам:
   
GET      http://localhost:8080/api/v1/admin

GET      http://localhost:8080/api/v1/user

GET      http://localhost:8080/api/v1/public

Для проверки доступа пользователя с правами администратора, в БД добавлен пользователь с правами “ADMIN” (логин: ADMIN, пароль: ADMIN)

Представленные в приложении API имеют подробную документацию, которая доступна по адресу:

http://localhost:8090/swagger-ui/index.html



### Быстрый запуск
1) Склонировать репозиторий проекта
2) С помощью терминала перейти в каталог проекта
3) Набрать команду ниже и подождать окончания сборки:    
   docker-compose up


###  В проекте использовано:

Spring boot 3

Spring Security 3.3.0

Postgres

Docker

JUnit5

Liquibase

Lombok

Swagger

