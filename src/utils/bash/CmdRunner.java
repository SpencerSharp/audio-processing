package utils;

public class CmdRunner {
    ProcessBuilder proc;

    public CmdRunner(String cmd) {
        this.proc = new ProcessBuilder(cmd.split(" "));
    }

    public void run() {
        try {
            Process process = proc.start();
            process.waitFor();
        } catch(Exception exception) {}
    }
}