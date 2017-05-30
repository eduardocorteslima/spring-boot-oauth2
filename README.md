Dependencias:
Maven: 3.3.*
Java: 8
Mongo: 3.2.9

```
mvn clean package
```
```
mvn spring-boot:run
```

Instalar o PostMan:

> URL: http://localhost:8080/oauth/token?grant_type=password&scope=read&username=user&password=123
> Metodo: POST
> Authorization: Basic Auth
> Username: foo
> Password: bar
> Content-Type: application/json


Resultado (exemplo):
```
{
  "access_token": "77781e5f-8f4e-4f55-9fd0-344c2a6e367d",
  "token_type": "bearer",
  "refresh_token": "28a61a63-d95e-49e2-a4dc-fd3a68740716",
  "expires_in": 43199,
  "scope": "read"
}
```

Realizar Requisição:
> URL: http://localhost:8080/application/
> Metodo: GET
> Headers: Authorization
> Value: bearer 77781e5f-8f4e-4f55-9fd0-344c2a6e367d

Resultado (exemplo):
```	
  {
    "id": "592caf211e8d641a64c9c755",
    "nome": "Eduardo",
    "valor": 2000,
    "data": "2017-05-29T20:30:41.493"
  }
```



