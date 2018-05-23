<h2>Maven Components</h2>

<h3>parent</h3>
spring boot starter parent
allows the project to inherit sensible defaults from Spring Boot

		<parent>
		    <groupId>org.springframework.boot</groupId>
		    <artifactId>spring-boot-starter-parent</artifactId>
		    <version>1.3.3.RELEASE</version>
		</parent>

<h3>Dependancies</h3>
Spring Boot Web Starter
Starter for building web, including RESTful, applications using Spring MVC. Uses Tomcat as the default embedded container.

		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-web</artifactId>
		</dependency>

spring jdbc
Provides the JdbcTemplate to handle resource acquisition, connection management, exception handling, and general error checking to simplify database communication with the application.

	<dependency>
			<groupId>org.springframework</groupId>
			<artifactId>spring-jdbc</artifactId>
	</dependency>

mySQL connector java
A database driver

		<dependency>
			<groupId>mysql</groupId>
			<artifactId>mysql-connector-java</artifactId>
			<version>5.1.26</version>
		</dependency>

h2
A cloud database driver

		<dependency>
			<groupId>com.h2database</groupId>
			<artifactId>h2</artifactId>
		</dependency>

Spring boot starter data jpa
Spring Data JPA simplifies the data access layer with is the ability to create repository implementations automatically from a repository interface at runtime.

		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-data-jpa</artifactId>
		</dependency>

spring cloud cloudfoundry connector
Spring Cloud Services uses the Spring Cloud Cloud Foundry Connector, which discovers services bound to applications running in Cloud Foundry, to connect client applications to the Service Registry. The connector inspects Cloud Foundry’s VCAP_SERVICES environment variable to detect available services. VCAP_SERVICES stores connection and identification information for service instances that are bound to Cloud Foundry applications.

		<dependency>
			<groupId>org.springframework.cloud</groupId>
			<artifactId>spring-cloud-cloudfoundry-connector</artifactId>
		</dependency>

spring cloud spring service connector
is used to create cloud datasource beans. It can be configured to connect to cloud services

		<dependency>
			<groupId>org.springframework.cloud</groupId>
			<artifactId>spring-cloud-spring-service-connector</artifactId>
		</dependency>


jackson-datatype-hibernate4
The tutorial Spring 3.1, Hibernate 4 and Jackson-Module-Hibernate have a good solution for Spring 3.1 and earlier versions. But since version 3.1.2 Spring have his own MappingJackson2HttpMessageConverter with almost the same functionality as the one in the tutorial, so we don't need to create this custom HTTPMessageConverter.
With javaconfig we don't need to create a HibernateAwareObjectMapper too, we just need to add the Hibernate4Module to the default MappingJackson2HttpMessageConverter that Spring already have and add it to the HttpMessageConverters of the application, so we need to:
Extend our spring config class from WebMvcConfigurerAdapter and override the method configureMessageConverters.
On that method add the MappingJackson2HttpMessageConverter with the Hibernate4Module registered in a previus method.

<dependency>
	<groupId>com.fasterxml.jackson.datatype</groupId>
	<artifactId>jackson-datatype-hibernate4</artifactId>
	<version>2.7.3</version>
</dependency>

<h3>resources</h3>
Specifies the resources folder that contains the front end assets 
application.properties is used to configure connection information to the cloud services, such as instance name, url, username, and password.

		<resources>
			<resource>
				<directory>src/main/resources</directory>
				<includes>
					<include>application.properties</include>
				</includes>
			</resource>
		</resources>

<h3>Plugins</h3>
Maven war plugin
Spring boot will always make a jar, but cloud foundry only takes a war
To force the project to build a war add <packaging> war in the pom.xml at the project declaration. Also add the maven war plugin in the plugins section.

	<groupId>com.cooksys</groupId>
	<artifactId>fasttrackd-maps</artifactId>
	<version>0.1.0</version>
	<packaging>war</packaging>

			<plugin>
				<artifactId>maven-war-plugin</artifactId>
				<version>2.6</version>
			</plugin>

cf maven plugin
The Cloud Foundry Maven plugin allows you to deploy and manage applications with Maven goals, it provides access to the Cloud Foundry cf command-line tool.


			<plugin>
				<groupId>org.cloudfoundry</groupId>
				<artifactId>cf-maven-plugin</artifactId>
				<version>1.0.0.M1</version>
			</plugin>

spring boot maven plugin
Provides compatability between maven and spring boot

			<plugin>
				<groupId>org.springframework.boot</groupId>
				<artifactId>spring-boot-maven-plugin</artifactId>
			</plugin>


