
Flock flock;

void setup(){
  size(600, 600);
  frameRate(60);

  flock = new Flock(20);
}

void draw(){
  flock.update();

  background(200);
  flock.display(false);
}
