package ngordnet.ngrams;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

/**
 * An object for mapping a year number (e.g. 1996) to numerical data. Provides
 * utility methods useful for data analysis.
 *
 * @author Josh Hug
 */
public class TimeSeries extends TreeMap<Integer, Double> {

    private static final int MIN_YEAR = 1400;
    private static final int MAX_YEAR = 2100;
    // TODO: Add any necessary static/instance variables.

    // map year to values
    /**
     * Constructs a new empty TimeSeries.
     */
    public TimeSeries() {
        super();
    }

    /**
     * Creates a copy of TS, but only between STARTYEAR and ENDYEAR,
     * inclusive of both end points.
     */
    public TimeSeries(TimeSeries ts, int startYear, int endYear) {
        super();

        for (Integer year : ts.keySet()) {
            if (year >= startYear && year <= endYear) {
                this.put(year, ts.get(year));
            }
        }
    }

    /**
     * Returns all years for this TimeSeries (in any order).
     */
    public List<Integer> years() {
        List<Integer> yearsList = new ArrayList<>(super.keySet());
        return yearsList;
    }

    /**
     * Returns all data for this TimeSeries (in any order).
     * Must be in the same order as years().
     */
    public List<Double> data() {
        List<Double> dataList = new ArrayList<>(super.values());
        return dataList;
    }

    /**
     * Returns the year-wise sum of this TimeSeries with the given TS. In other words, for
     * each year, sum the data from this TimeSeries with the data from TS. Should return a
     * new TimeSeries (does not modify this TimeSeries).
     *
     * If both TimeSeries don't contain any years, return an empty TimeSeries.
     * If one TimeSeries contains a year that the other one doesn't, the returned TimeSeries
     * should store the value from the TimeSeries that contains that year.
     */
    public TimeSeries plus(TimeSeries ts) {
        TimeSeries result = new TimeSeries();

        // the putAll method is used to add all mappings from the current TimeSeries (this) into the result TimeSeries.
        // TimeSeries totalPopulation = catPopulation.plus(dogPopulation);, this would refer to catPopulation because the plus method is called on the catPopulation
        // In the result.putAll(this); it is adding all mappings from this (catPopulation) into the result TimeSeries.

        // add all catPop into result
        // merge result with dogPop
        result.putAll(this);
        ts.forEach((year, value) -> result.merge(year, value, Double::sum));

        return result;
    }

    // create a new TimeSeries
    // add T1 and T2 values into 1
    // if T1 or T2 doesn't have a year, returned T1 should store the value that contains that year

    // expected: 1991: 0,
    //           1992: 100
    //           1994: 600
    //           1995: 500


    /**
     * Returns the quotient of the value for each year this TimeSeries divided by the
     * value for the same year in TS. Should return a new TimeSeries (does not modify this
     * TimeSeries).
     *
     * If TS is missing a year that exists in this TimeSeries, throw an
     * IllegalArgumentException.
     * If TS has a year that is not in this TimeSeries, ignore it.
     */
    public TimeSeries dividedBy(TimeSeries ts) {
        TimeSeries result = new TimeSeries();

        for (Integer year : this.keySet()) {
            if (!ts.containsKey(year)) {
                throw new IllegalArgumentException();

            }

            //result.putAll(this);
            //ts.forEach((year, value) -> result.merge(year, this.get(year), (a, b) -> a + b));

            double divisor = ts.get(year);
            if (divisor != 0) {
                result.put(year, this.get(year) / divisor);
            }
        }

        return result;
    }

    public static void main(String[] args) {
        TimeSeries catPopulation = new TimeSeries();
        //catPopulation.put(1991, 0.0);
        //catPopulation.put(1992, 100.0);
        catPopulation.put(1994, 200.0);
        TimeSeries dogPopulation = new TimeSeries();
        dogPopulation.put(1994, 400.0);
       // dogPopulation.put(1995, 500.0);
        TimeSeries totalPop = catPopulation.plus(dogPopulation);

        System.out.println(catPopulation);
        System.out.println(dogPopulation);
//
//        System.out.println(totalPop);
//        System.out.println(totalPop.years());

        TimeSeries dividePop = catPopulation.dividedBy(dogPopulation);
        System.out.println(dividePop);
    }
}
