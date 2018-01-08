# ScreenshotCallback

[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg?style=flat-squ)](https://opensource.org/licenses/Apache-2.0) ![API](https://img.shields.io/badge/API-19%2B-green.svg) [![1.0](https://jitpack.io/v/rbague/ScreenshotCallback.svg)](https://jitpack.io/#rbague/ScreenshotCallback) [![GitHub issues](https://img.shields.io/github/issues/rbague/ScreenshotCallback.svg)](https://github.com/rbague/ScreenshotCallback/issues)
ScreenshotCallback is a library to let the app know when the user has taken a screenshot.
Have a look at the [javadocs](https://rbague.github.io/ScreenshotCallback/).

### Set-up
**1. Gradle dependency**
Add it in your project-level `build.gradle` at the end of repositories:
```gradle
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}
```
Add the dependency in yout app-level `build.gradle`:
```gradle
dependencies {
    compile 'com.github.rbague:ScreenshotCallback:v1.0'
}
```

**2. Maven**
Add it in the repositories section of your project's `pom.xml`:
```xml
<repositories>
    ...
	<repository>
	    <id>jitpack.io</id>
	    <url>https://jitpack.io</url>
	</repository>
</repositories>
```
Add it in the dependencies section of your project's `pom.xml`:
```xml
<dependency>
    ...
    <groupId>com.github.rbague</groupId>
    <artifactId>ScreenshotCallback</artifactId>
    <version>v1.0</version>
</dependency>
```

### Usage
Add this permission to `AndroidManifest.xml`:
```xml
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE"/>
```
Example of listening for screenshots while an activity is being shown:
```java
public class MainActivity extends AppCompatActivity {
    private ScreenshotObserver mObserver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        mObserver = new ScreenshotObserver() {
            @Override
            public void onScreenshotTaken(String path) {
                //Your code here
            }
        };
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        if (mObserver != null) {
            //Must be called or mObserver won't receive any calls
            mObserver.startListnening(); 
        }
    }
    
    @Override
    protected void onPause() {
        super.onPause();
        if (mObserver != null) {
            mObserver.stopListening();
        }
    }
}
```
