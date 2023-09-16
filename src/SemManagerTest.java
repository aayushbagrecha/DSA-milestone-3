import student.TestCase;
import org.junit.Test;

/**
 * @author Aayush Bagrecha
 * @author Yash Shrikant
 * @version 1.0
 * 
 */
public class SemManagerTest extends TestCase {
    /**
     * Test for SemManager with valid arguments,
     * ensuring no error messages.
     * 
     * @throws Exception
     */
    @Test
    public void testValidArguments() throws Exception {

        String[] args = { "64", "4", "input.txt" };
        SemManager.main(args);
    }


    /**
     * Test case to check if a
     * given number is a power of two.
     */
    @Test
    public void testIsPowerOfTwo() {
        assertTrue(SemManager.isPowerOfTwo(1)); // 2^0 = 1
        assertTrue(SemManager.isPowerOfTwo(2)); // 2^1 = 2
        assertTrue(SemManager.isPowerOfTwo(4)); // 2^2 = 4
        assertTrue(SemManager.isPowerOfTwo(8)); // 2^3 = 8
        assertTrue(SemManager.isPowerOfTwo(16)); // 2^4 = 16

        assertFalse(SemManager.isPowerOfTwo(0)); // Not a power of two
        assertFalse(SemManager.isPowerOfTwo(3)); // Not a power of two
        assertFalse(SemManager.isPowerOfTwo(-8)); // Not a power of two

    }
}
