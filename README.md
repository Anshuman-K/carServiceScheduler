

# Appointment scheduler

>This is a Spring Boot Web Application to manage and schedule appointments between operators and users. It features such as appointments cancellation, appointments reschedule, list all the booked slots of an operator, and list all the opened slots of an operator.

## Steps to Setup

**1. Clone the application from github or Unzip the sent zipped file.**

```bash
git clone https://github.com/Anshuman-K/carServiceScheduler
```
**2. Create MySQL database**

```bash
create database car_service_vm
```
- And change the properties(such as username and password) of the database in the file application.properties in Application.

**3. Run the app using maven**

```bash
mvn spring-boot:run
```

The app will start running at <http://localhost:8080>

## STEP BY STEP APIs

**1. Create an user.**

- POST - http://localhost:8080/api/v1/create-user
```bash
curl --location 'http://localhost:8080/api/v1/create-user' \
--header 'Content-Type: application/json' \
--data '{
    "name":"Ayushman"
}'
```

**2. Create an operator.**

- POST - http://localhost:8080/api/v1/create-operator
```bash
curl --location 'http://localhost:8080/api/v1/create-operator' \
--header 'Content-Type: application/json' \
--data '{
    "name":"Anshuman"
}'
```

**3. Book Appointment.**

- With specific operator: POST - http://localhost:8080/api/v1/appointment/book

Here in the body operatorId is mentioned.
```bash
curl --location 'http://localhost:8080/api/v1/appointment/book' \
--header 'Content-Type: application/json' \
--data '{
    "operatorId" : "1",
    "userId" : "1",
    "startTime": "6",
    "endTime" : "7",
    "bookingReason" : "Tyre Service"
}'
```


- With random operator: POST - http://localhost:8080/api/v1/appointment/book

Here operatorId is not mentioned, so whosoever operator would be available for the given time slot, he will get booked.
```bash
curl --location 'http://localhost:8080/api/v1/appointment/book' \
--header 'Content-Type: application/json' \
--data '{
    "userId" : "1",
    "startTime": "6",
    "endTime" : "7",
    "bookingReason" : "Tyre Service"
}'
```

**4. Get All the booked slots of an operatorId.**

- GET - http://localhost:8080/api/v1/operator/{operatorId}/booked

```bash
curl --location 'http://localhost:8080/api/v1/operator/{operatorId}/booked'
```
**5. Get All the available slots of an operator.**

- GET - http://localhost:8080/api/v1/operator/{operatorId}/open

```bash
curl --location 'http://localhost:8080/api/v1/operator/{operatorId}/open'
```

**6. Cancel Appointment.**

Only the user who booked the appointment can cancel the appointment.

- PUT - http://localhost:8080/api/v1/appointment/cancel/{appointmentId}?userId={userId}

```bash
curl --location --request PUT 'http://localhost:8080/api/v1/appointment/{appointmentId}?userId={userId}'
```

**7. Reschedule Appointment.**

Reschedule appointment api needs new schedule also along with appointmentId. Internally it's calling the bookAppointment() function after cancelling the existing appointment. So if the operator Id is present then it will book with the specific operator and if not present then it will go ahead with random operator.
- PUT - http://localhost:8080/api/v1/appointment/reschedule/{appointmentId}

```bash
curl --location --request PUT 'http://localhost:8080/api/v1/appointment/reschedule/{appointmentId}' \
--header 'Content-Type: application/json' \
--data '{
    "userId" : "1",
    "startTime": "6",
    "endTime" : "7",
    "bookingReason" : "Tyre Service"
}'
```

## Assumptions and Logics

- To book an appointment the user should be present in the DB. 
- To book an appointment with the specific operator that operator should be available in the given slot. Else error will pop.
- To book a random appointment with any operator, the user shouldn't pass any operatorId.
- Internally reschedule appointment is changing the state of existing appointment from BOOKED -> CANCELLED, then calling bookAppointment() function internally so if the operator id will be present it will proceed with the specific operator else will go ahead with random operator.
- To get the booked slot, it'll be shown as a Map, a list of booked slots against the operatorId.
- To get the available slot, it'll be shown as a Map, a list of *merged* available slots against the operatorId.
- This system can handle multiple user, multiple operator and many appointment crosswise. No need to hardcode any operator service.
- Proper Error Message is shown with respective error code.




