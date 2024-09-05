
package eco.livingthing;

import eco.*;

/**
 *
 * @author Robin Wootton
 */
public abstract class Plant extends LivingThing {
   /**
    * Constructor with one argument for species name.
    */
   public Plant(Position position, double mass, double maxMass, int lifespan) {
      super(position, mass, maxMass, lifespan);
   }

   public void grow() {
      if (this.getMass() < this.getTypicalMass()) {
         this.setMass(this.getMass() + 1);
      }
   }

   /*
    * Will live until it reaches a certain age.
    */
   public void run() {
      while (age() < getLifespan()) {

      }
      death();
   }
}
