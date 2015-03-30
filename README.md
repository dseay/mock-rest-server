# mock-rest-server
REST server suitable for simulating typical entity based CRUD operations.

## How it works

java -jar mock-rest-server.jar \<port:8080\> \<data-dir:.\> \<id-property:id\> \<id-type:uuid\>

Server will start and assume that each sub directory under data-dir is the start of a valid REST path.

You should create a directory structure under data-dir to mimic the REST URI paths you want to simulate.

For example if the REST API you are simulating servers user entities from /api/user and your data-dir
is /mockdata you could setup your files like such:
```
mkdir /mockdata/api/user
echo "{ \"id\": \"1\", \"name\": \"Joe\" }" > /mockdata/api/user/1
echo "{ \"id\": \"2\", \"name\": \"Jane\" }" > /mockdata/api/user/2
```
### GET /api/user

Will return JSON array of all users under /mockdata/api/user

### GET /api/user/1

Will return single JSON file /mockdata/api/user/1

### DELETE /api/user

Will delete all files under /mockdata/api/user

### DELETE /api/user/1 

Will delete the single JSON file /mockdata/api/user/1

### PUT /api/user/1
```
{ \"id\": \"1\", \"name\": \"Steve\" }
```
Will update the JSON file /mockdata/api/user/1

### POST /api/user
```
{ \"name\": \"Ted\" }
```
Will create new JSON file under /mockdata/api/user/ with a generated id that will be
at the end of the path in a Location header returned.

### POST /api/user
```
[ { \"name\": \"John\" }, { \"name\": \"Sally\" } ]
```
Will create two new JSON files under /mockdata/api/user with unique id's

### POST /api/user/action

If there is a file created named 'action' in /mockdata/api/user it will be returned with 200 status
