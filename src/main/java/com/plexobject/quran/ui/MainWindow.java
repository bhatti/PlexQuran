package com.plexobject.quran.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.UIManager;

import org.apache.log4j.Logger;

import com.plexobject.quran.ApplicationDelegate;
import com.plexobject.quran.listeners.ExceptionListener;
import com.plexobject.quran.listeners.StatusListener;
import com.plexobject.quran.utils.ButtonUtils;
import com.plexobject.quran.utils.LogUtils;
import com.plexobject.quran.utils.Preferences;

// See http://corpus.quran.com/
// See http://www.tyndalearchive.com/tabs/lane/
// See http://tanzil.net/wiki/
public class MainWindow extends JFrame {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(MainWindow.class);

	public static final String LANGUAGE_PROPERTY_NAME = "language";
	private static final String TITLE = "PlexQuran - Quran analytical Tool";
	private JTabbedPane tabbedPanel;

	public MainWindow() {
		super(TITLE);
		setIconImage(ButtonUtils.loadImage("quran.png").getImage());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		setPreferredSize(new Dimension(1200, 1000));
		getContentPane().setLayout(new BorderLayout());

		// setJMenuBar(createMenuBar());
		JToolBar toolbar = createToolBar();
		getContentPane().add(toolbar, BorderLayout.NORTH);
		getContentPane().setBackground(toolbar.getBackground());
		final JLabel statusL = new JLabel("");
		ApplicationDelegate.getInstance().addStatusListener(
		        new StatusListener() {
			        @Override
			        public void statusChanged(String m) {
				        statusL.setText(m);
			        }
		        });

		tabbedPanel = new JTabbedPane();
		tabbedPanel.setTabPlacement(JTabbedPane.TOP);
		tabbedPanel.addTab("Browse", new BrowseView());
		tabbedPanel.addTab("Search", new SearchView());
		tabbedPanel.addTab("Topics", new TopicsView());
		tabbedPanel.addTab("Words By Alphabets", new InitialsView());
		tabbedPanel.addTab("Stats", new StatsView());

		getContentPane().add(tabbedPanel, BorderLayout.CENTER);
		JPanel southP = new JPanel();
		southP.setLayout(new FlowLayout());
		southP.add(ApplicationDelegate.getInstance().getProgressBar());
		southP.add(statusL);
		getContentPane().add(southP, BorderLayout.SOUTH);

		pack();
		setLocationRelativeTo(null);
		setVisible(true);

		ApplicationDelegate.getInstance().addExceptionListener(
		        new ExceptionListener() {
			        @Override
			        public void onException(String action, Exception e) {
				        logger.error(action, e);
			        }
		        });
		ApplicationDelegate.getInstance().fireApplicationStarted();
	}

	private JToolBar createToolBar() {
		JToolBar toolbar = new JToolBar();
		toolbar.setFloatable(false);
		toolbar.setRollover(true);

		JButton browseB = new JButton();
		browseB.setToolTipText("Browse Quran");
		browseB.setIcon(ButtonUtils.loadImage("home.png"));
		browseB.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				tabbedPanel.setSelectedIndex(0);
			}
		});
		browseB.setActionCommand("browse");
		toolbar.add(browseB);

		JButton searchB = new JButton();
		searchB.setToolTipText("Search Quran");
		searchB.setIcon(ButtonUtils.loadImage("search.png"));
		searchB.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				tabbedPanel.setSelectedIndex(1);
			}
		});
		searchB.setActionCommand("search");
		toolbar.add(searchB);

		JButton topicsB = new JButton();
		topicsB.setToolTipText("Topics");
		topicsB.setIcon(ButtonUtils.loadImage("topics.png"));
		topicsB.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				tabbedPanel.setSelectedIndex(2);
			}
		});
		topicsB.setActionCommand("topics");
		toolbar.add(topicsB);

		JButton wordsB = new JButton();
		wordsB.setToolTipText("Words By Alphabets");
		wordsB.setIcon(ButtonUtils.loadImage("words.png"));
		wordsB.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				tabbedPanel.setSelectedIndex(3);
			}
		});
		wordsB.setActionCommand("words");
		toolbar.add(wordsB);

		JButton statsB = new JButton();
		statsB.setToolTipText("Quran Statistics");
		statsB.setIcon(ButtonUtils.loadImage("stats.png"));
		statsB.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				tabbedPanel.setSelectedIndex(4);
			}
		});
		statsB.setEnabled(true);
		statsB.setActionCommand("stats");
		toolbar.add(statsB);

		toolbar.addSeparator();

		JButton exitB = new JButton();
		exitB.setToolTipText("Exit Application");
		exitB.setIcon(ButtonUtils.loadImage("exit.png"));
		exitB.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		exitB.setEnabled(true);
		exitB.setActionCommand("exit");
		toolbar.add(exitB);
		return toolbar;
	}

	JMenuBar createMenuBar() {
		JMenuBar menuBar = new JMenuBar();

		JMenu fileM = new JMenu("File");
		fileM.setMnemonic(KeyEvent.VK_D);
		menuBar.add(fileM);

		JMenuItem homeM = new JMenuItem("Home", KeyEvent.VK_H);
		homeM.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H, Toolkit
		        .getDefaultToolkit().getMenuShortcutKeyMask()));
		fileM.add(homeM);
		homeM.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				tabbedPanel.setSelectedIndex(0);
			}
		});
		homeM.setActionCommand("home");

		fileM.addSeparator();

		JMenuItem statsM = new JMenuItem("Statistics", KeyEvent.VK_S);
		statsM.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, Toolkit
		        .getDefaultToolkit().getMenuShortcutKeyMask()));
		fileM.add(statsM);
		statsM.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				tabbedPanel.setSelectedIndex(1);
			}
		});
		homeM.setActionCommand("stats");

		JMenuItem exitMenuItem = new JMenuItem("Exit", KeyEvent.VK_X);
		exitMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X,
		        Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		exitMenuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		exitMenuItem.setActionCommand("Exit");

		JMenuItem aboutMenuItem = new JMenuItem("About", KeyEvent.VK_A);
		aboutMenuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		aboutMenuItem.setActionCommand("About");

		return menuBar;

	}

	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					LogUtils.configureLog(logger);
					Preferences.load();

					// Use the System look and feel
					UIManager.setLookAndFeel(UIManager
					        .getSystemLookAndFeelClassName());

					Double jvmVersion = new Double(System
					        .getProperty("java.specification.version"));
					if (jvmVersion.doubleValue() < 1.6) {
						JOptionPane
						        .showMessageDialog(
						                null,
						                "This application requires Java 1.6 or above, please download latest version from http://java.com/en/download/index.jsp",
						                "Warning", JOptionPane.ERROR_MESSAGE);

						System.exit(1);
					} else {
						new MainWindow();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
