# MadCamp Week2: Holly Molly

### 팀원

> 편장욱, 류수현

### Summary

> 할리갈리 게임을 안드로이드 Real-Time Multiplayer 게임으로 구현하였음.
>
> <b>Frontend</b>는 Android Studio에서 Kotlin언어로 개발
>
> <b>Backend</b>는 Visual Studio Code에서 Javascript언어로 개발. NodeJS에서 Express, Socket.io, MongoDB를 이용했음.

## Main Screen

> Sign up, Log in 기능을 구현하였음. 카카오SDK를 활용해 카카오 계정 또한 연동이 됨.
> 
> 회원가입시 id 중복 불가. 동일한 아이디로 동시 접속 불가능하도록 구현하였음.


## Waiting Room

> 사용자 누적 점수 및 해당 Tier 확인가능.
>
> 방 생성시 참가 인원 (1~4명), 진행될 턴 수를 기입.
>
> SwipeRefreshLayout을 통해 대기방 리스트 update 가능.


## Game

> 덱(deck)의 수가 0이 되는 참가자, 혹은 정해둔 턴이 모두 지났을 때 남은 덱의 수가 가장 작은 참가자가 패배하며 패배한 사람은 누적점수 -10, 나머지는 +10점이 된다.
>
> 턴은 3초마다 한번씩 자동으로 넘어가며 펼쳐진 카드 중 동일한 과일의 합이 5가 되는 상황에서 벨을 울리면 펼쳐진 모든 카드가 벨을 울린 사람의 덱에 추가된다.
>
> 참가자들 모두 자신의 폰에서 실시간으로 동일한 정보를 보며, 벨을 먼저 울리는 사람을 정하는 기준은 클라이언트 자체에서의 시간차를 통해 계산하기 때문에 통신 속도의 영향을 적게 받는다.
