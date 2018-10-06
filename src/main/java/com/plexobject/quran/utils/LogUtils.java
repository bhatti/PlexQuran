package com.plexobject.quran.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Properties;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.SimpleLayout;

import com.plexobject.quran.persistence.DatabaseManagerBdb;
import com.plexobject.quran.ui.MainWindow;

public class LogUtils {
	public static void configureLog(Logger logger) throws IOException {
		try {
			Logger root = Logger.getRootLogger();
			root.setLevel(Level.INFO);

			root.addAppender(new ConsoleAppender(new PatternLayout(
			        PatternLayout.TTCC_CONVERSION_PATTERN)));

			logger.setLevel(Level.INFO);
			logger.addAppender(new ConsoleAppender(new SimpleLayout()));
			Properties props = new Properties();
			try {
				InputStream configStream = MainWindow.class
				        .getResourceAsStream("/log4j.properties");
				props.load(configStream);
				configStream.close();
			} catch (IOException e) {
				System.out.println("Error: Cannot laod configuration file ");
			}
			File logFile = new File(DatabaseManagerBdb.DB_DIR, "plexquran.log");

			// props.setProperty("log4j.rootLogger", "INFO, file");
			props.setProperty("log4j.appender.file",
			        "org.apache.log4j.RollingFileAppender");
			props.setProperty("log4j.appender.file.layout",
			        "org.apache.log4j.PatternLayout");

			props.setProperty("log4j.appender.file.file",
			        logFile.getAbsolutePath());

			props.setProperty("log4j.appender.CONSOLE",
			        "org.apache.log4j.ConsoleAppender");
			// props.setProperty("log4j.appender.CONSOLE.conversionPattern",
			// "%m%n");
			props.setProperty("log4j.appender.CONSOLE.layout",
			        "org.apache.log4j.PatternLayout");
			props.setProperty("log4j.rootLogger", "INFO, CONSOLE");

			PropertyConfigurator.configure(props);

			FileAppender appender = new FileAppender(new SimpleLayout(),
			        logFile.getAbsolutePath(), false);
			logger.addAppender(appender);
		} catch (Exception e) {
		}
	}

	static String _getStackTrace(Throwable aThrowable) {
		final Writer result = new StringWriter();
		final PrintWriter printWriter = new PrintWriter(result);
		aThrowable.printStackTrace(printWriter);
		return result.toString();
	}

	public static String getStackTrace(Throwable aThrowable) {
		final StringBuilder result = new StringBuilder("XXb: ");
		result.append(aThrowable.toString());
		final String NEW_LINE = System.getProperty("line.separator");
		result.append(NEW_LINE);
		int n = 0;
		for (StackTraceElement element : aThrowable.getStackTrace()) {
			result.append(element);
			// String t = element.toString();
			// int start = t.length() - 40;
			// int end = t.length();
			// t = t.substring(start, end);
			// result.append( t );
			result.append(element);
			result.append(NEW_LINE);
			if (++n == 20)
				break;
		}
		return result.toString();
	}
}
