# T3-Wallet
Este es un proyecto de una desarrollado en Java Spring Boot. 
Proporciona una plataforma para administrar cuentas y realizar transacciones en diferentes monedas.

## Requisitos
Para probar este proyecto es necesario:
- Tener instalado [Java 17] (https://www.oracle.com/java/technologies/downloads/#java17)
- Tener instalado [Maven] (https://maven.apache.org/download.cgi)
- Tener instalado [MySQL] (https://dev.mysql.com/downloads/mysql/)

## Testeo

Para testear el proyecto:
1. Clonar el repositorio
2. Abrir una terminal en la carpeta raiz del proyecto
3. Ejecutar el comando `mvn spring-boot:run` o abrir IDE de preferencia y ejecutar
4. Abrir Postman e importar:
   1. el environment `T3-Wallet.postman_environment.json` y seleccionarlo
   2. la coleccion de requests `T3-Wallet.postman_collection.json`
5. Ejecutar los requests de la coleccion


## Datos de Prueba

Este archivo contiene los datos de prueba para el proyecto.

### Users

| Name                 | Email                         | Role  | Account     |
|----------------------|-------------------------------|-------|-------------|
| Diego Aprosoff       | diegoaprosoff@email.com       | USER  | USD         |
| Diego Martin Perez   | diegomartinperez@email.com    | USER  | ARS         |
| Evaristo Compagnucci | evaristocompagnucci@email.com | USER  | USD         |
| Melody Amaro         | melodyamaro@email.com         | USER  | ARS         |
| Neyen Ergas          | neyenergas@email.com          | USER  | USD         |
| Rodrigo Juarez       | rodrigojuarez@email.com       | USER  | ARS         |
| Simon Nava           | simonnava@email.com           | USER  | USD         |
| Soledad Grilletta    | soledadgrilletta@email.com    | USER  | ARS         |
| Valentino Veralli    | valentinoveralli@email.com    | USER  | USD         |
| Michele Obama        | micheleobama@email.com        | USER  | ARS         |
| Lionel Messi         | lionelmessi@email.com         | ADMIN | USD         |
| Angel Di Maria       | angeldimaría@email.com        | ADMIN | ARS         |
| Lautaro Martinez     | lautaromartínez@email.com     | ADMIN | USD         |
| Paulo Dybala         | paulodybala@email.com         | ADMIN | ARS         |
| Angel Correa         | angelcorrea@email.com         | ADMIN | USD         |
| Rodrigo De Paul      | rodrigodepaul@email.com       | ADMIN | ARS         |
| Leandro Paredes      | leandroparedes@email.com      | ADMIN | USD         |
| Nicolas Tagliafico   | nicolastagliafico@email.com   | ADMIN | **ARS<br/>USD** |
| Gonzalo Montiel      | gonzalomontiel@email.com      | ADMIN | USD         |
| Nicolas Otamendi     | nicolasotamendi@email.com     | ADMIN | **ARS<br/>USD** |
| admin                | admin@admin.com               | ADMIN | **ARS<br/>USD** |


### Roles

Estos son los roles que existen en el sistema:

| Role  | Access                                                  |
|-------|---------------------------------------------------------|
| ADMIN | Full access                                             |
| USER  | Solo acceso a los datos publicos o asociados al usuario |

### Notas

* _**Usuario:**_
  * Las contraseñas de todos los usuarios con rol USER es `123`.
  * Las contraseñas de todos los usuarios con rol ADMIN es `admin`.
  * Para cargar los datos de **usuarios** de prueba (por unica vez), la base de datos debe estar vacía.
* **Accounts:**
  * Para cargar las cuentas asociadas, los usuarios de prueba:
    * si no existen: se cargan antes `automaticamente`
    * si existen: se traen de la DB
  * Cada account tiene un `Balance inicial entre 0 y 500mil _random_`
  * Todos los users tienen `al menos 1 account asociada`, ya sea en ARS o USD
  * El usuario `admin`,` Nicolas Tagliafico` y `Otamendi` tienen `2 accounts asociadas`, una en ARS y otra en USD
  * En la tabla de usuarios, la columna `Account` indica la moneda de la cuenta asociada
  