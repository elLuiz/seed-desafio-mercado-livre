# Story 2: Register category
As a product owner, 

I want to create categories,

So that I can categorize and organize my products.

## Functional Requirement
To create a category the user must inform the following: the name of category, the parent category (optional).

### Input Restrictions
The following restrictions are observed:
- **Category name:** Represents the name of the category
  - **Pattern:** Alphanumeric characters;
  - **Length:** 255;
  - **Unique:** Yes;
  - **Required:** Yes.
- **Parent Category:** Represents the parent category (i.e., the category above)
  - **Required:** No;
  - **Pattern:** Must be a positive integer;
  - **Restriction:** The category must already be registered and active in the system.

### Output
If the input is correct, the system must display a success message to the user. Otherwise, it must show the errors that prevented
the user from creating the category.

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
  "name": "CATEGORY NAME",
  "parentId": 10
}
```

### NFR 2: Responses

#### Success
If the category is successfully created, the system must return the HTTP status `201 Created`. The header `Location` must also be returned with the URI to
access the resource in the server.

#### Input errors
If the user has committed any mistakes while registering the category, the system must return the following payload under the `400 Bad Request` status code:
```json
{
  "errors": [
    {
      "fieldId": "Field ID",
      "error": "Description of the error",
      "code": "error.code"
    }
  ]
}
```

### NFR 3: Auditing
The creation of a category should be stored in the corresponding column.

