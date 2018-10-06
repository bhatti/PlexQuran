package com.plexobject.quran;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.prefs.Preferences;

import javax.swing.JProgressBar;
import javax.swing.SwingWorker;

import org.apache.log4j.Logger;

import com.plexobject.quran.listeners.ApplicationLifecycleListener;
import com.plexobject.quran.listeners.ExceptionListener;
import com.plexobject.quran.listeners.NetworkActivityListener;
import com.plexobject.quran.listeners.QuranSearchListener;
import com.plexobject.quran.listeners.StatusListener;
import com.plexobject.quran.listeners.SuratSelectionListener;
import com.plexobject.quran.listeners.WindowResizeListener;
import com.plexobject.quran.model.Quran;
import com.plexobject.quran.model.Surat;

/**
 * Manages application state
 * 
 * @author shahzad bhatti
 * 
 */
public class ApplicationDelegate {
	private static final Preferences PREFERENCES = Preferences
	        .userNodeForPackage(ApplicationDelegate.class);

	private static final Logger LOGGER = Logger
	        .getLogger(ApplicationDelegate.class);

	private static ApplicationDelegate INSTANCE = new ApplicationDelegate();
	private final List<ApplicationLifecycleListener> applicationLifecycleListeners = new ArrayList<ApplicationLifecycleListener>();
	private final List<ExceptionListener> exceptionListeners = new ArrayList<ExceptionListener>();
	private final List<NetworkActivityListener> networkActivityListeners = new ArrayList<NetworkActivityListener>();

	private final List<StatusListener> statusListeners = new ArrayList<StatusListener>();
	private final List<WindowResizeListener> windowResizeListeners = new ArrayList<WindowResizeListener>();
	private final List<SuratSelectionListener> suratSelectionListeners = new ArrayList<SuratSelectionListener>();
	private final List<QuranSearchListener> quranSearchListeners = new ArrayList<QuranSearchListener>();
	private final JProgressBar progressBar = new JProgressBar();
	private final Quran quran;

	private class Task extends SwingWorker<Void, Void> {
		private Runnable work;

		private Task(Runnable r) {
			this.work = r;
		}

		/*
		 * Main task. Executed in background thread.
		 */
		@Override
		public Void doInBackground() {
			progressBar.setValue(0);
			progressBar.setVisible(true);
			progressBar.setStringPainted(true);
			progressBar.setIndeterminate(true);
			work.run();
			return null;
		}

		/*
		 * Executed in event dispatching thread
		 */
		@Override
		public void done() {
			// Toolkit.getDefaultToolkit().beep();
			progressBar.setVisible(false);
		}
	}

	private ApplicationDelegate() {
		quran = new Quran();
	}

	public static ApplicationDelegate getInstance() {
		return INSTANCE;
	}

	public void doInBackGround(Runnable r) {
		new Task(r).execute();
	}

	public void addExceptionListener(final ExceptionListener l) {
		if (!exceptionListeners.contains(l)) {
			exceptionListeners.add(l);
		}
	}

	public void addApplicationLifecycleListener(
	        final ApplicationLifecycleListener l) {
		if (!applicationLifecycleListeners.contains(l)) {
			applicationLifecycleListeners.add(l);
		}
	}

	public void addStatusListener(final StatusListener l) {
		if (!statusListeners.contains(l)) {
			statusListeners.add(l);
		}
	}

	public void addSuratSelectionListener(final SuratSelectionListener l) {
		if (!suratSelectionListeners.contains(l)) {
			suratSelectionListeners.add(l);
		}
	}

	public void fireSuratSelected(Surat surat) {
		final List<SuratSelectionListener> copyListeners = new ArrayList<SuratSelectionListener>(
		        suratSelectionListeners);
		for (final SuratSelectionListener l : copyListeners) {
			l.selectedSurat(surat);
		}
	}

	public void addQuranSearchListener(final QuranSearchListener l) {
		if (!quranSearchListeners.contains(l)) {
			quranSearchListeners.add(l);
		}
	}

