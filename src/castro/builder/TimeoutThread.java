package castro.builder;

// Essentialy this thread monitor elapsed time since starting an operation.
// If an operation is taking too long to complete, a global value is set.
// Global value is an indicator for other threads to stop their work.
// Global value is being reset after each servers tick
public class TimeoutThread implements Runnable
{
	private static boolean initialized = false;
	private static long timeoutThreshold = 2500;
	private static long operationStartMillis;
	public static boolean timeout = false;
	
	public TimeoutThread() throws Exception
	{
		if(initialized)
			throw new Exception("Only one instance of TimeoutThread is allowed at a time");
		initialized = true;
	}
	
	public void reset()
	{
		operationStartMillis = System.currentTimeMillis();
		timeout = false;
	}
	
	@Override
	public void run()
	{
		if(!timeout)
		{
			long now = System.currentTimeMillis();
			if(now > operationStartMillis + timeoutThreshold)
				timeout = true;
		}
		
		try
		{
			Thread.sleep(500);
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}
}
