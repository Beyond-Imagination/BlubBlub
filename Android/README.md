# 뻐끔뻐끔(Android)
<hr />

## 개요
>뻐끔뻐끔은 IoT어항과 물고기 비서가 결합된 프로젝트로 라즈베리파이가 부착된 어항을 통해 자동화된 어항관리와 챗봇과 문자열 파싱을 이용하여 사용자와 소통하는 물고기를 만드는 프로젝트 입니다.

## 시작하기
<hr/>

>뻐끔뻐끔(Android)는 여러 스마트폰에서 하나의 어항을 관리할 수 있도록 만들었습니다.

### 전제조건
>어항과 뻐끔뻐끔 모듈이 서로 연결되어있어야하며, 랜선을 통해 인터넷과 연결이 되어있어야 합니다.

### 설치
>1. Google Play Store 접속
>2. '뻐끔뻐끔'을 입력
>3. 어플리케이션 다운로드
>4. 설치된 어플리케이션 실행


## Running test
>1. 어항 확인
>>+ 어항 상태를 확인하기위해 웹캠을 이용하여 어항을 촬영.
>>+ 촬영된 정보를 웹페이지를 통해 스트리밍. 
>>+ 스트리밍 된 정보를 webview를 통해 출력

>2. 어항 상태 데이터 수신 및 출력
>>+ 일정 주기마다 어항 상태에 대한 데이터가 웹페이지에 update.
>>+ 같은 주기로 해당 웹페이지에 대한 정보를 획득. 
>>+ 정보를 얻고 UI에 반영하기 위해 AsyncTask를 사용.
>>+ 필요한 데이터를 추출하여 UI에 반영.

>3. 상태 관리 설정값 조정
>>+ 어종, 환경 등 여러 요소에 따라, 관리 방법이 다름. 
>>+ 어항의 상태에 따라 위험 메시지를 보내주는 곳은 챗봇서버이므로, 상태 관리 설정값이 조정되면 챗봇 서버로 전송.

>4. 대화 메시지 챗봇 서버로 전송
>>+ 단순히 어항을 보는 것은 지루하기에 대화 시스템을 사용.
>>+ 채팅창에 메시지를 입력하면 해당 메시지가 챗봇 서버로 전송.
>>+ 전송된 메시지에따라 FCM을 통해 결과 수신.
>>+ 수신된 데이터를 채팅창에 출력

>5. 유저 일정 확인
>>+ 채팅창에 특정 명령어 입력.
>>+ 구글 서비스에 사용자 인증 확인.
>>+ 구글 캘린더 api를 통해 정보 수신.
>>+ 수신된 정보를 출력.

>6. 유저 일정 등록
>>+ 채팅창에 특정 명령어 입력.
>>+ 구글 서비스에 사용자 인증 확인.
>>+ 입력된 message에서 일정 정보 획득.
>>+ google calendar api 양식에 맞게 포맷.
>>+ 포맷된 정보를 전송. 

>7. 상태 위험 경고 FCM 수신
>>+ 얻은 상태데이터를 통해 어항에 생긴 문제를 확인.
>>+ 챗봇 서버에서 해당 문제 정보가 담긴 메시지를 FCM을 통해 전송.
>>+ 전송된 메시지를 알람을 통해 수신.

>8. 상태에 따른 작업 요청
>>+ 챗봇 서버에서 전송된 FCM 수신.
>>+ 수신된 정보의 type에 따라 url을 변경하여 웹요청.

## 제작
>+ 1.안드로이드 스튜디오 6.0
>+ 2.FCM 10.0.1
>+ 3.Google Calender API v3-rev254-1.22.0 

## 버전관리
>우리는 버전관리를 위해 GitHub를 사용하였으며, 확인하시려면 repository의 tag를 보세요.

## ChangeLog
> ### 2017.08.02
>> UI update

>> Merge branch 'gangjung/03_1/update'

> ### 2017.08.04
>> add SettingActivity

> ### 2017.08.18 /
>> 03/FCM Clear

> ### 2017.08.21 
>> Basic UI Clear

> ### 2017.09.10 
>> Connect with webservver and recieve data

> ### 2017.09.25 
>> FCM,Google Calendar connect finish

>> Merge branch '13/gangjung/APIconnect'

>> Code Value를 잘 확인하자

>> FCM Data receive at Backgroung-state

> ### 2017.09.27
>> chatting system and FCM(Background) and UI design clear

>> Merge branch '15/gangjung/ChattingSystem'

> ### 2017.09.28
>> Final test, (modify)caculate total score

>> Merge branch '19/gangjung/finaltest'

>> Arrangement code

## THANKS
>+ 백필준
>+ 최용주
>+ 구글링

## BUGS
> 버그가 발견되면 해당 버그 현상을 cru6548@gmail.com 리포트해주시면 됩니다.

## 저자
>박예훈 - Beyond_Imagination-고려대학교(세종)-https://github.com/gangjung

## License
> Copyright (c) 2017, Beyond_Imagination
> This project is licenced under a Creative Commons license: http://creativecommons.org/licenses/by/2.5/
