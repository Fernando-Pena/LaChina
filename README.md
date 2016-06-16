
repositories {
    maven { url "https://jitpack.io" }
}

dependencies {
  compile 'com.github.Fernando-Pena:LaChina:v1.3'
}


//Plays LaChina with max volume (overriding mute/vibration/low volume)
LaChina.play(Context);


//Sets an OnClicklistener with LaChina.Play() inside OnClick of View
LaChina.cinificar(View);
