# Datos de Prueba

Este archivo contiene los datos de prueba para el proyecto.

## Users

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


## Roles

Estos son los roles que existen en el sistema:

| Role  | Access                                                  |
|-------|---------------------------------------------------------|
| ADMIN | Full access                                             |
| USER  | Solo acceso a los datos publicos o asociados al usuario |

## Notas

* _**Usuario:**_
  * Las contraseñas de todos los usuarios con rol USER es `123`.
  * Las contraseñas de todos los usuarios con rol ADMIN es `admin`.
  * Para cargar los datos de **usuarios** de prueba (por unica vez), la base de datos debe estar vacía.
* _**Accounts:**_
  * Para cargar las cuentas asociadas, los usuarios de prueba:
    * si no existen: se cargan antes `automaticamente`
    * si existen: se traen de la DB
  * Cada account tiene un `Balance inicial entre 0 y 500mil random`
  * Todos los users tienen `al menos 1 account asociada`, ya sea en ARS o USD
  * El usuario `admin`,` Nicolas Tagliafico` y `Otamendi` tienen `2 accounts asociadas`, una en ARS y otra en USD
  * En la tabla de usuarios, la columna `Account` indica la moneda de la cuenta asociada
* **Transactions**:
  * Para cargar las transacciones asociadas, las account de prueba:
    * si no existen: se cargan antes `automaticamente`
    * si existen: se traen de la DB
  * Respetan el balance de la cuenta y no se pasan del limite de transferencia
  * El balance de la cuenta se ve afectado por las transacciones
  