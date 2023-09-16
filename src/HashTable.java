/**@author Aayush Bagrecha
 * @author Yash Shrikant
 * @version 1.0
 */
/*
 * The HashTable class represents a hash table data structure that stores
 * key-value pairs.
 * It provides methods for inserting, deleting, searching, and resizing the
 * table.
 */
public class HashTable {
    
    /**
     * The array that represents the underlying data structure of the hash table.
     * Entries are stored in this array to allow for efficient key-value pair storage
     * and retrieval. Each element in this array may hold an Entry object, which
     * contains a key, a value, and a tombstone flag to handle deletions.
     */
    public Entry[] table;  
    private int capacity;
    private int size;
    private static final double LOAD_FACTOR = 0.5;

    /**
     * Initializes a new instance of the HashTable class with the specified
     * initial capacity.
     *
     * @param initialCapacity
     *            The initial capacity of the hash table.
     */
    public HashTable(int initialCapacity) {
        capacity = initialCapacity;
        size = 0;
        table = new Entry[capacity];
    }


    /**
     * Inserts a key-value pair into the hash table.
     *
     * @param key
     *            The key to insert.
     * @param value
     *            The value associated with the key.
     * @return {@code true} if the insertion was successful, {@code false} if
     *         the key already exists.
     */
    public boolean insert(int key, Handle value) {
        if (size >= table.length * LOAD_FACTOR) {
            // Resize the table if load factor is exceeded
            resize();
        }

        int index = find(key); // find if the element already exists or not
                               // return -1 if it doesn't
                               // return actual index if it does

        if (index == -1) { // Indicates that the element is not present

            // Insert the key-value pair
            index = findEmptySlot(key);
            table[index] = new Entry(key, value);

            // System.out.println(table[index].value);
            size++;
            return true;
        }
        else { // Indicates that the element is already present in the
               // table
            return false;
        }
    }


    /**
     * Deletes a key-value pair from the hash table.
     *
     * @param key
     *            The key to delete.
     * @return {@code true} if the deletion was successful,
     *         {@code false} if the key was not found.
     */
    public boolean delete(int key) {
        int index = find(key);

        if (index != -1) {

            // Mark the entry as a tombstone
            table[index].isTombstone = true;
            size--;
            return true;
        }
        return false;
    }


    /**
     * Searches for a key in the hash table and returns its associated value.
     *
     * @param key
     *            The key to search for.
     * @return The value associated with the key,
     *         or {@code null} if the key was not found.
     */
    public Handle search(int key) {
        int index = find(key);

        if (index == -1) // element is not found in the hash table
            return null;
        else {
            return table[index].value;
        }
    }


    /**
     * Prints the contents of the hash table,
     * including tombstones and the total number of records.
     */
    public void printHashTable() {
        String output = "Hashtable:\n";
        for (int i = 0; i < capacity; i++) {
            if (table[i] != null) {
                if (table[i].isTombstone)
                    output += i + ": TOMBSTONE\n";
                else
                    output += i + ": " + table[i].key + "\n";
            }
        }
        System.out.println(output + "total records: " + size);
    }


    /**
     * Finds the index of a key in the hash table.
     *
     * @param key
     *            The key to find.
     * @return The index of the key if found, or {@code -1} if not found.
     */
    public int find(int key) {
        int index = hash(key);
        int step = (((key / capacity) % (capacity / 2)) * 2) + 1;
        int initialIndex = index;

        while (table[index] != null) {
            if (table[index].key == key && !table[index].isTombstone) {
                return index;
            }
            index = (index + step) % capacity;

            // If we've looped back to the initial index, stop searching.
            if (index == initialIndex) {
                break;
            }
        }

        return -1; // Key not found
    }


    /**
     * Gets the current capacity of the hash table.
     *
     * @return The current capacity.
     */
    public int getSize() {
        return capacity;
    }


    private int findEmptySlot(int key) {
        int index = hash(key);
        int step = (((key / capacity) % (capacity / 2)) * 2) + 1;

        while (table[index] != null && table[index].key != key
            && !table[index].isTombstone) {
            index = (index + step) % capacity;
        }
        return index;
    }


    private int hash(int key) {
        return key % capacity;
    }


    /**
     * Resizes the hash table when the load factor is exceeded.
     */
    public void resize() {
        int newCapacity = capacity * 2;
        Entry[] newTable = new Entry[newCapacity];

        System.out.println("Hash table expanded to " + newCapacity
            + " records");

        for (int i = 0; i < capacity; i++) {
            if (table[i] != null && !table[i].isTombstone) {
                int newIndex = hash(table[i].key); // Use the hash function for
                                                   // indexing
                while (newTable[newIndex] != null) {
                    newIndex = (newIndex + 1) % newCapacity;
                }
                newTable[newIndex] = table[i];
            }
        }

        table = newTable;
        capacity = newCapacity;
        // printHashTable();
    }

    /**
     * The Entry class represents a key-value pair stored in the hash table.
     */
    public class Entry {
        /**
         * The unique identifier (key) associated with this entry.
         */
        int key;

        /**
         * The handle to the value associated with this entry.
         */
        Handle value;

        /**
         * A flag indicating whether this entry has been marked as a tombstone. Tombstones
         * represent entries that have been deleted from the hash table but still occupy
         * a slot.
         */
        boolean isTombstone;

        /**
         * Initializes a new instance of the Entry class with the specified key
         * and value.
         *
         * @param key
         *            The key of the entry.
         * @param value
         *            The value associated with the key.
         */
        Entry(int key, Handle value) {
            this.key = key;
            this.value = value;
            this.isTombstone = false;
        }
    }

}
