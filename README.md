![header](https://capsule-render.vercel.app/api?type=waving&color=000000&height=300&section=header&text=ER%20Info&fontSize=90&fontColor=7FEBFF)


# 트러블슈팅
- 중복체크기능 구현문제
   
  - 내용
    - 회원가입 시 기존 DB에 저장된 닉네임과 동일하다면 사용불가하다는 textview를 띄워줘야하나 중복된 닉네임이 있어도 사용가능하다는 text가 생성됨
  - 원인
    - DB에 저장된 모든 닉네임의 중복체크 결과를 거치기전에 TEXT를 반환하는 메서드가 실행됨
  - 해결방법
    - 닉네임에 중복체크 메서드와 TEXT출력 메서드를 통합하여 로직문제를 해결함

   
- 프로필사진 업데이트 문제
   
  - 내용
    - 프로필 사진 변경 시 이전 사진이 그대로 노출되는 문제
  - 원인
    - 프로필 변경 시 Storage에 업로드가 완료되기전에 패치 메서드가 실행되어 이전 이미지를 가져옴
  - 해결방법
    - 기존에는 업로드, 패치 메서드를 별개로 두었으나 업로드 성공 시 패치 메서드를 실행하여 문제를 해결함


<br>

# 기술의사결정
- FireBase(DB)
  - 요구사항
    - 어플 내 데이터 저장을 위해 데이터베이스가 필요했다
  - 선택지
    - Firebase
    - Room
  - 의견조율
    - Firebase는 서버 없이도 DB 기능을 제공해주는 서비스로 일정 사용량까지 무료로 사용이 가능하다
    - Room은 로컬데이터베이스로서 앱이 오프라인 상태에도 데이터에  접근이 가능하다 단 데이터를 여러 기기간에 동기화 하거나
다중 사용자 간에 실시간으로 공유해야하는 경우에는 서버가 필요하다
  - 결정
    - 채팅, 듀오찾기, 게시판 기능을 위해서 서버를 구현해야하
지만 구현할 시간이 부족할 것 같아 Firebase를 선택하였습니다

<br>

# 🎧 팀 소개

- 팀명 :  **이리온나**

- 팀 노션 : **[[Notion]](https://www.notion.so/7ed4416e04c644568de39205605569d7)**

| 이름   | 역할 | MBTI        | BLOG                                               | GITHUB                                                  | 
| ------ | ---- | ---------- | -------------------------------------------------- | -------------------------------------------------------- |
| 이호식 | 팀장 | ENFP        | [qhj6068(Owler).velog](https://velog.io/@ghj6068)  | [hosiker](https://github.com/hosiker)                |
| 양윤혁 | 팀원 | ESTJ        | [Yangdriod.tistory](https://yangdriod.tistory.com/)       | [yangyunhyeok](https://github.com/yangyunhyeok) |
| 장재용 | 팀원 | ISFP        | [choco5732.log.velog](https://velog.io/@choco5732)       | [choco5732](https://github.com/choco5732) |
| 추지연 | 팀원 | ISFP        | [jiyeon-tistory.tistory](https://jiyeon-tistory.tistory.com/)       | [Ji-Yeon-98](https://github.com/Ji-Yeon-98) |



<br>

# 📽️ 프로젝트
- **Team Repository** : **[[EternalReturnInfo]](https://github.com/EternalReturnInfo/EternalReuturnInfo)**

- 주제 : 온라인 게임 Eternal Return 에 대한 정보를 제공하고 공유하는 커뮤니티 앱


<br>

# 💡 설계

- **WireFrame** : **[[Figma]](https://www.figma.com/file/XuJjkieTkTy1Oz63AeLL5e/TEAM18_%EC%9D%B4%EB%A6%AC%EC%98%A8%EB%82%98?type=design&node-id=0-1&mode=design&t=uo3ZA4LfYOYDcF15-0)**

![image](https://github.com/EternalReturnInfo/EternalReuturnInfo/assets/69956389/35c34389-8879-440f-8cc6-f85a257e1f66)

<br>


# ☑️ 주요 기능

### ⭐ 1. 로그인 & 회원가입
- 일반 로그인 기능
- 비밀번호 찾기 기능
- 회원가입 기능



### ⭐ 2. 메인 화면


- 전적 검색 기능
- 공식 홈페이지 공지사항 확인
- 관련 유튜브 영상 확인


### ⭐ 3. 듀오 찾기 화면
- 듀오 찾기 CRUD
- 채팅 기능 연결

### ⭐ 4. 게시판 화면

- 게시글, 댓글 CRUD
- 검색 기능
- 새로고침 기능

### ⭐ 5. 채팅 화면
- 메시지 전달, 확인
- 안 읽은 메시지, 채팅 시간 표시
- 채팅 목록에 마지막 메시지 출력

### ⭐ 6. 프로필 화면
- 게임 통계, 내가 쓴 게시글 확인
- 프로필 사진 변경, 실험체(캐릭터) 수정, 비밀번호 변경 기능
- 로그아웃, 회원 탈퇴 기능


<br>

# 📹 실행 영상

| Login | MAIN |
| -------- | -------- |
| <img src="https://github.com/EternalReturnInfo/EternalReuturnInfo/blob/feature/board2/img/login.gif" width="380" height="800"> | <img src="https://github.com/EternalReturnInfo/EternalReuturnInfo/blob/feature/board2/img/main.gif" width="380" height="800"> |

| FindDuo | Board |
| -------- | -------- |
| <img src="https://github.com/EternalReturnInfo/EternalReuturnInfo/blob/feature/board2/img/findduo.gif" width="380" height="800"> | <img src="https://github.com/EternalReturnInfo/EternalReuturnInfo/blob/feature/board2/img/board.gif" width="380" height="800"> |

| Chat | Myprofile |
| -------- | -------- |
| ![chat](https://github.com/EternalReturnInfo/EternalReuturnInfo/assets/69956389/75b696e9-7216-4dd4-a095-54f09da53dda) | <img src="https://github.com/EternalReturnInfo/EternalReuturnInfo/blob/feature/board2/img/myprofile.gif" width="380" height="800"> |


<br>

# 📚 STACKS

### Environment ###
<img src="https://img.shields.io/badge/Androidstudio-3DDC84?style=for-the-badge&logo=androidstudio&logoColor=white"> <img src="https://img.shields.io/badge/Figma-F24E1E?style=for-the-badge&logo=figma&logoColor=white"> <img src="https://img.shields.io/badge/Github-181717?style=for-the-badge&logo=github&logoColor=white"> <img src="https://img.shields.io/badge/Google Play-414141?style=for-the-badge&logo=googleplay&logoColor=white"> 

### Development ###
<img src="https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=Android&logoColor=white"> <img src="https://img.shields.io/badge/Kotlin-7F52FF?style=for-the-badge&logo=Kotlin&logoColor=white"> 
<img src="https://img.shields.io/badge/Firebase-FFCA28?style=for-the-badge&logo=firebase&logoColor=white">

### Communication ###
<img src="https://img.shields.io/badge/Slack-4A154B?style=for-the-badge&logo=slack&logoColor=white"> <img src="https://img.shields.io/badge/Notion-000000?style=for-the-badge&logo=notion&logoColor=white">

