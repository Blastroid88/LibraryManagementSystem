LIBRARY MANAGMENT SYSTEM - PROJECT DESCRIPTION
This Library Management System is a simple project developed using Java, precisely with JavaFX in the IDE Intellij Ultimate.
KEY FEATURES:

1. User Authentication and Registration:
   The system includes a secure login module that authenticates users through a dedicated "LoginService". 
   New users can register using the "RegisterController", ensuring that only authorized personnal have access
   to the system functionalities.

2. Book Management:
   The "BookController" oversees the management of the library's book inventory. Librarians acan add new books,
   edit existing records, or delete outdated entries. The system maintains detailed information about each book,
   including title, author, publisher, ISBN, CATEGORY and available quantity.

3. Member Management:
   The MemberController manages library membership. Librarians can add new members, edit member details, and view
   a comprehensive list of registered members. This module ensures that member data is accurate and up-to-date, facilitating
   smooth operations in loan management.

4. Loan Management:
   The LoanController is responsible for handling book loans. It tracks which books have been borrowed by which members and
   monitors the status of each loan. This module ensures that loans are processed efficiently, and books are returned on time,
   reducing the risk of overdue or lost books.

5. Dashboard and Navigation:
   The main interface, managed by the MainController, serves as the central dashboard, providing quick access to all sections
   of the system, including book management, member management, and loan processing. The MainApp class handles the navigation
   between different views, ensuring a smooth and user-friendly experience.

Operational Workflow:
Login and Access Control:

Upon launching the application, users are prompted to log in. After successful authentication, they are directed to the main 
dashboard, where they can navigate to different sections of the system.
Managing Books:

From the main dashboard, users can access the book management module, where they can view the library's entire book collection.
Users can add new books, update existing records, or remove books that are no longer in circulation.
Handling Members:

The member management module allows users to register new members and update or view existing member information. This ensures
that the library maintains an accurate record of all patrons, enabling smooth processing of loans and returns.
Processing Loans:

In the loan management section, users can assign books to members, track borrowed books, and manage returns. The system automatically
updates the availability status of books and ensures that all loan transactions are accurately recorded.
Navigation and Logout:

The system's intuitive navigation allows users to switch between different modules with ease. The main dashboard provides an overview
of all operations, and users can log out securely when their tasks are completed.
