#! /bin/bash
mvn clean package
java -jar modules/swagger-codegen-cli/target/swagger-codegen-cli.jar generate -i swagger.json -l com.wordnik.swagger.codegen.languages.OAuthJavaClientCodegen -o oauthio
cd oauthio
mvn clean install
cd ../testapp
mvn clean package