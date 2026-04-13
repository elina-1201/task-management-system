# Task Management System API

REST API for user registration, JWT authentication, task management, assignment, status updates, and comments.

## Base Path

- All business endpoints are under: `/api`

## Authentication

- `POST /api/accounts` is public.
- `POST /api/auth/token` requires **HTTP Basic Auth** (`email` + `password`).
- `/api/tasks/**` endpoints require **Bearer JWT**.
- JWT token is issued with a short expiration (~60 seconds).

---

## Data Models

### AppUser (request)
```json
{
  "email": "alice@email.com",
  "password": "password"
}
```
Validation:

- **email**: required, valid email format
- **password**: required, min length 6

Task (create/update response shape)
```json
{
  "id": "1",
  "title": "title 1",
  "description": "description 1",
  "status": "CREATED",
  "author": "alice@email.com",
  "assignee": "none"
}
```

TaskDTO (task list response shape)
```json
{
  "id": "1",
  "title": "title 1",
  "description": "description 1",
  "status": "CREATED",
  "author": "alice@email.com",
  "assignee": "none",
  "total_comments": 0
}
```
CommentDTO
```json
{
  "id": "3",
  "task_id": "1",
  "text": "comment text",
  "author": "bob@example.com"
}
```

**TaskStatus enum**
- CREATED
- IN_PROGRESS
- COMPLETED
 ___
## Endpoints
1) Create account

`POST /api/accounts`
Creates a new user account.

Request body:
```json
{
  "email": "user@example.com",
  "password": "123456"
}
```
Responses:
- 200 OK - account created (empty body)
- 400 Bad Request - invalid input
- 409 Conflict - email already exists (case-insensitive check)
 
2) Get JWT token

`POST /api/auth/token`

Generates a JWT for authenticated user. </br>
Authentication: Basic Auth header required.
 
3) Get tasks

`GET /api/tasks`

Returns tasks ordered by newest first (descending id), including comment counts.

Optional query params:
- `author=<email>`
- `assignee=<email>`

Examples:
```
/api/tasks
/api/tasks?author=alice@email.com
/api/tasks?assignee=bob@example.com
/api/tasks?author=alice@email.com&assignee=bob@example.com
```

Response 200 OK:
```json
[
  {
    "id": "3",
    "title": "title 3",
    "description": "description 3",
    "status": "CREATED",
    "author": "bob@example.com",
    "assignee": "alice@email.com",
    "total_comments": 2
  }
]
```
**Errors**:
401 Unauthorized - missing/invalid bearer token
 
4) Create task

`POST /api/tasks`
Creates a task; authenticated user becomes task author.

Request body:
```json
{
  "title": "title 1",
  "description": "description 1"
}
```
Response 200 OK:

```json
{
  "id": "1",
  "title": "title 1",
  "description": "description 1",
  "status": "CREATED",
  "author": "alice@email.com",
  "assignee": "none"
}
```
Errors:
- 400 Bad Request - blank/missing title or description
- 401 Unauthorized - missing/invalid bearer token
 
5) Assign / unassign task

`PUT /api/tasks/{taskId}/assign`

Assigns task to a user. Only task author can do this.

Request body:
```json
{
  "assignee": "bob@example.com"
}
```
Use "none" to unassign:
```json
{
  "assignee": "none"
}
```
Response 200 OK returns updated Task object.

Errors:
- 401 Unauthorized - missing/invalid bearer token
- 403 Forbidden - caller is not task author
- 404 Not Found - task not found or assignee user not found
 
6) Update task status

`PUT /api/tasks/{taskId}/status`

Updates status. Allowed for task author or current assignee.

Request body:
```json
{
  "status": "IN_PROGRESS"
}
```
Response 200 OK returns updated Task object.

Errors:
- 400 Bad Request - invalid/missing status
- 401 Unauthorized - missing/invalid bearer token
- 403 Forbidden - caller is neither author nor assignee
- 404 Not Found - task not found
 
7) Add comment to task

`POST /api/tasks/{taskId}/comments`

Adds a comment to a task. Any authenticated user can comment.

Request body:
```json    
{
  "text": "comment 1"
}
```
Response 200 OK:
```json
{
  "id": "1",
  "task_id": "1",
  "text": "comment 1",
  "author": "alice@email.com"
}
```
Errors:
- 400 Bad Request - blank/missing text
- 401 Unauthorized - missing/invalid bearer token
- 404 Not Found - task not found (or current user not found)
 
8) Get task comments

`GET /api/tasks/{taskId}/comments`
Returns comments for a task, newest first (descending comment id).

Response 200 OK:
```json
[
  {
    "id": "3",
    "task_id": "1",
    "text": "comment 3",
    "author": "bob@example.com"
  }
]
```
Errors:
- 401 Unauthorized - missing/invalid bearer token
- 404 Not Found - task not found
 
### Error Response Notes
Validation errors are handled globally as:

- 400 Bad Request with text "Invalid input data" for bean validation failures.

JSON parse / enum conversion errors return:
- 400 Bad Request with parser-specific message text.

Some service-layer errors return plain text reason bodies (e.g., "Task not found").