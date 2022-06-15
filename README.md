# Revature-Project-2
### Created by Chris Powell, Jayden Rainsey, and Annette Reese
## Description:
This is my Revature Devops Project 2,
which is the backend implementation of a shopping application, where a user can register, login and
logout of their account, view what items are currently instock, view their cart contents and total
price, add an item to their cart, and either checkout and see the total they were charged or 
empty their cart without being charged. ADMIN users can also add new items to the available stock,
update the item information, or delete items from the database. This project will be deployed using Jenkin to Docker
to a Kubernetes cluster.
## Features Implemented:
1. A @Bean method in Project1Application.java called timedAspect to record custom metrics
2. @Timed annotations over most of the methods in UserController that will record the time it 
takes for each method to run
3. A prometheus.yml file with rules for alerting based on the custom metrics created
4. A directory called Kubernetesfiles which contains the p2-deployment.yml file needed to deploy the 
application in Kubernetes

## Roles
1. Chris:
   1. 
2. Jayden:
   1. 
3. Annette:
   1. 

## Technologies Used:
Grafana, Prometheus, Kubernetes, Jenkins, Minikube 

## Project URLs: 
### UserController:
1. **register user:** _http://ec2-3-95-55-213.compute-1.amazonaws.com:8088/register_
    * requires requestBody
    * POST Mapping
    * Timed custom metric time_to_register 
2. **update user info:** _http://ec2-3-95-55-213.compute-1.amazonaws.com:8088/update_
    * requires RequestBody
    * PUT Mapping
    * allowed roles - ADMIN,CUSTOMER,EMPLOYEE
    * Must be signed in as user being updated
3. **login:** _http://ec2-3-95-55-213.compute-1.amazonaws.com:8088/login/{username}/{password}_
    * Requires path variables
    * POST Mapping
4. **logout** _http://ec2-3-95-55-213.compute-1.amazonaws.com:8088/logout_
    * does not require input
    * POST Mapping
    * logs out of current user
5. **Get all users and their cart:** _http://ec2-3-95-55-213.compute-1.amazonaws.com:8088/getusersandcarts_
    * Does not require input
    * GET Mapping
    * allowed roles - **ADMIN** only
6. **Get current the users cart:** _http://ec2-3-95-55-213.compute-1.amazonaws.com:8088/getmycart/{userid}_
    * Requires 1 path variable
      * userId
    * GET Mapping
    * allowed roles - ADMIN,CUSTOMER,EMPLOYEE
      * Must be signed in as user being access
7. **Add Product to Cart** _http://ec2-3-95-55-213.compute-1.amazonaws.com:8088/addproducttocart/{userid}/{itemId}_
    * Requires 2 path variables
      * userId
      * itemId
    * PUT Mapping
    * allowed roles - ADMIN,CUSTOMER,EMPLOYEE
      * Must be signed in as user being access
8. **Delete User:** _http://ec2-3-95-55-213.compute-1.amazonaws.com:8088/deleteuser/{userid}_
   * Requires 1 path variable
     * userId
   * DELETE Mapping
   * allowed roles - ADMIN,CUSTOMER,EMPLOYEE
       * Must be signed in as user being access
9. **Get All Items Instock:** _http://ec2-3-95-55-213.compute-1.amazonaws.com:8088/getitemsinstock_
    * Does not require input
    * GET Mapping
10. **Checkout:** _http://ec2-3-95-55-213.compute-1.amazonaws.com:8088/checkout/{userid}_
    * Requires 1 Path variable
      * userId
    * PUT Mapping
    * Will delete the item from table after checkout
    * allowed roles - ADMIN,CUSTOMER,EMPLOYEE
       * Must be signed in as user being access
11. **Empty Cart** _http://ec2-3-95-55-213.compute-1.amazonaws.com:8088/emptycart/{userid}_
    * Requires 1 Path variable
      * userId
    * PUT Mapping
    * Will not delete the item from table after checkout
    * allowed roles - ADMIN,CUSTOMER,EMPLOYEE
      * Must be signed in as user being access
---
### Item Controller:
1. **Add Item** _http://ec2-3-95-55-213.compute-1.amazonaws.com:8088/additem_
    * requires RequestBody
    * POST Mapping
    * allowed roles - **ADMIN** only
2. **Delete Item:** _http://ec2-3-95-55-213.compute-1.amazonaws.com:8088/deleteitem/{itemid}_
    * requires 1 Path variable
      * itemId
    * DELETE Mapping
    * allowed roles - **ADMIN** only
3. **Update Item:** _http://ec2-3-95-55-213.compute-1.amazonaws.com:8088/updateitem_
    * requires RequestBody
    * PUT Mapping
    * allowed roles - **ADMIN** only