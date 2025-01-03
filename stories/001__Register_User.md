# Story 1: Register user
As a possible customer,

I want to create an account

So that I can manage products.

## Functional Requirement
The process of creating an account requires the following information: name, login, and password.

### Input restrictions
The following restrictions are observed:
- **Name:** Represents the formal normal of the user.
  - **Length:** Must not surpass 120 characters long;
  - **Required:** Yes;
  - **Pattern:** Only alphabetic characters.
- **Login:** Represents the username by which the user can log in his/her account.
  - **Length:** Must not surpass 250 characters long;
  - **Required:** Yes;
  - **Pattern:** Must be a valid email;
  - **Unique:** Yes.
- **Password:** Represents the password by which the user can log in his/her account in combination with his/her login.
  - **Length:** Must be between 6 and 11 characters long;
  - **Required:** Yes;
  - **Pattern:** Must contain at least one special character symbol, one number, and one uppercase letter.
- **Creation date:** Represents the moment when the user was created.

### Output
If the input is correct, the system must display a success message to the user. Otherwise, it must show the errors that prevented
the user from creating the account.

## Non Functional Requirements

### NFR 1: Requests
Using the REST architectural design, the requests must be made using the HTTP method `POST`.

#### Headers
The system must accept the following HTTP headers:
- Content;
- Content-Type=application/json;
- Accept=application/json;
- Accept-Language=en-US;pt-BR.
#### Payload
The payload must be similar to this:
```json
{
  "name": "The user name",
  "login": "email@valid.email.com",
  "password": "QWJjZGVmZw=="
}
```
The password must be sent in `base64`.

### NFR 2: Password storage
The passwords must be stored in hash, using the `SHA-256` hashing algorithm. The system must not store any password information in plain text.

### NFR 3: Responses

#### Success
If the account is successfully created, the system must return the HTTP status `204 No Content`

#### Input errors
If the user has committed any mistakes while registering the account, the system must return the following payload under the `400 Bad Request` status code:
```json
{
  "errors": [
    {
      "fieldId": "Field ID",
      "error": "Description of the error",
      "errorCode": "error.code"
    }
  ]
}
```