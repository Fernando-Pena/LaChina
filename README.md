#LaChina

[China preview](https://clyp.it/j4j003gx)

## Import

1. in `build.gradle` add:
```
    repositories {
        maven { url "https://jitpack.io" }
    }
    
    dependencies {
        compile 'com.github.Fernando-Pena:LaChina:v1.3'
    }
```

##Usage

`LaChina.play(Context)` plays `LaChina` with the max possible volume (overriding mute/vibration/low volume)


`LaChina.chinificar(View)` sets an `OnClicklistener` in `View` with `LaChina.Play()` inside the `OnClick`
