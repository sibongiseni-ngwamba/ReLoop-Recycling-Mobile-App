# Welcome to the ReLoop Mobile App Repository

Welcome to the official repository for the **ReLoop Mobile App**, developed as part of the Work-Integrated Learning (WIL) 2026 project by **The Avengers** team.

This Android mobile application was created for **ReLoop Technologies SA** to support digital recycling services such as user registration, pickup scheduling, recycling guidance, reward tracking, and waste classification simulation.

---

## Project Overview

The **ReLoop Mobile App** is a Kotlin-based Android application that allows users to manage recycling activities from their mobile devices. The app helps users schedule recycling pickups, view pickup history, earn reward points, redeem rewards, receive notifications, and access recycling guidance.

The application uses a local Room database, meaning it can run without Firebase or a live online server. This makes it suitable for academic demonstration, testing, and local development.

---

## Main Features

### User Features

- User registration
- User login
- Persistent login session
- User dashboard
- Schedule recycling pickups
- View pickup history
- View recycling guidance
- Track reward points
- Redeem rewards
- View notifications
- Edit user profile
- Simulated waste classification

### Admin Features

- Admin login
- Admin dashboard
- View total users
- View total pickups
- Manage users
- Manage pickup requests
- Update pickup status
- Manage reward items
- View system reports

---
### UI screenshots

  <div style="display:flex;flex-wrap:wrap;gap:12px;align-items:flex-start;">
  <img src="assets/screenshots/Image%201.jpeg" alt="Image 1" style="width:240px;height:auto;border-radius:8px;box-shadow:0 2px 6px rgba(0,0,0,0.08);" />
  <img src="assets/screenshots/image%202.jpeg" alt="Image 2" style="width:240px;height:auto;border-radius:8px;box-shadow:0 2px 6px rgba(0,0,0,0.08);" />
  <img src="assets/screenshots/image%203.jpeg" alt="Image 3" style="width:240px;height:auto;border-radius:8px;box-shadow:0 2px 6px rgba(0,0,0,0.08);" />
  <img src="assets/screenshots/image%204.jpeg" alt="Image 4" style="width:240px;height:auto;border-radius:8px;box-shadow:0 2px 6px rgba(0,0,0,0.08);" />
  <img src="assets/screenshots/image%205.jpeg" alt="Image 5" style="width:240px;height:auto;border-radius:8px;box-shadow:0 2px 6px rgba(0,0,0,0.08);" />
  <img src="assets/screenshots/image%206.jpeg" alt="Image 6" style="width:240px;height:auto;border-radius:8px;box-shadow:0 2px 6px rgba(0,0,0,0.08);" />
  <img src="assets/screenshots/image%207.jpeg" alt="Image 7" style="width:240px;height:auto;border-radius:8px;box-shadow:0 2px 6px rgba(0,0,0,0.08);" />
  <img src="assets/screenshots/image%208.jpeg" alt="Image 8" style="width:240px;height:auto;border-radius:8px;box-shadow:0 2px 6px rgba(0,0,0,0.08);" />
  <img src="assets/screenshots/image%209.jpeg" alt="Image 9" style="width:240px;height:auto;border-radius:8px;box-shadow:0 2px 6px rgba(0,0,0,0.08);" />
  <img src="assets/screenshots/image%2010.jpeg" alt="Image 10" style="width:240px;height:auto;border-radius:8px;box-shadow:0 2px 6px rgba(0,0,0,0.08);" />
  <img src="assets/screenshots/image%2011.jpeg" alt="Image 11" style="width:240px;height:auto;border-radius:8px;box-shadow:0 2px 6px rgba(0,0,0,0.08);" />
  <img src="assets/screenshots/image%2012.jpeg" alt="Image 12" style="width:240px;height:auto;border-radius:8px;box-shadow:0 2px 6px rgba(0,0,0,0.08);" />
  <img src="assets/screenshots/image%2013.jpeg" alt="Image 13" style="width:240px;height:auto;border-radius:8px;box-shadow:0 2px 6px rgba(0,0,0,0.08);" />
  <img src="assets/screenshots/image%2014.jpeg" alt="Image 14" style="width:240px;height:auto;border-radius:8px;box-shadow:0 2px 6px rgba(0,0,0,0.08);" />
  <img src="assets/screenshots/image%2015.jpeg" alt="Image 15" style="width:240px;height:auto;border-radius:8px;box-shadow:0 2px 6px rgba(0,0,0,0.08);" />
  <img src="assets/screenshots/image%2016.jpeg" alt="Image 16" style="width:240px;height:auto;border-radius:8px;box-shadow:0 2px 6px rgba(0,0,0,0.08);" />
  <img src="assets/screenshots/image%2017.jpeg" alt="Image 17" style="width:240px;height:auto;border-radius:8px;box-shadow:0 2px 6px rgba(0,0,0,0.08);" />
  <img src="assets/screenshots/image%2018.jpeg" alt="Image 18" style="width:240px;height:auto;border-radius:8px;box-shadow:0 2px 6px rgba(0,0,0,0.08);" />
  </div>

---
## Technologies Used

- Kotlin
- Android Studio
- XML Layouts
- RoomDB
- ViewModel
- LiveData
- RecyclerView
- Material Components
- SharedPreferences

---

## Project Architecture

The app follows the **MVVM architecture pattern**:

```text
Model - View - ViewModel
