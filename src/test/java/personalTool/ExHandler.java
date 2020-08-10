package personalTool;
public class ExHandler
{
	public static void main(String[] args) 
	{
		//设置主线程的异常处理器
        Thread.currentThread().setUncaughtExceptionHandler(
            (t,e)->{
                System.out.println(t + " 线程出现了异常2：" + e);
            });
		int a = 5 / 0;
	}
}
