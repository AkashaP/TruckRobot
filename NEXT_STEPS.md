## Next Steps and Future Considerations

This section outlines potential next steps and extensions that could be considered if the Truck Robot service were to be evolved beyond the scope of this coding challenge. These items were intentionally not implemented in order to keep the solution aligned with the problem statement and avoid unnecessary complexity.

## Multi-robot and multi-client support

Current: The service models a single global robot instance held in memory, shared across all requests.

Next steps: Introduce a robot resource and scope state per robot (and optionally per client). For example:

POST /api/v1/robots to create a new robot and return a robotId

POST /api/v1/robots/{robotId}/commands to control a specific robot

This keeps the domain framework-agnostic, while allowing the web layer to manage multiple independent robot instances. 

Supporting multiple robots would require clarifying whether robots operate on the same table, as the original problem assumes no obstacles.

## Persistence

Currently, Robot state is in-memory and resets on restart.

Next step: Persist robot state and/or idempotency records using a datastore such as PostgreSQL (for example via Spring Data JPA/Hibernate). This enables usability across restarts and supports a lot of follow on features:

## API Key / Basic Auth

The project can be deployed to a live service but currently has no authentication. 

Next step: Add optional authentication for non-local deployments (for example, a simple API key via X-API-Key enabled only when configured by environment variables). For a fuller solution, Spring Security can be introduced to support Basic Auth or token-based authentication, along with rate limiting and request auditing.

## Idempotency

This feature becomes relevant when a Persistence layer is introduced, and the system is extended to consider multiple services and multiple robots, and availability concerns arise due to horizontal scaling (for example, in a microservices architecture).

Specifically, this is when clients send a command, and a service retrieves and processes the command, but the service fails to send back a reply. This may be for several reasons including network congestion or services going down. 

This may cause clients to think the request had failed, they may re-send the command and end up 'doubling up' on stateful commands, so a client intending to send one MOVE command may be processed as two or more.

### Implementation

To prevent this double-up behaviour, Idempotecy can be considered to handle retries. The client may generate a HTTP Idempotency-Key for each request to uniquely identify it. 
The server needs to cache this Idempotency-Key.
This key needs to be persisted to a Database with a TTL because it is not certain when the clients may decide to retry a command. 

### Scenario

1. Client sends MOVE request with Idempotency key
2. Server receives MOVE request, *records* the Idempotency key and tries to process the request
3. Server restarts before it can send a reply to client (processing either succeeds or fails)
4. Client thinks MOVE command failed, so it retries with the same Idempotency key
5. Newly restarted server receives the request, recognises the idempotency key, and returns the originally intended HTTP response (e.g. 200 OK) rather than reapplying the MOVE

## Conclusion

Overall, the current solution focuses on delivering a clear maintainable implementation of the problem, pioritising simplicity, clarity, and correctness within the scope of the coding challenge. The design keeps core domain logic decoupled from the web layer, allowing additional features to be introduced if requirements evolve.