# leslie
Leslie - ERP for software projects

Current features:
* Strong database authentication (salted SHA-512 encrypted passwords)
* User, permission and role administration
* Fine-grained (per-project) authorization & authentication
* Projects & membership assignment
* Project resource assignment

Architecture:
* Eclipse Scout (Neon.2)
* JPA (Eclipselink)
* PostgreSQL database (is portable)
* Liquibase (DB definition and test DB setup - also portable)
* Custom-built Entity/form/page data mapping

Screenshots:

![Project Resources](/resources/img/1.png "Project Resources")

![Administration Outline](/resources/img/2.png "Administration Outline")
