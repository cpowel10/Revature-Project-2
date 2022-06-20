# Revature-Project-2
### Created by Chris Powell, Jayden Rainsey, and Annette Reese
## Description:
This is my Revature Devops Project 2,
which is the backend implementation of a shopping application, where a user can register, login and
logout of their account, view what items are currently instock, view their cart contents and total
price, add an item to their cart, and either checkout and see the total they were charged or 
empty their cart without being charged. ADMIN users can also add new items to the available stock,
update the item information, or delete items from the database. Project 2 will take the Project 1 application 
and deploy it using Jenkins to build and push the Docker image then that Docker image will be used to deploy the application 
in a Kubernetes cluster.
## Features Implemented:
1. Register: Creates a new row in the User table from data provided by the user
2. Login: Login as a user by providing the appropriate username and password
3. Logout: Logout as current user
4. Get all customers and their cart contents: Prints a list of all users and their cart contents. Only usable by users with the Role ADMIN
5. Update user information: Updates current user information (except for userId) with new information provided by the user. You must be logged in as the user getting updated.
6. Get current logged in user and their cart: Get the name and cart contents of current logged in user. Must be logged in as user whose info is being accessed
7. Add product to cart: Add a product to the current user's cart. and removes that item from the available items instock.
8. Delete users: Delete current logged in user.
9. Get all items in stock: Prints all items currently instock
10. Checkout: Proceeds to remove the items from the current user's cart, and deletes the items from the item table. User must have a credit card number on their account to checkout.
11. Empty Cart: Empties current user's cart without charging them and makes the items available for other users to add to their carts
12. Add Item: Adds an item to the item table. Only usable by ADMIN users
13. Delete Item: Deletes an item from the item table. Only usable by ADMIN users
14. Update Item: Updates the information about an item. Only usable by ADMIN users
15. A @Bean method in Project1Application.java called timedAspect to record custom metrics
16. @Timed annotations over most of the methods in UserController that will record the time it 
takes for each method to run
17. A prometheus.yml file with rules for alerting based on the custom metrics created
18. A directory called Kubernetesfiles which contains the p2-deployment.yml file needed to deploy the 
application in Kubernetes

## Roles
1. Chris:
   1. Set up Jenkins Pipeline with custom Dockerfile and Jenkinsfile
   2. Wrote Custom metrics using micrometer
   3. Created custom alert rules in Prometheus
2. Jayden:
   1. Wrote the SLO
   2. Defined custom recording and reporting rules based on the SLO
   3. Helped create the Grafana dashboard
3. Annette:
   1. Helped create the Grafana dashboard

## Technologies Used:
Grafana, Prometheus, Kubernetes, Jenkins, Minikube, Kubectl, Micrometer, 
Maven, Git, Docker, Spring Boot, Java

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
    * Timed custom metric time_to_update_user
3. **login:** _http://ec2-3-95-55-213.compute-1.amazonaws.com:8088/login/{username}/{password}_
    * Requires path variables
    * POST Mapping
    * Timed custom metric time_to_login
4. **logout** _http://ec2-3-95-55-213.compute-1.amazonaws.com:8088/logout_
    * does not require input
    * POST Mapping
    * logs out of current user
5. **Get all users and their cart:** _http://ec2-3-95-55-213.compute-1.amazonaws.com:8088/getusersandcarts_
    * Does not require input
    * GET Mapping
    * allowed roles - **ADMIN** only
    * Timed custom metric time_to_get_users_and_carts
6. **Get current the users cart:** _http://ec2-3-95-55-213.compute-1.amazonaws.com:8088/getmycart/{userid}_
    * Requires 1 path variable
      * userId
    * GET Mapping
    * allowed roles - ADMIN,CUSTOMER,EMPLOYEE
      * Must be signed in as user being access
    * Timed custom metric time_to_get_single_cart
7. **Add Product to Cart** _http://ec2-3-95-55-213.compute-1.amazonaws.com:8088/addproducttocart/{userid}/{itemId}_
    * Requires 2 path variables
      * userId
      * itemId
    * PUT Mapping
    * allowed roles - ADMIN,CUSTOMER,EMPLOYEE
      * Must be signed in as user being access
    * Timed custom metric time_to_add_product_to_cart
8. **Delete User:** _http://ec2-3-95-55-213.compute-1.amazonaws.com:8088/deleteuser/{userid}_
   * Requires 1 path variable
     * userId
   * DELETE Mapping
   * allowed roles - ADMIN,CUSTOMER,EMPLOYEE
       * Must be signed in as user being access
   * Timed custom metric time_to_delete_user
9. **Get All Items Instock:** _http://ec2-3-95-55-213.compute-1.amazonaws.com:8088/getitemsinstock_
    * Does not require input
    * GET Mapping
    * Timed custom metric time_to_get_items_instock
10. **Checkout:** _http://ec2-3-95-55-213.compute-1.amazonaws.com:8088/checkout/{userid}_
    * Requires 1 Path variable
      * userId
    * PUT Mapping
    * Will delete the item from table after checkout
    * allowed roles - ADMIN,CUSTOMER,EMPLOYEE
       * Must be signed in as user being access
    * Timed custom metric time_to_checkout
11. **Empty Cart** _http://ec2-3-95-55-213.compute-1.amazonaws.com:8088/emptycart/{userid}_
    * Requires 1 Path variable
      * userId
    * PUT Mapping
    * Will not delete the item from table after checkout
    * allowed roles - ADMIN,CUSTOMER,EMPLOYEE
      * Must be signed in as user being access
    * Timed custom metric time_to_empty_cart
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