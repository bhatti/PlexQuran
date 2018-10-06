package com.plexobject.quran.listeners;

import java.util.Map;

public interface NetworkActivityListener {
	void activityStarted(String kind, Map<String, Object> params);

	void activityStopped(String kind, Map<String, Object> params,
			Exception error);
}
