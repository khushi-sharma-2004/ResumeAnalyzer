##  Link (Check Outputs in my LinkedIn)

🔗 [LinkedIn Profile](https://www.linkedin.com/feed/update/urn:li:activity:7354553476820951041/)  

#  Smart Resume Analyzer (Spring Boot + MySQL + Thymeleaf)

An advanced full-stack Resume Analyzer web application built using **Spring Boot**, **MySQL**, **Apache Tika**, **Thymeleaf**, and **Spring Security**. It parses resumes (PDF/DOCX), extracts structured data, performs skill matching against job descriptions, generates AI-based feedback, and provides admin management tools with analytics and export features.

---

##  Project Features

| Module | Description |
|--------|-------------|
|  Resume Upload | Upload resumes in PDF/DOCX format |
|  Resume Parsing | Extract Name, Email, Phone, Skills, Education, Experience |
|  Skill Matching | Match resume skills with job description |
|  AI Scoring | Assign match score based on skill weight |
|  Feedback Generation | Recommend missing skills, feedback messages |
|  Admin Panel | View all resumes, feedback, filter, delete |
|  Authentication | DB-based login with Role-Based Access Control (RBAC) |
|  Analytics Dashboard | View top matched and missing skills |
|  PDF/CSV Export | Export feedback reports in PDF/CSV |
|  Resume Upload History | Track resume submission date and time |
|  Search/Filter | Search resumes by skill or name |

---

##  Tech Stack

- **Backend**: Java, Spring Boot, Spring MVC, Spring Data JPA, Spring Security
- **Frontend**: HTML, CSS, Bootstrap, Thymeleaf
- **Database**: MySQL
- **Resume Parsing**: Apache Tika
- **Authentication**: DB-based user roles (ADMIN)
- **Export**: OpenPDF, CSV Writer
- **Tools**: Maven, Git, VS Code

---

##  Skill Matching + Weighted AI Scoring (Logic)

- Resume skills vs. job required skills are compared
- Each skill is assigned a custom weight
- Feedback is generated based on:
- Match percentage
- Missing skills
- Suggested improvements

---

##  Resume Upload Flow 

- Upload PDF/DOCX resume
- Parsed using Apache Tika
- Extracted fields:
- Full Name
- Email ID
- Phone Number
- Education
- Experience
- Skills (as List + Text)
- Stored in MySQL `resume_data` table
- Timestamped using `uploadedAt`

---

##  Feedback Generation

- Checks:
- Matched Skills
- Missing Skills
- Score
- Dynamic feedback examples:
- `"Excellent match!"`
- `"Good match. Consider learning: Spring Boot"`
- `"Low match. Learn key skills: HTML, CSS"`

---

##  Role-Based Access

- Users:
- Can upload resumes
- Admin:
- Can view all resumes
- Delete/edit records
- Access analytics, export data
- Implemented with **Spring Security + DB roles**

---

##  Admin Analytics Dashboard

- View most frequently matched and missing skills
- See resume feedback stats
- Dashboard Table:
| Skill | Matched Count | Missing Count |


---

##  Export Features

- Download PDF Report for each resume
- Export all feedback as CSV
- Admin panel buttons:
- `Export All to PDF`
- `Export All to CSV`

---

##  Resume Upload History

- Each resume has a timestamp (date & time)
- Page: `/my-resumes`
- Table:
| ID | Name | Email | Skills | Uploaded At | Actions |

---

##  Database Structure

**Tables:**
- `users`: id, username, password, role
- `resume_data`: resume details, skills, uploadedAt, user_id
- `job_description`: job required skills
- `feedback_data`: resumeId, jobId, matchedSkills, missingSkills, feedback, score

---

##  URL Endpoints

| URL | Description |
|-----|-------------|
| `/` | Home Page |
| `/upload` | Resume Upload |
| `/my-resumes` | User Resume History |
| `/match/{resumeId}/{jobId}` | Skill Matching Preview |
| `/feedback/{resumeId}/{jobId}` | Save Feedback |
| `/admin/dashboard` | Admin Panel |
| `/admin/analytics` | Skill Analytics |
| `/admin/export-pdf` | PDF Export |
| `/admin/export-csv` | CSV Export |

---

##  Project Setup (How to Run)

1. Clone the repo:
 ```bash
 git clone https://github.com/yourusername/smart-resume-analyzer.git
 cd smart-resume-analyzer



2. Setup MySQL:

CREATE DATABASE resume_analyzer_db;


3. Configure application.properties:

spring.datasource.url=jdbc:mysql://localhost:3306/resume_analyzer_db
spring.datasource.username=root
spring.datasource.password=yourpassword


4. Run the app:

mvn spring-boot:run


5. Access:

User: http://localhost:8080/my-resumes

Admin: http://localhost:8080/admin/dashboard


---

 Sample Credentials

Role	Username	Password

Admin	admin	admin123

PROJECT FOLDER :

resume-analyzer/
├── pom.xml
├── mvnw
├── mvnw.cmd
├── .gitignore
├── HELP.md
├── README.md
├── src/
│   └── main/
│       ├── java/
│       │   └── com/
│       │       └── example/
│       │           └── smartresume/
│       │               ├── ResumeAnalyzerApplication.java
│       │               ├── TestBCrypt.java
│       │
│       │               ├── api/
│       │               │   └── ResumeApiController.java
│       │
│       │               ├── config/
│       │               │   └── SecurityConfig.java
│       │
│       │               ├── controller/
│       │               │   ├── AdminController.java
│       │               │   ├── AnalyticsController.java
│       │               │   ├── FeedbackController.java
│       │               │   ├── JobDescriptionController.java
│       │               │   ├── LoginController.java
│       │               │   ├── ResumeController.java
│       │               │   ├── ResumeMatchController.java
│       │               │   └── ResumeUploadController.java
│       │
│       │               ├── dto/
│       │               │   ├── AnalyticsDTO.java
│       │               │   ├── FeedbackResult.java
│       │               │   └── SkillMatchResult.java
│       │
│       │               ├── model/
│       │               │   ├── FeedbackData.java
│       │               │   ├── JobDescription.java
│       │               │   ├── ResumeData.java
│       │               │   ├── SkillMatchResult.java
│       │               │   └── User.java
│       │
│       │               ├── repository/
│       │               │   ├── FeedbackRepository.java
│       │               │   ├── JobDescriptionRepository.java
│       │               │   ├── ResumeRepository.java
│       │               │   └── UserRepository.java
│       │
│       │               ├── security/
│       │               │   ├── CustomUserDetails.java
│       │               │   └── CustomUserDetailsService.java
│       │
│       │               ├── service/
│       │               │   ├── AnalyticsService.java
│       │               │   ├── FeedbackService.java
│       │               │   ├── ResumeParserService.java
│       │               │   ├── ResumeService.java
│       │               │   └── SkillMatchingService.java
│       │
│       │               └── util/
│       │                   ├── PasswordGenerator.java
│       │                   ├── ResumeParserUtil.java
│       │                   └── SkillMatcher.java
│
│       └── resources/
│           ├── application.properties
│           ├── static/
│           └── templates/
│               ├── add_job.html
│               ├── admin_analytics.html
│               ├── admin_dashboard.html
│               ├── editResume.html
│               ├── error.html
│               ├── feedbackResult.html
│               ├── home.html
│               ├── layout.html
│               ├── login.html
│               ├── match_result.html
│               ├── my_resumes.html
│               ├── resume_update_form.html
│               ├── skillMatchResult.html
│               ├── success.html
│               └── upload.html
│
└── test/
    └── java/
        └── com/
            └── example/
                └── smartresume/
                    └── ResumeAnalyzerApplicationTests.java


 Developed & maintained by: Pittu Vaishnavi
