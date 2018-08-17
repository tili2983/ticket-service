package model;

public class Seat implements Comparable{
    private int seatId;
    private String seatCode;
    private int row;
    private double rate;
    private int seatState;

    public Seat(int seatId, int row, double rate) {
        this.seatId = seatId;
        this.row = row;
        this.rate = rate;
        this.seatCode = String.format("%d-%d", this.row, this.seatId);
    }

    public int getSeatId() {
        return seatId;
    }

    public String getSeatCode() {
        return seatCode;
    }

    public int getRow() {
        return row;
    }

    public double getRate() {
        return rate;
    }

    public int getSeatState() {
        return seatState;
    }

    public void setSeatState(int seatState) {
        this.seatState = seatState;
    }

    @Override
    public int hashCode() {
        return seatCode.hashCode();
    }

    @Override
    public boolean equals(Object o){
        if (o instanceof Seat) {
            return this.seatCode.equals(((Seat)o).seatCode);
        }

        throw new IllegalArgumentException(String.format("Invalid parameter type: %s", o.getClass()));
    }

    public int compareTo(Object o) {
        if (o instanceof Seat) {
            return Double.compare(((Seat)o).rate, this.rate);
        }

        throw new IllegalArgumentException(o.getClass() + "not comparable to " + this.getClass());
    }
}
