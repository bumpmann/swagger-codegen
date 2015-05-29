#! /bin/sh
cd ../oauthio
mvn clean install
cd ../testapp
mvn clean package && mvn exec:java -Dexec.mainClass=io.oauth.testapp.App