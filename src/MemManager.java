/**
 * @author Aayush Bagrecha
 * @author Yash Shrikant
 * @version 1.0
 *
 *          /*
 *          The `MemManager` class is responsible for managing a memory pool,
 *          allocating and deallocating memory blocks, and providing handles for
 *          data
 *          insertion and retrieval. It ensures efficient utilization of memory
 *          by
 *          dynamically expanding the memory pool when needed.
 */
public class MemManager {
    private byte[] memoryPool;
    private int poolSize;
    private int freeBlockStart;
    private int freeBlockSize;

    /**
     * Constructs a new `MemManager` instance with an initial memory pool size.
     *
     * @param initialSize
     *            The initial size of the memory pool.
     */
    public MemManager(int initialSize) {
        memoryPool = new byte[initialSize];
        poolSize = initialSize;
        freeBlockStart = 0;
        freeBlockSize = initialSize;
    }


    /**
     * Inserts data into the memory pool and returns a handle to the inserted
     * data. If the available free space is insufficient, it expands the memory
     * pool.
     *
     * @param data
     *            The data to be inserted.
     * @param length
     *            The length of the data to be inserted.
     * @return A handle to the inserted data.
     */
    public Handle insert(byte[] data, int length) {
        if (length > freeBlockSize) {
            expandMemoryPool(length);
        }

        Handle handle = new Handle(freeBlockStart, length);
        System.arraycopy(data, 0, memoryPool, freeBlockStart, length);
        freeBlockStart += length;
        freeBlockSize -= length;

        return handle;
    }


    /**
     * Retrieves data from the memory pool using a provided handle and length.
     *
     * @param output
     *            The byte array where the retrieved data will be placed.
     * @param handle
     *            The handle to the data in the memory pool.
     * @param length
     *            The length of data to retrieve.
     */
    public void get(byte[] output, Handle handle, int length) {
        if (handle != null && handle.getRecordLength() == length) {
            System.arraycopy(memoryPool, handle.getStartingPosition(), output,
                0, length);
        }
    }


    /**
     * Removes data from the memory pool associated with the given handle.
     *
     * @param handle
     *            The handle to the data to be removed.
     */
    public void remove(Handle handle) {
        if (handle != null) {
            int blockIndex = handle.getStartingPosition();
            int recordLength = handle.getRecordLength();

            // Fill the memory block with zeros to "delete" the record
            for (int i = blockIndex; i < blockIndex + recordLength; i++) {
                memoryPool[i] = 0;
            }

            // Update free block information
            freeBlockStart = Math.min(freeBlockStart, blockIndex);
            freeBlockSize += recordLength;
        }
    }


    private void expandMemoryPool(int blockSize) {
        // Calculate the new size of the memory pool
        int newSize = poolSize;
        while (newSize < freeBlockStart + blockSize) {
            newSize *= 2;
            System.out.println("Memory pool expanded to " + newSize + " bytes");
        }

        // Create a new memory pool with the expanded size
        byte[] newMemoryPool = new byte[newSize];

        // Copy the existing data to the new memory pool
        System.arraycopy(memoryPool, 0, newMemoryPool, 0, poolSize);

        // Update the memory pool reference and size
        memoryPool = newMemoryPool;
        poolSize = newSize;

        // Update the freeBlockSize to account for the additional space
        freeBlockSize = poolSize - freeBlockStart;
    }
}
