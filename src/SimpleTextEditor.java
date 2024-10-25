import javax.swing.*;
import javax.swing.text.DefaultCaret;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.AttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledEditorKit;
import java.awt.*;
import java.awt.event.*;
import java.awt.Font;
import java.awt.print.PrinterJob;
import java.awt.print.PrinterException;
import java.awt.font.TextAttribute;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.*;

public class SimpleTextEditor extends JFrame implements ActionListener, MouseListener, Serializable {
    private static final long serialVersionUID = 1L;

    protected JTextArea textArea;
    protected JFileChooser fileChooser;
    protected String currentFilePath;

    protected JMenu styleMenu;
    protected JMenu viewMenu;

    public SimpleTextEditor() {
        setTitle("Text Editor");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        textArea = new JTextArea();
        textArea.setFont(new Font("Arial", Font.PLAIN, 14));
        textArea.setCaret(new DefaultCaret());
        textArea.addMouseListener(this);
        JScrollPane scrollPane = new JScrollPane(textArea);
        add(scrollPane, BorderLayout.CENTER);

        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem newMenuItem = new JMenuItem("New");
        newMenuItem.addActionListener(this);
        JMenuItem openMenuItem = new JMenuItem("Open");
        openMenuItem.addActionListener(this);
        JMenuItem saveMenuItem = new JMenuItem("Save");
        saveMenuItem.addActionListener(this);
        JMenuItem saveAsMenuItem = new JMenuItem("Save As");
        saveAsMenuItem.addActionListener(this);
        JMenuItem printMenuItem = new JMenuItem("Print");
        printMenuItem.addActionListener(this);
        JMenuItem exitMenuItem = new JMenuItem("Exit");
        exitMenuItem.addActionListener(this);
        fileMenu.add(newMenuItem);
        fileMenu.add(openMenuItem);
        fileMenu.add(saveMenuItem);
        fileMenu.add(saveAsMenuItem);
        fileMenu.add(printMenuItem);
        fileMenu.addSeparator();
        fileMenu.add(exitMenuItem);
        menuBar.add(fileMenu);

        JMenu formatMenu = new JMenu("Format");
        JMenuItem boldMenuItem = new JMenuItem("Bold");
        boldMenuItem.addActionListener(this);
        JMenuItem italicMenuItem = new JMenuItem("Italic");
        italicMenuItem.addActionListener(this);
        JMenuItem underlineMenuItem = new JMenuItem("Underline");
        underlineMenuItem.addActionListener(this);
        formatMenu.add(boldMenuItem);
        formatMenu.add(italicMenuItem);
        formatMenu.add(underlineMenuItem);
        menuBar.add(formatMenu);

        styleMenu = new JMenu("Style");
        JMenuItem fontSizeMenuItem = new JMenuItem("Font Size");
        fontSizeMenuItem.addActionListener(this);
        styleMenu.add(fontSizeMenuItem);
        JMenuItem textColorMenuItem = new JMenuItem("Text Color");
        textColorMenuItem.addActionListener(this);
        styleMenu.add(textColorMenuItem);
        JMenuItem bgColorMenuItem = new JMenuItem("Background Color");
        bgColorMenuItem.addActionListener(this);
        styleMenu.add(bgColorMenuItem);
        JMenuItem bgImageMenuItem = new JMenuItem("Background Image");
        bgImageMenuItem.addActionListener(this);
        styleMenu.add(bgImageMenuItem);
        menuBar.add(styleMenu);

        viewMenu = new JMenu("View");
        JMenuItem zoomMenuItem = new JMenuItem("Zoom");
        zoomMenuItem.addActionListener(this);
        viewMenu.add(zoomMenuItem);
        JMenuItem pageSetupMenuItem = new JMenuItem("Page Setup");
        pageSetupMenuItem.addActionListener(this);
        viewMenu.add(pageSetupMenuItem);
        JMenuItem wordWrapMenuItem = new JMenuItem("Word Wrap");
        wordWrapMenuItem.addActionListener(this);
        viewMenu.add(wordWrapMenuItem);
        JMenuItem statusBarMenuItem = new JMenuItem("Status Bar");
        statusBarMenuItem.addActionListener(this);
        viewMenu.add(statusBarMenuItem);
        menuBar.add(viewMenu);

        JMenu helpMenu = new JMenu("Help");
        JMenuItem helpMenuItem = new JMenuItem("Help");
        helpMenuItem.addActionListener(this);
        helpMenu.add(helpMenuItem);
        menuBar.add(helpMenu);

        setJMenuBar(menuBar);

        fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Text files (*.txt)", "txt");
        fileChooser.setFileFilter(filter);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        switch (command) {
            case "New":
                newFile();
                break;
            case "Open":
                openFile();
                break;
            case "Save":
                saveFile();
                break;
            case "Save As":
                saveAsFile();
                break;
            case "Print":
                printFile();
                break;
            case "Exit":
                System.exit(0);
                break;
            case "Bold":
                setBold();
                break;
            case "Italic":
                setItalic();
                break;
            case "Underline":
                setUnderline();
                break;
            case "Font Size":
                setFontSize();
                break;
            case "Text Color":
                setTextColor();
                break;
            case "Background Color":
                setBackgroundColor();
                break;
            case "Word Wrap":
                setWordWrap();
                break;
            case "Background Image":
                setBackgroundImage();
                break;
            case "Zoom":
                setZoom();
                break;
            case "Page Setup":
                setPageSetup();
                break;
            case "Help":
                showHelp();
                break;
            case "Status Bar":
                showStatusBar();
                break;
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    protected void newFile() {
        textArea.setText("");
        currentFilePath = null;
    }

    protected void openFile() {
        int returnVal = fileChooser.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try {
                BufferedReader reader = new BufferedReader(new FileReader(file));
                textArea.setText("");
                String line;
                while ((line = reader.readLine()) != null) {
                    textArea.append(line + "\n");
                }
                reader.close();
                currentFilePath = file.getAbsolutePath();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    protected void saveFile() {
        if (currentFilePath == null) {
            int returnVal = fileChooser.showSaveDialog(this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                try {
                    BufferedWriter writer = new BufferedWriter(new FileWriter(file));
                    writer.write(textArea.getText());
                    writer.close();
                    currentFilePath = file.getAbsolutePath();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        } else {
            try {
                BufferedWriter writer = new BufferedWriter(new FileWriter(currentFilePath));
                writer.write(textArea.getText());
                writer.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    protected void saveAsFile() {
        int returnVal = fileChooser.showSaveDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try {
                BufferedWriter writer = new BufferedWriter(new FileWriter(file));
                writer.write(textArea.getText());
                writer.close();
                currentFilePath = file.getAbsolutePath();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }


    protected void printFile() {
        PrinterJob job = PrinterJob.getPrinterJob();
        if (job.printDialog()) {
            try {
                job.print();
            } catch (PrinterException ex) {
                ex.printStackTrace();
            }
        }
    }
    protected void setBold() {
        Font currentFont = textArea.getFont();
        Font newFont;
        if (currentFont.isBold()) {
            newFont = currentFont.deriveFont(Font.PLAIN);
        } else {
            newFont = currentFont.deriveFont(Font.BOLD);
        }
        textArea.setFont(newFont);
    }

    protected void setItalic() {
        Font currentFont = textArea.getFont();
        Font newFont;
        if (currentFont.isItalic()) {
            newFont = currentFont.deriveFont(Font.PLAIN);
        } else {
            newFont = currentFont.deriveFont(Font.ITALIC);
        }
        textArea.setFont(newFont);
    }

    protected void setUnderline() {
        Font currentFont = textArea.getFont();
        Map<TextAttribute, Object> attributes = new HashMap<>(currentFont.getAttributes());
        attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
        Font newFont = new Font(attributes);
        textArea.setFont(newFont);
    }

    protected void setFontSize() {
        String input = JOptionPane.showInputDialog("Enter font size:");
        try {
            int size = Integer.parseInt(input);
            Font currentFont = textArea.getFont();
            textArea.setFont(currentFont.deriveFont((float) size));
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid input. Please enter a valid number.");
        }
    }
    
    protected void setTextColor() {
        Color textColor = JColorChooser.showDialog(this, "Choose Text Color", textArea.getForeground());
        if (textColor != null) {
            textArea.setForeground(textColor);
            textArea.setSelectionColor(textColor);
            textArea.repaint();
        }
    }


    protected void setBackgroundColor() {
        Color color = JColorChooser.showDialog(this, "Choose Background Color", textArea.getBackground());
        if (color != null) {
            textArea.setBackground(color);
        }
    }

    protected void setWordWrap() {
        boolean wrap = !textArea.getLineWrap();
        textArea.setLineWrap(wrap);
        textArea.setWrapStyleWord(wrap);
    }

    protected void setBackgroundImage() {
        int returnVal = fileChooser.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            Image backgroundImage = Toolkit.getDefaultToolkit().createImage(file.getAbsolutePath());

            textArea = new JTextArea() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                }
            };

            textArea.setOpaque(false);
            textArea.setBackground(new Color(0, 0, 0, 0));
            textArea.repaint();
        }
    }

    protected void setZoom() {
        String input = JOptionPane.showInputDialog("Enter zoom percentage (e.g., 100 for default size):");
        try {
            int zoomPercentage = Integer.parseInt(input);
            if (zoomPercentage > 0) {
                float scaleFactor = zoomPercentage / 100f;
                Font currentFont = textArea.getFont();
                Font newFont = currentFont.deriveFont(currentFont.getSize() * scaleFactor);
                textArea.setFont(newFont);
            } else {
                JOptionPane.showMessageDialog(this, "Invalid zoom percentage. Please enter a positive number.");
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid input. Please enter a valid number.");
        }
    }


    protected void setPageSetup() {
        JOptionPane.showMessageDialog(this, "Page Setup functionality is not implemented yet.");
    }

    protected void showHelp() {
        JOptionPane.showMessageDialog(this, "Help functionality is not implemented yet.");
    }

    protected void showStatusBar() {
        JOptionPane.showMessageDialog(this, "Status Bar functionality is not implemented yet.");
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SimpleTextEditor());
    }
}
