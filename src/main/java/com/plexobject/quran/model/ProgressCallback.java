package com.plexobject.quran.model;

public interface ProgressCallback {
	void onProgress(long bytes, long total);
}
