# README #

This frameWorks is build using TestNg and Maven with allure reporting


### PreRequisite  ###

  * git
  * maven 
  * java 11
  * Allure  
  * Development code editor

### Where is the test cases ###

*src/test/java/api_test

### How to run ###

* open terminal and run this command `mvn clean test`

### How to run allure report  ###

  * after running command as mentioned
  * Step allure in your local system
  * In order to generate a report, we should install the Allure command-line interpreter.
    (This step is for first time installer)
    Download the latest version as a zip archive from [page](https://allurereport.org/docs/gettingstarted-installation/).
    * [Download repository](https://github.com/allure-framework/allure2/releases/tag/2.24.1)
    * after installation run this line in CMD to check version `allure --version`
  * run this command line in the automation path project  
    * `mvn clean test`
    * `allure serve allure-results --host localhost --port 8081`
  * Local test will be located on http://localhost:8081/index.html#suites/


### For any question or issues ###

  * Nybble Team - QA Automation