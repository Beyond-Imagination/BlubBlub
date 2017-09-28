#  뻐끔뻐끔(IoT어항과 물고기비서)
<hr/>

## 개요
> 뻐끔뻐끔은 IoT어항과 물고기 비서가 결합된 프로젝트로 라즈베리파이가 부착된 어항을 통해 자동화된 어항관리와 챗봇과 문자열 파싱을 이용하여 사용자와 소통하는 물고기를 만드는 프로젝트 입니다.

## 필요장비
> 라즈베리파이3 1대
> 5pin 충전기(라즈베리파이용) 1개  
> led 2개 (p110 슬림 LED바 사용)  
> 12V 어댑터 1개(LED용)  
> 스테퍼 모터 1개 (28BYJ-48 사용)  

> 아두이노 우노 1대  
> 수중 온도 센서 1개 (DS18B20 사용)  
> 수중 탁도 센서 1개 (SEN0189 사용)  
> 조도 센서 1개 (CDS사용)  
> LCD 1개 (1602 Character LCD 1개)  
> 웹캠 1개 (SPC-A1200MB 사용)  

> 고정 ip 2개  
> 서버용 컴퓨터 1대  
> 3D 프린터  

> 어항  
> 물고기

## SW 개발 환경
>+ 라즈베리파이
>>+ 운영체제 : 라즈비안
>>+ 사용언어 : Python 3.4.2, Django 1.11.4
>>+ 기타 사용프로그램 : MJPG-STREAMER

>+ 아두이노
>>+ 라이브러리 : Arduino-Temperature-control-Library-Master, LiquidCrystal-I2C-Master, OneWire-Master

>+ 챗봇서버
>>+ 운영체제 : 윈도우10
>>+ 사용언어 : Python
>>+ 라이브러리 : Pyfcm 1.4.2, Konlpy 0.4.4, Beautiful soup 4.6.0

>+ 안드로이드
>>+ 운영체제 : 윈도우10, 안드로이드 6.0
>>+ 개발언어 : Java, Android
>>+ 라이브러리 : FCM 10.0.1, Google Calender API v3-rev254-1.22.0
  

## 설치방법
> 1. 어항설치
> 2. 아두이노에 각종 센서(온도,조도,탁도)와 LCD를 연결
> 3. 라즈베리파이와 아두이노를 연결
> 4. 카메라를 라즈베리파이에 연결
> 5. 라즈베리파이에 Django웹서버와 MJPG-STREAMER파일 설치
> 6. 서버용 컴퓨터에 챗봇서버 프로그램 설치
> 7. 스마트폰에 뻐끔뻐끔 애플리케이션 설치

## License
> Copyright (c) 2017, Beyond_Imagination  
> This file is licenced under a Creative Commons license: 
> http://creativecommons.org/licenses/by/2.5/ 
