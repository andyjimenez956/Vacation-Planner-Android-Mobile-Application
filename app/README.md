# Vacation Scheduler - Adroid Application

## Purpose
The Vacation Scheduler application allows users to create, manage, and track vacations,
excursions with vacations, validate dates, receive alerts, and share vacation information.


## How to Use the Application

## Vacation Management
1. Launch the application.
2. From the home screen, select "View Vacations".
3. Tap "Add Vacation" to create a new vacation.
4. Enter title, hotel, start date, and end date.
5. Save the vacation to store it in the database.
6. Tap an existing vacation to edit or delete it.
7. Vacations cannot be deleted if excursions are associated with them.
8. Use the "Share" button to share vacation details via email, text, or clipboard copy.
9. Alerts can be set for vacation start and end dates.

## Excursion Management
1. From the Vacation Detail screen, select "View Excursions".
2. Tap "Add Excursion" to create a new excursion.
3. Enter and excursion title and select a date.
4. Excursion dates must fall within the associated vacation date range.
5. Save the excursion to store it in the database.
6. Tap and existing excursion to edit or delete it.
7. Use the "Set Alert" button to receive a notification on the excursion date.


## Android Version
This application is compatible with "Android 8.0 (API level 26" and higher.
The signed APK is built targeting "Android API level36".

## Git Lab Repo
https://gitlab.com/wgu-gitlab-environment/student-repos/ajim294/d308-mobile-application-development-android.git





### B4: Excursion title and date
- Excursion entity fields: `title`, `date` (app/src/main/java/.../entities/Excursion.java)
- Excursion detail screen inputs: Title + Date (activity_excursion_detail.xml)
- Excursion list shows title + date (ExcursionAdapter.java)

### B5: Excursion features
- B5a (detailed view): ExcursionDetailActivity.java loads/displays title + date
- B5b (enter/edit/delete): Save/Delete buttons in ExcursionDetailActivity.java
- B5c (date format validation): SimpleDateFormat parse check in ExcursionDetailActivity.java
- B5d (alert): “Set Alert” button schedules notification via AlertReceiver.java
- B5e (date within vacation): validation compares excursion date to parent vacation start/end
