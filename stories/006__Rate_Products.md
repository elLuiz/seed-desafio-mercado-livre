# Story 6: Rate Products
As a buyer, 

I want to review and rate products

So that other consumers can be aware of its pros and cons.

## 1. Functional Requirements
### 1.1 - FR 01 - Rate a product
When the user is viewing a product, the system must provide a button to the user validate the product.
Upon clicking the button, the system must open a modal with the following components:
1. A 5-star rating system where users can select the number of stars;
2. A text field for providing a title (summary);
3. A text field for providing a detailed description of the product;
4. A save button.

#### 1.1.1 - Restrictions
To rate a product, the system must ensure the following constraints:
1. The **score** must be a number between 1 and 5;
2. The title must not be empty and must not exceed 255 characters;
3. The description must not be empty and must not exceed 500 characters;
4. A rating must belong to exactly one product and one product may have many reviews;
5. The system must also save the author and the time when the review was registered;
6. Only logged-in users can write reviews;
7. Owners cannot review their own products;
8. A user can submit only one review per product.

#### 1.1.2 - Expected Behavior
- Upon clicking the **"Save"** button, the system must persist the review and make it publicly available.
- If there is any error, the system must display a comprehensive overview of what went wrong.

## 2. Non-Functional Requirements
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
  "rating": 5,
  "title": "A comprehensive title",
  "description": "A description"
}
```