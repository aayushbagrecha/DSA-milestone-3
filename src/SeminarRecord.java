import java.io.*;

/**
 * @author Aayush Bagrecha
 * @author Yash Shrikant
 * @version 1.0
 *
 * 
 *          The SeminarRecord class represents a seminar record and provides
 *          methods for serialization and deserialization.
 */
public class SeminarRecord implements Serializable {
    private int id;
    private String title;
    private String dateTime;
    private int length;
    private short x;
    private short y;
    private String description;
    private String[] keywords;
    private int cost;

    /**
     * Initializes a new instance of the SeminarRecord class.
     *
     * @param id
     *            The ID of the seminar record.
     * @param title
     *            The title of the seminar.
     * @param dateTime
     *            The date and time of the seminar.
     * @param length
     *            The length of the seminar.
     * @param x
     *            The X-coordinate of the seminar location.
     * @param y
     *            The Y-coordinate of the seminar location.
     * @param cost
     *            The cost of the seminar.
     * @param description
     *            The description of the seminar.
     * @param keywords
     *            The keywords associated with the seminar.
     */
    public SeminarRecord(
        int id,
        String title,
        String dateTime,
        int length,
        short x,
        short y,
        int cost,
        String description,
        String[] keywords) {
        this.id = id;
        this.title = title;
        this.dateTime = dateTime;
        this.length = length;
        this.x = x;
        this.y = y;
        this.cost = cost;
        this.description = description;
        this.keywords = keywords;
    }


    /**
     * Gets the ID of the seminar record.
     *
     * @return The ID of the seminar record.
     */
    public int getId() {
        return id;
    }


    /**
     * Gets the title of the seminar.
     *
     * @return The title of the seminar.
     */
    public String getTitle() {
        return title;
    }


    /**
     * Gets the date and time of the seminar.
     *
     * @return The date and time of the seminar.
     */
    public String getDateTime() {
        return dateTime;
    }


    /**
     * Gets the length of the seminar.
     *
     * @return The length of the seminar.
     */
    public int getLength() {
        return length;
    }


    /**
     * Gets the X-coordinate of the seminar location.
     *
     * @return The X-coordinate of the seminar location.
     */
    public short getX() {
        return x;
    }


    /**
     * Gets the Y-coordinate of the seminar location.
     *
     * @return The Y-coordinate of the seminar location.
     */
    public short getY() {
        return y;
    }


    /**
     * Gets the description of the seminar.
     *
     * @return The description of the seminar.
     */
    public String getDescription() {
        return description;
    }


    /**
     * Gets the keywords associated with the seminar.
     *
     * @return The keywords associated with the seminar.
     */
    public String[] getKeywords() {
        return keywords;
    }


    /**
     * Gets the cost of the seminar.
     *
     * @return The cost of the seminar.
     */
    public int getCost() {
        return cost;
    }


    /**
     * Deserializes a byte array into a SeminarRecord object.
     *
     * @param inputbytes
     *            The byte array to deserialize.
     * @return The deserialized SeminarRecord object.
     * @throws Exception
     *             If an error occurs during deserialization.
     */
    public static SeminarRecord deserialize(byte[] inputbytes)
        throws Exception {
        ByteArrayInputStream bis = new ByteArrayInputStream(inputbytes);
        try (ObjectInputStream inputStream = new ObjectInputStream(bis)) {
            int id = inputStream.readInt();
            String title = inputStream.readUTF();
            String date = inputStream.readUTF();
            int length = inputStream.readInt();
            short x = inputStream.readShort();
            short y = inputStream.readShort();
            int cost = inputStream.readInt();

            int numKeywords = inputStream.readInt();
            String[] keywords = new String[numKeywords];
            for (int i = 0; i < numKeywords; i++) {
                keywords[i] = inputStream.readUTF();
            }

            String desc = inputStream.readUTF();

            return new SeminarRecord(id, title, date, length, x, y, cost, desc,
                keywords);
        }
    }


    /**
     * Serializes the SeminarRecord object into a byte array.
     *
     * @return The serialized byte array representation of the
     *         SeminarRecord object.
     * @throws Exception
     *             If an error occurs during serialization.
     */
    public byte[] serialize() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try (ObjectOutputStream outputStream = new ObjectOutputStream(out)) {
            outputStream.writeInt(id);
            outputStream.writeUTF(title);
            outputStream.writeUTF(dateTime);
            outputStream.writeInt(length);
            outputStream.writeShort(x);
            outputStream.writeShort(y);
            outputStream.writeInt(cost);

            // Write the number of keywords and then each keyword
            outputStream.writeInt(keywords.length);
            for (String keyword : keywords) {
                outputStream.writeUTF(keyword);
            }

            outputStream.writeUTF(description);
        }
        return out.toByteArray();
    }


    /**
     * Returns a string representation of the SeminarRecord object.
     *
     * @return A string representation of the SeminarRecord object.
     */
    public String toString() {
        int i;
        String mykeys = "";
        for (i = 0; i < keywords.length; i++) {
            mykeys += keywords[i];
            if (i != keywords.length - 1)
                mykeys += ", ";
        }
        return "ID: " + id + ", Title: " + title + "\nDate: " + dateTime
            + ", Length: " + length + ", X: " + x + ", Y: " + y + ", Cost: "
            + cost + "\nDescription: " + description + "\nKeywords: " + mykeys;
    }
}
