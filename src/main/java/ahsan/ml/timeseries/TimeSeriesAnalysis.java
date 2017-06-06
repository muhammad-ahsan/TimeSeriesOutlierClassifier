package ahsan.ml.timeseries;

import java.time.Instant;
import java.util.Map;

public class TimeSeriesAnalysis {

	public static void main(final String[] args) {
		final TimeSeries timeseries = new TimeSeries(TimeBucket.HOUR);
		final OutlierClassifierImpl classifier = new OutlierClassifierImpl();
		final Map<OutlierStrategy, Boolean> results = classifier.getOutlierMap(Instant.now(), -2d, timeseries);
		results.forEach((key, value) -> System.out.println(key + " " + value));
	}
}
