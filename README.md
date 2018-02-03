# securityservice

### What is this repository for? ###
Security service for applications. To use it in individual application, you can add this module
as dependency. You can configure the application access urls by using ``synectiks.shiro.secure.urls`` property.
In this property you can configuration security with role and permission access rule, Its a
multivalued property so you can specify comma separated list of these rules in ``application.properties`` file with following format:

	{"url"*: "/api/v1/auth", "authc"*: true, "roles": "role1, role2", "permissions": "permis1, permis2"}

In case you would like to exclude any url from secure path, add it into ``synectiks.shiro.public.urls`` property in following format:

	{"url"*: "/api/v1/public/**", "authc"*: false}

### How to import project for editing ###

* Import as maven project in your IDE

### Build, install and run application ###

To get started build the build the latest sources with Maven 3 and Java 8 
(or higher). 

	$ cd security
	$ mvn clean install 

You can run this application as spring-boot app by following command:

	$ mvn spring-boot:run

Once done you can run the application by executing 

	$ java -jar target/security-exec.jar

### Configuration parameters for application run ###
	Key	       		Default Value
	---------------------------------------
	SERVER_PORT		8094
	PSQL_HOST	 		localhost
	PSQL_PORT	 		5432
	PSQL_DB   		synectiks
	PSQL_PSWD	 		xxxxxx

## Application api's documentation ##

### /security/public/login ###

Api to get authenticate user with username and password.

	Method: POST | GET
	Params:
		username*		String
		password*		String
		rememberMe	boolean
		redirectTo	String url to redirect on success
	Response:
		Success	authentication response

### /security/public/signup ###

Api to get authenticate user with LoginRequest object.

	Method: POST
	Params:
		request*	LoginRequest object
	Response:
		Success	authentication response

### /security/public/signin ###

Api to get authenticate user with User object.

	Method: POST
	Params:
		request*	User object
	Response:
		Success	authentication response

### /security/public/authenticate ###

Api to get authenticate user with UsernamePasswordToken object.

	Method: POST
	Params:
		token*		UsernamePasswordToken object
	Response:
		Success	authentication response

### /security/public/logout ###

Api to get authenticate user with UsernamePasswordToken object.

	Method: POST | GET
	Response:
		Success

### /security/permissions/listAll ###

Api to all permission objects.

	Method: POST | GET
	Response:
		{} Json list of Permission objects

### /security/permissions/create ###

Api to create new permission objects.

	Method: POST
	Params:
		service Json permission object
	Response:
		{} Json of Permission objects

### /security/permissions/{id} ###

Api to fetch permission objects by id.

	Method: POST
	Response:
		{} Json of Permission objects

### /security/permissions/delete/{id} ###

Api to delete a permission objects by id.

	Method: POST
	Response:
		Success message

### /security/permissions/update ###

Api to update a permission by json objects.

	Method: POST
	Params:
		entity Json permission object
	Response:
		{} Json of Permission objects

### /security/permissions/delete ###

Api to delete a permission by json object.

	Method: POST
	Params:
		entity Json permission object
	Response:
		Success

### /security/roles/listAll ###

Api to all Role objects.

	Method: POST | GET
	Response:
		{} Json list of Role objects

### /security/roles/create ###

Api to create new Role objects.

	Method: POST
	Params:
		service Json Role object
	Response:
		{} Json of Role objects

### /security/role/{id} ###

Api to fetch Role objects by id.

	Method: POST
	Response:
		{} Json of Role objects

### /security/roles/delete/{id} ###

Api to delete a Role objects by id.

	Method: POST
	Response:
		Success message

### /security/roles/update ###

Api to update a Role by json objects.

	Method: POST
	Params:
		entity Json Role object
	Response:
		{} Json of Role objects

### /security/roles/delete ###

Api to delete a Role by json object.

	Method: POST
	Params:
		entity Json Role object
	Response:
		Success

### /security/users/listAll ###

Api to all User objects.

	Method: POST | GET
	Response:
		{} Json list of User objects

### /security/users/create ###

Api to create new User objects.

	Method: POST
	Params:
		service Json User object
	Response:
		{} Json of User objects

### /security/users/{id} ###

Api to fetch User objects by id.

	Method: POST
	Response:
		{} Json of User objects

### /security/users/delete/{id} ###

Api to delete a User objects by id.

	Method: POST
	Response:
		Success message

### /security/users/update ###

Api to update a User by json objects.

	Method: POST
	Params:
		entity Json User object
	Response:
		{} Json of User objects

### /security/users/delete ###

Api to delete a User by json object.

	Method: POST
	Params:
		entity Json User object
	Response:
		Success

### Who do I talk to? ###
	Please mail us on
	info@syenctiks.com
