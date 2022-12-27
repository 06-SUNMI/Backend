# 에브리 헬스 (Every Health) 1



## Description

> 2022.09.01 - 2022.12.12

### Contests

📌 2022 아주대학교 SoftCon 장려상

📌 2022 아주대학교 SoftCon 인기상

<br>

### Summary


* 운동 루틴을 Todo List 형식으로 관리

* 헬스인들을 위한 SNS 기능

* 의지력이 부족한 사람들을 위한 운동 챌린지

  <br>

  <br>

## About Project
<img src="https://img.shields.io/badge/spring-6DB33F?style=for-the-badge&logo=spring&logoColor=white"><img src="https://img.shields.io/badge/mysql-4479A1?style=for-the-badge&logo=mysql&logoColor=white"><img src="https://img.shields.io/badge/flutter-02569B?style=for-the-badge&logo=flutter&logoColor=white"><img src="https://img.shields.io/badge/amazonaws-232F3E?style=for-the-badge&logo=amazonaws&logoColor=white"><img src="https://img.shields.io/badge/github-181717?style=for-the-badge&logo=github&logoColor=white">
  <img src="https://img.shields.io/badge/git-F05032?style=for-the-badge&logo=git&logoColor=white">


### 루틴관리


* ToDoList 형식으로 운동의 수행 여부를 확인

* 운동의 목록을 보고 원하는 운동을 선택하여 추가 가능

* 자신이 수행한 운동의 기록을 확인 가능


  <br>

### 챌린지 참가


* 운영자가 등록한 챌린지를 앱 사용자가 참여가능

* 챌린지에 참여하고 완료시 상금 수령 가능

* 챌린지 수행 중 인증과 신고를 통해서 공정한 챌린지 진행 가능



  <br>

### SNS


* SNS의 피드 작성과 댓글 기능을 통해서 다른 사용자들과 소통 가능

* 사용자의 이름 또는 등록한 헬스장이름을 검색해 다른 사용자를 검색 가능 


  <br>


### 관리자 페이지

* 관리자용 페이지를 이용하여 챌린지 등록, 완료 가능

* 챌린지 인증의 신고목록 관리와 해당 사용자 제제 가능

* SNS 글의 신고글과 해당 사용자 제재 가능



  <br>


### 프로젝트 파일 구조

* github/workflows : ci / cd를 위한 workflow를 정의

* scripts : 빌드된 파일을 복사 후 실행하는 과정을 정의

* appspec.yml : 빌드된 파일을 저장할 EC2 서버 내 경로를 정의 

* src : 
    * src/main/java/capstone/everyhealth에 실제 프로그램의 동작에 필요한 config, controller, domain, service, repository, exception을 폴더화 한 후 각 폴더에 세부 내용들을 저장<br>
   <pre> 1) config : AWS S3, KAKAO LOCAL API, SWAGGER 관련 config 정의 <br> 2) domain : db에 매핑될 entity 정의 <br> 3) controller : client 및 외부 API의 HTTP 메시지를 수신 및 응답하기 위한 controller 정의 <br> 4) service : 비즈니스 로직 정의 <br> 5) repository : db와의 데이터 전달 정의 <br> 6) exception : 예외 정의 </pre><br>
    * src/main/resources에 관리자 페이지 렌더링에 필요한 html, css, js 파일을 저장
