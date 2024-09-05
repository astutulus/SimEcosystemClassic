/*
 * EcoSystem.java
 *
 * Created on 05 July 2008, 09:55
 *
 */

package eco;

/**
 *
 * @author Robin
 */
public class EcoSystem {
   /* ------------------------ VARIABLES ----------------------- */

   public static final int MAX_SIM_SPEED = 5;
   private static int simSpeed;

   /* ---------------------- CONSTRUCTORS ---------------------- */

   public EcoSystem() {
      setSimSpeed(0);

      EcoSystemGUI run = new EcoSystemGUI();
      new Thread(run).start();
   }

   /* --------------------- ACCESSOR METHODS ------------------- */

   public static int getSimSpeed() {
      return simSpeed;
   }

   public static void setSimSpeed(int s) {
      if ((s >= 0) && (s <= MAX_SIM_SPEED)) {
         simSpeed = s;
      }
   }

   /* -------------------- PROGRAM ENTRY POINT ----------------- */

   public static void main(String[] args) {
      new EcoSystem();
   }
}
