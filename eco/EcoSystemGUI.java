/*
 * EcoSystemGUI.java
 *
 * Created on 03 July 2008, 21:58
 *
 */

package eco;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.EtchedBorder;
import eco.livingthing.Rabbit;
import eco.livingthing.Fox;
import eco.livingthing.Grass;
import eco.livingthing.LivingThing;

/**
 *
 * @author Robin
 */
public class EcoSystemGUI extends JFrame implements Runnable {
   /* ------------------------ VARIABLES ----------------------- */

   // Declare constants

   private int TERRAIN_TOP = 50;
   private int TERRAIN_LEFT = 50;
   private int TERRAIN_BOTTOM = 200;
   private int TERRAIN_RIGHT = 700;

   // Declare JPanels

   private Terrain terrainPanel;

   private JPanel controlPanel;

   private JPanel messagePanel;
   private JPanel buttonPanel;
   private JPanel speedPanel;

   // Declare menu components

   private JMenuBar menuBar;
   private JMenu fileMenu;
   private JMenu helpMenu;
   private JMenuItem clearMenuItem;
   private JMenuItem aboutMenuItem;

   // Declare other visual components

   private JTextArea messageArea;

   private JButton grassButton;
   private JButton rabbitButton;
   private JButton foxButton;
   private JButton smiteButton;

   private JLabel speedLabel;
   private JSlider speedSlider;

   // Declare helper variables

   private char activeTool;
   private LivingThing highlighted;

   /* ---------------------- CONSTRUCTORS ---------------------- */

   /**
    * Creates a new instance of EcoSystemGUI
    */
   public EcoSystemGUI() {
      // JFrame basics

      this.setLocation(200, 200);
      this.setSize(800, 600);
      this.setResizable(false);
      this.setTitle("SIM EcoSystem");
      this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      this.setLayout(new BorderLayout());

      // Operational initialisations

      activeTool = 'X';
      highlighted = null;

      // Create menu

      menuBar = new JMenuBar();

      fileMenu = new JMenu("File");
      helpMenu = new JMenu("Help");

      clearMenuItem = new JMenuItem("Clear");
      clearMenuItem.addActionListener(new EcoMenuListener());
      clearMenuItem.setAccelerator(KeyStroke.getKeyStroke('C', KeyEvent.CTRL_DOWN_MASK));

      aboutMenuItem = new JMenuItem("About");
      aboutMenuItem.addActionListener(new EcoMenuListener());

      fileMenu.add(clearMenuItem);
      helpMenu.add(aboutMenuItem);

      menuBar.add(fileMenu);
      menuBar.add(helpMenu);

      // Create visual components: TERRAIN PANEL

      terrainPanel = new Terrain();
      terrainPanel.setBackground(Color.GRAY);
      terrainPanel.addMouseListener(new EcoMouseClickListener());
      terrainPanel.addMouseMotionListener(new EcoMouseMoveListener());

      // Create visual components: MESSAGE PANEL

      messageArea = new JTextArea(4, 50); // rows, columns
      messageArea.setEditable(false);
      messageArea.setBorder(new EtchedBorder());

      messagePanel = new JPanel();
      messagePanel.add(messageArea);

      // Create visual components: BUTTON PANEL

      grassButton = new JButton("Grass");
      grassButton.addActionListener(new EcoButtonListener());

      rabbitButton = new JButton("Rabbit");
      rabbitButton.addActionListener(new EcoButtonListener());

      foxButton = new JButton("Fox");
      foxButton.addActionListener(new EcoButtonListener());

      smiteButton = new JButton("Smite");
      smiteButton.addActionListener(new EcoButtonListener());

      buttonPanel = new JPanel();
      buttonPanel.setBorder(new EtchedBorder());
      buttonPanel.add(grassButton);
      buttonPanel.add(rabbitButton);
      buttonPanel.add(foxButton);
      buttonPanel.add(smiteButton);

      // Create visual components: SPEED PANEL

      speedLabel = new JLabel();
      updateSpeedLabel();

      speedSlider = new JSlider(0, EcoSystem.MAX_SIM_SPEED, EcoSystem.getSimSpeed());
      speedSlider.addChangeListener(new EcoSpeedChangeListener());

      speedPanel = new JPanel();
      speedPanel.add(speedLabel);
      speedPanel.add(speedSlider);

      // Create visual components: CONTROL PANEL

      controlPanel = new JPanel();
      controlPanel.setLayout(new GridLayout(3, 1));
      controlPanel.add(messagePanel);
      controlPanel.add(buttonPanel);
      controlPanel.add(speedPanel);

      // Create visual components: JFRAME

      this.setJMenuBar(menuBar);

      this.add(terrainPanel, BorderLayout.CENTER);
      this.add(controlPanel, BorderLayout.SOUTH);

      this.setVisible(true);
   }

   /* ---------------- GUI ASSISTANCE METHODS ----------------- */

