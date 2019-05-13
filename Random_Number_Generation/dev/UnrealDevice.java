package dev;

/**
 * This class implements a unreal device which doesn't exhibit any
 * computational delays.
 * 
 * @author Balwinder Sodhi
 */
public class UnrealDevice implements Device {

	private volatile int operationCount = 0;

	/**
	 * Initializes the device.
	 */
	public UnrealDevice(int s) {
	}

	@Override
	public long f(long l, int i) throws InterruptedException {
		operationCount++;
		return l * MULTIPLIERS[i];
	}

	@Override
	public long fInverse(long l, int i) throws InterruptedException {
		operationCount++;
		if (l % MULTIPLIERS[i] > 0)
			return 0;
		return l / MULTIPLIERS[i];
	}

	@Override
	public int age() {
		return operationCount;
	}

}
