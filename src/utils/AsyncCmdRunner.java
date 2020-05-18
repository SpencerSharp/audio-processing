package utils;

public class AsyncCmdRunner extends CmdRunner {
    public AsyncCmdRunner(String cmd) {
        super(cmd);
    }

    public void run() {
        Runnable runnable = () -> { super.run(); };
        Thread thread = new Thread(runnable);
        thread.setPriority(Thread.MIN_PRIORITY);
        thread.start();
    }
}