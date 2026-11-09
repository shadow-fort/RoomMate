# RoomMate

Shared university project for reserving workplaces. A person signs in with GitHub, searches rooms and equipment, and books a workplace for a time range. An administrator can review bookings, update workplace equipment, and change or remove a reservation. The model does not decide who receives a workplace: the signed-in person or an administrator confirms the booking.

This repository describes the project as a whole. It does not assign individual features to one author.

## Purpose

RoomMate supports a small office where workplaces belong to rooms and each workplace has equipment. People need a way to see what is free and to reserve it without double-booking the same place. Administrators need a separate view so they can correct a booking or change the equipment list.

## Features

- GitHub sign-in for users and administrators
- Search for rooms and workplace equipment
- Create, update, and delete a booking
- Administrator screens for rooms, workplaces, and all bookings
- PostgreSQL storage with a Flyway migration
- Optional synchronisation of current bookings with a local KeyMaster service

## Architecture

The application follows an onion layout:

| Area | Role |
| --- | --- |
| `src/main/resources/templates` | Thymeleaf pages for sign-in, search, booking, and administration |
| `controllers` | Web and KeyMaster HTTP endpoints |
| `application` | Booking, room, user, and scheduling services |
| `domain` | Room, workplace, booking, and person model |
| `database` | JDBC repositories and Flyway migration `V1__init.sql` |

On an empty database, `DatabaseSimulator` adds example rooms and workplaces. A separate KeyMaster process can listen on port 3000. `EventService` reads its `/room` and `/key` endpoints and matches them with current bookings.

## Prerequisites

- Java 21
- Docker, for the local PostgreSQL database

## Database

The local database settings live in `application.yml` and `docker-compose.yml`. They are development placeholders, not production credentials.

```powershell
docker compose up -d
```

| Setting | Local value |
| --- | --- |
| Database | `myRoommate` |
| User | `user` |
| Password | `mySecret` |
| URL | `jdbc:postgresql://localhost:5432/myRoommate` |

PostgreSQL data is stored in `data/` and is not committed.

## GitHub sign-in

Create a GitHub OAuth app and set these environment variables before starting the application:

```powershell
$env:CLIENT_ID = "<GitHub OAuth client id>"
$env:CLIENT_SECRET = "<GitHub OAuth client secret>"
```

The callback URL is `http://localhost:8080/login/oauth2/code/github`.

To give one GitHub login the administrator role, set `roommate.rollen.admin` in a local `application.yml` to that login. Leave it empty in the committed file.

## Start

```powershell
.\gradlew.bat bootRun
```

Open `http://localhost:8080`.

The optional KeyMaster service from this snapshot listens on port 3000:

```powershell
java -jar keymaster.jar
```

## Tests

The tests use an in-memory H2 database and do not need PostgreSQL or GitHub credentials.

```powershell
.\gradlew.bat test
```

## What this copy leaves out

The imported snapshot does not include the course feedback notes, the student-identifier list, or real OAuth client secrets. Test fixtures use example names.

## License

The imported snapshot does not contain a separate license file. Existing copyright and license notices in the source stay as they are.
