# Story 5: Add images to products
As a seller 

I want to add images to the products I've registered

So that consumers can be persuaded to buy my products.

## 1. Functional Requirements
### 1.1 - FR 01 - Add images
The system must provide to the user a button for uploading images related to product registered by the
logged-in user.

#### 1.1.1 - Restrictions
In order to upload one or more images, the following constraints are observed:
1. The system must accept .png, .jpeg, and .jpg file formats;
2. Each file must not exceed 2MB in size;
3. Each product can have up to 5 files;
4. Users can only upload images for products they have registered and own.

#### 1.1.2 - Expected Output
The images should be linked directly to the specific product and accessible to all customers browsing that product on the site.

## 2. Non Functional Requirements
### 2.1 - NFR 01 - Add images via HTTP
The server must provide a REST endpoint to receive a product's images. The `Content-Type` of the request must be `multipart/form-data`.

Each request should have at most 10MB in size. If the request exceeds the limit, the server should respond with the status `413 Request Entity Too Large`.
### 2.2 - NFR 02 Responses
#### 2.2.1 - Success
If the images are successfully registered, the server must return the HTTP status `204 No Content`.

#### 2.2.2 - Bad Input
If the files are not compliant, the server must return the HTTP status code `400 Bad Request` with a payload in `application/json`.

#### 2.2.3 - Internal Server Errors
If there is an unexpected error, the server must return the HTTP status code `500 Internal Server Error`.

### 2.3 - Implementation
For simplicity, images will not be stored externally or saved directly to disk. Instead, the application will implement a mock storage class during development. To facilitate potential future integration with actual external storage providers (e.g., cloud storage), the system will include an abstract storage interface.

The system will provide an abstract class or interface that defines the required methods for image storage.

#### Configuration
The storage implementation will be determined by the application property `ecommerce.storage.implementation`. This allows selecting the appropriate storage provider (mock or real) based on the environment.