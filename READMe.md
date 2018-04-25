# article-crud-micro-service

A CRUD REST/HTTP Proof-of-Concept application showcasing Spring Boot and Embedded MongoDB. The model is based on a 
simple 'article' model containing a headline, content, author and a list of topics.

There is a usermanual.docx included in the root holding further details of the design/tools used for architectural
decisions regarding code-coverage, source control, continuous integration etc - treat as a blog of useful info.

Application Architecture:
------------------------

Frameworks = Spring Boot\n
Database = MongoDB (embedded)\n
Build Tool = Maven\n
Language = Java (8)\n
Source Control = GitHub\n
Code-Coverage = JaCoCo + codecov.io\n
Tests = Spring Test/Mock + JUnit + Postman (RESTful API)\n
CI = TravisCI (integrated with GitHub + Postman + JaCoCo)\n

TODO:
----

Web Service Doco = RAML\n
Container = Docker

