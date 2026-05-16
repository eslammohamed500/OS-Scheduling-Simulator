# 🖥️ CPU Process Scheduler Simulator

A Java-based CPU scheduling simulator that implements and compares **Round Robin (RR)** and **Shortest Remaining Time First (SRTF)** scheduling algorithms, complete with a graphical user interface (GUI) and detailed performance metrics.

---

## 📋 Table of Contents

- [Overview](#overview)
- [Features](#features)
- [Algorithms](#algorithms)
- [Project Structure](#project-structure)
- [How to Run](#how-to-run)
- [Usage Example](#usage-example)
- [Performance Metrics](#performance-metrics)
- [Technologies Used](#technologies-used)

---

## 📌 Overview

This project simulates CPU process scheduling for the **Operating Systems 1 (CS251)** course final project. It allows users to input a set of processes with their arrival and burst times, then simulates scheduling using two well-known algorithms. Results are displayed via a **Gantt Chart**, and the algorithms are compared based on key performance metrics.

---

## ✨ Features

- ✅ Input validation (arrival time ≥ 0, burst time > 0, quantum > 0)
- ✅ Gantt Chart visualization for both algorithms
- ✅ Calculation of Waiting Time, Turnaround Time, and Response Time per process
- ✅ Automatic comparison summary (which algorithm performs better)
- ✅ GUI interface via `SchedulerGUI`
- ✅ Console-based interface via `Main`

---

## ⚙️ Algorithms

### 🔁 Round Robin (RR)
- **Preemptive** scheduling algorithm
- Each process gets a fixed time slice (quantum)
- Processes are queued and cycled through until completion
- Fair CPU distribution among all processes

### ⚡ Shortest Remaining Time First (SRTF)
- **Preemptive** version of Shortest Job First (SJF)
- At every time unit, the process with the least remaining burst time is executed
- Minimizes average waiting time but may cause starvation

---

## 📁 Project Structure

```
OS_PROJECT_FINAL/
│
├── Main.java               # Console entry point — handles user input & output
├── SchedulerGUI.java       # GUI entry point — graphical interface
├── Process.java            # Process model (id, arrivalTime, burstTime, remainingTime)
├── ScheduleLog.java        # Log model for Gantt Chart entries (process, start, end)
├── RoundRobinEngine.java   # Round Robin scheduling logic
├── SRTFEngine.java         # SRTF scheduling logic
├── MetricsCalculator.java  # Calculates WT, TAT, RT averages
└── ScenarioData.java       # Predefined test scenarios
```

---

## 🚀 How to Run

### Prerequisites
- Java JDK 8 or higher installed
- Any Java IDE (IntelliJ IDEA, Eclipse, VS Code) **or** command line

### Compile & Run (Command Line)

```bash
# Navigate to the project folder
cd "OS PROJECT _FINAL"

# Compile all Java files
javac *.java

# Run the console version
java Main

# Run the GUI version
java SchedulerGUI
```

### Run via IDE
1. Open the project folder in your IDE
2. Run `Main.java` for the console version
3. Run `SchedulerGUI.java` for the graphical version

---

## 💡 Usage Example

```
Enter number of processes: 3

Process ID: P1
Arrival Time: 0
Burst Time: 6

Process ID: P2
Arrival Time: 2
Burst Time: 4

Process ID: P3
Arrival Time: 4
Burst Time: 2

Enter Time Quantum: 2

========== Round Robin Gantt Chart ==========
| P1 (0-2) | P2 (2-4) | P3 (4-6) | P1 (6-8) | P2 (8-10) | P1 (10-12) |

========== SRTF Gantt Chart ==========
| P1 (0-2) | P2 (2-4) | P3 (4-6) | P2 (6-8) | P1 (8-12) |

========== Comparison Summary ==========
Better Waiting Time   : SRTF
Better Turnaround Time: SRTF
Better Response Time  : Round Robin
```

---

## 📊 Performance Metrics

| Metric | Description |
|--------|-------------|
| **Waiting Time (WT)** | Time a process spends waiting in the ready queue |
| **Turnaround Time (TAT)** | Total time from arrival to completion |
| **Response Time (RT)** | Time from arrival to first CPU execution |

Averages are calculated across all processes and compared between the two algorithms automatically.

---

## 🛠️ Technologies Used

- **Language:** Java (JDK 8+)
- **GUI:** Java Swing (`SchedulerGUI.java`)
- **Data Structures:** `ArrayList`, custom model classes
- **Paradigm:** Object-Oriented Programming (OOP)

---

## 👥 Team

> Operating Systems 1 — CS251 | Spring 2024/2025
