# Trackify 📝⏰

A simple yet powerful task manager app built with **Jetpack Compose** for Android. Trackify allows users to create tasks, optionally set reminder alarms for tasks, and manage their to-do list with ease.

---

## ✨ Features

- ✅ Add, view, and delete tasks.
- ⏰ Optional reminder alarms for each task.
- 🔔 Get notification popups (via Toast) when a task alarm triggers.
- 💾 Data persistence using **Jetpack DataStore** with Gson serialization.
- 🎨 Modern UI with **Jetpack Compose** and **Material3 Design**.

---

## 🚀 Tech Stack

- **Kotlin**
- **Jetpack Compose** (UI)
- **Material3 Design**
- **DataStore Preferences** (local storage)
- **BroadcastReceiver + AlarmManager** (for alarms)
- **Coroutines + Flows**

---

## 🔧 Project Setup

### ✅ Prerequisites
- Android Studio Flamingo/Koala or later
- Android Emulator or real device (API Level 31+ recommended)
- Kotlin version `1.8+`

### 🚀 How to Run

1. Clone the repo:

```bash
git clone https://github.com/your-username/Trackify.git
