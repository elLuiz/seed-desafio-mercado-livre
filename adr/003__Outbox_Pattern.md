# ADR 003: Outbox Problem
**Date:** 2025/03/12

**Status:** Approved

## Problem
The application has evolved to a point where we need to trigger behaviors in other system, such as sending an email, sending payment info, etc.
Since most of these interactions will be performed asynchronously, via a message broker, we need to be able to publish the events atomically.
That is, the application must not perform a _dual write_ without the guarantee of publishing the event or updating the database.

## Solution
The outbox pattern has been proven to be effective to publish the events atomically, without adding complexity to the application code. When inserting or updating
a row, the events can also be persisted in the outbox table within the same transaction.

Later, another process or a CDC mechanism, such as _Debezium_, can read the entries and publish them sequentially to a message broker.

### Steps
1. Define a listener, using the `@TransactionalEventListener` annotation to persist the events within the same transaction;
2. Create the outbox table with the following columns:
   1. ID;
   2. payload (jsonb);
   3. creation_date;
   4. topic;
   5. aggregate_id;
   6. aggregate_type.
3. Persist the events to the outbox;
4. Create a polling mechanism to publish new events to the corresponding topics;
   1. The polling can be a background job or a dedicated service.
5. Delete the old entries from the table;

## Consequences
The outbox pattern has been highly used in microservices architectures, where dual writes are common. It solves the problem by using the atomicity guarantee provided by
most relational databases. This solution comes with the following drawbacks:
- **Performance:** Polling the database periodically to route events is a naive solution, as it may overwhelm the application database. A better solution would be to use a CDC tool, such as _Debezium_.
Unfortunately, the main drawback is the addition of another infrastructure component and its built-in complexity;
- **Dual writes are still there:** The application still needs to publish the event and write back to the database, so the problem is not completely solved. The application may publish the same event twice.
Therefore, the consumer needs to be idempotent.
  - Eventually, the event will be removed from the database.
- **Observability:** There must be an observability tool to visualize how the events are being routed and if there is any event being routed repeatedly.