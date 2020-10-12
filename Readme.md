# Spring Boot Message Scheduler Example: Building Message Scheduling app

## Requirements

1. Java - 1.8.x

2. Maven - 3.x.x

3. MySQL - 5.x.x

## Steps to Setup

**1. Clone the application**

```bash
git clone https://github.com/vamsi135/ScheduleMessaging.git
```

**2. Create MySQL database**

```bash
create database quartz_demo
```

**3. Change MySQL username and password as per your MySQL installation**

open `src/main/resources/application.properties`, and change `spring.datasource.username` and `spring.datasource.password` properties as per your mysql installation


**5. Create Quartz Tables**

The project stores all the scheduled Jobs in MySQL database. You'll need to create the tables that Quartz uses to store Jobs and other job-related data. Please create Quartz specific tables by executing the `quartz_tables.sql` script located inside `src/main/resources` directory.

```bash
mysql> source <PATH_TO_QUARTZ_TABLES.sql>
```

**6. Build and run the app using maven**

Finally, You can run the app by typing the following command from the root directory of the project -

```bash
mvn spring-boot:run
```

## Scheduling Message using the /scheduleMessage API

```bash
curl -i -H "Content-Type: application/json" -X POST \
-d '{"message":"Printing Scheduled Message","dateTime":"2020-10-07T16:15:00","timeZone":"America/New_York"}' \
http://localhost:8080/scheduleMessage

# Output
{"success":true,"jobId":"562373b5-db29-48ba-80e9-5e119ae6d07e","jobGroup":"message-jobs","message":"Message Scheduled Successfully!"}
```