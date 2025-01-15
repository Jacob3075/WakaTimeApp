# WakaTimeApp

Simple app to view your coding stats from [WakaTime](https://wakatime.com/dashboard)

Uses `Export API` to get all user stats without having to upgrade your account

## Screenshots

| Login                | ![Login Page](/assets/imgs/Login.png)                 | ![Loading extracts from WakaTime](/assets/imgs/Extract.png)  |
|----------------------|-------------------------------------------------------|--------------------------------------------------------------|
| Home Page            | ![Home Page 1](/assets/imgs/HomePage1.png)            | ![Home Page 2](/assets/imgs/HomePage2.png)                   |
| Project Details Page | ![Project Details Page 1](/assets/imgs/HomePage1.png) | ![Project Details Page 2](/assets/vids/ProjectDetails1.webm) |

## TODO

- Show detailed stats for projects
- Show details stats for specific time range

## Setup

Create file `local.properties` if not already created and add below properties for app id and app secret from
[here](https://wakatime.com/apps). Values will be read from build script and used in code.

```properties
CLIENT_ID=APP_ID_HERE
CLIENT_SECRET=APP_SECRET_HERE
```
