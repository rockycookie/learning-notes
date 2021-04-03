## Run a Maven project with everything inside one folder
Example: https://github.com/ataylor284/saml-example

1. Clone the repo, and `cd` to the directory
2. `mvn clean install`, which create jar file in `./target`
    ```
    $ jar -tf target/saml-example-1.0.0.jar
    META-INF/MANIFEST.MF
    META-INF/
    ca/
    ca/redtoad/
    META-INF/maven/
    META-INF/maven/ca.redtoad/
    META-INF/maven/ca.redtoad/saml-example/
    home.html
    trust.keystore
    ca/redtoad/WebServer.class
    META-INF/maven/ca.redtoad/saml-example/pom.xml
    META-INF/maven/ca.redtoad/saml-example/pom.properties
    ca/redtoad/Login.class
    ca/redtoad/Logout.class
    ca/redtoad/Home.class
    ca/redtoad/Secure.class
    ca/redtoad/Main.class
    ca/redtoad/SAML2ClientBuilder.class
    ```
3. `mvn dependency:copy-dependencies`, which copy all dependencies to `./target/dependency`
    ```
    $ ls target/dependency/
    animal-sniffer-annotations-1.14.jar  javax.annotation-api-1.2.jar	  metrics-core-3.1.2.jar	     opensaml-xmlsec-impl-3.4.0.jar
    bcprov-jdk15on-1.59.jar		     javax.servlet-api-3.1.0.jar	  opensaml-core-3.4.0.jar	     pac4j-core-3.7.0.jar
    checker-compat-qual-2.0.0.jar	     jcl-over-slf4j-1.7.25.jar		  opensaml-messaging-api-3.4.0.jar   pac4j-saml-3.7.0.jar
    commons-codec-1.10.jar		     jcommander-1.48.jar		  opensaml-messaging-impl-3.4.0.jar  serializer-2.7.2.jar
    commons-collections-3.2.2.jar	     jetty-http-9.4.18.v20190429.jar	  opensaml-profile-api-3.4.0.jar     slf4j-api-1.7.25.jar
    commons-io-2.6.jar		     jetty-io-9.4.18.v20190429.jar	  opensaml-profile-impl-3.4.0.jar    spring-core-5.0.2.RELEASE.jar
    commons-lang-2.4.jar		     jetty-security-9.4.18.v20190429.jar  opensaml-saml-api-3.4.0.jar	     spring-jcl-5.0.2.RELEASE.jar
    cryptacular-1.2.1.jar		     jetty-server-9.4.18.v20190429.jar	  opensaml-saml-impl-3.4.0.jar	     stax2-api-3.1.4.jar
    error_prone_annotations-2.1.3.jar    jetty-servlet-9.4.18.v20190429.jar   opensaml-security-api-3.4.0.jar    velocity-1.7.jar
    guava-23.6-jre.jar		     jetty-util-9.4.18.v20190429.jar	  opensaml-security-impl-3.4.0.jar   woodstox-core-5.0.3.jar
    httpclient-4.5.3.jar		     joda-time-2.9.9.jar		  opensaml-soap-api-3.4.0.jar	     xalan-2.7.2.jar
    httpcore-4.4.8.jar		     jsr305-1.3.9.jar			  opensaml-soap-impl-3.4.0.jar	     xml-apis-1.3.04.jar
    j2objc-annotations-1.1.jar	     logback-classic-1.1.3.jar		  opensaml-storage-api-3.4.0.jar     xmlsec-2.0.10.jar
    java-support-7.4.0.jar		     logback-core-1.1.3.jar		  opensaml-xmlsec-api-3.4.0.jar      xmlsectool-2.0.0.jar
    ```
4. Run it
    ```
    $ java -cp target/dependency/ -jar target/saml-example-1.0.0.jar
    ```
