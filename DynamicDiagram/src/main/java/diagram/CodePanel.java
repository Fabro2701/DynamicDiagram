package diagram;

import java.awt.Dimension;

import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Style;

public class CodePanel extends JTextPane{
	public CodePanel() {
		 this.setPreferredSize(new Dimension(600,500));
	}
	public void insertString(String s) {
		Document doc = this.getDocument();
		try {
			doc.insertString(doc.getLength(), s, null);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}
	public void clear() {
		this.setText("");
	}
	public void stylize() {
		String s = this.getText();
		String lines[] = s.split(System.lineSeparator());
		StringBuilder sb = new StringBuilder();
		
		int tab=0;
		for(String line:lines) {
			if(line.contains("}"))tab--;
			for(int i=0;i<tab;i++)sb.append("\t");
			sb.append(line).append('\n');
			if(line.contains("{"))tab++;
		}
		
		this.setText(sb.toString());
	}
}
