# article-crud-micro-service

A CRUD REST/HTTP Proof-of-Concept application showcasing Spring Boot and Embedded MongoDB. The model is based on a 
simple 'article' model containing a headline, content, author and a list of topics.

There is a usermanual.docx included in the root holding further details of the design/tools used for architectural
decisions regarding code-coverage, source control, continuous integration etc - treat as a blog of useful info - 
screenshots illustrate the various integration hooks configured.

To Run:
------

On GitHub at shigger/article-crud-micro-service (master and development branches)

Download https://github.com/shigger/article-crud-micro-service/blob/master/deploy/article-microservice-0.0.1-SNAPSHOT.jar (Or build from source)

And run e.g. >java -jar article-microservice-0.0.1-SNAPSHOT.jar

RESTful API Specification is available at https://app.swaggerhub.com/apis/shigger/article-services/0.1

Postman test scripts are also included (for load tests later on (TODO)) at https://github.com/shigger/article-crud-micro-service/blob/master/src/test/article_tests.postman_collection.json

Code coverage reports (html) are available at https://github.com/shigger/article-crud-micro-service/tree/master/deploy/jacoco but are integrated into the code coverage reporting tool 'codecov.io' via https://codecov.io/gh/shigger/article-crud-micro-service/branch/master

Integrated CI and build status is managed by TravisCI at: https://travis-ci.org/shigger/article-crud-micro-service


Application Architecture:
------------------------

Frameworks = Spring Boot (with embedded Tomcat server)

Database = MongoDB (embedded)

Build Tool = Maven

Language = Java (8)

Source Control = GitHub

Code-Coverage = JaCoCo + codecov.io

Tests = Spring Test/Mock + JUnit + Postman (RESTful API)

CI = TravisCI (integrated with GitHub + Postman + JaCoCo)

RESTful API Design = swagger (inspector + hub) + postman

TODO:
----

Container = Docker

