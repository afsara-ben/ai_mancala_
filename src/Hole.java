/**
 *
 */
public class Hole {

    private int stones;
    private int row;
    private int column;
    private boolean is_storage;
    private boolean isBlue = true;

    /*
        constructors
    */
    public Hole(int stones, int row, int column, boolean is_storage) {

        this.stones = stones;
        this.row = row;
        this.column = column;
        this.is_storage = is_storage;

    }

    public Hole(int stones, int row, int column, boolean is_storage, boolean makeRed) {

        this.stones = stones;
        this.row = row;
        this.column = column;
        this.is_storage = is_storage;
        if (makeRed == true) setRed();

    }

    public Hole copy() {

        Hole copiedVersion = new Hole(this.stones, this.row, this.column, this.is_storage);

        if (!this.isBlue()) {
            copiedVersion.setRed();
        }

        return copiedVersion;
    }

    public void setRed() {
        isBlue = false;
    }

    public boolean isBlue() {
        return isBlue;
    }

    public int getStones() {
        return stones;
    }

    public void removeStones() {
        stones = 0;
    }

    public void addStone() {
        stones += 1;
    }

    public boolean isEmpty() {
        if (stones == 0) {
            return true;
        } else {
            return false;
        }
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public boolean getStorage() {
        return is_storage;
    }

    public String showPosition() {
        return "" + row + "," + column;
    }

    public String toString() {
        return "" + getStones();
    }
}
