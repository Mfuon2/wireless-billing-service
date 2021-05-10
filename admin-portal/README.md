# Vuka Wireless Admin Dashboard 


#### Getting started
root/parent directory is **vuka-wireless-billing**

- change directory to `admin-portal`
- run _`yarn dockerize`_  

#### Explanation on What Happens during yarn dockerize
**The following will run these commands** 

` yarn install && ng ng build --delete-output-path --optimization --progress --aot --build-optimizer && cd ../mpesa-gateway && mvn clean package && cd ../ && docker-compose up --build --force-recreate`

**What it does**
1. Install packages , build the admin portal and optimizes it for deployment 
   
    i.e `yarn install && ng build --delete-output-path --optimization --progress --aot --build-optimizer`

2. changes directory to mpesa-gateway cleans and recreates the backed packaging ( prepares the *.jar file used to deploy)
   
    i.e `cd ../mpesa-gateway && mvn clean package` 

3. Changes directory back to root directory **vuka-wireless-billing** and executes deployment to docker 
   
    i.e `cd ../ && docker-compose up --build --force-recreate`


##### NOTE: This process automates the building stage for packages both on admin portal and mpesa-gateway and generates the build artifacts for both applications

- mpesa-*.jar (mpesa-gateway)
- dist/admin-portal (admin-portal)
