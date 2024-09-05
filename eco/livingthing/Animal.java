/*
 * Animal.java
 *
 * Created on 06 April 2008, 10:57
 *
 */

package eco.livingthing;

import java.util.*;
import eco.*;

/**
 *
 * @author Robin
 */
public abstract class Animal extends LivingThing {
   /* ------------------------ VARIABLES ----------------------- */

   private Set<String> foodSpecies;
   private Set<LivingThing> foodSeen;
   private Set<String> predatorSpecies;
   private Set<LivingThing> predatorsSeen;

   private int energyLevel;
   private int maxSpeed;

   /* ---------------------- CONSTRUCTORS ---------------------- */

   /**
    * Constructor with one argument for species name.
    */
   public Animal(Position position, double mass, double maxMass, int lifespan) {
      super(position, mass, maxMass, lifespan);

      setFoodSpecies(new HashSet<String>());
      setFoodSeen(new HashSet<LivingThing>());

      setPredatorSpecies(new HashSet<String>());
      setPredatorsSeen(new HashSet<LivingThing>());
   }

   /* -------------------- ACCESSOR METHODS -------------------- */

   public Set<String> getFoodSpecies() {
      return foodSpecies;
   }

   public void setFoodSpecies(Set<String> foodSpecies) {
      this.foodSpecies = foodSpecies;
   }

   public Set<LivingThing> getFoodSeen() {
      updateFoodSeen();
      return foodSeen;
   }

   public void setFoodSeen(Set<LivingThing> foodSeen) {
      this.foodSeen = foodSeen;
   }

   public Set<String> getPredatorSpecies() {
      return predatorSpecies;
   }

   public void setPredatorSpecies(Set<String> predatorSpecies) {
      this.predatorSpecies = predatorSpecies;
   }

   public Set<LivingThing> getPredatorsSeen() {
      updatePredatorsSeen();
      return predatorsSeen;
   }

   public void setPredatorsSeen(Set<LivingThing> predatorsSeen) {
      this.predatorsSeen = predatorsSeen;
   }

   public int getEnergyLevel() {
      return energyLevel;
   }

   public void setEnergyLevel(int energyLevel) {
      this.energyLevel = energyLevel;
   }

   public int getMaxSpeed() {
      return maxSpeed;
   }

   public void setMaxSpeed(int maxSpeed) {
      this.maxSpeed = maxSpeed;
   }

   /* ----------------- ANIMAL BEHAVIOR METHODS ---------------- */

   public void updateFoodSeen() {
      Set<LivingThing> found = new HashSet<LivingThing>();

      for (LivingThing lt : LivingThing.getAll()) {
         if (this.getFoodSpecies().contains(lt.species())) {
            if (this.distanceTo(lt) < 10000) {
               found.add(lt);
            }
         }
      }
      this.setFoodSeen(found);
   }

   public void updatePredatorsSeen() {
      Set<LivingThing> found = new HashSet<LivingThing>();

      for (LivingThing lt : LivingThing.getAll()) {
         if (this.getPredatorSpecies().contains(lt.species())) {
            if (this.distanceTo(lt) < 500) {
               found.add(lt);
            }
         }
      }
      this.setPredatorsSeen(found);
   }

   public void consumeFood(LivingThing lt) {
      lt.setMass(lt.getMass() - 1);
      this.setMass(this.getMass() + 1);
   }

   /**
    * The less heavy, the more hungry
    */
   public int hungerPercent() {
      return (int) (this.getTypicalMass() - this.getMass());// Math.abs(this.energyLevel - 100);
   }

   /**
    * The more predators known about, the more fear felt.
    * Ten is as bad as anything more than ten.
    */
   public int fearPercent() {
      return this.predatorsSeen.size();
   }

   public void solveHunger() {
      if (getFoodSeen().size() > 0) {
         LivingThing nearestFood = nearestFood();

         moveTowards(nearestFood, false);
         if (at(nearestFood)) {
            consumeFood(nearestFood);
         }
      }
   }

   // Not sophisticated -- need to make good for circles not squares
   public boolean at(LivingThing lt) {
      double xDiff = (lt.getX() - this.getX());
      double yDiff = (lt.getY() - this.getY());

      return ((Math.abs(xDiff) < ((this.getMass() / 2) + (lt.getMass() / 2)))
            &&
            (Math.abs(yDiff) < ((this.getMass() / 2) + (lt.getMass() / 2))));
   }

   public void solveFear() {
      if (getPredatorsSeen().size() > 0) {
         moveTowards(nearestPredator(), true);
      }
   }

   public void moveTowards(LivingThing lt, boolean reverseDirection) {
      double xDiff = (lt.getX() - this.getX());
      double yDiff = (lt.getY() - this.getY());

      if (reverseDirection) {
         this.setPosition(new Position(
               this.getX() - (xDiff * EcoSystem.getSimSpeed()) / 100,
               this.getY() - (yDiff * EcoSystem.getSimSpeed()) / 100));
      } else {
         if (!at(lt)) {
            this.setPosition(new Position(
                  this.getX() + (xDiff * EcoSystem.getSimSpeed()) / 100,
                  this.getY() + (yDiff * EcoSystem.getSimSpeed()) / 100));
         }
      }
      delay(50);
   }

   public LivingThing nearestFood() {
      return LivingThing.nearestToPosition(getFoodSeen(), getPosition());
   }

   public LivingThing nearestPredator() {
      return LivingThing.nearestToPosition(getPredatorsSeen(), getPosition());
   }

   /* -------------------- THREADING METHODS ------------------- */

   /*
    * Will go towards food or evade predators,
    * depending on which is most important.
    */
   public void run() {
      while (age() < getLifespan()) {
         if (hungerPercent() > fearPercent()) {
            solveHunger();
         } else {
            solveFear();
         }
         // TO DO grow; reproduce
      }
      death();
   }
}
