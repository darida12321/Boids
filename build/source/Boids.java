import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class Boids extends PApplet {


Flock flock;

public void setup(){
  
  frameRate(60);

  flock = new Flock(20);
}

public void draw(){
  flock.update();

  background(200);
  flock.display(false);
}
class Boid{
  static final float RANGE = 100;
  static final float SPEED = 5;
  static final float STEER_MAX = 0.05f;

  PVector pos;
  float angle;
  float steer;
  int col;

  Boid(){
    pos = new PVector(random(600), random(600));
    angle = random(2*PI);
    col = color(random(255), random(255), random(255));
  }

  public void steerTo(float toAngle){
    float diff = toAngle - angle;
    while(diff < -PI){ diff += 2*PI; }
    while(diff >  PI){ diff -= 2*PI; }

    steer += diff;
  }
  public void steerTo(PVector toPos){
    if(toPos.dist(pos) < 1){ return; }
    steerTo(atan2(toPos.y - pos.y, toPos.x - pos.x));
  }

  public PVector getPosComparedTo(Boid other){
    float x = pos.x, y = pos.y;
    if(abs(x-600 - other.pos.x) < abs(x - other.pos.x)){ x -= 600; }
    if(abs(x+600 - other.pos.x) < abs(x - other.pos.x)){ x += 600; }
    if(abs(y-600 - other.pos.y) < abs(y - other.pos.y)){ y -= 600; }
    if(abs(y+600 - other.pos.y) < abs(y - other.pos.y)){ y += 600; }

    return new PVector(x, y);
  }

  public void update(){
    steer = max(-STEER_MAX, min(steer, STEER_MAX));
    angle += steer; steer = 0;

    pos.x += cos(angle)*SPEED;
    pos.y += sin(angle)*SPEED;

    if(pos.x < 0){ pos.x = 600; }
    if(600 < pos.x){ pos.x = 0; }
    if(pos.y < 0){ pos.y = 600; }
    if(600 < pos.y){ pos.y = 0; }
  }

  public void display(boolean range){
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
class Flock{
  ArrayList<Boid> boids;

  Flock(int n){
    boids = new ArrayList<Boid>();
    for(int i = 0; i < n; i++){
      boids.add(new Boid());
    }
  }

  public ArrayList<Boid> neighbourhood(Boid boid){
    ArrayList<Boid> out = new ArrayList<Boid>();
    for(Boid b : boids){
      if(b == boid){ continue; }

      if(boid.getPosComparedTo(b).dist(b.pos) < Boid.RANGE){
        out.add(b);
      }
    }
    return out;
  }
  public PVector awayFromNeighbours(Boid b, ArrayList<Boid> neighbours){
    PVector away = b.pos.copy();
    for(Boid n : neighbours){
      PVector diff = n.getPosComparedTo(b).copy().sub(b.pos);
      float mag = 50/max(0.001f, diff.mag());
      away.add(diff.normalize().mult(-mag));
    }
    return away;
  }
  public PVector toNeighbours(Boid b, ArrayList<Boid> neighbours){
    PVector sum = new PVector(0, 0);
    for(Boid n : neighbours){
      sum.add(n.getPosComparedTo(b));
    }
    return sum.mult(1/max(1, neighbours.size()));
  }
  public float neighboursDirection(ArrayList<Boid> neighbours){
    float sum = 0;
    for(Boid n : neighbours){
      sum += n.angle;
    }
    return sum/max(1, (float)neighbours.size());
  }

  public void update(){
    for(Boid b : boids){
      ArrayList<Boid> neighbours = neighbourhood(b);

      b.steerTo(awayFromNeighbours(b, neighbours));
      b.steerTo(toNeighbours(b, neighbours));
      b.steerTo(neighboursDirection(neighbours));
      b.update();
    }
  }

  public void display(boolean range){
    for(Boid b : boids){
      b.display(range);
    }
  }
}
  public void settings() {  size(600, 600); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "Boids" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
