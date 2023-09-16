
/**
 * On my honor:
 * - I have not used source code obtained from another current or
 * former student, or any other unauthorized source, either
 * modified or unmodified.
 * - All source code and documentation used in my program is
 * either my original work, or was derived by me from the
 * source code published in the textbook for this course.
 * - I have not discussed coding details about this project with
 * anyone other than my partner (in the case of a joint
 * submission), instructor, ACM/UPE tutors, or the TAs assigned
 * to this course. I understand that I may discuss the concepts
 * of this program with other students, and that another student
 * may help me debug my program so long as neither of us writes
 * anything during the discussion or modifies any computer file
 * during the discussion. I have violated neither the spirit nor
 * letter of this restriction.
 *
 */
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * @author Aayush Bagrecha
 * @author Yash Shrikant
 * @version 1.0
 * 
 * The SemManager class manages seminar records using memory and a hash table.
 */
public class SemManager {
    private MemManager memoryManager;
    private HashTable hashTable;
    public String[] args;  //Stores a string argument denoting 
                           //the location of the input file

    /**
     * Initializes a new instance of the SemManager class.
     *
     * @param initialMemorySize
     *            The initial size of memory.
     * @param initialHashSize
     *            The initial size of the hash table.
     */
    public SemManager(int initialMemorySize, int initialHashSize) {
        memoryManager = new MemManager(initialMemorySize);
        hashTable = new HashTable(initialHashSize);
    }


    /**
     * The entry point of the program.
     *
     * @param args
     *            Command-line arguments containing initial memory size,
     *            initial hash size, and a command file.
     * @throws Exception
     *             If an error occurs during program execution.
     */
    public static void main(String[] args) throws Exception {
        if (args.length != 3) {
            System.err.println("Usage: java SemManager {initial-memory-size}"
                + " {initial-hash-size} {command-file}");
            System.exit(1);
        }

        int initialMemorySize = Integer.parseInt(args[0]);
        int initialHashSize = Integer.parseInt(args[1]);
        String commandFile = args[2];

        SemManager semManager = new SemManager(initialMemorySize,
            initialHashSize);
        semManager.processCommands(commandFile);
    }


    /**
     * Processes commands from a command file.
     *
     * @param commandFile
     *            The path to the command file.
     * @throws Exception
     *             If an error occurs while processing commands.
     */
    public void processCommands(String commandFile) throws Exception {
        try (Scanner scanner = new Scanner(new File(commandFile))) {
            while (scanner.hasNextLine()) {
                String command = scanner.nextLine().trim().replaceAll("\\s+",
                    " ");

                if (command.startsWith("insert")) {
                    int id = Integer.parseInt(command.split("\\s")[1]);
                    processInsertCommand(scanner, id);
                }
                else if (command.startsWith("delete")) {
                    int id = Integer.parseInt(command.split("\\s")[1]);
                    processDeleteCommand(scanner, id);
                }
                else if (command.startsWith("search")) {
                    int id = Integer.parseInt(command.split("\\s")[1]);
                    processSearchCommand(scanner, id);
                }
                else if (command.startsWith("print hashtable")) {
                    hashTable.printHashTable();
                }
                else if (command.startsWith("print blocks")) {
                    // memoryManager.printFreeBlocks();
                }
                else {
                    // System.out.println("command not found");
                }
            }
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    /**
     * Processes the "insert" command to add a seminar record.
     *
     * @param scanner
     *            The scanner for reading input.
     * @param id
     *            The ID of the seminar record to insert.
     * @throws Exception
     *             If an error occurs during insertion.
     */
    private void processInsertCommand(Scanner scanner, int id)
        throws Exception {
        String title = scanner.nextLine().trim();
        String dateLine = scanner.nextLine().trim().replaceAll("\\s+", " ");
        String dateTime = dateLine.split("\\s")[0];
        int length = Integer.parseInt(dateLine.split("\\s")[1]);
        short x = Short.parseShort(dateLine.split("\\s")[2]);
        short y = Short.parseShort(dateLine.split("\\s")[3]);
        int cost = Short.parseShort(dateLine.split("\\s")[4]);
        String[] keywords = scanner.nextLine().trim().replaceAll("\\s+", " ")
            .split(" ");
        String description = scanner.nextLine().trim();

        if (hashTable.search(id) != null) {
            System.out.println(
                "Insert FAILED - There is already a record with ID " + id);
            return;
        }

        // Convert keywords to ArrayList
        ArrayList<String> keywordList = new ArrayList<>();
        for (String keyword : keywords) {
            keywordList.add(keyword);
        }

        // Create a SeminarRecord
        SeminarRecord seminarRecord = new SeminarRecord(id, title, dateTime,
            length, x, y, cost, description, keywords);

        // Serialize the SeminarRecord and insert it into memory
        byte[] serializedRecord = seminarRecord.serialize();
        Handle handle = memoryManager.insert(serializedRecord,
            serializedRecord.length);

        // // Insert the handle into the hash table
        boolean insertStatus = hashTable.insert(id, handle);
        if (insertStatus) {
            System.out.println("Successfully inserted record with ID " + id);
            System.out.println(seminarRecord.toString());
            System.out.println("Size: " + serializedRecord.length);
        }
        else
            System.out.println(
                "Insert FAILED - There is already a record with ID " + id);
    }


    /**
     * Processes the "delete" command to remove a seminar record.
     *
     * @param scanner
     *            The scanner for reading input.
     * @param id
     *            The ID of the seminar record to delete.
     */
    private void processDeleteCommand(Scanner scanner, int id) {
        // Check if the key exists in the hash table
        Handle handle = hashTable.search(id);
        if (handle != null) {
            // Remove the record from the memory manager
            memoryManager.remove(handle);

            // Delete the entry from the hash table
            hashTable.delete(id);

            System.out.println("Record with ID " + id
                + " successfully deleted from database");
        }
        else {
            System.out.println("Delete FAILED -- There is no record with ID "
                + id);
        }
    }


    /**
     * Processes the "search" command to find and display a seminar record.
     *
     * @param scanner
     *            The scanner for reading input.
     * @param id
     *            The ID of the seminar record to search for.
     * @throws Exception
     *             If an error occurs during searching.
     */
    private void processSearchCommand(Scanner scanner, int id)
        throws Exception {
        // Search for the record in the hash table
        Handle handle = hashTable.search(id);
        if (handle != null) {
            System.out.println("Found record with ID " + id + ":");
            byte[] serializedRecord = new byte[handle.getRecordLength()];
            memoryManager.get(serializedRecord, handle,
                serializedRecord.length);
            SeminarRecord seminarRecord = SeminarRecord.deserialize(
                serializedRecord);
            System.out.println(seminarRecord.toString());
        }
        else {
            System.out.println("Search FAILED -- There is no record with ID "
                + id);
        }
    }


    /**
     * Checks if a number is a power of two.
     *
     * @param number
     *            The number to check.
     * @return {@code true} if the number is a power of two,
     *         otherwise {@code false}.
     */
    public static boolean isPowerOfTwo(int number) {
        if (number <= 0) {
            return false;
        }

        // Check if the number has only one bit set to 1
        // (i.e., it's a power of 2)
        return (number & (number - 1)) == 0;
    }


    /**
     * Checks if two numbers are both powers of two.
     *
     * @param num1
     *            The first number to check.
     * @param num2
     *            The second number to check.
     * @return {@code true} if both numbers are powers of two,
     *         otherwise {@code false}.
     */
    public static boolean arePowersOfTwo(int num1, int num2) {
        return isPowerOfTwo(num1) && isPowerOfTwo(num2);
    }
}
