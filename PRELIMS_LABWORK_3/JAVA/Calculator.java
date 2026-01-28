// =====================================================================
// IMPORTS - These are like "tools" we borrow from Java's library
// Without these, we can't use JFrame, JButton, etc.
// Think of it like: "Hey Java, I need to use these things!"
// =====================================================================
import javax.swing.JFrame;      // JFrame = The main window (the box you see)
import javax.swing.JTextField;  // JTextField = A box where user can TYPE text
import javax.swing.JLabel;      // JLabel = Just displays text (user can't edit it)
import javax.swing.JButton;     // JButton = A clickable button
import javax.swing.JPanel;      // JPanel = An invisible container to group things together
import java.awt.GridLayout;     // GridLayout = Arranges things in rows & columns (like a table)
import java.awt.FlowLayout;     // FlowLayout = Arranges things in a row, natural size
import java.awt.BorderLayout;   // BorderLayout = Arranges things by position (top, bottom, left, right, center)
import java.awt.Font;           // Font = Controls text style and size
import java.awt.Color;          // Color = Controls colors
import java.awt.event.ActionEvent;      // ActionEvent = Event when button is clicked
import java.awt.event.ActionListener;   // ActionListener = Listens for button clicks


public class Calculator {
    
    public static void main(String[] args) {
        
        // ============================================
        // STEP 1: Create the window (JFrame)
        // ============================================
        // JFrame is the actual window that pops up on your screen
        // Everything else (buttons, text fields) goes INSIDE this window
        
        JFrame window = new JFrame();           // Create a new empty window
        window.setTitle("Prelim Grade Calculator");    // Text shown at the top bar
        window.setSize(500, 550);               // Width = 500 pixels, Height = 550 pixels (bigger!)
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  // Close program when X is clicked
        window.setLocationRelativeTo(null);    // null = center of screen
        window.setResizable(false);            // Prevent user from resizing (so layout doesn't break)
        
        
        // ============================================
        // STEP 2: Create the input fields (JTextField)
        // ============================================
        // JTextField = a text box where users can type
        // The number inside (10) is the width in characters
        // We need 4 input boxes for: Quiz1, Quiz2, Quiz3, and Exam
        
        JTextField input1 = new JTextField(10);  // Box for Quiz 1 score
        JTextField input2 = new JTextField(10);  // Box for Quiz 2 score
        JTextField input3 = new JTextField(10);  // Box for Quiz 3 score
        JTextField input4 = new JTextField(10);  // Box for Exam score
        
        
        // ============================================
        // STEP 3: Create labels (JLabel)
        // ============================================
        // JLabel = text that the user can SEE but NOT edit
        // We use labels to tell the user what each input box is for
        // Without labels, users won't know which box is for what!
        
        // setPreferredSize makes all labels the same width so inputs line up!
        java.awt.Dimension labelSize = new java.awt.Dimension(130, 25);
        
        JLabel label1 = new JLabel("Lab work grade 1:");
        label1.setPreferredSize(labelSize);    // Set fixed width
        
        JLabel label2 = new JLabel("Lab work grade 2:");
        label2.setPreferredSize(labelSize);
        
        JLabel label3 = new JLabel("Lab work grade 3:");
        label3.setPreferredSize(labelSize);
        
        JLabel label4 = new JLabel("No. of Absents:");
        label4.setPreferredSize(labelSize);
        
        
        // ============================================
        // STEP 4: Create panel for first 3 inputs (JPanel + FlowLayout)
        // ============================================
        // JPanel = an invisible box/container to group things together
        // FlowLayout = arranges items in a row, keeps their natural size!
        //
        // Unlike GridLayout which STRETCHES everything to fill space,
        // FlowLayout keeps items at their preferred size.
        //
        // FlowLayout.LEFT means items align to the left side
        
        JPanel topPanel = new JPanel();                        // Create the container
        topPanel.setLayout(new GridLayout(3, 1));              // 3 rows for 3 input pairs
        
        // Create a mini panel for each label+input pair
        // Each mini panel uses FlowLayout to keep the input size normal
        
        JPanel row1 = new JPanel(new FlowLayout(FlowLayout.LEFT));  // FlowLayout, align left
        row1.add(label1);
        row1.add(input1);
        
        JPanel row2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        row2.add(label2);
        row2.add(input2);
        
        JPanel row3 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        row3.add(label3);
        row3.add(input3);
        
        topPanel.add(row1);   // Add row 1 to topPanel
        topPanel.add(row2);   // Add row 2 to topPanel
        topPanel.add(row3);   // Add row 3 to topPanel
        
        
        // ============================================
        // STEP 5: Create panel for the 4th input (separate)
        // ============================================
        // Same idea - use FlowLayout to keep input at normal size
        
        JPanel bottomPanel = new JPanel();                     // Create container
        bottomPanel.setLayout(new FlowLayout(FlowLayout.LEFT)); // FlowLayout, align left
        
        bottomPanel.add(label4);   // "No. of Absents" label
        bottomPanel.add(input4);   // Absents input box
        
        
        // ============================================
        // STEP 6: Create the Calculate button and Result label
        // ============================================
        // JButton = a clickable button
        // When clicked, it will calculate the grade (we'll add this later)
        
        JButton calcButton = new JButton("Calculate Grade");   // Button with text
        calcButton.setFont(new Font("Arial", Font.BOLD, 16));  // Make text bigger & bold
        // Font("Arial", Font.BOLD, 16) means:
        //   - Font name: Arial
        //   - Style: BOLD (could also be PLAIN or ITALIC)
        //   - Size: 16 points
        
        // This label will show the calculated result
        JLabel resultLabel = new JLabel("<html><center>Your grade will show here</center></html>");
        resultLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        resultLabel.setHorizontalAlignment(JLabel.CENTER);  // Center the text horizontally
        
        // Label to show required prelim exam score
        JLabel requiredLabel = new JLabel("<html><center>Required prelim exam scores will show here</center></html>");
        requiredLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        requiredLabel.setHorizontalAlignment(JLabel.CENTER);
        
        
        // ============================================
        // STEP 7: Create a main panel to hold EVERYTHING
        // ============================================
        // We need one big container to organize all our panels and buttons
        // GridLayout(5, 1) = 5 rows, 1 column (everything stacked vertically)
        //
        // +---------------------+
        // |     topPanel        |  <- Row 1 (Quiz 1, 2, 3)
        // +---------------------+
        // |    bottomPanel      |  <- Row 2 (Exam)
        // +---------------------+
        // |    calcButton       |  <- Row 3 (Calculate button)
        // +---------------------+
        // |    resultLabel      |  <- Row 4 (Shows the grade)
        // +---------------------+
        // |     (empty)         |  <- Row 5 (extra space)
        // +---------------------+
        
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(6, 1, 10, 10));  // 6 rows, 1 column
        
