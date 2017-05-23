package ahsan.ml.timeseries;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import com.google.common.base.Preconditions;

public class OutlierClassifierImpl implements OutlierClassifier {
	public OutlierClassifierImpl() {

	}

	@Override
	public Map<OutlierStrategy, Boolean> getOutlierMap(final Instant instant, final double value,
			final TimeSeries timeseries, final OutlierStrategy strategy) {
		final Map<OutlierStrategy, Boolean> result = new HashMap<>();
		result.put(strategy, isOutlier(instant, value, timeseries, strategy));
		return result;
	}

	@Override
	public boolean isOutlier(final Instant instant, final double value, final TimeSeries timeseries,
			final OutlierStrategy strategy) {
		final Map<Integer, DescriptiveStatistics> stats = timeseries.getStats();
		final int window = TimeSeriesUtilities.getPointTimeWindow(timeseries.getAggregateWindow(), instant);
		final DescriptiveStatistics windowStats = stats.get(window);
		Preconditions.checkArgument(windowStats != null);
		if (strategy.equals(OutlierStrategy.QUARTILE_BASED)) {
			return isQuartileBasedOutlier(value, windowStats);
		} else {
			return isTestBasedOutlier(value, windowStats);
		}

	}

	private boolean isQuartileBasedOutlier(final double value, final DescriptiveStatistics windowStats) {
		// Q1 – FACTOR × IQR >= Outlier >= Q3 + FACTOR × IQR
		final double q1 = windowStats.getPercentile(30);
		final double q3 = windowStats.getPercentile(60);
		Preconditions.checkArgument(q3 >= q1);
		final double iqr = q3 - q1;

		final double factor = 1.5;
		if (value < (q1 - factor * iqr)) {
			return true;
		}
		if (value > (q3 + factor * iqr)) {
			return true;
		}
		return false;
	}

	private boolean isTestBasedOutlier(final double value, final DescriptiveStatistics windowStats) {
		throw new UnsupportedOperationException();
	}

}
