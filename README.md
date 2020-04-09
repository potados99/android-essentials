# Android Essentials

내가 쓰려고 만든 라이브러리.

## Navigation

**Android Jet-pack Navigation + Instagram style tab navigation**

### 기본 작동 원리

각 탭은 `NonSwipingViewPager`의 한 페이지를 나타낸다.

해당 View Pager의 요소는 `NavigationHostFragment` 타입의 네비게이션 호스트를 가지고 있는 `NavigationFragment`이다.

하나의 `NavigationFragment`는 다음 정보를 `argument`로부터 가지고 온다:

- 레이아웃 id
- 툴바 id
- 네비게이션 호스트 id
- 해당 프래그먼트가 할당된 `BottomNavigationView` 메뉴 item의 id
- 각 네비게이션 그래프 root destination들의 배열

### 백스택

각 탭은 고유의 네비게이션을 가진다. 뒤로 가기 이벤트가 발생하면 먼저 탭 내의 중첩 네비게이션에 해당 이벤트가 전달된다.

만약 중첩된 프래그먼트가 없는 상황이라면 탭 전환 백스택이 하나씩 pop 된다.

이때 최대 탭 갯수만큼 뒤로 버튼이 눌려야 액티비티가 종료된다.

이는 Google Play Store 애플리케이션의 동작을 모방한 것이다.

## Failable

**Global event driven error reporting system**

### 개요

어디서든 아래와 같이 쓸 수 있다.

~~~kotlin
Fail.debug(Failure("Nothing."))
Fail.usual(Failure("Something went wrong."))
Fail.wtf(Failure("What a terrible failure!"))
~~~

해당 메소드를 실행하면 Fail 전역적으로 전달된다. 기본적으로 Toast 메시지로도 출력된다.

아래 메소드를 이용해 observer를 등록한다.

~~~kotlin
Fail.observe(lifecycleOwner) { failure, channel ->
    Log.d("Fail", "$failure on $channel")
}

// Inside LifecycleOwner
onFail { failure, channel ->
    Log.d("Fail", "$failure on $channel")
}
~~~

### 사용법

Fail의 Toast가 동작하기 위해서는 Context가 필요하다.

앱의 시작 포인트에서 다음과 같이 호출해준다.

~~~kotlin
Fail.initialize(this/* Application */)
~~~