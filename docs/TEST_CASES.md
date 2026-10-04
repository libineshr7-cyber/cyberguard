# Test cases

Automated (no database needed): `mvn test` runs `OopAndValidationTest` (hashing, factory, polymorphism, validation, login throttle).

## Manual test cases (run after loading `schema.sql` + `sample_data.sql`)

| ID | Area | Steps | Expected result |
|----|------|-------|-----------------|
| T01 | Register | Register with a new email and a valid password | Redirected to login with success message |
| T02 | Register | Register again with the same email | "An account with this email already exists" |
| T03 | Register | Password `abc` / mismatched confirm | Friendly validation message / browser prompt |
| T04 | Login | Wrong password | "Invalid email or password." |
| T05 | Login | 5 wrong passwords in a row | "Too many failed attempts..." for 5 minutes |
| T06 | Login | `aarav@demo.test` / `User@1234` on /login | Citizen dashboard with 2 demo reports |
| T07 | Role | Citizen opens `/admin/dashboard` | 403 "Unauthorized access" page |
| T08 | Role | Logged-out visitor opens `/user/dashboard` | Redirect to login with "Please log in" |
| T09 | Role | Staff logs in on /login (citizen page) | Invalid email or password |
| T10 | Report | Submit with empty description | Browser/server validation message |
| T11 | Report | Valid report, no file, logged out | Confirmation page with Case ID `CYB-2026-000NN` |
| T12 | Report | Valid report with a 1 MB PNG | Report saved, file listed in case details |
| T13 | Report | Upload `virus.exe` | "is not allowed. Upload JPG, PNG, PDF or TXT" |
| T14 | Report | Upload a text file renamed to `.png` | "does not look like a real .png file" |
| T15 | Report | Upload file larger than 5 MB | "larger than 5 MB" / upload too large |
| T16 | Report | Incident date in the future | "cannot be in the future" |
| T17 | Report | URL `javascript:alert(1)` | "must start with http:// or https://" |
| T18 | Track | Enter `CYB-2026-00003` | Timeline highlights Investigation, public notes only |
| T19 | Track | Enter `ABC` | "Invalid Case ID format" |
| T20 | Track | Enter `CYB-2026-99999` | "No case was found for ..." |
| T21 | Track | View demo case 3 | No internal note ("INTERNAL: ...") visible |
| T22 | Ownership | Aarav opens `/user/case?caseId=CYB-2026-00003` (Priya's) | 403 Unauthorized |
| T23 | Admin | Login `admin@cyberguard.demo` / `Admin@123` at /admin-login | Dashboard counts: 10 total, 2 new, 2 under review, 3 investigating, 2 resolved, 1 closed |
| T24 | Admin | Filter by status = Resolved | Only cases 2 and 10 |
| T25 | Admin | Search `00007` | Only CYB-2026-00007 |
| T26 | Admin | Open case 8, assign Meera, save | Status becomes Assigned, history row added |
| T27 | Admin | Add public note, then track the case | Note appears on Track page |
| T28 | Admin | Add internal note (uncheck checkbox) | Visible to admin only |
| T29 | Admin | Mark as Resolved, then Close | History shows both transitions |
| T30 | Evidence | Logged-in citizen downloads someone else's evidence URL | 403 Unauthorized |
| T31 | DB failure | Stop MySQL and open /track?caseId=CYB-2026-00001 | "temporarily unavailable (database connection failed)" |
| T32 | XSS | Report description `<script>alert(1)</script>` | Shown as text, nothing executes |
