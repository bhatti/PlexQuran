package com.plexobject.quran.metrics;

import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.log4j.Logger;

import com.plexobject.quran.config.Configuration;

/**
 * This class collects timing data but it's not thread safe.
 * 
 * 
 */
public class Timer {
    private static final Logger LOGGER = Logger.getLogger(Timer.class);
    private static final long LOGGING_THRESHOLD_IN_MILLISECS = Configuration
            .getInstance().getInteger("timer.logging.threshold.millis", 1000);
    private final Metric metric;

    private static class TimingData {
        private long started;
        private long ended;

        private TimingData() {
            this.started = System.currentTimeMillis();
        }

        private void stop() {
            this.ended = System.currentTimeMillis();
        }

        private long getTotalDurationInMillisecs() {
            return ended - started;
        }
    }

    private TimingData currentTimingData;
    private Collection<TimingData> elapsedTimingData;

    // This class can only be created by Metric and it's using
    // package scope for testing.
    Timer(final Metric metric) {
        this.metric = metric;
        this.currentTimingData = new TimingData();
    }

    /**
     * stop - stops the timer
     * 
     * @throws IllegalStateException
     *             - if timer is used again after stop
     */
    public void stop() {
        stop("");
    }

    /**
     * stop - stops the timer
     * 
     * @throws IllegalStateException
     *             - if timer is used again after stop
     */
    public void stop(final String logMessage) {
        if (currentTimingData == null) {
            throw new IllegalStateException("Timer has already been stopped");
        }
        currentTimingData.stop();

        final int totalCalls = elapsedTimingData == null ? 1
                : elapsedTimingData.size() + 1;
        long totalDurationInMillisecs = currentTimingData
                .getTotalDurationInMillisecs();

        if (elapsedTimingData != null) {
            for (TimingData data : elapsedTimingData) {
                totalDurationInMillisecs += data.getTotalDurationInMillisecs();
            }
        }
        metric.finishedTimer(totalCalls, totalDurationInMillisecs);

        if ((logMessage != null && logMessage.length() > 0)
                || totalDurationInMillisecs > LOGGING_THRESHOLD_IN_MILLISECS
                && LOGGER.isInfoEnabled()) {
            LOGGER.info("ended " + logMessage + " for metrics " + metric
                    + getSystemStats());
        }
        currentTimingData = null;

    }

    /**
     * lapse - marks current timing and starts another timer
     * 
     * @throws IllegalStateException
     *             - if timer is already stopped
     */
    public void lapse() {
        lapse("");
    }

    /**
     * lapse - marks current timing and starts another timer
     * 
     * @param logMessage
     *            - will print log message if duration exceeds logging threshold
     * @throws IllegalStateException
     *             - if timer is already stopped
     */
    public void lapse(final String logMessage) {
        if (currentTimingData == null) {
            throw new IllegalStateException("Timer has already been stopped");
        }
        currentTimingData.stop();
        getElapsedTimingData().add(currentTimingData);
        if (currentTimingData.getTotalDurationInMillisecs() > LOGGING_THRESHOLD_IN_MILLISECS
                && LOGGER.isInfoEnabled()) {
            LOGGER.info("lapsed " + logMessage + " for metrics " + metric);
        }
        currentTimingData = new TimingData();
    }

    private Collection<TimingData> getElapsedTimingData() {
        if (elapsedTimingData == null) {
            elapsedTimingData = new ArrayList<TimingData>();
        }
        return elapsedTimingData;
    }

    public static String getSystemStats() {
        final StringBuilder sb = new StringBuilder();
        Runtime runtime = Runtime.getRuntime();
        sb.append(", load: ").append(
                String.format("%.2f", ManagementFactory
                        .getOperatingSystemMXBean().getSystemLoadAverage()));
        sb.append(", memory: ").append(runtime.freeMemory() / 1024 / 1024)
                .append("M/").append(runtime.totalMemory() / 1024 / 1024 + "M");
        sb.append(", threads: ").append(Thread.activeCount());
        return sb.toString();
    }
}
