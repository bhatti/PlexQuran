package com.plexobject.quran.ui;

import javax.swing.JTextPane;
import javax.swing.LookAndFeel;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;

public class MultilineHeader extends JTextPane {
	private static final long serialVersionUID = 1L;

	public MultilineHeader() {
		// StyledDocument doc = getStyledDocument();
		// addStylesToDocument(doc);
		setContentType("text/html");
		// SimpleAttributeSet set = new SimpleAttributeSet();
		// StyleConstants.setSpaceAbove(set, 20);
		// StyleConstants.setLeftIndent(set, 10);
		// StyleConstants.setLineSpacing(set, 60);

		// StyledDocument doc= getStyledDocument();
		MutableAttributeSet attr = new SimpleAttributeSet();
		StyleConstants.setLineSpacing(attr, -0.2f); // NOTE: negative value.
	}

	public void updateUI() {
		super.updateUI();
		// this.setLineWrap(true);
		// this.setWrapStyleWord(true);
		this.setEditable(false);
		this.setFocusable(false);
		// make text area look like a table header
		LookAndFeel.installColorsAndFont(this, "TableHeader.background",
		        "TableHeader.foreground", "TableHeader.font");
		LookAndFeel.installBorder(this, "TableHeader.cellBorder");
		// this.setHighlighter(null);
		// this.setCursor(null);
		// this.setOpaque(false);
	}

	protected void addStylesToDocument(StyledDocument doc) {
		// Initialize some styles.
		Style def = StyleContext.getDefaultStyleContext().getStyle(
		        StyleContext.DEFAULT_STYLE);

		Style regular = doc.addStyle("regular", def);
		StyleConstants.setFontFamily(def, "SansSerif");

		Style s = doc.addStyle("italic", regular);
		StyleConstants.setItalic(s, true);

		s = doc.addStyle("bold", regular);
		StyleConstants.setBold(s, true);

		s = doc.addStyle("small", regular);
		StyleConstants.setFontSize(s, 10);

		s = doc.addStyle("large", regular);
		StyleConstants.setFontSize(s, 16);
	}

}
