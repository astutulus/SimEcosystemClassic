/*
 * Fox.java
 *
 * Created on 03 July 2008, 22:47
 *
 */

package eco.livingthing;

import java.util.*;
import eco.*;

/**
 *
 * @author Robin
 */
public class Fox extends Animal implements Carnivore {

   /** Creates a new instance of Fox */
   public Fox(Position position) {
      super(position, 20, 100, 360);

      Set<String> food = new HashSet<String>();
      food.add("Rabbit");

      this.setFoodSpecies(food);
   }
}
