# Revature-Project-1
#### Created by Chris Powell
This is my Revature Devops Project 1,
which uses the Spring Frameworks, such as Spring JPA to
be able to register users, let users log in and out, 
let users see every item instock, let users see their
cart, and add item to their cart if they are logged in. 

## Project URLs: 
### UserController:
1. **register user:** _localhost:8088/register_
    * requires requestBody
    * POST Mapping
2. **update user info:** _localhost:8088/update_
    * requires RequestBody
    * PUT Mapping
    * allowed roles - ADMIN,CUSTOMER,EMPLOYEE
    * Must be signed in as user being updated
3. **login:** _localhost:8088/login/{username}/{password}_
    * Requires path variables
    * POST Mapping
4. **logout** _localhost:8088/logout_
    * does not require input
    * POST Mapping
    * logs out of current user
5. **Get all users and their cart:** _localhost:8088/getusersandcarts_
    * Does not require input
    * GET Mapping
    * allowed roles - **ADMIN** only
6. **Get current the users cart:** _localhost:8088/getmycart/{userid}_
    * Requires 1 path variable
      * userId
    * GET Mapping
    * allowed roles - ADMIN,CUSTOMER,EMPLOYEE
      * Must be signed in as user being access
7. **Add Product to Cart** _localhost:8088/addproducttocart/{userid}/{itemId}_
    * Requires 2 path variables
      * userId
      * itemId
    * PUT Mapping
    * allowed roles - ADMIN,CUSTOMER,EMPLOYEE
      * Must be signed in as user being access
8. **Delete User:** _localhost:8088/deleteuser/{userid}_
   * Requires 1 path variable
     * userId
   * DELETE Mapping
   * allowed roles - ADMIN,CUSTOMER,EMPLOYEE
       * Must be signed in as user being access
9. **Get All Items Instock:** _localhost:8088/getitemsinstock_
    * Does not require input
    * GET Mapping
10. **Checkout:** _localhost:8088/checkout/{userid}_
    * Requires 1 Path variable
      * userId
    * PUT Mapping
    * Will delete the item from table after checkout
    * allowed roles - ADMIN,CUSTOMER,EMPLOYEE
       * Must be signed in as user being access
11. **Empty Cart** _localhost:8088/emptycart/{userid}_
    * Requires 1 Path variable
      * userId
    * PUT Mapping
    * Will not delete the item from table after checkout
    * allowed roles - ADMIN,CUSTOMER,EMPLOYEE
      * Must be signed in as user being access
---
### Item Controller:
1. **Add Item** _localhost:8088/additem_
    * requires RequestBody
    * POST Mapping
    * allowed roles - **ADMIN** only
2. **Delete Item:** _localhost:8088/deleteitem/{itemid}_
    * requires 1 Path variable
      * itemId
    * DELETE Mapping
    * allowed roles - **ADMIN** only
3. **Update Item:** _localhost:8088/updateitem_
    * requires RequestBody
    * PUT Mapping
    * allowed roles - **ADMIN** only