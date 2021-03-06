# Bears
버스정류장에서 시각장애인이 원하는 버스를 탑승할 수 있도록 돕는 APP

- 사용기술: AWS, Android, Git, Java, MySQL, Node.js
- 진행 기간: 2021/06/28 → 2021/08/26
- 팀 구성: Android 3명, Server 2명
- 한 줄 소개: 시각장애인 버스 탑승 어플(Bus+Ears)

## 📖 상세 내용

**버스 번호의 음성인식과 정확한 버스 도착 정보 제공 및 버스기사의 시각장애인 탑승정보 파악**

**이를 통해 시각 장애인이 비장애인의 도움 없이도 원하는 버스를 타고 내릴 수 있을 것으로 예상**

**시각장애인의 대중교통 접근성 높이는 것을 목표로 함**

![Untitled](https://user-images.githubusercontent.com/55080677/139180978-c15651c0-7080-43bd-ae23-6337fec147ab.png)


**시각장애인에게 실시간 버스 정보를 제공하는 어플로, 사용자의 편의성을 높이기 위해 음성인식을 이용하며  사용자의 위치를 기반으로 자동으로 가까운 버스정류장의 위치를 파악함**

**버스에 블루투스(비콘)을 부착하여 어플과 통신을 주고 받음. 사용자가 음성으로 버스 번호를 입력한 뒤, 해당 버스가 일정 거리 내에 들어오면 진동으로 알림을 주어 직접적인 탑승을 도움**

**시각장애인이 탑승 알림을 누르면 버스기사에게 전달하여 버스기사가 시각장애인의 탑승정보를 확인 할 수 있음**
![bears_구조](https://user-images.githubusercontent.com/55080677/139181092-76fa4825-c654-4f71-be03-c97544d11628.png)

## 🔧 사용 기술 및 라이브러리

- 버스 공공 데이터 OPEN API
- 아두이노 블루투스
- Google STT, TTS Api
- Retrofit2, 소켓, BLE 통신
- Room

## Backend
- node.js
- express 모듈
- socket.io 모듈
- AWS ec2, RDS 활용했으며 MYSQL로 디비 개발

## Beacon
![비콘](https://user-images.githubusercontent.com/55080677/139181370-f2682873-2d43-458a-ba5d-99139ada8192.png)

