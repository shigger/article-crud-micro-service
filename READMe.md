# article-crud-micro-service

A CRUD REST/HTTP Proof-of-Concept application showcasing Spring Boot and Embedded MongoDB. The model is based on a 
simple 'article' model containing a headline, content, author and a list of topics.

There is a usermanual.docx included in the root holding further details of the design/tools used for architectural
decisions regarding code-coverage, source control, continuous integration etc - treat as a blog of useful info.

Application Architecture:
------------------------

Frameworks = Spring Boot
Database = MongoDB (embedded)
Build Tool = Maven
Language = Java (8)
Source Control = GitHub
Code-Coverage = JaCoCo + codecov.io
Tests = Spring Test/Mock + JUnit + Postman (RESTful API) 
CI = TravisCI (integrated with GitHub + Postman + JaCoCo).

TODO:
----

Web Service Doco = RAML
Container = Docker

