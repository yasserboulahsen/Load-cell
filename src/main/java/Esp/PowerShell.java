package Esp;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public  class PowerShell {
    public static int main(String[] args,String command) throws IOException {
        String command1 = " $port = New-Object System.IO.Ports.SerialPort "+command+", 115200, none, 8, one ";
        String command2 = " $port.open() ";
        String command3 = " $port.ReadExisting()";
        return comTest(command1,command2,command3);
    }

    private static int comTest(String command1,String command2,String command3) throws IOException {
        String command = "powershell.exe -noprofile -executionpolicy bypass "+command1+ ";"+command2+
                ";"+command3+"; $port.close()";

        Process powerShellProcess = Runtime.getRuntime().exec(command);

        powerShellProcess.getOutputStream().close();
        String line;

        BufferedReader stdout = new BufferedReader(new InputStreamReader(
                powerShellProcess.getInputStream()));
        if ((line = stdout.readLine()) != null) {
            if (line.length() != 0) {
                //System.out.println(line.length());
                return line.length();
            }else{
                return -1;
            }

        }
        stdout.close();

        return -1;
    }
}
