package pojava.projekt;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.HeadlessException;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import net.objecthunter.*;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.Border;

public class InputFunctionDrawing extends JPanel {

	List<Double> fValues;
	List<Double> xValues;
	Color color1;
	

	public InputFunctionDrawing() throws HeadlessException {
		fValues = new ArrayList<>();
		xValues = new ArrayList<>();
		color1 = Color.RED;
	}

	public void counting(String input) {
		fValues.clear();
		xValues.clear();
		Expression e = new ExpressionBuilder(input)
                .variables("pi", "e", "x")
                .build()
                .setVariable("pi", Math.PI)
                .setVariable("e", Math.E);

        for (double x = 0; x < 2 * Math.PI; x += 0.0001) {
            e.setVariable("x", x);
            double result = e.evaluate();
            fValues.add(result);
            xValues.add(x);
            System.out.println(result);
        }
    }
	
	@Override
	protected void paintComponent(Graphics g) {
	    super.paintComponent(g);
	    Graphics2D g2d = (Graphics2D) g;

	    color1 = GUI.getColor1();
	    int width = getWidth();
	    int height = getHeight();

	    // Draw axes
	    g2d.setColor(Color.BLACK);
	    g2d.drawLine(50, height - 50, width - 50, height - 50); // X-axis
	    g2d.drawLine(50, 50, 50, height - 50); // Y-axis

	    // Draw function graph
	    g2d.setColor(color1);
	    g2d.setStroke(new BasicStroke(2.0f));

	    int dataSize = xValues.size();
	    double xMaxValue = getMaxValue(xValues);
	    double xMinValue = getMinValue(xValues);
	    double fMaxValue = getMaxValue(fValues);
	    double fMinValue = getMinValue(fValues);

	    for (int i = 0; i < dataSize - 1; i++) {
	        double x1 = 50 + (width - 100) * (xValues.get(i) - xMinValue) / (xMaxValue - xMinValue);
	        double y1 = height - 50 - (height - 100) * (fValues.get(i) - fMinValue) / (fMaxValue - fMinValue);
	        double x2 = 50 + (width - 100) * (xValues.get(i + 1) - xMinValue) / (xMaxValue - xMinValue);
	        double y2 = height - 50 - (height - 100) * (fValues.get(i + 1) - fMinValue) / (fMaxValue - fMinValue);
	        g2d.drawLine((int) x1, (int) y1, (int) x2, (int) y2);
	    }
	    if(fValues.size()!=0) {
		    int numTicksX = 10;
		    double xTickInterval = (double) (width - 100) / (numTicksX - 1);
		    for (int i = 0; i < numTicksX; i++) {
		        int x = (int) (50 + i * xTickInterval);
		        g2d.drawLine(x, height - 50, x, height - 45);
		        g2d.drawString(String.format("%.1f", xMinValue + i * (xMaxValue - xMinValue) / (numTicksX - 1)), x - 10, height - 30); // Draw label
		    }
	

		    int numTicksY = 10;
		    double yTickInterval = (double) (height - 100) / (numTicksY - 1);
		    for (int i = 0; i < numTicksY; i++) {
		        int y = (int) (height - 50 - i * yTickInterval);
		        g2d.drawLine(45, y, 50, y); // Draw tick mark
		        g2d.drawString(String.format("%.1f", fMinValue + i * (fMaxValue - fMinValue) / (numTicksY - 1)), 10, y + 5); // Draw label
		    }
	    }
	}



    private double getMaxValue(List<Double> fValues2) {
        double maxValue = Double.MIN_VALUE;
        for (double value : fValues2) {
            if (value > maxValue) {
                maxValue = value;
            }
        }
        return maxValue;
    }


    private double getMinValue(List<Double> fValues2) {
        double minValue = Double.MAX_VALUE;
        for (double value : fValues2) {
            if (value < minValue) {
                minValue = value;
            }
        }
        return minValue;
    }
	public void setBorder(Border lineBorder) {
		// TODO Auto-generated method stub
		
	}

}
