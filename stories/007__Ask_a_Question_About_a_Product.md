# Story 7: Ask a question about a product
As I consumer 

I want to ask the seller questions about the product

So that I can make an informed purchase decision without any doubts.

## 1. Functional Requirements
### 1.1 - FR 01 - Ask a question
A consumer can ask the seller one or more question about a product.

#### 1.1.1 - Restrictions
To create a question, the following constraints are observed:
- The question must not be empty;
- The question must not exceed 300 characters;
- The creation date must not be null;
- The author of the question must not be null;
- Owners can't register questions about their own products.

#### 1.1.2 - Expected behavior
Upon registering the question, the system must notify the owner by email.

If there is any violation, the system must not save the question and must show the user a list of errors encountered.

### 1.2 - FR 02 - Notify the owner
When the question is saved, the product owner is notified by email.

#### 1.2.1 - Restrictions
- The owner must have a valid email;
- Each email must have the following information:
  - The question;
  - The product at which the question was made;
  - The time when the question was made;
  - A link to answer the question.
- Owners can only answer questions about products they have registered.

#### 1.2.2 - Expected behavior

To enhance the user experience, the email must be sent in an HTML layout.

## 2. Non Functional Requirements
### 2.1 - Responses
#### 2.1.1 - Success
If the images are successfully registered, the server must return the HTTP status `201 Created`.

#### 2.1.2 - Bad Input
If the files are not compliant, the server must return the HTTP status code `400 Bad Request` with a payload in `application/json`.

#### 2.1.3 - Forbidden
If the user is owner of the product, the server must return the HTTP status `403 Forbidden` with a description of the problem in `application/json`.

#### 2.1.3 - Internal Server Errors
If there is an unexpected error, the server must return the HTTP status code `500 Internal Server Error`.

### 2.2 - Payload
The payload must be in `application/json` with the following attributes:
```json
{
  "question": "Eine Frage?"
}
```

### 2.2 - Trigger email notification
The notification must be configured and sent via _novu_, an open-source workflow tool. Additionally, the system must have a listener for triggering the email (workflow endpoint), i.e., the email
notification must occur asynchronously. 

If possible, the _Outbox Pattern_ must be used to send the notification (events) atomically.