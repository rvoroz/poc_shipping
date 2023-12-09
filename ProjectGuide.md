# Project Guide - UPS Shipping Service
## Create a single REST application with the following endpoints:
### 1. Validate Address
   a. REST API receives an address and returns the standardized address, or a list of alternatives.
### 2. Generate Shipping Labels:
   
   * A REST API should be created taking:
      * valid From address
      * valid To address
      * Shipping Account ID
      * Service Type
   * Returns a shipping label URL (to a PDF) and tracking number (or relevant error)
   * For now, assume the package type/dimensions are always an envelope
### 3. Track Package
   * Given a tracking number, return the delivery date, time, status code, status description.

##   Non-functional requirements:
### 1. Authentication should use Bearer Tokens
   * A list of valid bearer tokens should be maintained in a MySQL database table, using MyBatis.

### 2. Configurable to point to the UPS sandbox or production API
### 3. Should manage it's own data and enumerations using MySQL and MyBatis
   * Shipper info should be static (only one shipper), and sourced from the internal database

   * Shipping Account and payment information should be stored in the database for use within the API.

   * Allowed Service Types (e.g. overnight express, express, standard) should be enumerated in the database
### 4. Should use Spring Boot, Java, MySQL, MyBatis
##   Deliverables:
   * github repository with complete history (e.g. pull requests, comments, etc)
   * API documentation
   * Automated tests as appropriate
   * working/callable api in a test environment
