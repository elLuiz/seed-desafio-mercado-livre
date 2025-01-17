# Story 4: Register product
As a user I want to register products, so that I can sell them.

## 1. Functional Requirements
### 1.1 FR 01 - Register products
To register a product the announcer must provide the following information:
- Name;
- Price;
- Quantity available;
- Characteristics;
- Description;
- Category.

### 1.2 Input Restrictions
A product must follow the restrictions provided below:
1. **Name:** Represents the name of the product that will be shown to the consumer.
   - **Required:** Yes;
   - **Length:** 255 characters.
2. **Price:** Represents the price of the product.
   - **Required:** Yes;
   - **Restriction:** Must be greater than or equal than $2;
   - **Pattern:** Must have at most 2 decimal places.
3. **Characteristics:** A product can have multiple characteristics. The announcer must specify the product's characteristics in a key-value format, where the key represents the characteristic name and the value its description.
   - **Required:** Yes;
   - **Restriction:** Must have at least one characteristic.
4. **Description:** Represents the product's description. It can be written in the Markdown format. That way, the announcer is free to organize the description's structure.
   - **Required:** Yes;
   - **Length:** 1000 characters
5. **Category:** Represents the category of the product. A product must belong to just one category, and one category may belong to many products.
   - **Required: Yes**;
   - **Restriction:** Must be an existing category and active in the system.
6. **Stock availability:** Represents the quantity of items available in stock.
   - **Required:** Yes;
   - **Restriction:** Must be greater than 0.
7. **Creation date:** Represents the time when the product is being registered.
   - **Required**: Yes;
   - **Restriction:** Must not be in the past, and must be stored in the UTC timezone.

### 1.3 Expected Output
If any restriction is violated, the system must inform the user of the error. Otherwise, the system must display a success message.

## 2. Non Functional Requirements

### 2.1 NFR 01 - Register Products
Products should be registered by invoking a REST endpoint using the HTTP method `POST`. The client must pass the product's information via JSON, using the `Content-Type: application/json`.

#### 2.1.1 Payload
The payload must be as follows:
```json
{
  "name": "string",
  "price": 2.00,
  "stockQuantity": 10,
  "characteristics": [
    {
       "property": "string",
       "value": "string"
    }
  ],
  "description": "string",
  "categoryId": 10
}
```
The price must be sent as a string in US format.

#### 2.1.1 Responses
##### 2.1.1.1 Success
If the product is stored successfully, the server must return the `204 No Content` status code.  

##### 2.1.1.2 Bad Input
If the user violates at least one constraint, the system must return a payload with the erros under the `400 Bad Request` status code. The response must be returned in `application/json` as well.

### 2.2 NFR 02 - Audit
When a product is saved, the system must also persist the information about the responsible for the registry as well as the creation date.