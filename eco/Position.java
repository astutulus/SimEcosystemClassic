package eco;

/**
 * Position.java
 *
 * Strictly generic utility class for handling positions.
 *
 * @author Robin Wootton
 */
public class Position {
   /* ------------------------ VARIABLES ----------------------- */

   private double xPos, yPos;

   /* ---------------------- CONSTRUCTORS ---------------------- */

   /**
    * Creates a new instance of Position
    * at the specified X, Y.
    */
   public Position(double x, double y) {
      setXPos(x);
      setYPos(y);
   }

   /* --------------------- ACCESSOR METHODS ------------------- */

   public double getXPos() {
      return xPos;
   }

   public void setXPos(double xPos) {
      this.xPos = xPos;
   }

   public double getYPos() {
      return yPos;
   }

   public void setYPos(double yPos) {
      this.yPos = yPos;
   }

   /* -------------------------- METHODS ------------------------ */

   /**
    * Returns the distance between current position and another position
    */
   public double distanceTo(Position p) {
      double xDiff = this.getXPos() - p.getXPos();
      double yDiff = this.getYPos() - p.getYPos();

      return Math.hypot(xDiff, yDiff);
   }
}
