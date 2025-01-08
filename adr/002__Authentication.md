# ADR 002: Authentication through JWT

## Problem
The users must be authenticated to perform actions in the system, according to their roles. We need to have a stateless way to implement authentication, enabling integration with other systems and changing
the authentication provider, if applicable. 

## Solution
JWT (Json Web Token) is the proposed solution for this problem. In the first moment, it will be implemented with the Spring Security suite. 
If the system increases its authentication / authorization complexity, we can start using a proper SSO without changing crucial parts
of the system.

## Consequences
This approach simplifies the system's adaptation to new architectures, like microservices, by decentralizing session management rather than relying on a single, centralized component. Also, it makes easier to adapt to 
other production-ready SSO.

To implement this solution the system must offer the following features:
1. Groups and roles;
2. Map endpoints to the corresponding set of groups or roles.

Additionally, the system may offer the functionality to revoke tokens. For that, Redis can be used for faster access.