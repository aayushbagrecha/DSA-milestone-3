/**
 * @author Aayush Bagrecha
 * @author Yash Shrikant
 * @version 1.0
 */

/**
 * The `Handle` class represents a handle to a specific data block in the
 * memory pool. It includes information about the starting position of the
 * data block and its length.
 */
public class Handle {
    private int startingPosition;
    private int recordLength;

    /**
     * Constructs a new `Handle` instance with the specified starting position
     * and record length.
     *
     * @param startingPosition
     *            The starting position of the data block.
     * @param recordLength
     *            The length of the data block.
     */
    public Handle(int startingPosition, int recordLength) {
        this.startingPosition = startingPosition;
        this.recordLength = recordLength;
    }


    /**
     * Gets the starting position of the data block.
     *
     * @return The starting position of the data block.
     */
    public int getStartingPosition() {
        return startingPosition;
    }


    /**
     * Sets the starting position of the data block.
     *
     * @param startingPosition
     *            The starting position to set.
     */
    public void setStartingPosition(int startingPosition) {
        this.startingPosition = startingPosition;
    }


    /**
     * Gets the length of the data block.
     *
     * @return The length of the data block.
     */
    public int getRecordLength() {
        return recordLength;
    }


    /**
     * Sets the length of the data block.
     *
     * @param recordLength
     *            The length to set for the data block.
     */
    public void setRecordLength(int recordLength) {
        this.recordLength = recordLength;
    }
}
