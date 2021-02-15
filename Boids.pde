

class Boid{
  float spd = 5;
  float steer = 0;
  float steerMax = 0.05;

  PVector pos;
  float angle;
  color col;

  Boid(){
    pos = new PVector(random(600), random(600));
    angle = random(2*PI);
    col = color(random(255), random(255), random(255));
  }

  void steerTo(float toAngle){
    float diff = toAngle - angle;
    while(diff < -PI){ diff += 2*PI; }
    while(diff >  PI){ diff -= 2*PI; }

    steer += diff;
  }
  void steerTo(PVector toPos){
    steerTo(atan2(toPos.y - pos.y, toPos.x - pos.x));
  }

  void update(){
    steer = max(-steerMax, min(steer, steerMax));
    angle += steer; steer = 0;

    pos.x += cos(angle)*spd;
    pos.y += sin(angle)*spd;

    if(pos.x < 0){ pos.x = 600; }
    if(600 < pos.x){ pos.x = 0; }
    if(pos.y < 0){ pos.y = 600; }
    if(600 < pos.y){ pos.y = 0; }
  }

  void display(){
    fill(col); stroke(0); strokeWeight(1);

    pushMatrix();
    translate(pos.x,pos.y);rotate(angle);
    triangle(15, 0, -10, 10, -10, -10);
    popMatrix();
  }
}



Boid boid;

void setup(){
  size(600, 600);
  frameRate(60);

  boid = new Boid();
  boid.pos = new PVector(300, 300);
  boid.angle = PI/2;
}

void draw(){
  boid.steerTo(new PVector(mouseX, mouseY));
  boid.update();

  background(200);
  boid.display();
}
