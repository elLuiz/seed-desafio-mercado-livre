# ADR 001: Validate unique fields through annotations

## Problem
ome entities in our system contain unique fields that must be validated for uniqueness upon resource creation or update. This ensures that no duplicate values are introduced.

In Spring Boot, implementing this type of validation is relatively straightforward. However, if not designed carefully, it can lead to redundant code and potential inconsistencies across the codebase. 
To prevent this, it's crucial to centralize and standardize the validation logic to maintain both efficiency and clarity throughout the application.

## Solution
A potential solution to enforce uniqueness validation without redundancy in the codebase is to create a generic validation component. This can be achieved using Java annotations. 
The validator would then depend on these annotations, which allows reusability and maintainability.
### Steps
The steps necessary to implement this solution are:
1. Design a generic base entity to limit the number of classes that can be validated. This ensures that only entities with certain characteristics (such as having unique fields) will be subject to the validation process.
2. Create the annotation with the following properties:
   1. owner, defining who is the class at which the validation is going to be performed;
   2. message, the message to be returned to the user if the unique constraint is violated;
   3. name, represents the name of the field;
   4. `groups` and `payload`, in order to comply with the Jakarta specification.
3. Create the generic validation class extending the `jakarta.validation.ConstraintValidator` interface.
4. Use the `jakarta.persistence.EntityManager` to generate the query at runtime.

## Consequences
This solution promotes reusability, maintainability, and avoids code duplication. By creating an annotation and a generic entity class, it is possible to generate queries at runtime, making it possible to evolve the system 
over time. Unfortunately, at the moment, this solution is only viable for the creation of resources; for updates, this approach will not work correctly.

Another challenge lies in the complexity of this solution. The Java Reflection API, which is a core part of this design, can be difficult for developers who are not familiar with it. Its usage introduces a layer of abstraction that may not be intuitive, potentially making the system harder to maintain or extend for less experienced developers. Therefore, it’s crucial to balance complexity and maintainability. Over time, the solution should be adjusted to ensure it remains manageable without sacrificing the clarity and simplicity that developers need to work effectively.

For data types other than String, it is necessary to create custom validators to ensure proper validation.  