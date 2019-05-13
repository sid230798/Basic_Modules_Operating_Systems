package dev;

import java.util.Random;
/**
 * A real device that exhibits realistic computations delays.
 * 
 * @author Balwinder Sodhi
 */
public class RealDevice implements Device {

	private volatile int operationCount = 0;
	private final Random rand; 
	private final int faultFreq;
	private DeviceConfig config;
	
	/**
	 * Creates an initialized real device
	 * @param config Device configuration
	 */
	public RealDevice(DeviceConfig config) {
		this.config = config;
		this.rand = new Random(config.randomSeed);
		faultFreq = 100 + rand.nextInt(200);
	}

	/**
	 * Introduces computational delays.
	 * @throws InterruptedException
	 */
	private void computationalDelay() throws InterruptedException {
		Thread.currentThread().sleep(config.computeDelayInMs);
	}

	/**
	 * Introduces computational delays.
	 * @throws InterruptedException
	 */
	private void delays() throws InterruptedException {
		computationalDelay();
		if (faultFreq / 2 + rand.nextInt(faultFreq / 2) < operationCount++) {
			// System.out.print("#");
			operationCount = 0;
			Thread.currentThread().sleep(config.bootDelayInMs);
		}
	}

	/**
	 * How many operations have been done by this device instance.
	 */
	@Override
	public int age() {
		return operationCount;
	}

	@Override
	public long f(long variant, int multiplierIndex) throws InterruptedException {
		operationCount++;
		delays();
		return variant * MULTIPLIERS[multiplierIndex];
	}

	@Override
	public long fInverse(long variant, int multiplierIndex) throws InterruptedException {
		operationCount++;
		delays();
		if (variant % MULTIPLIERS[multiplierIndex] > 0)
			return 0;
		return variant / MULTIPLIERS[multiplierIndex];
	}

	/**
	 * Class that holds the configuration information for this device.
	 * @author bsodhi
	 *
	 */
	public static class DeviceConfig {
		/**
		 * Compute delay for operations of this device.
		 */
		long computeDelayInMs;
		/**
		 * Boot latency for this device.
		 */
		long bootDelayInMs;
		/**
		 * Random seed for random number generator.
		 */
		long randomSeed;
		/**
		 * Created a config object with supplied params.
		 * @param computeDelayInMs
		 * @param bootDelayInMs
		 * @param randomSeed
		 */
		public DeviceConfig(long computeDelayInMs, long bootDelayInMs, long randomSeed) {
			this.computeDelayInMs = computeDelayInMs;
			this.bootDelayInMs = bootDelayInMs;
			this.randomSeed = randomSeed;
		}
	}
}
