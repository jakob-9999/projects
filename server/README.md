# How to connect to the DB
Download Docker:
This can be done by either installing docker with brew on a mac or installing Docker Desktop:\
https://docs.docker.com/desktop/setup/install/mac-install/ \
https://docs.docker.com/desktop/setup/install/windows-install/ \
\
After installing Docker you can run the following command in your terminal.
Remember to replace the credentials (placeholder password)
```
docker run --name vudp-postgres-db -e POSTGRES_USER=vudp-user -e POSTGRES_PASSWORD=placeholder -e POSTGRES_DB=vudp -p 5434:5432 -v pgdata:/var/lib/postgresql/data -d postgres:17
```

After running the command a Postgres DB called vudp is initialized in a docker container. The application cannot start 
without being connected to the DB as the application is dependant on the DB being alive.

### Optional - Connect to a DB in IntelliJ
To connect to the DB in IntelliJ follow the documentation linked below.
https://www.jetbrains.com/help/idea/postgresql.html#connect-to-postgresql-database \
For running on your local machine:\
Host: localhost \
Port: 5434 \
User and password is found in the application.yml and the actual password instead of the placeholder.\
Database: vudp \
Test Connection and Apply.