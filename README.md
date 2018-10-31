# esb-mng-console

exlink_dsl은 camel 기반의 ESB의 db to file 및  file to db로  데이터를 이관할 수 있는  coustom component 입니다. 
<!--more-->

exlink_dsl을 이용하여 camel기반 ESB로 수신한 데이터를 설정 파일 기반으로 효과적으로 변환 및 저장 처리 할 수 있습니다. 

## feature

 - data format configuration generating based on excel 
 - database schema generation based on data format configuration
 - byte file format parsing
 - json file format parsing 
 - byte file data saving on database
 - json file data saving on database 
 - file parsing post process adaptor 
 - json format message receiving rest service adaptor 

  
## Building from the source

 1. jdk 8 이상 설치 
 2. apache ant 설치
 3. exlink_dsl 프로젝트 저장소에서 소스를 clone 혹은 다운르도 (https://github.com/torpedocorp/exlink_dsl)
 4. 커맨드 라인상에서 ant build 수행

## Architecture
  - spring 4.0
  - JDBC
  - GSON
  - JDOM
  - POI