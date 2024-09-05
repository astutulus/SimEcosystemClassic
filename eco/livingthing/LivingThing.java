
package eco.livingthing;

import java.util.*;
import eco.*;

// A concept, not an actual category to be used, so "abstract"
public abstract class LivingThing implements Runnable {
   /* ------------------------ VARIABLES ----------------------- */

   // All animals and plants in the ecosystem:

   private static Set<LivingThing> livingThings = new HashSet<LivingThing>();

   // Characteristics of every living thing:

   private Calendar birthday;
   private int lifespan;
   private double mass, typicalMass;
   private Position position;

   /* ---------------------- CONSTRUCTORS ---------------------- */

   /**
    * Ultimate constructor for all living things.
    */
   protected LivingThing(Position position,
         double mass, double maxMass, int lifespan) {
      this.setPosition(position);
      this.setMass(mass);
      this.setTypicalMass(maxMass);
      this.setLifespan(lifespan);

      this.birth();
   }

   /* -------------------- ACCESSOR METHODS -------------------- */

   public synchronized static Set<LivingThing> getAll() {
      return livingThings;
   }

   public Position getPosition() {
      return position;
   }

   public void setPosition(Position position) {
      this.position = position;
   }

   public double getMass() {
      return mass;
   }

   public void setMass(double mass) {
      this.mass = mass;
   }

   public double getTypicalMass() {
      return typicalMass;
   }

   public void setTypicalMass(double maxMass) {
      this.typicalMass = maxMass;
   }

   public Calendar getBirthday() {
      return birthday;
   }

   public void setBirthday(Calendar birthday) {
      this.birthday = birthday;
   }

   public int getLifespan() {
      return lifespan;
   }

   private void setLifespan(int lifespan) {
      this.lifespan = lifespan;
   }

   /* ---------------------- USEFUL METHODS ------------------ */

   public synchronized void birth() {
      this.setBirthday(Calendar.getInstance());
      livingThings.add(this);
   }

   public synchronized void death() {
      livingThings.remove(this);
   }

   public double getX() {
      return position.getXPos();
   }

   public double getY() {
      return position.getYPos();
   }

   public String species() {
      return this.getClass().getSimpleName();
   }

   public double distanceTo(LivingThing lt) {
      return this.getPosition().distanceTo(lt.getPosition());

   }

   /* --------------------- ANALYSIS METHODS ------------------- */

   public int age() {
      Calendar now = Calendar.getInstance();

      int secsOld;
      int minsOld;
      int hoursOld;

      secsOld = now.get(Calendar.SECOND) - this.getBirthday().get(Calendar.SECOND);
      minsOld = now.get(Calendar.MINUTE) - this.getBirthday().get(Calendar.MINUTE);
      hoursOld = now.get(Calendar.HOUR_OF_DAY) - this.getBirthday().get(Calendar.HOUR_OF_DAY);

      if (secsOld < 0) {
         minsOld--;
         secsOld += 60;
      }

      if (minsOld < 0) {
         hoursOld--;
         minsOld += 60;
      }

      return (hoursOld * 60 * 60) + (minsOld * 60) + secsOld;
   }

   public String toString() {
      StringBuilder info = new StringBuilder();

      info.append(this.getClass().getSimpleName());
      info.append("\nMass: " + this.getMass() + " / " + this.getTypicalMass());
      info.append("\nAge: " + this.age() + " / " + this.getLifespan());

      return info.toString();
   }

   public synchronized static int quantityOfSpecies(String species) {
      int count = 0;
      for (LivingThing lt : LivingThing.getAll()) {
         if (lt.species().equals(species)) {
            count++;
         }
      }
      return count;
   }

   /**
    * Given an arbitary sub-list of LivingThings,
    * returns the closest to the given Position.
    */
   public synchronized static LivingThing nearestToPosition(Set<LivingThing> setLT, Position p) {
      LivingThing nearest = null;
      double distanceToNearest = -1;
      for (LivingThing lt : setLT) {
         if ((distanceToNearest == -1) ||
               (p.distanceTo(lt.getPosition()) < distanceToNearest)) {
            nearest = lt;
            distanceToNearest = p.distanceTo(lt.getPosition());
         }
      }
      return nearest;
   }

   /* -------------- LIVING THING BEHAVIOR METHODS ------------- */

   public void grow() {
      if (this.getMass() < this.getTypicalMass()) {
         this.setMass(this.getMass() + 2);
      }
   }

   /* -------------------- THREADING METHODS ------------------- */

   /*
    * What happens when the thread starts.
    * Overridden by subclasses.
    */
   public void run() {
   }

   /*
    * Used to simulate things not happening immediately.
    * Inherited and used by subclasses.
    */
   public void delay(int time) {
      try {
         Thread.sleep(time);
      } catch (InterruptedException e) {
         System.out.println("delay exception " + e.getMessage());
      }
   }
}
