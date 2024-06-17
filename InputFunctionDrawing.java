package pojava.projekt;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.HeadlessException;
import java.util.ArrayList;
import java.util.List;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;
import net.objecthunter.exp4j.tokenizer.UnknownFunctionOrVariableException;
import javax.swing.*;


import static pojava.projekt.GUI.a0_label;

public class InputFunctionDrawing extends JPanel {

	final List<Double> fValues;
	final List<Double> xValues;
	final List<Double> an;
	final List<Double> bn;
	final List<Double> integralX;
	final List<Double> integralY;
	Color color1;
	public double period;
	static int N;
	Expression e;
	JEditorPane fxTextField = GUI.fxTextField;

	public InputFunctionDrawing() throws HeadlessException {
		fValues = new ArrayList<>();
		xValues = new ArrayList<>();
		an = new ArrayList<>();
		bn = new ArrayList<>();
		integralX = new ArrayList<>();
		integralY = new ArrayList<>();
		color1 = Color.RED;
	}

	public double parsePeriod(String periodText) {
		if(periodText == null || periodText.isEmpty()){
			return 10;
		}
		try {
			Expression expression = new ExpressionBuilder(periodText)
					.variables("pi", "e")
					.build();
			if (periodText.contains("pi")) {
				expression.setVariable("pi", Math.PI);
			}
			if (periodText.contains("e")) {
				expression.setVariable("e", Math.E);
			}
			double period = expression.evaluate();
			if (period <= 0) {
				JOptionPane.showMessageDialog(this, "Okres nie moze byc ujemny lub zerowy", "Zly okres...", JOptionPane.ERROR_MESSAGE);
				return -1;
			}
			return period;
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "Nieprawidlowy okres", "Error", JOptionPane.ERROR_MESSAGE);
			return -1;
		}
	}

	boolean errorDisplayed = false;

	public void counting(String input) {
		fValues.clear();
		xValues.clear();
		period = parsePeriod(GUI.periodText);
		double xValue = 0;

		try {
			e = new ExpressionBuilder(input)
					.variables("pi", "e", "x")
					.build()
					.setVariable("pi", Math.PI)
					.setVariable("e", Math.E);

			final double step = 0.001;
			for (double x = 0; x < 10; x += step) {
				if (xValue > period) {
					xValue = 0;
				}
				e.setVariable("x", xValue);
				try {
					double result = e.evaluate();
					fValues.add(result);
					xValues.add(x);
				} catch (ArithmeticException | IllegalArgumentException | IllegalStateException ex) {
					JOptionPane.showMessageDialog(this, "Function is not absolutely integrable.", "1Error", JOptionPane.ERROR_MESSAGE);
					fxTextField.setText("");
				}
				xValue += step;
			}
		} catch (UnknownFunctionOrVariableException ex) {
			JOptionPane.showMessageDialog(this, "Unknown function or variable: " + ex.getLocalizedMessage(), "2Error", JOptionPane.ERROR_MESSAGE);
			fxTextField.setText("");
		}
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;

		color1 = GUI.getColor1();

		int width = getWidth();
		int height = getHeight();

		g2d.setColor(Color.BLACK);
		g2d.drawLine(50, height - 50, width - 50, height - 50);
		g2d.drawLine(50, 50, 50, height - 50);

		g2d.setColor(color1);
		g2d.setStroke(new BasicStroke(2.0f));

		int dataSize1 = xValues.size();
		int dataSize2 = integralX.size();
		double xMaxValue = getMaxValue(xValues);
		double xMinValue = getMinValue(xValues);
		double fMaxValue = getMaxValue(fValues);
		double fMinValue = getMinValue(fValues);
		double xScale = (width - 100) / (xMaxValue - xMinValue);
		double yScale = (height - 100) / (fMaxValue - fMinValue);

		for (int i = 0; i < dataSize1 - 1; i++) {
			int x1 = 50 + (int) ((xValues.get(i) - xMinValue) * xScale);
			int y1 = height - 50 - (int) ((fValues.get(i) - fMinValue) * yScale);
			int x2 = 50 + (int) ((xValues.get(i + 1) - xMinValue) * xScale);
			int y2 = height - 50 - (int) ((fValues.get(i + 1) - fMinValue) * yScale);
			g2d.drawLine(x1, y1, x2, y2);
		}

		g2d.setColor(Color.BLUE);
		for (int i = 0; i < dataSize2 - 1; i++) {
			int x1 = 50 + (int) ((integralX.get(i) - xMinValue) * xScale);
			int y1 = height - 50 - (int) ((integralY.get(i) - fMinValue) * yScale);
			int x2 = 50 + (int) ((integralX.get(i + 1) - xMinValue) * xScale);
			int y2 = height - 50 - (int) ((integralY.get(i + 1) - fMinValue) * yScale);
			g2d.drawLine(x1, y1, x2, y2);
		}

		if (fValues.size() != 0) {
			int numTicksX = 10;
			double xTickInterval = (width - 100) / (numTicksX - 1);
			for (int i = 0; i < numTicksX; i++) {
				int x = 50 + (int) (i * xTickInterval);
				g2d.drawLine(x, height - 50, x, height - 45);
				g2d.drawString(String.format("%.1f", xMinValue + i * (xMaxValue - xMinValue) / (numTicksX - 1)), x - 10, height - 30);
			}

			int numTicksY = 10;
			double yTickInterval = (height - 100) / (numTicksY - 1);
			for (int i = 0; i < numTicksY; i++) {
				int y = height - 50 - (int) (i * yTickInterval);
				g2d.drawLine(45, y, 50, y);
				g2d.drawString(String.format("%.1f", fMinValue + i * (fMaxValue - fMinValue) / (numTicksY - 1)), 10, y + 5);
			}
		}
	}

	private double getMaxValue(List<Double> values) {
		double maxValue = Double.MIN_VALUE;
		for (double value : values) {
			if (value > maxValue) {
				maxValue = value;
			}
		}
		return maxValue;
	}

	private double getMinValue(List<Double> values) {
		double minValue = Double.MAX_VALUE;
		for (double value : values) {
			if (value < minValue) {
				minValue = value;
			}
		}
		return minValue;
	}


	public void setN(int n) {
		N = n;
	}

	public double wspolczynnik(String input, int N, int wyborWspolczynnika) {
		double xp = 0;
		double xk = period;
		double dx = (xk - xp) / 1000.0;

		double s = 0.0;
		double st = 0.0;
		double result1 = 0, result2 = 0, result3 = 0;

		for (int i = 0; i < 1000; i++) {
			double x = xp + dx * i;
			double xValue = x;

			while (xValue >= period) {
				xValue -= period;
			}

			try {
				e.setVariable("x", xValue - dx);
				result1 = e.evaluate();
				if (wyborWspolczynnika == 0) {
					result1 *= Math.cos(2 * Math.PI * N * (xValue - dx) / period);
				} else if (wyborWspolczynnika == 1) {
					result1 *= Math.sin(2 * Math.PI * N * (xValue - dx) / period);
				}

				e.setVariable("x", xp);
				result2 = e.evaluate();
				if (wyborWspolczynnika == 0) {
					result2 *= Math.cos(2 * Math.PI * N * (xValue - dx) / period);
				} else if (wyborWspolczynnika == 1) {
					result2 *= Math.sin(2 * Math.PI * N * (xValue - dx) / period);
				}

				e.setVariable("x", xk);
				result3 = e.evaluate();
				if (wyborWspolczynnika == 0) {
					result3 *= Math.cos(2 * Math.PI * N * (xValue - dx) / period);
				} else if (wyborWspolczynnika == 1) {
					result3 *= Math.sin(2 * Math.PI * N * (xValue - dx) / period);
				}

				st += result1;

			} catch (UnknownFunctionOrVariableException ex) {
				JOptionPane.showMessageDialog(this, "Unknown function or variable: " + ex.getLocalizedMessage(), "3Error", JOptionPane.ERROR_MESSAGE);
				fxTextField.setText("");
				reset();
			} catch (ArithmeticException | IllegalArgumentException | IllegalStateException ex) {
				JOptionPane.showMessageDialog(this, "Error evaluating expression: " + ex.getMessage(), "4Error", JOptionPane.ERROR_MESSAGE);
				fxTextField.setText("");
				reset();;
			}
			s += result1;
		}

		s = dx / 6 * (result2 + result3 + 2 * s + 4 * st);
		return s * 2 / period;
	}


	public void szeregFouriera(String input) {
		double[] an = new double[this.N + 1];
		double[] bn = new double[this.N + 1];
		double a0 = wspolczynnik(input, 0, 0) / 2; // a0/2 term
		boolean isAbsolutelyIntegrable = true;
		errorDisplayed = false;

		for (int i = 1; i <= this.N; i++) {
			an[i] = wspolczynnik(input, i, 0);
			bn[i] = wspolczynnik(input, i, 1);
		}
		integralX.clear();
		integralY.clear();

		double xValue = 0;
		try{
			for (double x = 0; x < 10; x += 0.001) {
				xValue %= period;
				double y = a0;
				for (int i = 1; i <= this.N; i++) {
					y += an[i] * Math.cos(2 * i * Math.PI * xValue / period);
					y += bn[i] * Math.sin(2 * i * Math.PI * xValue / period);
				}

				integralX.add(x);
				integralY.add(y);

				xValue += 0.001;
			}
		} catch (Exception e) {
			isAbsolutelyIntegrable = false;
			reset();
		}

		if (!isAbsolutelyIntegrable) {
			JOptionPane.showMessageDialog(this, "Dirichlet condition unfulfilled: the function is not absolutely integrable.", "9Error", JOptionPane.ERROR_MESSAGE);
			fxTextField.setText("");
			reset();
		} else {
			a0_label.setText("a_0 = " + String.format("%.4f", wspolczynnik(input, 0, 0) / 2));
		}
	}

	public void reset() {
		fValues.clear();
		xValues.clear();
		an.clear();
		bn.clear();
		integralX.clear();
		integralY.clear();
		period = 1;
		N = 0;
		errorDisplayed = false;
		repaint();

	}
}
