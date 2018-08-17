package rater;

import model.Venue;
import org.apache.log4j.Logger;

public class FrontMiddleBetterRater implements Rater {
    static Logger logger = Logger.getLogger(FrontMiddleBetterRater.class);

    public double rateSeat(int seatIndex, int rowIndex, Venue venue) {
        double seatRating = rateSeatOfARow(seatIndex, venue.getRowSize());
        double rowRating = rateRow(rowIndex, venue.getRows());
        double rating = (seatRating + rowRating) / 2.0d;
        logger.debug("raw overall rating: " + rating);
        return Math.round(rating * 1000d) / 1000d;
    }

    private double rateRow(int rowIndex, int numberOfRow) {
        double result = (double) (numberOfRow + 1 - rowIndex) / (double) numberOfRow;
        logger.debug("row rate: " + result);
        return result;
    }

    private double rateSeatOfARow(int seatIndex, int rowSize) {
        double rating;
        if (rowSize % 2 == 1) {
            int mid = rowSize / 2 + 1;
            if (seatIndex == mid) {
                rating = 1.0d;
            } else if (seatIndex < mid) {
                rating = (double)seatIndex / (double) mid;
            } else { // seatIndex > mid
                rating = (double)(rowSize + 1 - seatIndex) / (double) mid;
            }
        } else {
            int mid = rowSize / 2;
            if (seatIndex == mid || seatIndex == mid + 1) {
                rating = 1.0d;
            } else if (seatIndex < mid) {
                rating = (double)seatIndex / (double) mid;
            } else {
                rating = (double) (rowSize + 1 - seatIndex) / (double) mid;
            }
        }
        logger.debug("seat rate: " + rating);
        return rating;
    }
}