        mainPanel.add(topPanel);      // Add the lab work inputs panel
        mainPanel.add(bottomPanel);   // Add the absents input panel
        mainPanel.add(calcButton);    // Add the calculate button
        mainPanel.add(resultLabel);   // Add the result display
        mainPanel.add(requiredLabel); // Add the required score display
        
        
        // ============================================
        // STEP 8: Add everything to the window and show it!
        // ============================================
        // window.add() puts the mainPanel inside the window
        // window.setVisible(true) actually displays the window on screen
        
        window.add(mainPanel);        // Put mainPanel inside the window
        window.setVisible(true);      // Show the window! (IMPORTANT - without this, nothing appears!)
        
        // ============================================
        // STEP 9: Add ActionListener to make button work!
        // ============================================
        // When the button is clicked, this code runs and calculates the grade
        
        calcButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    // Get values from input fields
                    double lab1 = Double.parseDouble(input1.getText());
                    double lab2 = Double.parseDouble(input2.getText());
                    double lab3 = Double.parseDouble(input3.getText());
                    int absents = Integer.parseInt(input4.getText());
                    
                    // Calculate Attendance (100 base score, -10 per absent)
                    double attendance = 100 - (absents * 10);
                    if (attendance < 0) attendance = 0;  // Can't go below 0
                    
                    // Calculate Lab Work Average
                    double labWorkAverage = (lab1 + lab2 + lab3) / 3.0;
                    
                    // Calculate Class Standing
                    // Class Standing = (Attendance x 0.40) + (Lab Work Average x 0.60)
                    double classStanding = (attendance * 0.40) + (labWorkAverage * 0.60);
                    
                    // Calculate required Prelim Exam score to pass (75) and get 100
                    // Prelim Grade = (Prelim Exam x 0.30) + (Class Standing x 0.70)
                    // To find Prelim Exam: Prelim Exam = (Target Grade - Class Standing x 0.70) / 0.30
                    
                    double requiredToPass = (75 - (classStanding * 0.70)) / 0.30;
                    double requiredFor100 = (100 - (classStanding * 0.70)) / 0.30;
                    
                    // Display results
                    String resultText = String.format(
                        "<html><center>" +
                        "Attendance Score: %.2f<br>" +
                        "Lab Work Average: %.2f<br>" +
                        "Class Standing: %.2f" +
                        "</center></html>",
                        attendance, labWorkAverage, classStanding
                    );
                    resultLabel.setText(resultText);
                    
                    // Calculate max possible grade if they get 100 on prelim exam
                    double maxPossibleGrade = (100 * 0.30) + (classStanding * 0.70);
                    
                    // Determine required score to pass (75)
                    String passStatus;
                    if (requiredToPass > 100) {
                        passStatus = String.format("<font color='red'>Cannot pass (need %.2f)</font>", requiredToPass);
                    } else if (requiredToPass <= 0) {
                        passStatus = "<font color='green'>ALREADY PASSING! (need 0 or less)</font>";
                    } else {
                        passStatus = String.format("<font color='blue'>%.2f</font>", requiredToPass);
                    }
                    
                    // For 100: show required exam score AND the max possible grade
                    String perfectStatus;
                    if (requiredFor100 > 100) {
                        perfectStatus = String.format("<font color='orange'>Need %.2f (Max grade: %.2f)</font>", requiredFor100, maxPossibleGrade);
                    } else if (requiredFor100 <= 0) {
                        perfectStatus = String.format("<font color='green'>GUARANTEED! (Max grade: %.2f)</font>", maxPossibleGrade);
                    } else {
                        perfectStatus = String.format("<font color='blue'>%.2f (Max grade: %.2f)</font>", requiredFor100, maxPossibleGrade);
                    }
                    
                    String requiredText = String.format(
                        "<html><center>" +
                        "<b>Required Prelim Exam Score:</b><br>" +
                        "To Pass (75): %s<br>" +
                        "For Perfect (100): %s" +
                        "</center></html>",
                        passStatus, perfectStatus
                    );
                    requiredLabel.setText(requiredText);
                    
                } catch (NumberFormatException ex) {
                    resultLabel.setText("<html><center><font color='red'>Please enter valid numbers!</font></center></html>");
                    requiredLabel.setText("");
                }
            }
        });
    }
}
