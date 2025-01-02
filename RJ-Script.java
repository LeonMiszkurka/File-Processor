import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class FileProcessorGUI {
    public static void main(String[] args) {
        JFrame frame = new JFrame("File Processor");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 200);

        // Set the icon for the JFrame
        ImageIcon icon = new ImageIcon("RJS.png");
        frame.setIconImage(icon.getImage());

        JPanel panel = new JPanel();
        JLabel label = new JLabel("Enter the path to your file:");
        JTextField textField = new JTextField(25);
        JButton button = new JButton("Process File");

        panel.add(label);
        panel.add(textField);
        panel.add(button);
        frame.add(panel);

        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String filePath = textField.getText();
                File file = new File(filePath);

                if (file.exists() && file.isFile()) {
                    String fileName = file.getName().toLowerCase();
                    if (fileName.endsWith(".txt")) {
                        readFile(file);
                    } else if (fileName.endsWith(".java") || fileName.endsWith(".bat") ||
                            fileName.endsWith(".py") || fileName.endsWith(".ps1") ||
                            fileName.endsWith(".cmd")) {
                        runFile(file);
                    } else {
                        JOptionPane.showMessageDialog(frame, "Unsupported file type. Please input a .txt, .java, .bat, .py, .ps1, or .cmd file.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(frame, "File not found. Please check the path and try again.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        frame.setVisible(true);
    }

    private static void readFile(File file) {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            StringBuilder content = new StringBuilder();
            while ((line = br.readLine()) != null) {
                content.append(line).append("\n");
            }
            JOptionPane.showMessageDialog(null, content.toString(), "File Content", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error reading the file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static void runFile(File file) {
        try {
            ProcessBuilder pb = new ProcessBuilder(file.getAbsolutePath());
            pb.redirectErrorStream(true);
            Process p = pb.start();
            BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;
            StringBuilder output = new StringBuilder();
            while ((line = br.readLine()) != null) {
                output.append(line).append("\n");
            }
            int exitCode = p.waitFor();
            output.append("File executed with exit code: ").append(exitCode);
            JOptionPane.showMessageDialog(null, output.toString(), "Execution Output", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException | InterruptedException e) {
            JOptionPane.showMessageDialog(null, "Error running the file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
