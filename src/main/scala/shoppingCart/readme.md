A Guitar store located in the US has hired our services to develop the backend system of their online store.
The requirements are clear to the business

However, they don’t know much about what the necessities of the backend might be.
So this is our task.

We are free to architect and design the backend system in the best way possible.
For now, they only need to sell guitars. Though, in the future, they want to add other products.

Here are the requirements we have got from them:
 * A guest user should be able to:

    – register into the system with a unique username and password.
    
    – login into the system given some valid credentials.

    – see all the guitar catalog as well as to search per brand.

* A registered user should be able to:
    
    – add products to the shopping cart.

    – remove products from the shopping cart.

    – modify the quantity of a particular product in the shopping cart.
    
    – check out the shopping cart, which involves:
        
        * sending the user Id and cart to an external payment system (see below).
  
        * persisting order details including the Payment Id.

– list existing orders as well as retrieving a specific one by Id.

– log out of the system.

* An admin user should be able to:  
  – add brands.

  – add categories.

  – add products to the catalog. – modify the prices of products.

• The frontend should be able to:

– consume data using an HTTP API that we need to define.