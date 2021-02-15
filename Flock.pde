class Flock{
  ArrayList<Boid> boids;

  Flock(int n){
    boids = new ArrayList<Boid>();
    for(int i = 0; i < n; i++){
      boids.add(new Boid());
    }
  }

  ArrayList<Boid> neighbourhood(Boid boid){
    ArrayList<Boid> out = new ArrayList<Boid>();
    for(Boid b : boids){
      if(b == boid){ continue; }

      if(boid.getPosComparedTo(b).dist(b.pos) < Boid.RANGE){
        out.add(b);
      }
    }
    return out;
  }
  PVector awayFromNeighbours(Boid b, ArrayList<Boid> neighbours){
    PVector away = b.pos.copy();
    for(Boid n : neighbours){
      PVector diff = n.getPosComparedTo(b).copy().sub(b.pos);
      float mag = 50/max(0.001, diff.mag());
      away.add(diff.normalize().mult(-mag));
    }
    return away;
  }
  PVector toNeighbours(Boid b, ArrayList<Boid> neighbours){
    PVector sum = new PVector(0, 0);
    for(Boid n : neighbours){
      sum.add(n.getPosComparedTo(b));
    }
    return sum.mult(1/max(1, neighbours.size()));
  }
  float neighboursDirection(ArrayList<Boid> neighbours){
    float sum = 0;
    for(Boid n : neighbours){
      sum += n.angle;
    }
    return sum/max(1, (float)neighbours.size());
  }

  void update(){
    for(Boid b : boids){
      ArrayList<Boid> neighbours = neighbourhood(b);

      b.steerTo(awayFromNeighbours(b, neighbours));
      b.steerTo(toNeighbours(b, neighbours));
      b.steerTo(neighboursDirection(neighbours));
      b.update();
    }
  }

  void display(boolean range){
    for(Boid b : boids){
      b.display(range);
    }
  }
}