   private void renumberButtons() {
      int qGrass = LivingThing.quantityOfSpecies("Grass");
      int qRabbit = LivingThing.quantityOfSpecies("Rabbit");
      int qFox = LivingThing.quantityOfSpecies("Fox");

      if (qGrass == 0) {
         grassButton.setText("Grass");
      } else {
         grassButton.setText("Grass (" + qGrass + ")");
      }

      if (qRabbit == 0) {
         rabbitButton.setText("Rabbit");
      } else {
         rabbitButton.setText("Rabbit (" + qRabbit + ")");
      }

      if (qFox == 0) {
         foxButton.setText("Fox");
      } else {
         foxButton.setText("Fox (" + qFox + ")");
      }
   }

   private void updateSpeedLabel() {
      int currentSpeed = EcoSystem.getSimSpeed();

      if (currentSpeed == 0) {
         speedLabel.setText("Paused");
      } else {
         speedLabel.setText("Speed " + currentSpeed);
      }
   }

   private void aboutDialog() {
      JOptionPane.showMessageDialog(null, "SIM EcoSystem \n\n by Robin Wootton");
   }

   /* -------------- GUI INNER CLASSES: DRAWING ---------------- */

   private class Terrain extends JPanel {
      /**
       * Re-draws the working area
       */
      public void paintComponent(Graphics g) {
         super.paintComponent(g);

         // Paint background

         g.setColor(Color.BLACK);
         g.fillRect(TERRAIN_LEFT, TERRAIN_TOP, TERRAIN_RIGHT, TERRAIN_BOTTOM);

         // Paint dots and highlight box

         for (LivingThing lt : LivingThing.getAll()) {
            // Size of circle relates to mass of living thing.
            int diameter = (int) lt.getMass();
            int radius = (int) diameter / 2;

            // This makes the dot the right colour.
            if (lt.species().equals("Grass")) {
               g.setColor(Color.GREEN);
            } else if (lt.species().equals("Rabbit")) {
               g.setColor(Color.GRAY);
            } else if (lt.species().equals("Fox")) {
               g.setColor(Color.RED);
            }

            // This actually draws the dot.
            g.fillOval((int) lt.getX() - radius, (int) lt.getY() - radius, diameter, diameter);

            // Highlight if necessary
            if (lt.equals(highlighted)) {
               g.setColor(Color.WHITE);
               g.drawRect((int) lt.getX() - radius - 10, (int) lt.getY() - radius - 10, diameter + 20, diameter + 20);
            }
         }

         // Paint text area

         if (highlighted != null) {
            messageArea.setText(highlighted.toString());
         }
      }
   }

   /* ------------- GUI INNER CLASSES: LISTENING --------------- */

   private class EcoMenuListener implements ActionListener {
      public void actionPerformed(ActionEvent e) {
         if (e.getSource().equals(clearMenuItem)) {
            LivingThing.getAll().clear();
            renumberButtons();

            messageArea.setText("Cleared.");
         }
         if (e.getSource().equals(aboutMenuItem)) {
            aboutDialog();
         }
      }
   }

   private class EcoButtonListener implements ActionListener {
      public void actionPerformed(ActionEvent e) {
         if (e.getSource().equals(grassButton)) {
            activeTool = 'G';
         }
         if (e.getSource().equals(rabbitButton)) {
            activeTool = 'R';
         }
         if (e.getSource().equals(foxButton)) {
            activeTool = 'F';
         }
         if (e.getSource().equals(smiteButton)) {
            activeTool = 'S';
         }
      }
   }

   private class EcoMouseClickListener extends MouseAdapter {
      public void mouseClicked(MouseEvent e) {
         LivingThing newLT = null;
         Position p = new Position(e.getX(), e.getY());
         switch (activeTool) {
            case 'G': {
               newLT = new Grass(p);
               new Thread(newLT).start();
               break;
            }
            case 'R': {
               newLT = new Rabbit(p);
               new Thread(newLT).start();
               break;
            }
            case 'F': {
               newLT = new Fox(p);
               new Thread(newLT).start();
               break;
            }
            case 'S': {
               highlighted.death();
               break;
            }
         }
         renumberButtons();
         highlighted = newLT;
      }

      public void mouseExited(MouseEvent e) {
         highlighted = null;
         messageArea.setText("");
      }
   }

   private class EcoMouseMoveListener extends MouseMotionAdapter {
      public void mouseMoved(MouseEvent e) {
         Position p = new Position(e.getX(), e.getY());

         highlighted = LivingThing.nearestToPosition(LivingThing.getAll(), p);
      }
   }

   private class EcoSpeedChangeListener implements ChangeListener {
      public void stateChanged(ChangeEvent e) {
         EcoSystem.setSimSpeed(speedSlider.getValue());
         updateSpeedLabel();
      }
   }

   /* -------------------- THREADING METHODS ------------------- */

   /*
    * Continually updates the display.
    */
   public void run() {
      while (true) {
         repaint();
         delay(50);
      }
   }

   public void delay(int time) {
      try {
         Thread.sleep(time);
      } catch (InterruptedException e) {
         System.out.println("delay exception " + e.getMessage());
      }
   }
}
