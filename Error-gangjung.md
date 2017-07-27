Error - gangjung

--- 2017-07-26

## 1
[문제]
```bash
액션바를 없애기 위해
android:theme="@android:style/Theme.Holo.NoActionBar"
를 사용하다.
java.lang.IllegalStateException: You need to use a Theme.AppCompat theme (or descendant) with this activity.
에러 발생
```

[해결]
```bash
1.
 사용하려는 테마는 ActionbarActivity를 extends하기 때문에 AppCompaActivity를 extends 하지말고 Activity를 extends해야한다.

2.(사용)
 styles.xml의 테마를 수정한다.
<style name="AppTheme" parent="Theme.AppCompat.Light.DarkActionBar">
...
<item name="windowActionBar">false</item>
<item name="windowNoTitle">true</item>
를 추가한다.

 3.
 Java 코드에서 
	ActionBar actionBar = getActionBar();
        actionBar.hide();
를 사용하여 숨긴다.

4.
새로운 테마를 만들어서 사용한다.

In styles.xml
<style name="AppTheme.NoActionbar">
        <item name="windowActionBar">false</item>
        <item name="windowNoTitle">true</item>
</style>

In manifests
 <activity android:name=".MainActivity"
            android:theme="@style/AppTheme.NoActionbar"
	........
```

## 2
[문제]
```bash
 xml -> Design에서 "class not found" or "can't instantiate"가 뜬다.
```

[해결]
```bash
테마가 맞지 않거나 sdk가 하위여서 맞지않아 해당 테마를 사용할 수가 없는 상황.

오류코드를 읽어보면서 java코드를 수정해도 되지만, 
대부분 테마를 알맞게 수정해주면 상황 종료.
```






##
[문제]
```bash

```

[해결]
```bash

```
