/*
 * Rabbit.java
 *
 * Created on 10 March 2008, 22:36
 *
 */

package eco.livingthing;

import java.util.*;
import eco.*;

/**
 *
 * @author Setup
 */
public class Rabbit extends Animal implements Herbivore {

   /** Creates a new instance of Rabbit */
   public Rabbit(Position position) {
      super(position, 10, 60, 30);

      Set<String> food = new HashSet<String>();
      food.add("Grass");

      this.setFoodSpecies(food);
      this.setEnergyLevel(2);
   }
}
