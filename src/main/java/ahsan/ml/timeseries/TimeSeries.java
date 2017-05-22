package ahsan.ml.timeseries;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import com.google.common.base.Preconditions;

public class TimeSeries {
	private final Map<Instant, Double> data;
	private final Map<Integer, DescriptiveStatistics> stats;
	private final String aggregateWindow;

	/**
	 * Read the series training from external file
	 *
	 * @param path
	 * @param timeBucket
	 */
	public TimeSeries(final String path, final String timeBucket) {
		throw new UnsupportedOperationException("Not supported yet");
	}

	public TimeSeries(final String timeBucket) {
		this(TimeSeriesUtilities.getSyntheticTimeSeries(), timeBucket);
		// Show time series using separate thread
		TimeSeriesUtilities.visualizeTimeSeries(data.values());
	}

	public TimeSeries(final Map<Instant, Double> data, final String timeBucket) {
		Preconditions.checkArgument(data != null && !data.isEmpty());
		this.data = data;
		this.stats = new HashMap<>();
		computeWindowStats(timeBucket);
		this.aggregateWindow = timeBucket;
	}

	/**
	 * Statistics computed as batch
	 *
	 */
	private void computeWindowStats(final String timeBucket) {
		// Partitioning series using time bucket
		for (final Entry<Instant, Double> point : getData().entrySet()) {
			final int timeWindow = TimeSeriesUtilities.getPointTimeWindow(timeBucket, point.getKey());
			getStats().computeIfAbsent(timeWindow, $ -> new DescriptiveStatistics()).addValue(point.getValue());
		}
	}

	public String getAggregateWindow() {
		return aggregateWindow;
	}

	public Map<Integer, DescriptiveStatistics> getStats() {
		return stats;
	}

	public Map<Instant, Double> getData() {
		return data;
	}
}
