
package model;

public class Venue {
    private int rowSize;
    private int rows;

    public Venue(int rowSize, int rows) {
        this.rowSize = rowSize;
        this.rows = rows;
    }

    public int getRowSize() {
        return rowSize;
    }

    public int getRows() {
        return rows;
    }

    public int getMaxSeats() {
        return this.rowSize * rows;
    }

    public String toString() {
        return String.format("Venue(rowSize: %d, numberOfRows: %d)", this.rowSize, this.rows);
    }
}
