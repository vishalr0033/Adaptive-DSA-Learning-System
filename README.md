# AI-Based Personalized Coding Mistake Analysis and Adaptive DSA Learning System


> A full-stack, AI-powered educational platform where students practice Data Structures and Algorithms (DSA) in Java, receive root-cause diagnostic feedback on compilation & logic mistakes, detect recurring conceptual patterns, and follow personalized adaptive practice paths.

---

## 🌟 Key Features

1. **Live Java Execution Sandbox**
   - Direct in-browser Java code editor supporting standard `Solution` classes.
   - Real-time compilation with `javac` and sandboxed execution with `java` via `ProcessBuilder`.
   - Comprehensive test case evaluation against sample and hidden test cases.
   - Enforced 3.0-second execution timeout to catch infinite loops and recursion blowups.
   - Status classifications: `Accepted`, `Wrong Answer`, `Compilation Error`, `Time Limit Exceeded`, `Runtime Error`.

2. **AI-Powered Mistake Diagnosis (Gemini API + Fallback)**
   - Automatically analyzes failed submissions using Google Gemini API (`gemini-1.5-flash`).
   - Categorizes errors into 10 standard taxonomies: `Logic Error`, `Wrong Algorithm`, `Wrong Data Structure`, `Boundary Condition`, `Edge Case`, `Loop Error`, `Recursion Base Case`, `Pointer Movement`, `Time Complexity`, `Space Complexity`.
   - Explains **WHY** the student is struggling (conceptual root cause rather than generic advice).
   - Built-in heuristic `DeterministicDsaAnalyzer` ensures 100% offline uptime and demo resilience even if API keys or internet are unavailable.

3. **Recurring Mistake & Pattern Aggregation**
   - Automatically tracks historical mistake signatures across problems in the same DSA topic.
   - Detects repeated conceptual flaws (e.g. *Incorrect left pointer movement in Sliding Window (5x)* or *Missing base case in Recursion (4x)*).

4. **Weakness Detection & Profiling**
   - Multi-dimensional scoring evaluating topic accuracy %, attempt count, failure rate, and recurring mistake frequency across all 14 DSA topics.
   - Computes Primary Weakness and Secondary Weakness profiles.

5. **Adaptive Personalized Question Generator**
   - Recommends tailored practice questions specifically matched to overcome diagnosed weaknesses.
   - Sequences problems progressively from **Easy → Medium → Hard** to scaffold learning.

6. **Improvement Tracking & Visual Analytics**
   - Tracks student performance before vs after AI practice.
   - Visual comparison charts showing accuracy growth, attempt reductions, and mistake rate decreases.

7. **30+ Pre-Seeded Curated DSA Problems**
   - Spanning all 14 fundamental topics: *Arrays, Strings, HashMap, Stack, Queue, Linked List, Recursion, Binary Search, Sliding Window, Two Pointer, Sorting, Trees, Graphs, Dynamic Programming*.

8. **Admin Control Panel**
   - Problem CRUD management (Add, Edit, Delete DSA problems).
   - Test harness suite management (Add Sample and Hidden test cases).
   - Student enrollment monitoring and systemic failure pattern analytics.

---

## 🛠️ Technology Stack

| Layer | Technologies |
|---|---|
| **Backend** | Java 21+ / Java 24, Spring Boot 3.3.4 (Spring Web, Spring Data JPA, Spring Validation) |
| **Frontend** | HTML5, CSS3 (Modern Glassmorphic Dark UI), Vanilla JavaScript (ES6+), Chart.js |
| **Database** | MySQL 8.0 (Primary) with intelligent auto-switching embedded H2 fallback |
| **AI Integration** | Google Gemini 1.5 Flash API (`GEMINI_API_KEY`) + Deterministic AST Analyzer |
| **Code Runner** | JavaCompiler API / `javac` / `java` ProcessBuilder Sandbox |

---

## 🚀 Quick Start Guide

### Prerequisites
- **Java JDK 21 or newer** (Java 24 verified)
- **MySQL Server 8.0** (Optional: application automatically operates with persistent fallback if MySQL is offline)
- **Internet access** (Optional for Gemini API; built-in analyzer handles offline mode)

### 1. Configure Environment Variables (Optional for Gemini AI)
Set your Google Gemini API key:
```powershell
# Windows PowerShell
$env:GEMINI_API_KEY = "your_gemini_api_key_here"

# Windows Command Prompt
set GEMINI_API_KEY=your_gemini_api_key_here

# Linux / macOS
export GEMINI_API_KEY="your_gemini_api_key_here"
```
*(If no API key is provided, the system seamlessly uses the built-in deterministic DSA mistake analyzer!)*