	public void fireSearch(String text) {
		final List<QuranSearchListener> copyListeners = new ArrayList<QuranSearchListener>(
		        quranSearchListeners);
		for (final QuranSearchListener l : copyListeners) {
			l.search(text);
		}
	}

	public void fireResetSearch() {
		final List<QuranSearchListener> copyListeners = new ArrayList<QuranSearchListener>(
		        quranSearchListeners);
		for (final QuranSearchListener l : copyListeners) {
			l.resetSearch();
		}
	}

	public void addWindowResizeListener(final WindowResizeListener l) {
		if (!windowResizeListeners.contains(l)) {
			windowResizeListeners.add(l);
		}
	}

	public void fireApplicationStarted() {
		final List<ApplicationLifecycleListener> copyListeners = new ArrayList<ApplicationLifecycleListener>(
		        applicationLifecycleListeners);
		for (final ApplicationLifecycleListener l : copyListeners) {
			l.applicationStarted();
		}
		fireSuratSelected(quran.getSurat(0));
	}

	public void fireApplicationStopped() {
		final List<ApplicationLifecycleListener> copyListeners = new ArrayList<ApplicationLifecycleListener>(
		        applicationLifecycleListeners);
		for (final ApplicationLifecycleListener l : copyListeners) {
			l.applicationStopped();
		}
	}

	public void fireNetworkActivityStarted(final String uri) {
		try {
			URL url = new URL(uri);
			final String kind = url.getPath();
			final Map<String, Object> params = toParams(url);
			final List<NetworkActivityListener> copyListeners = new ArrayList<NetworkActivityListener>(
			        networkActivityListeners);

			for (final NetworkActivityListener l : copyListeners) {
				l.activityStarted(kind, params);
			}
		} catch (MalformedURLException e) {
			LOGGER.error("Failed to parse " + uri, e);
		}
	}

	public void fireNetworkActivityStopped(final String uri,
	        final Exception error) {
		try {
			URL url = new URL(uri);
			final String kind = url.getPath();
			final Map<String, Object> params = toParams(url);
			final List<NetworkActivityListener> copyListeners = new ArrayList<NetworkActivityListener>(
			        networkActivityListeners);

			for (final NetworkActivityListener l : copyListeners) {
				l.activityStopped(kind, params, error);
			}
		} catch (MalformedURLException e) {
			LOGGER.error("Failed to parse " + uri, e);
		}
	}

	public void fireException(final String action, final Exception e) {
		final List<ExceptionListener> copyListeners = new ArrayList<ExceptionListener>(
		        exceptionListeners);

		for (final ExceptionListener l : copyListeners) {
			l.onException(action, e);
		}
	}

	public void fireWindowResizedChanged(final int w, final int h) {
		final List<WindowResizeListener> copyListeners = new ArrayList<WindowResizeListener>(
		        windowResizeListeners);

		for (final WindowResizeListener l : copyListeners) {
			l.windowResized(w, h);
		}
	}

	public void fireStatsChanged(final String status) {
		final List<StatusListener> copyListeners = new ArrayList<StatusListener>(
		        statusListeners);

		for (final StatusListener l : copyListeners) {
			l.statusChanged(status);
		}
	}

	private static Map<String, Object> toParams(URL url) {
		final Map<String, Object> params = new HashMap<String, Object>();
		if (url.getQuery() != null) {
			String[] query = url.getQuery().split("&");
			for (String q : query) {
				String[] nv = q.split("=");
				if (nv.length == 2) {
					params.put(nv[0], nv[1]);
				}
			}
		}
		return params;
	}

	public String getPreference(String key) {
		return getPreference(key, null);
	}

	public String getPreference(String key, String def) {
		return PREFERENCES.get(key, def);
	}

	public void setPreference(String key, String value) {
		PREFERENCES.put(key, value);
	}

	public Quran getQuran() {
		return quran;
	}

	public JProgressBar getProgressBar() {
		return progressBar;
	}

}
