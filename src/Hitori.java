// r is a column,c is a row numbered from 0..4 where 0 is the first row or column and 4is last column
//checks for empty strings
/**
 *
 * @author naimi
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import java.text.NumberFormat;
import javax.swing.JOptionPane;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.AttributeSet;
import javax.swing.text.PlainDocument;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class Hitori extends PlainDocument {

    public static String value;
    public static int r;
    public static int c;
    private int limit;
    public static JTextField[][] panels = new JTextField[5][5];

    Hitori(int limit) {
        super();
        this.limit = limit;
    }

    Hitori(int limit, boolean upper) {
        super();
        this.limit = limit;
    }

    public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException {
        if (str == null) {
            return;
        }

        if ((getLength() + str.length()) <= limit) {
            super.insertString(offset, str, attr);
        }
    }

    public static void gui() {
        NumberFormat valueFormat;
        JFrame hitori = new JFrame("hitori");
        hitori.setSize(500, 500);
        hitori.setVisible(true);
        hitori.setDefaultCloseOperation(hitori.EXIT_ON_CLOSE);
        hitori.setResizable(false);
        hitori.setBackground(Color.cyan);
        JPanel board = new JPanel(new GridBagLayout());
        board.setBackground(Color.WHITE);
        GridBagConstraints gb = new GridBagConstraints();
       // System.out.println(board.getSize());
       board.setBackground(Color.orange);
        Border outerBorder = BorderFactory.createLineBorder(Color.black, 2);
        Border innerBorder = BorderFactory.createLineBorder(Color.BLUE, 2);
        board.setSize(new Dimension(440, 440));
        int digit = 1;
        for (r = 0; r < 5; r++) {
            for (c = 0; c < 5; c++) {
                //final JTextField boxes = new JTextField(5);
                JFormattedTextField boxes = new JFormattedTextField();
                boxes.setColumns(4);
                gb.gridx = r;
                gb.gridy = c;
                boxes.setPreferredSize(new Dimension(100, 70));
                Font font = new Font("Courier", Font.BOLD, 20);
                boxes.setForeground(Color.red);
                boxes.setFont(font);
// String value = boxes.getText();
// System.out.println(value);
                boxes.addKeyListener(new KeyAdapter() {
                    public void keyTyped(KeyEvent e) {
                        int max = 0;
                        char val = e.getKeyChar();
                        max = max + 1;
                        // System.out.println("1st" + max);
                        // System.out.println("1st" + val);
                        if (!((Character.isDigit(val) || (val == KeyEvent.VK_BACK_SPACE) || (val == KeyEvent.VK_DELETE) || (val == '0')))) {

                            e.consume();

                        }

                    }
                });

                boxes.setHorizontalAlignment(boxes.CENTER);
                boxes.setBorder(outerBorder);
                boxes.setBackground(Color.WHITE);
                board.add(boxes, gb);
                panels[r][c] = boxes;
                boxes.setDocument(new Hitori(1));
                hitori.add(board);
            }

        }
        // String workingDir=System.getProperty("user.dir")+"//Files";

        JPanel buttonpannel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonpannel.setBackground(Color.orange);
        JButton b1 = new JButton("solve");
        buttonpannel.add(b1);
        b1.addActionListener(new ActionListenerImpl());
                
                JButton b2 = new JButton("clear");
                buttonpannel.add(b2);
                b2.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {

                        for (r = 0; r < 5; r++) {
                            for (c = 0; c < 5; c++) {
                                panels[r][c].setEditable(true);
                                panels[r][c].setText("");
                                panels[r][c].setBackground(Color.white);
                                panels[r][c].setForeground(Color.red);
                            }
                        }
                    }
                });
                FlowLayout flow = new FlowLayout();
                buttonpannel.setLayout(flow);
                hitori.add(buttonpannel, BorderLayout.SOUTH);
                

            }
   
            public static void main(String[] args) {
                javax.swing.SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        gui();
                    }
                });

            }

    private static class ActionListenerImpl implements ActionListener {

        public ActionListenerImpl() {
        }
        private Component gridPanel;

        public void actionPerformed(ActionEvent e) {
            try {
                FileOutputStream inputFile = new FileOutputStream("input.txt");
                //System.out.println(workingDir);
                for (c = 0; c < 5; c++) {
                    for (r = 0; r < 5; r++) {
                        value = panels[r][c].getText();
                        // int  row = c+1;
                        //int column = r+1;//later can remove +1
                        if(value.isEmpty())
                        {
                        JOptionPane.showMessageDialog(gridPanel,
							    "clear the puzzle enter  value into the all the  cells to solve",
							    "warning",
							    JOptionPane.WARNING_MESSAGE);
                        
						break;
                                                
                        }
                        else{
                        String text = "values(" + r + "," + c + "," + value + ")." + "\n";//change order of row=c to row = r,colum =r to c
                        byte[] bytes = text.getBytes();
                        try {
                            inputFile.write(bytes);
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }}
                    }
                }
                inputFile.close();
                Process p = Runtime.getRuntime().exec("cmd /C clingo hitori2.sm input.txt | mkatoms > output");
               // System.out.println("1"+p.waitFor());
               p.waitFor();
                
                String fileName = "output";
                
                // This will reference one line at a time
                String line = null;

        try {
            // FileReader reads text files in the default encoding.
            FileReader fileReader = 
                new FileReader(fileName);

            // Always wrap FileReader in BufferedReader.
            BufferedReader bufferedReader = 
                new BufferedReader(fileReader);

            while((line = bufferedReader.readLine()) != null) {
                readOutput(panels);
                System.out.println(line);
            }   

            // Always close files.
            bufferedReader.close();         
        }
        catch(FileNotFoundException ex) {
            System.out.println(
                "Unable to open file '" + 
                fileName + "'");                
        }
        catch(IOException ex) {
            System.out.println(
                "Error reading file '" 
                + fileName + "'");                  
            // Or we could just do this: 
            // ex.printStackTrace();
            
        }
                
                
                    
                
            } catch (IOException ex) {
                Logger.getLogger(Hitori.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InterruptedException ex) {
                Logger.getLogger(Hitori.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        private void readOutput(JTextField[][] panels) {
            
           // System.out.println("inside readoutput");
            String fileName = "output";
            
            ArrayList<String> list = new ArrayList<>();
            
            try (BufferedReader br = Files.newBufferedReader(Paths.get(fileName))) {
//br returns as stream and convert it into a List
list = (ArrayList<String>) br.lines().collect(Collectors.toList());

            } catch (IOException e) {
                e.printStackTrace();
                
            }
            //list.forEach(System.out::println);
            for (String value : list) {
                
                if(value.contains(" no models found."))
                {
                JOptionPane.showMessageDialog(gridPanel,
							    "No solution exist for given set.",
							    "warning",
							    JOptionPane.WARNING_MESSAGE);
						break;
                
                }
                if(value.charAt(0) == 'r') 
                        {
                    char[] valueChar = value.toCharArray();
                    
                    int r = Character.getNumericValue(valueChar[2]);
                    int c = Character.getNumericValue(valueChar[4]);
                    System.out.println(panels[r][c]);
                    //System.out.println(panels[r][c]);
                    //	int outputValue=Character.getNumericValue(valueChar[6]);
                    //panels[r][c].setText(insert);
                    panels[r][c].setBackground(Color.black);
                    panels[r][c].setForeground(Color.green);
                    // System.out.println(panels[r][c]);
                }
                
            }
         for (r = 0; r < 5; r++) {
                            for (c = 0; c < 5; c++) {
                                panels[r][c].setEditable(false);
                            }
                        }     
        }
    }

        }
        /*File file = new File("/users/mkyong/filename.txt");

			// if file doesnt exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}

			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(content);
			bw.close();

			System.out.println("Done");
                           */
