# 2022-08-otus-spring-kafka-stream-traced
OTUS репозиторий для проекта по курсу Разработчик на Spring Framework

ДИЕВ АНДРЕЙ НИКОЛАЕВИЧ

Разработчик на Spring Framework

spring-2022-08

Сервисы:  
Сервис 1: contract-service - сервис для работы с договорами  
Сервис 2: branch-processor - сервис маршрутизации сообщений по договору в соответствующие статусу сервисы обработки  
Сервис 3: contract-check-service - сервис проверки договора  
Сервис 4: contract-correcting-service - сервис корректировки договора, непрошедшего проверку
Сервис 5: contract-executing-service - сервис обработки договора  
Сервис 6: contract-confirmed-service - сервис постобработки подтвержденного договора    
Сервис 7: contract-confirmed-service - сервис постобработки аннулированного договора  
Сервис 8: tracing-spring-boot-starter - стартер трассировки    
Сервис 9: services-common-models - библиотека общих моделей процесса обработки договора 



SWAGGER:
http://localhost:8080/api/swagger-ui/index.html

UI:
cd contract-service\library-ui
npm start
http://localhost:3000/

KAFKA + ZIPKIN
022-08-otus-spring-kafka-stream-traced\materials\run-with-compose.bat
http://localhost:9411/zipkin/
brokers: "http://localhost:9092"