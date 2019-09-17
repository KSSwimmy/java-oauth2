# java-oauth2

Rory's Notes (Thanks!) https://lambdaschoolstudents.slack.com/files/T4JUEB3ME/FMEA7KLAU

JX User Authentication Guided Project



Link to video: [YouTube](https://www.youtube.com/watch?v=MNDT4ExfFQ8&feature=youtu.be)



Today we cover:



How to Use custom SQL in our application



Auditing Fields - created/modified



User Authentication with Oauth2



__Opening Notes:__



 Inside of model -> User our users have a username and password, which will help set up our authentication

  

 model -> Role assigns users a role. 

  

 We are using a role based system, which will be how we manage access

  

 model -> Quotes: Users have quotes associated with them

  

 __Ways to do seed data:__

  

 i. data.sql - Put it under resources folder. Spring Data will use it to seed your database. Works from the jump.

  

 ii. repos - downside is it skips business logic

  

 iii. services - only if available, but requires mostly completed application



__/// ** BEGIN CUSTOM QUERY PORTION ** //__



I. clone repository [GitHub - LambdaSchool/java-oauth2](https://github.com/LambdaSchool/java-oauth2)



II. We have a working/prebuilt application. What are the things we need to know?

 A. What endpoints that have been exposed in this application

      i. The endpoints will be under the controllers package
    
      ii. The controller has our user interface
    
      iii. This application has QuotesController, RoleController, and UserController

  B. What's the host system it's running on **?**

 C. What port is it using

  

III. Check pom.xml file

 A. check Java version for version 11

  B. comment out <scope>runtime</scope> (when using H2 database)



IV. Add the following to the application.properties

 A. spring.h2.console.enabled=true // enables console for H2 database

 B. spring.h2.console.path=/h2-console // gives path to manipulate the H2 database 

 C. server.port=${PORT:2019} // specified which port Tomcat will use

 D. spring.jackson.serialization.FAIL_ON_EMPTY_BEANS=false //#Feature that determines what happens when no accessors are found for a type (and there are no annotations to indicate it is meant to be serialized).



V. Run the application as is



VI. Navigate to Postman

  A. We have the /users endpoint which we will check. The exact path is localhost:2019/users/users. We should be receiving all the seeded user data

  

VII. Add an endpoint

 A. Go to QuotesController

 B. Add "/quotescounts" endpoint which will give amount of quotes a given user has (1)

 C. WIll be an ArrayList of data type CountQuotes



VIII. We got ahead of ourselves. We are going to need a SQL statement that lists the users, and then counts how many quotes they have

 A. Run the application (to create the H2 database)

  B. Navigate to the H2 console at [H2 Console](http://localhost:2019/h2-console). Hit Connect with default setup

 C. When we have a custom query that uses a select, we create a view

  D. Create a new package view under authenticatedusers

  E. Create an Interface CountQuotes under the view package

    i. Naming convention must be very specific. In this case, we have String getUser(); and int getCount();

 F. Navigate to service -> QuoteService

    i. add List<CountQuotes> getCountQuotes();

 G. Navigate to service -> QuoteServiceImpl

  i. Generate Override Method

  ii. Pick the getCountQuotes method (should be only one not part of the object methods)

    iii. This will call getCountQuotes(); from the repository (which we need to create)

  H. We need to change the ArrayList<CountQuotes> in the QuotesController into a List since that is what the repository will be working with 



IX. Add custom SQL to QuoteRepository

  A. We will use the @Query annotation and give it a value of the custom SQL we wrote

 B. We have the JPA or the database run our SQL by setting nativeQuery = true

 C. Below annotation we give List<CountQuotes> getCountQuotes

  

Recap: We had a repository that returns a custom SQL that we got to name the method (getCountQuotes). We used the annotation @Query. We call that from our QuoteServiceImpl, which we had to create a method for in QuoteService. Out controller QuotesController can now call that method in the service (which we have used the @Autowire annotation to wire up) which will use the CountQuotes in the view package to format our JSON coming out.



X. Check endpoint

 A. navigate to Postman

 B. input GET: localhost:2019/quotes/quotecounts



XI. We have also created our own custom queries for inserting and deleting user roles, which can be seen in repository -> RoleRepository

  A. When we use a @Modifying annotation, we don't need to worry about creating a new interface in the view package, because nothing is getting displayed

  

__// ** END CUSTOM QUERY PORTION ** //__

__// 42:00/2:08:07 //__



__/// ** BEGIN AUDITING PORTION** //__

__// 45:00/2:08:07 //__



I. Navigate to H2 console <http://localhost:2019/h2-console>

 A. take a look at Users data

  B. We have defaulted CREATED_BY to SYSTEM and LAST_MODIFIED_BY to SYSTEM since the data has not been modified yet

 C. Notice our endpoint does not print these 

  D. We will create the model -> Auditable model

    i. this is a @MappedSuperClass. This means we will never 'instantiate' it i.e. create an object from it. We also know this because it is an abstract class.

    ii. We also have the annotation @EntityListener meaning that every time something happens to something annotated with the entity listener, Spring will check this super class to see if it needs to be updated or changed

  iii. We will see a similar pattern with Exception handling

    iv. We are working specifically with thue AuditingEntityListener class

  E. The annotations will map to the name in the database. i.e. @CreatedBy becomes CREATED_BY in the database. The rest is formatting for how we want to see and display the data

 F. This is fairly standard boilerplate. This copy and pastable in your projects.

  G. For every class that we want to use the Auditing fields in, all we have to do is extend Auditable

    i. i.e. when we public class Quote extends Auditable this means the four auditing fields we have created will be a part of the Quotes table in our database 



II. Navigate to service -> UserAuditing

 A. This is mostly boilerplate

  B. What we need to change is if we do not have User Authentication enables, the String uname can be any name

 C. Once User Authentication is implemented, we need to change it.

 D. Implementing the UserAudtiing that extends AuditorAware<String> is what assigns the name for us, and we specify how we want the name assigned

  

III. Turn on Auditing

  A. navigate to main class, in this case AuthenticatedusersApplication

  B. Make sue we have the annotation @EnableJpaAuditing above the @SpringBootApplication annotation, which itself is places above our class declaration

 C. Without this annotation, Spring does not know to do the auditing



IV. Display Auditing to Users (optional)

  A. in model -> Auditable, we do not have any setters or getters that we created

  B. Spring handles the setting via the annotations, ex. @CreatedBy

  i. If we take out the getter, we no longer see the field

 C. The getters need to be in the actual class, not the extended class

    i. Generate getters in Auditable

  ii. copy and delete

    iii. navigate to the class you want to display them to the users for, in this case, model -> Quotes

 E. If we don't want to print out the ID, we can just get rid of the getter. However, if we are using that ID getting in another part of the program, we can use a JSON.ignore

  

__Recap:__



We added the audit fields in model - Auditable (boilerplate). We extended the abstract class Auditable to the tables/entities we want audited, in this case model -> Quote as an example. We added the UserAuditing class under service package and implemented AuditorAware on that class. Finally, we enabled auditing by adding the @EnableJpaAuditing in our main class, AuthenticatedusersApplication in this case.



__/// ** END AUDITING PORTION** //__

__// 58:00/2:08:07 //__



__/// ** BEGIN USER AUTHENTICATION PORTION** //__

__// 58:00/2:08:07 //__



I. Navigate to the database console

  A. When we look at our USERROLES, we see a user can have multiple roles, and roles can be assigned to multiple users

    i. this is a Many to Many relationship

    ii. However, our code is set-up as a @ManyToOne relationship in our UserRoles class

  iii. This is in order to add Auditing fields to UserRoles

 B. We give ONE role to MANY UserRoles (@OneToMany)

 C. We give ONE user MANY UserRoles (@OneToMany)

 D. We take MANY UserRoles from ONE role (@ManyToOne/@Join)

 E. This disintermediates the Many to Many relationships down to two One to Many relationships, in order to add auditing



II. Deleting and Inserting

 A. Navigate to UserServiceImpl

 B. Because of the way we have split our DB relationships, in order to simplify updating and deleting

  i. When updating, we delete all the old roles by user ID then add all the new ones coming in on the request body

 C. This is a "business decision" we have made

  D. We are doing this with custom queries which can be found in RoleRepository

  E. Structure recap: we have users that can be assigned roles and we know what roles they have because it is stored inside the UserRoles table



III. Spring Security

  A. User Authentication is part of Spring Security. We need to add Spring Security (part of Spring Boot) into our pom.xml file

  B. Navigate to pom.xml

 C. Add the dependency. If we add it in for the main system, we need to add it in for the test system as well (footnote 2)



IV. Tell Spring Security what type of Auth we will be using (oAuth2)

 A. Add the oAuth2 dependency (footnote 3)

 B. Add in the three Jax dependencies (footnote 4 - 5 - 6)



V. Configure Security Settings

  A. Create a new package config as authenticadedusers -> config (for directory hierarchy)

 B. In order for authorization to work, we need an Authorization Server

    i. Create a new class AuthorizationServerConfig in config -> AuthorizationServerConfig

  ii. This is fairly boilerplate code

 C. We know we are creating an authorization server because we have the annotation @EnableAuthorizationServer and we know its a configuration file from the annotation @Configuration

 D. When a client wants to access our system, the Authorization Server gives them the username and password to access our system

 i. for now we have the username and password hardcoded in. Later we will move that to a environment variable

  ii. We can think of a CLIENT_ID as a username and a CLIENT_SECRET as a password

  iii. edit ACCESS_TOKEN_VALIDITY to make the token valid shorter or longer

  iv. this is generally boilerplate. The only things we really edit are the CLIENT_ID, CLIENT_SECRET, and/or ACCESS_TOKEN_VALIDITY

  E. Create a new class SecurityConfig in config -> SecurityConfig

  i. this is pretty much boilerplate code, using BCrypt

    ii. A lot of the broken links in AuthorizationServerConfig will get picked up once we insert SecurityConfig

 F. Recap so far: AuthorizationServerConfig controls the tokens, and SecurityConfig controls types of passwords, validity intervals, scope of credentials, where they will be stored etc. But we still haven't specified "Who has access to what"

  G. Create new class ResourceServerConfig in config -> ResourceServerConfig

  i. this will be the file we change the most

  ii. This is our resource server. It controls which roles have access to which endpoints



VI. Configure CORS // 1:21:00/2:08:07

 A. We need to configure CORS to allow access to our backend by a client that doesn't run on the same system. 

  B. Create a new class SimpleCorsFilter in config -> SimpleCorsFilter

  i. You can have multiple filters, in this case we have only one

  

VII. Implement User Details Service // 1:25:00/2:08:07

  A. Navigate to userServiceImpl

  B. Add UserDetailsService as an implementation

    i. AuthorizationServiverConfig will be happy now that it can find UserDetailsService but we need to build the actual interace

  C. The @Resource name, in this case "userService" on SecurityConfig in config package must match the @Service annotation in UserServiceImpl exactly

  D. Generate Overridemethods in UserServiceImpl for UserDetailsService of loadUserByUsername

  i. Add in the boilerplate code (footnote 7)

    ii. We call loadUserByUserName and give it a username. it finds the username in our database. If it doesn't find it, it throws an exception saying this is not a valid username.

  iii. If it does find the username, it returns a new User of userdetails type, which is required by Spring Security

  E. We need to pass it getAuthority, a specific method we need to implement in User.

  i. Returns a list of roles the user has access to, which is used in turn by the ResourceServer to determine if the user has access to something

  F. Navigate to model -> User



VIII. Handle Password Encryption // 1:31:00/2:08:07

 A. Passwords stored in databases should ALWAYS be encrypted. To not do so is considered highly unethical.

  B. Navigate to model -> User and add t6he BCrypt encryption (footnote 9)

  i. This allows us to bring in a plaintext password, then encrypt it before it saves it

  C. If the password is already encrypted however, we want to use it as is (encrypted) so we add a new method, setPasswordNoEncryption (footnote 10)

  D. Navigate to service -> UserServiceImpl

  i. When we go to save a user internally, we have a user we are bringing in from the outside world. It goes in as JSON and Jackson converts it to a Java object. In the process of that behind the scenes Spring encrypts the password. If we want to work with that password, we don't want to encrypt it again

    ii. Anytime we are going from JSON to Jackson (Java object) Spring will encrypt our password. So we change newUser.setPassword(user.getPassword()); to newUser.setPasswordNoEncrypt(user.getPassword()); in our save method and currentUser.setPassword(user.getPassword()); to currentUser.setPasswordNoEncrypt(user.getPassword()); in update



 IX. New Get Mapping // 1:38:00/2:08:07

  A. Navigate to controller -> UserController

  B. Create new @GetMapping endpoint "/getusername" (footnote 10)

    i. This gets returns a user object. It takes an Authentication class that is in the background from Spring Security

  ii. inside authentication, it has the username. Now that we have that, we can get all of the information specific to the user

    iii. Authentication data type is based off a token

  

X. Check Endpoints

 A. Navigate to Postman

  B. Do GET request to localhost:2019/users/users

  i. We receive back message saying not authorized

 C. To sign on using oAuth 2, we need to get an access token:

    i. Go to Authorization, change dropdown type to oAuth2

    ii. Click Get New Access Token

    iii. Choose Grant Type Password Credentials

  iv. input valid username and password

    v. input Access Token URL as localhost:2019/oauth_token

  vi. input Client ID as `lambda-client'1

    vii. input Client Secret as lambda-secret

 D. Debugging

    i. Navigate to model -> UserRoles

  ii. WE are using Lazy fetches, which is only loading up data when it needs it

    iii. Get rid of Fetch.Lazy from @ManyToOne

    iv. Navigate to UserServiceImpl and add @Transactional annotation to UserDetails loadByUsername. We are changing data, and anytime we change data we need to make it transactional



XI. Restrict Access to Admin

  A. Navigate to controller -> UserController

    i. Adding @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')") above any @GetMapping (endpoints) will restrict access to admin only