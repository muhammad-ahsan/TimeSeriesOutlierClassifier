package ahsan.ml.timeseries;

import java.time.Instant;
import java.util.Map;

public interface OutlierClassifier {

	default boolean isOutlier(final double value, final double[] history) {
		throw new UnsupportedOperationException();
	}

	public boolean isOutlier(final Instant instant, final double value, final TimeSeries timeseries);

	public Map<OutlierStrategy, Boolean> getOutlierMap(final Instant instant, final double value,
			final TimeSeries timeseries, final OutlierStrategy strategy);
}