### 2. Database Setup (MySQL)
If using local MySQL Server:
1. Ensure MySQL is running on `localhost:3306`.
2. The database `dsa_learning` will be **automatically created** by Spring Boot on first boot (`createDatabaseIfNotExist=true`).
3. To customize database credentials, edit `src/main/resources/application.properties`:
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/dsa_learning?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
   spring.datasource.username=root
   spring.datasource.password=root
   ```

### 3. Run the Backend & Frontend Application
Run the application using the included Maven Wrapper:
```powershell
# Windows PowerShell
$env:JAVA_HOME = "C:\Program Files\Java\jdk-24" # (Or your JDK path)
.\mvnw.cmd spring-boot:run
```

The Spring Boot backend and static frontend will start on **`http://localhost:8080`**.

---

## 🔑 Login Credentials for Testing

| Role | Email / Identifier | Password | Default Name |
|---|---|---|---|
| **Student** | `alex@college.edu` | `student123` | Alex Rivera |
| **Admin** | `admin@dsalearning.com` (or `admin`) | `admin123` | Platform Administrator |

*You can also register a new student account at `/register`.*

---

## 🌐 Application Page Routes

| Route | Description |
|---|---|
| `http://localhost:8080/` | Landing Page |
| `http://localhost:8080/login` | Student & Admin Login |
| `http://localhost:8080/register` | Student Registration (with College, Branch, Grad Year) |
| `http://localhost:8080/dashboard` | Student Dashboard (KPIs, Topic Radar Chart, Weakness Spotlight) |
| `http://localhost:8080/problems` | DSA Practice Hub (30+ problems across 14 topics with filters) |
| `http://localhost:8080/editor?id=1` | Code Studio (Problem statement, Java editor, [Run Code], [Submit Code]) |
| `http://localhost:8080/submissions` | Submission History & Code Inspector |
| `http://localhost:8080/analysis` | **My Coding Analysis** (AI Diagnosis, Recurring Mistake Root Causes) |
| `http://localhost:8080/recommendations` | **Adaptive Practice Path** (Targeted questions based on mistakes) |
| `http://localhost:8080/improvement` | **Improvement Tracker** (Before vs After Practice Comparison) |
| `http://localhost:8080/profile` | Student Academic Profile |
| `http://localhost:8080/admin` | Admin Portal (Problem CRUD, Test Cases, Student Monitoring) |

---

## 📡 REST API Documentation

### Authentication
- `POST /api/auth/register` - Register a new student profile
- `POST /api/auth/login` - Student login
- `POST /api/auth/admin/login` - Admin login
- `POST /api/auth/logout` - Invalidate session

### Problems & Test Cases
- `GET /api/problems?topic={topic}&difficulty={diff}&search={query}` - Filter practice problems
- `GET /api/problems/{id}` - Fetch single problem details
- `GET /api/problems/{id}/testcases` - Fetch sample test cases for editor
- `GET /api/problems/topics` - Fetch list of all 14 DSA topics

### Code Execution & AI Analysis
- `POST /api/code/run` - Compile and execute against sample test cases
- `POST /api/code/submit` - Execute full test suite, persist submission, and run AI mistake diagnosis
- `GET /api/submissions/student/{studentId}` - List student submissions
- `GET /api/submissions/{id}` - Get submission detail with code and AI diagnosis
- `GET /api/analysis/student/{studentId}` - Generate comprehensive "My Coding Analysis" report
- `GET /api/recommendations/student/{studentId}` - Get adaptive practice recommendations
- `POST /api/recommendations/generate/{studentId}` - Recalculate recommendations
- `GET /api/improvement/student/{studentId}` - Before vs After improvement report

### Admin
- `GET /api/admin/dashboard` - Platform statistics, student list, common mistakes
- `POST /api/admin/problems` - Create new DSA problem
- `PUT /api/admin/problems/{id}` - Update problem
- `DELETE /api/admin/problems/{id}` - Delete problem
- `POST /api/admin/problems/{id}/testcases` - Add test cases to problem

---

## 🎓 Final-Year College Project Highlights
- **No Dummy Buttons**: Every button, chart, filter, and code execution pipeline is fully wired and functioning end-to-end.
- **Root-Cause Telemetry**: AI pinpoints specific conceptual bugs (e.g. *Incorrect pointer movement* or *Subproblem state loss in recursion*) rather than vague suggestions.
- **Fail-Safe Architecture**: Runs effortlessly out-of-the-box on localhost.
