public class CmdRunner {
    public CmdRunner(cmd) {
        this.proc = new ProcessBuilder(cmd.split());
    }

    private run() {
        this.proc.start();
        this.proc.waitFor();
    }
}

public class AsyncCmdRunner() {
    public AsyncCmdRunner() {
        super();
    }

    private run() {
        Runnable runnable = () -> { super.run(); }
        Thread thread = new Thread(runnable);
        thread.setPriority(Thread.MIN_PRIORITY);
        thread.start();
    }
}