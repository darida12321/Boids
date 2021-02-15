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



class Boid{
  float spd = 5;
  float steer = 0;
  float steerMax = 0.05f;

  PVector pos;
  float angle;
  int col;

  Boid(){
    pos = new PVector(random(600), random(600));
    angle = random(2*PI);
    col = color(random(255), random(255), random(255));
  }

  public void steerTo(float toAngle, float weight){
    float diff = toAngle - angle;
    while(diff < -PI){ diff += 2*PI; }
    while(diff >  PI){ diff -= 2*PI; }

    steer += diff*weight;
  }
  public void steerTo(PVector toPos, float weight){
    steerTo(atan2(toPos.y - pos.y, toPos.x - pos.x), weight);
  }

  public void update(){
    steer = max(-steerMax, min(steer, steerMax));
    angle += steer; steer = 0;


    pos.x += cos(angle)*spd;
    pos.y += sin(angle)*spd;

    if(pos.x < 0){ pos.x = 600; }
    if(600 < pos.x){ pos.x = 0; }
    if(pos.y < 0){ pos.y = 600; }
    if(600 < pos.y){ pos.y = 0; }
  }

  public void display(){
    fill(col); stroke(0); strokeWeight(1);

    pushMatrix();
    translate(pos.x,pos.y);rotate(angle);
    triangle(15, 0, -10, 10, -10, -10);
    popMatrix();
  }
}

Boid boid;

public void setup(){
  
  frameRate(60);

  boid = new Boid();
  boid.pos = new PVector(300, 300);
  boid.angle = PI/2;
}

public void draw(){
  boid.steerTo(new PVector(mouseX, mouseY), 0.3f);
  boid.update();

  background(200);
  boid.display();
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
