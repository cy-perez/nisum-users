#TODO: Validar si existe
#!/usr/bin/env bash
./gradlew sonarqube \
  -Dsonar.projectKey=clean-service-base \
  -Dsonar.organization=sofka \
  -Dsonar.host.url=https://sonarcloud.io \
  -Dsonar.login=c3904b0fa05b609c122332094d0ba05be994d304