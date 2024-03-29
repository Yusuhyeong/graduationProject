# 사용자 플레이리스트를 통한 매칭(채팅)어플
사용자들의 노래 취향 유사도를 통해 매칭을 하여 채팅할 수 있는 어플리케이션

## 개발 환경
- OS : Window
- IDE : Android Studio Giraffe
- SDK : API 33 : Android 9.0(pie)
- 언어 : java, python

## 사용 기술
- ### retrofit2 (java <-> python 통신)
1. python의 계산을 java와 통신하기 위해 retrofit2 구현
2. json데이터를 통한 통신
3. Callback을 통한 비동기 처리

- ### firebase
1. 사용자 데이터를 저장하기 위한 데이터베이스 구축
2. 해당 사용자들의 플레이리스트에 대한 유사도 계산
3. realtime database를 통해 채팅기능 구현

- ### 활용 api
1. maniaDB를 통한 노래 데이터 사용
2. 해당 api에서 가져올 수 있는 이미지 url, 곡 관련 데이터 수집

- ### View
1. ListView
2. RecyclerView
3. ViewPager2

## 개발 내역
- 2022.03.09 ~ 2022.03.31 : 프로젝트 요구사항 분석 및 스토리보드 작성
- 2022.04.01 ~ 2022.04.14 : 스토리보드에 따른 간단한 프로토타입 제작 // 안드로이드
- 2022.04.01 ~ 2022.04.14 : 샘플 데이터를 통한 자카드, 코사인 유사도 구현 // 파이썬
- 2022.04.31 ~ 2022.04.31 : 프로토타입에 사용자 데이터를 넣어 데이터 흐름 파악 // 파이어베이스
- 2022.05.01 ~ 2022.05.07 : 구체적인 UI제작 // 안드로이드
- 2022.06.01 ~ 2022.06.31 : python과 java의 통신을 위한 서버 구축 및 retrofit2 구현
- 2022.09.05 ~ 2022.09.15 : maniaDB를 통해 사용자 데이터 추가 및 기능 설정 // 파이어베이스
- 2022.09.16 ~ 2022.09.30 : 직접적인 java와 python의 통신을 확인
- 2022.10.01 ~ 2022.10.03 : 상세 UI개선 및 테스트
- 2022.10.04 ~ 2022.10.10 : 프로젝트 문서 수정 및 발표자료 작성
