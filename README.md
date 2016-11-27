# Quick Action 
[![License](https://img.shields.io/badge/License-Apache%202.0-red.svg)](http://www.apache.org/licenses/LICENSE-2.0)
[![Build Status](https://travis-ci.org/piruin/quickaction.svg?branch=master)](https://travis-ci.org/piruin/quickaction) 
[![Download](https://api.bintray.com/packages/blazei/maven/QuickAction/images/download.svg)](https://bintray.com/blazei/maven/QuickAction/_latestVersion)
[![Download](https://jitpack.io/v/piruin/quickaction.svg)](https://jitpack.io/#piruin/quickaction)

Quick Action is a small android library for easy create Tooltips with some action or
just as decoration. folk from [NewQuickAction3D] by [Lorensius W. L. T].

> Not just a Tooltips, This a Tooltips with Action!.

Because *NewQuickAction3D* is design of Android 2.x. So, I change it's style to fit with Material Design
but still compatible with old java source code interface, Refactor, Transform to Gradle project
and publish to JCenter.

## Demo

![Quick Action demo][demo]

## Download

### [JCenter]

- **Step 1** - set JCenter repository (This step not require for modern android project)
- **Step 2** - Add dependencies on app module

```groovy
dependencies {
  compile 'me.piruin:quickaction:LATEST_VERSION'
}
```
Change `LATEST_VERSION` to latest version name

### [JitPack]

- **Step 1** - Set JitPack repository

```groovy
allprojects {
  repositories {
      ...
      maven { url "https://jitpack.io" }
    }
  }
```

- **Step 2** - Add dependencies on app module

```groovy
dependencies {
  compile 'com.github.piruin:quickaction:LATEST_VERSION'
}
```
Change `LATEST_VERSION` to latest version name

### Maven Central

No, I'm not publish to Maven Central

## How to Use

See [SampleActivity] for Information how to use

QuickAction almost use same old [NewQuickAction3D] interface. Original ExampleActivity by [Lorensius W. L. T]
still work and cover main part of it.
You can get more information at his blog http://www.londatiga.net/it/how-to-create-quickaction-dialog-in-android/

## New Feature

My QuickAction have some additional feature more than original

### Set Popup's Color & Text Color

```java
  //Popup color
  quickAction.setColorRes(R.color.pink) //set by Color Resource
  quickAction.setColor(Color.WHITE) // or by Color class

  //Text color
  quickAction.setTextColorRes(R.color.red)
  quickAction.setTextColor(Color.Black)
```

NOTE! setTextColor apply only ActionItem that added afterward.

### Quick Intent Action

To lazy create list of Activity or Service that match with your Intent

```java
  Intent intent = new Intent(Action.VIEW) // intent your want to start
  QuickAction quickIntent = new QuickIntentAction(this)
    .setActivityIntent(intent)
    .create();
```

## Developer By

- [Piruin Panichphol]

### Original Developer

- [Lorensius W. L. T] 

## Changes

See [CHANGELOG] for details

## License

This project under [Apache 2.0](http://www.apache.org/licenses/LICENSE-2.0) license

### Notice

Project/File | License | Copyright |
--- | --- | ---|
[NewQuickAction3D] | Apache 2.0 | Copyright 2011 Lorensius W. L. T |
[ArrowDrawable.java] | MIT | Copyright (c) 2016. Viнt@rь


[JCenter]: https://bintray.com/bintray/jcenter
[JitPack]: https://jitpack.io/
[CHANGELOG]: https://github.com/piruin/quickaction/blob/master/CHANGELOG.md
[SampleActivity]: https://github.com/piruin/quickaction/blob/master/quickaction-sample/src/main/java/me/piruin/quickaction/sample/SampleActivity.java
[NewQuickAction3D]: https://github.com/lorensiuswlt/NewQuickAction3D
[Piruin Panichphol]: https://piruin.me
[Lorensius W. L. T]: http://www.londatiga.net/
[demo]: https://github.com/piruin/quickaction/blob/master/asset/demo.gif "Demo gif"
[ArrowDrawable.java]: https://github.com/ViHtarb/Tooltip/blob/master/library/src/main/java/com/tooltip/ArrowDrawable.java
