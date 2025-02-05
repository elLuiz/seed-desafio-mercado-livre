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
2. Each file must not surpass 2MB;
3. Each product can have up to 5 files;
4. Users can only add images to the products registered by them.

#### 1.1.2 - Expected Output
The images should be linked directly to the specific product and accessible to all customers browsing that product on the site.

## 2. Non Functional Requirements
### 2.1 - NFR 01 - Add images via HTTP
The server must provide a REST endpoint to receive a product's images. The `Content-Type` of the request must be `multipart/form-data`.

### 2.2 - Responses
#### 2.2.1 - Success
If the images are successfully registered, the server must return the HTTP status `204 No Content`.

#### 2.2.2 - Bad Input
If the files are not compliant, the server must return the HTTP status code `400 Bad Request` with a payload in `application/json`.

#### 2.2.3 - Internal Server Errors
If there is an unexpected error, the server must return the HTTP status code `500 Internal Server Error`.

### 2.3 - Implementation
For the sake of simplicity, the images will not be uploaded to an external service nor persisted to disk. Instead, the application must implement
a _fake class_. To support production-like storage providers, the system must provide an abstract class or interface. 

There can be many storage implementations. However, just one must be used per deploy. The class will be selected based on an application property named `ecommerce.storage.implementation`.
![](../assets/stories/005_rnf_2.3.png)