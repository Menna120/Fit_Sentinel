![image](https://github.com/user-attachments/assets/7a445e88-dc12-4130-bc89-4196642e7f06)# Fit Sentinel 🛡️👟

**Your Smart Step Counter & Fitness Guardian**

**Fit Sentinel** is an Android app that helps users stay active and safe by accurately counting steps using both **Accelerometer & Gyroscope** or the device’s built-in **Pedometer**, depending on availability. Built with **Kotlin** and **Jetpack Compose**, the app offers a sleek and modern UI for step tracking, activity history, and optional safety features.

---

## 🚶 Core Feature: Intelligent Step Counting

🎯 **Highly Accurate Step Counter**

* Uses **Accelerometer + Gyroscope** for custom step detection
* Falls back to the **Pedometer sensor** (TYPE\_STEP\_COUNTER) if available
* Works reliably indoors and outdoors
* Battery-efficient and runs in the background

---

## ✨ Other Features

* 📈 **Daily & Weekly Step History** – Track progress over time
* 🎨 **Modern Compose UI** – Material 3 theming with light/dark mode
* 🔢 **Step Goal Notifications** – Set daily step targets
* 🔒 **Privacy-First Design** – No unnecessary data collection

---

## 📱 Screenshots

| Dashboard | Step Counter |   |
| --------- | ------------ | - |
| ![Dashboard](![image](https://github.com/user-attachments/assets/55b056c2-2a6f-4d58-b9cc-67b7c0e9acd5)) | ![Step Counter](https://www.figma.com/design/qUijDQn22xHJLTJfBfKqB1/Step-Counter-App?node-id=67-425&t=o5rNmcXPoahbvI6e-4) |   |

---

## 🛠️ Tech Stack

* **Language**: Kotlin
* **UI**: Jetpack Compose + Material 3
* **Architecture**: MVVM
* **Sensors**: Accelerometer, Gyroscope, Step Counter
* **Local Storage**: Room Database
* **Dependency Injection**: Hilt

---

## ⚙️ How It Works

The app checks for the availability of the **TYPE\_STEP\_COUNTER** sensor. If it's not available, it automatically falls back to a **custom algorithm** using **accelerometer and gyroscope data** to count steps based on movement patterns. This ensures compatibility with a wide range of Android devices.

---

## 🎨 Design Prototype

You can explore the app's UI/UX design via the Figma link below:

[Figma Design – Fit Sentinel](https://www.figma.com/design/qUijDQn22xHJLTJfBfKqB1/Step-Counter-App?m=auto&t=yJ0TrjmIM2SKAmTA-6)

## 🧑‍💻 Getting Started

### Prerequisites

* Android Studio Giraffe or later
* Android SDK 33+
* Kotlin 1.9+

### Build & Run

```bash
git clone https://github.com/yourusername/fit-sentinel.git
cd fit-sentinel
./gradlew installDebug
```
