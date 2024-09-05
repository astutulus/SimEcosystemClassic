/*
 * PositionEco.java
 *
 * Created on 19 October 2008, 19:24
 *
 */

package eco;

import java.util.*;

/**
 * Extension of Position class, with some useful things needed by SIM_Ecosystem
 *
 * @author Robin
 */
public class PositionEco extends Position {
   /* ------------------------ VARIABLES ----------------------- */

   private static Random rnd = new Random();

   public static final double MIN_X = 0;
   public static final double MIN_Y = 0;

   public static final double MAX_X = 1000;
   public static final double MAX_Y = 1000;

   /* ---------------------- CONSTRUCTORS ---------------------- */

   public PositionEco(double x, double y) {
      super(x, y);
   }

   /* -------------------- ACCESSOR METHODS -------------------- */

   /*
    * Unlike the superclass, we now check if the proposed co-ordinates are
    * within bounds
    */
   public void setXPosChecked(double xPos) throws BadCoordinateException {
      if ((xPos >= MIN_X) && (xPos <= MAX_X)) {
         setXPos(xPos);
      } else {
         throw new BadCoordinateException(" \"x\" coordinate too large or small: " + xPos);
      }
   }

   public void setYPosChecked(double yPos) throws BadCoordinateException {
      if ((yPos >= MIN_Y) && (yPos <= MAX_Y)) {
         setYPos(yPos);
      } else {
         throw new BadCoordinateException(" \"y\" coordinate too large or small: " + yPos);
      }
   }

   /* -------------------------- METHODS ------------------------ */

   /**
    * Might well be neded to add computer control, in a gaming scenario
    */
   public static PositionEco randomPositionLivingThing() {

      double randomX = ((rnd.nextDouble() * (MAX_X - MIN_X)) + MIN_X);
      double randomY = ((rnd.nextDouble() * (MAX_Y - MIN_Y)) + MIN_Y);

      return new PositionEco(randomX, randomY);
   }
}
