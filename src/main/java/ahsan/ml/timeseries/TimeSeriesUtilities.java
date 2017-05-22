package ahsan.ml.timeseries;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.IsoFields;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.knowm.xchart.QuickChart;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;

import com.google.common.base.Preconditions;

public class TimeSeriesUtilities {

	public static int getPointTimeWindow(final String timeBucket, final Instant instant) {
		final ZonedDateTime zonedDateTime = instant.atZone(ZoneId.systemDefault());
		final int timeWindow = getTimeWindow(zonedDateTime, timeBucket);
		return timeWindow;
	}

	/**
	 * Available time windows for analysis
	 *
	 * @param zonedDateTime
	 * @param bucket
	 * @return
	 */
	private static int getTimeWindow(final ZonedDateTime zonedDateTime, final String bucket) {
		if (bucket.equals(TimeBucket.SECOND)) {
			return zonedDateTime.getSecond();
		} else if (bucket.equals(TimeBucket.MINUTE)) {
			return zonedDateTime.getMinute();
		} else if (bucket.equals(TimeBucket.HOUR)) {
			return zonedDateTime.getHour();
		} else if (bucket.equals(TimeBucket.DAY)) {
			return zonedDateTime.getDayOfMonth();
		} else if (bucket.equals(TimeBucket.WEEK)) {
			return zonedDateTime.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR);
		} else {
			return zonedDateTime.getMonthValue();
		}
	}

	public static void printTimeSeries(final Map<Instant, Double> timeseries) {

		final Map<Instant, Double> transformSeries = getTransformedTimeSeries(timeseries);
		final double minPoint = Collections.min(transformSeries.values());
		final double maxPoint = Collections.max(transformSeries.values());
		Preconditions.checkArgument(minPoint >= 0 && maxPoint >= 0);
		// denominator not approaching 0
		Preconditions.checkArgument(maxPoint > 0.000001);

		for (final Double measure : transformSeries.values()) {
			// Scale measure between 0 and 1
			final double scaledMeasure = 100 * (measure - minPoint) / maxPoint;
			for (int i = 0; i < (int) scaledMeasure; i++) {
				System.out.print(" ");
			}
			System.out.println("*");
		}
	}

	private static Map<Instant, Double> getTransformedTimeSeries(final Map<Instant, Double> timeseries) {
		final double minPoint = Collections.min(timeseries.values());
		final Map<Instant, Double> transformedTimeSeries = new HashMap<>();
		if (minPoint < 0) {
			for (final Entry<Instant, Double> entry : timeseries.entrySet()) {
				transformedTimeSeries.put(entry.getKey(), entry.getValue() + Math.abs(minPoint));
			}

		} else {
			transformedTimeSeries.putAll(timeseries);
		}
		return transformedTimeSeries;
	}

	public static void visualizeTimeSeries(final Collection<Double> collection) {

		final Double[] temp = collection.toArray(new Double[collection.size()]);
		final double[] yData = new double[2500];

		for (int i = 0; i < yData.length; i++) {
			yData[i] = temp[i];
			if (i == 2499) {
				break;
			}
		}
		final double[] xData = new double[yData.length];
		for (int i = 0; i < yData.length; i++) {
			xData[i] = i;
		}

		final XYChart chart = QuickChart.getChart("Time Series", "time", "y", "y(time)", xData, yData);
		final SwingWrapper window = new SwingWrapper(chart);
		window.displayChart();
	}

	public static Map<Instant, Double> getSyntheticTimeSeries() {
		final Map<Instant, Double> timeseries = new TreeMap<>();
		// 2 * PI * 5
		for (long degree = 0; degree <= 1000000; degree++) {
			final double y = Math.sin(degree * Math.PI / 180);
			// final double y = new Random().nextDouble();
			timeseries.put(Instant.ofEpochSecond(degree), y);
		}
		return timeseries;
	}
}
