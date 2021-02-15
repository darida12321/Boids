class Boid{
  static final float RANGE = 100;
  static final float SPEED = 5;
  static final float STEER_MAX = 0.05;

  PVector pos;
  float angle;
  float steer;
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
    if(toPos.dist(pos) < 1){ return; }
    steerTo(atan2(toPos.y - pos.y, toPos.x - pos.x));
  }

  PVector getPosComparedTo(Boid other){
    float x = pos.x, y = pos.y;
    if(abs(x-600 - other.pos.x) < abs(x - other.pos.x)){ x -= 600; }
    if(abs(x+600 - other.pos.x) < abs(x - other.pos.x)){ x += 600; }
    if(abs(y-600 - other.pos.y) < abs(y - other.pos.y)){ y -= 600; }
    if(abs(y+600 - other.pos.y) < abs(y - other.pos.y)){ y += 600; }

    return new PVector(x, y);
  }

  void update(){
    steer = max(-STEER_MAX, min(steer, STEER_MAX));
    angle += steer; steer = 0;

    pos.x += cos(angle)*SPEED;
    pos.y += sin(angle)*SPEED;

    if(pos.x < 0){ pos.x = 600; }
    if(600 < pos.x){ pos.x = 0; }
    if(pos.y < 0){ pos.y = 600; }
    if(600 < pos.y){ pos.y = 0; }
  }

  void display(boolean range){
    pushMatrix();
    translate(pos.x,pos.y);rotate(angle);

    // Draw boid
    fill(col); stroke(0); strokeWeight(1);
    triangle(15, 0, -10, 10, -10, -10);

    // Draw range
    if(range){
      noFill();
      ellipse(0, 0, 2*RANGE, 2*RANGE);
    }

    popMatrix();
  }
}
