package dev;
/**
 * This interface captures the contract of a device which is used
 * to generate the numbers based on a specified formula and
 * device performance settings.
 * 
 * @author Balwinder Sodhi
 */
public interface Device {
	
	public static final int[] MULTIPLIERS = { 3, 5, 7, 11, 13, 17, 19 };

	/**
	 * How many operations have been done by this device instance.
	 * @return
	 */
	public abstract int age();

	/**
	 * Device function that calculates the numbers.
	 * @param variant
	 * @param multiplierIndex
	 * @return Generated number.
	 * @throws InterruptedException
	 */
	public abstract long f(long variant, int multiplierIndex) throws InterruptedException;

	/**
	 * Device function which calculates the inverse of generated number.
	 * @param variant
	 * @param multiplierIndex
	 * @return
	 * @throws InterruptedException
	 */
	public abstract long fInverse(long variant, int multiplierIndex) throws InterruptedException;
}
