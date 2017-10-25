#  뻐끔뻐끔(IoT어항과 물고기비서) HardWare
<hr/>

## 개요
> 하드웨어부는 크게 2부분으로 나뉘는데 첫번째 부분은 아두이노에서 각종 센서값(조도,탁도,온도)을 수신하고 두번째 부분은 LED와 먹이를 주는 모터부로 나뉩니다. 라즈베리파이 부분은 다른 폴더 RaspberryPI를 참고하시기 바랍니다

## 사용장비
> 아두이노 우노 1대  
> 수중 온도 센서 1개 (DS18B20 사용)  
> 수중 탁도 센서 1개 (SEN0189 사용)  
> 조도 센서 1개 (CDS사용)  
> LCD 1개 (1602 Character LCD 1개)  

## SW 개발 환경
> 아두이노 IDE
> 라이브러리 : Arduino-Temperature-control-Library-Master, LiquidCrystal-I2C-Master, OneWire-Master

## 핀 연결방법
> A0핀. 조도센서
> A1핀. 탁도센서
> D3핀. 온도센서
> A4핀. LCD SDA핀
> A5핀. LCD SCL핀 

## 3D 프린팅
>+ 응용프로그램
>>+ 디자인 : Autodesk 123D Design
>>+ 슬라이싱 : Cubicreator v3.5
>>+ 출력 프린터 : Cubicon3dp-210F
>>+ 필라멘트 : ABS

>+ 사용법
>>+ 1. 수정이 필요하다면 BlubBlub/HardWare/3D printing/123D design에 있는 파일에서 수정한다.
>>+ 2. stl파일로 export 한다.
>>+ 3. Cubicreator로 export한 stl파일을 열어 프린터 설정값에 맞추고 채우는 정도나 출력속도를 정한다
>>+ 3. 해당 프린터기에 파일을 옮겨 프린트 한다.

