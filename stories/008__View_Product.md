# Story 8: View Product

**ID**: US-8

## 1. User Story

**As a customer**, I want to visualize a product's details, reviews, and questions before buying, **so that I** can decide whether the product is worth or not.

## 2. Functional Requirements

### 2.1 Preconditions

1. A product's details can be accessed by anonymous or logged-in users;
2. A product must be active in the system.

### 2.2 Post-conditions

1. A product's detail is returned to the user. The following information is returned:
    1. Images;
    2. Product’s name;
    3. Price;
    4. Characteristics;
    5. Description;
    6. Average of reviews;
    7. Amount of reviews;
    8. Reviews and opinions;
    9. Questions about the product.

### 2.3 Basic Flow

1. The user navigates to the product’s page;
2. The system validates the product’s status;
3. The system returns the details.

### 2.4 Alternative Flow

1. If the product is invalid or does not exist, the system displays an error message to the user explaining why the request couldn’t go on.