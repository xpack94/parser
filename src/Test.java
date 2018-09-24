import java.awt.*;

import javax.swing.*;
 
public class Test {
    final static boolean shouldFill = true;
    final static boolean shouldWeightX = true;
    final static boolean RIGHT_TO_LEFT = false;
 
    public static void addComponentsToPane(Container pane) {
        if (RIGHT_TO_LEFT) {
            pane.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        }
 
    JButton button;
    pane.setLayout(new GridBagLayout());
    GridBagConstraints c = new GridBagConstraints();
  
 
    button = new JButton("Charger Fichier");
    c.gridx = 0;
    c.gridy = 0;
    pane.add(button, c);

    JTextField fileInputField = new JTextField("entrer un fichier");
    c.ipady=10;
    c.gridx = 1;
    c.gridy = 0;
    c.gridwidth=2;
    c.fill=GridBagConstraints.HORIZONTAL;
    pane.add(fileInputField, c);
    
    button = new JButton("Button 3");
    c.ipady=0;
    c.gridx = 0;
    c.gridy = 1;
    c.gridwidth=1;
    c.gridheight=3;
    c.fill=GridBagConstraints.VERTICAL;
    c.insets=new Insets(0, 15, 0, 15);
    pane.add(button, c);
    
    Panel p = new Panel(new BorderLayout());
   
    
    JLabel label =new JLabel("text");
    p.add(label, BorderLayout.NORTH);
    
    button = new JButton("Button 4");
    p.add(button, BorderLayout.CENTER);
    
    c.gridx=1;
    c.gridy=1;
    c.gridwidth=1;
    c.gridheight=1;
    c.fill=GridBagConstraints.RELATIVE;
    c.insets=new Insets(0, 0, 0, 15);
    pane.add(p,c);
    
    Panel p1 = new Panel(new BorderLayout());
    JLabel label2=new JLabel("text");
    p1.add(label2,BorderLayout.NORTH);
    

    
    button = new JButton("Button 5");
    p1.add(button,BorderLayout.CENTER);
    
    c.gridx = 2;
    c.gridy = 1;
    c.gridwidth=1;
    c.gridheight=1;
    c.fill=GridBagConstraints.RELATIVE;
    c.insets=new Insets(0, 0, 0, 0);
    pane.add(p1, c);
    
    button = new JButton("Button 6");
    c.gridx = 1;
    c.gridy = 2;
    c.gridwidth=1;
    c.gridheight=1;
    c.insets=new Insets(0, 0, 15, 15);
    c.fill=GridBagConstraints.RELATIVE;
    pane.add(button, c);
    
    button = new JButton("Button 7");
    c.gridx = 2;
    c.gridy = 2;
    c.gridwidth=1;
    c.gridheight=1;
    c.insets=new Insets(0, 0, 15, 0);
    c.fill=GridBagConstraints.RELATIVE;
    pane.add(button, c);
    
    button = new JButton("Button 5");
    c.gridx = 1;
    c.gridy = 3;
    c.gridwidth=2;
    c.gridheight=1;
    c.insets=new Insets(0, 0, 0, 0);
    c.fill=GridBagConstraints.HORIZONTAL;
    pane.add(button,c);
    
    
   

   

	

	
	pane.add(button, c);
    
    }
    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    private static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("GridBagLayoutDemo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
 
        //Set up the content pane.
        addComponentsToPane(frame.getContentPane());
 
        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }
 
    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
}