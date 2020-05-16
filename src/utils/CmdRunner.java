package utils;

class CmdRunner {
    ProcessBuilder proc;

    public CmdRunner(String cmd) {
        this.proc = new ProcessBuilder(cmd.split(" "));
    }

    protected void run() {
        try {
            Process process = proc.start();
            process.waitFor();
        } catch(Exception exception) {}
    }
}

class AsyncCmdRunner extends CmdRunner {
    public AsyncCmdRunner(String cmd) {
        super(cmd);
    }

    protected void run() {
        Runnable runnable = () -> { super.run(); };
        Thread thread = new Thread(runnable);
        thread.setPriority(Thread.MIN_PRIORITY);
        thread.start();
    }
}