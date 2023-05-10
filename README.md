# JSON Serialization Example

Run with:
```
    sbt "runMain io.janikdotzel.serialization.Main"
```

Create a user:
```
    curl -X POST -H "Content-Type: application/json" -d '{"id":"1", "name":"John Doe", "age":30}' http://localhost:8080/user
```

Get the details of one user:
```
    curl http://localhost:8080/user/1
```