package pojava.projekt;


import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class GUI extends JFrame implements ActionListener {


    InputFunctionDrawing graphPanel;
    JPanel inputPanel;
    static JEditorPane fxTextField;
    String inputFunction;
    static String periodText;
    JPanel centerPanel;
    JPanel param1Panel;
    JPanel param2Panel;
    JPanel sliderPanel;
    JSlider slider;
    GradientPanel mainPanel;
    JButton plotButton;
    JTextField periodTextField;
    JTextField sliderTextField;
    JLabel fxLabel;
    JLabel periodLabel;
    JLabel seriesInput;
    static JLabel a0_label = new JLabel("a_0 = ");

    JMenuBar menuBar;
    JMenu fileMenu;
    JMenuItem exitMenuItem;
    JMenuItem saveImageMenuItem;
    JMenuItem ChangeborderColor;
    JMenuItem gradient1;
    JMenuItem gradient2;
    JMenuItem saveDataMenuItem;
    JMenu colorsSubmenu;
    static Color gradientColor1;
    Color gradientColor2;
    Color borderColor;
    static int n;

    static final int SLIDER_MIN = 0;
    static final int SLIDER_MAX = 100;
    static final int SLIDER_INIT = 0;


    public GUI() throws HeadlessException {
        setTitle("SlomalMath");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(1200, 800);
        inputFunction = "";
        periodText = "";
        graphPanel = new InputFunctionDrawing();
        mainPanel = new GradientPanel();
        mainPanel.setLayout(new BorderLayout());

        gradientColor1 = new Color(169, 187, 199);
        gradientColor2 = new Color(99, 121, 150);
        setLayout(new BorderLayout());
        add(graphPanel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);
        graphPanel.setBorder(BorderFactory.createLineBorder(Color.WHITE, 6));
        graphPanel.setPreferredSize(new Dimension(getWidth(), getHeight() / 2));
        graphPanel.setBackground(Color.WHITE);

        menuBar = new JMenuBar();
        menuBar.setBackground(gradientColor1);
        fileMenu = new JMenu("File");
        exitMenuItem = new JMenuItem("Exit");
        saveDataMenuItem = new JMenuItem("Save Image");
        ChangeborderColor = new JMenuItem("Border Color");
        gradient1 = new JMenuItem("Gradient color 1");
        gradient2 = new JMenuItem("Gradient color 2");
        colorsSubmenu = new JMenu("Personalization");
        fileMenu.add(colorsSubmenu);
        colorsSubmenu.add(ChangeborderColor);
        colorsSubmenu.add(gradient1);
        colorsSubmenu.add(gradient2);
        saveDataMenuItem = new JMenuItem("Save data");
        fileMenu.add(saveDataMenuItem);
        fileMenu.add(exitMenuItem);
        menuBar.add(fileMenu);
        setJMenuBar(menuBar);
        ActionListener saveImageListener = arg0 -> {
            BufferedImage image = new BufferedImage(graphPanel.getWidth(), graphPanel.getHeight(), BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = image.createGraphics();
            graphPanel.paintAll(g2d);

            JFileChooser fileChooser = new JFileChooser();
            FileNameExtensionFilter filter = new FileNameExtensionFilter("PNG","JPEG","JPG");
            fileChooser.addChoosableFileFilter(filter);
            fileChooser.showSaveDialog(null);

            File outputfile = fileChooser.getSelectedFile();

            //Zapis do pliku
            try {
                ImageIO.write(image, "png", outputfile);
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        };
        saveDataMenuItem.addActionListener(saveImageListener);
        fileMenu.add(saveDataMenuItem);

        n=0;

        ChangeborderColor.addActionListener(e -> {
            JColorChooser colorChooser = new JColorChooser();
            Color borderColor = JColorChooser.showDialog(null, "Pick color", Color.white);
            graphPanel.setBorder(BorderFactory.createLineBorder(borderColor, 6));
        });

        gradient1.addActionListener(e -> {
            JColorChooser colorChooser = new JColorChooser();
            Color color = JColorChooser.showDialog(null, "Pick color", Color.white);
            gradientColor1 = color;
            menuBar.setBackground(gradientColor1);
            graphPanel.repaint();
            repaint();
        });

        gradient2.addActionListener(e -> {
            JColorChooser colorChooser = new JColorChooser();
            Color color = JColorChooser.showDialog(null, "Pick color", Color.white);
            gradientColor2 = color;
            repaint();
        });


        //slider i textfield do niego:
        slider = new JSlider(JSlider.HORIZONTAL, SLIDER_MIN, SLIDER_MAX, SLIDER_INIT);
        slider.setOpaque(false);
        slider.setMajorTickSpacing(5);
        slider.setMinorTickSpacing(10);
        slider.setPaintTicks(true);
        slider.setSnapToTicks(false);
        slider.setPaintLabels(true);
        slider.setLabelTable(slider.createStandardLabels(10));
        slider.setPreferredSize(new Dimension(500,50));

        fxLabel = new JLabel("F(x): ");
        Font plane = new Font("Plain", Font.PLAIN, 30);
        Font plane_smaller = new Font("Plain_smaller", Font.PLAIN, 15);
        fxLabel.setFont(plane);

        periodLabel = new JLabel("T = ");
        periodLabel.setFont(plane);

        seriesInput = new JLabel("F(x) = " + inputFunction);
        seriesInput.setFont(plane);

        //a0_label = new JLabel("a_0 = ");
        a0_label.setFont(plane);


        //czesc w ktorej ma byc to wszystko ulozone:
        inputPanel = new JPanel();
        inputPanel.setOpaque(false); //przezroczysty
        inputPanel.setPreferredSize(new Dimension(getWidth(), 65));
        inputPanel.setBorder(BorderFactory.createLineBorder(borderColor, 1));

        fxTextField = new JEditorPane();
        fxTextField.setPreferredSize(new Dimension(((getWidth()) / 2) + 100, 50));
        fxTextField.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
        fxTextField.setFont(plane_smaller);

        plotButton = new JButton("Plot");
        plotButton.addActionListener(e -> {

            inputFunction = getString(fxTextField);
            periodText = periodTextField.getText();
            graphPanel.counting(inputFunction);
            graphPanel.szeregFouriera((inputFunction));
            graphPanel.repaint();
            seriesInput.setText("F(x) = " + inputFunction);
            graphPanel.repaint();
        });
        periodTextField = new JTextField();
        periodTextField.setPreferredSize(new Dimension((getWidth()) / 6, 50));
        periodTextField.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
        periodTextField.setFont(plane_smaller);

        inputPanel.add(fxLabel);
        inputPanel.add(fxTextField);
        inputPanel.add(plotButton);
        inputPanel.add(periodLabel);
        inputPanel.add(periodTextField);

        mainPanel.add(inputPanel, BorderLayout.NORTH);

        centerPanel = new JPanel();
        centerPanel.setOpaque(false);
        centerPanel.setLayout(new BorderLayout());
        mainPanel.add(centerPanel, BorderLayout.CENTER);

        sliderPanel = new JPanel();
        sliderPanel.setOpaque(false);

        param1Panel = new JPanel();
        param1Panel.setOpaque(false);
        param1Panel.setLayout(new BorderLayout());
        param1Panel.add(sliderPanel, BorderLayout.SOUTH);
        sliderPanel.add(slider);
        slider.addChangeListener(new SliderChangeListener());

        sliderTextField = new JTextField("0");
        sliderTextField.setFont(plane_smaller);
        sliderPanel.add(sliderTextField);
        sliderTextField.setPreferredSize(new Dimension(50,30));
        sliderTextField.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
        System.out .println(periodText);
        seriesInput.setHorizontalAlignment(SwingConstants.CENTER);
        param1Panel.add(seriesInput, BorderLayout.CENTER);

        sliderTextField.addActionListener(e -> {
            String text = sliderTextField.getText();
            try {
                int value = Integer.parseInt(text);
                value = Math.max(SLIDER_MIN, Math.min(SLIDER_MAX, value));
                n = value;
                graphPanel.setN(n);
                slider.setValue(value);
            } catch (NumberFormatException ex) {
                System.err.println("Invalid input: " + text);
            }
        });

        param2Panel = new JPanel();
        param2Panel.setOpaque(false);
        param2Panel.setLayout(new GridLayout(3, 1));
        param2Panel.add(a0_label);
        param2Panel.setPreferredSize(new Dimension(280, centerPanel.getHeight()));

        centerPanel.add(param1Panel, BorderLayout.CENTER);
        centerPanel.add(param2Panel, BorderLayout.EAST);
    }

    public static void main(String[] args) {
        GUI gui = new GUI();
        gui.setVisible(true);
    }

    public class SliderChangeListener implements ChangeListener{
        public void stateChanged(ChangeEvent arg0) {
            String value = String.format("%d",slider.getValue());
            sliderTextField.setText(value);
            n = slider.getValue();
            graphPanel.setN(n);
            inputFunction = getString(fxTextField);
            periodText = periodTextField.getText();
            graphPanel.counting(inputFunction);
            graphPanel.szeregFouriera((inputFunction));
            graphPanel.repaint();
            seriesInput.setText("F(x) = " + inputFunction);
            graphPanel.repaint();
        }
    }


    public String getString(JEditorPane editorpane) {
        String string = editorpane.getText();
        return string;
    }

    public static String getPeriod(){
        //System.out .println(periodText);
        return periodText;
    }

    public static int getN(){
        return n;
    }
    public class GradientPanel extends JPanel {

        public GradientPanel() {

        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            int w = getWidth();
            int h = getHeight();
            GradientPaint gp = new GradientPaint(
                    0, 0, gradientColor1, 0, h, gradientColor2);
            g2d.setPaint(gp);
            g2d.fillRect(0, 0, w, h);
        }

    }

    public static Color getColor1(){
        return gradientColor1;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub

    }


}