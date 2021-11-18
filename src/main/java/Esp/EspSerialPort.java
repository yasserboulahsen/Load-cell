package Esp;

import com.fazecast.jSerialComm.SerialPort;

import java.io.IOException;

public class EspSerialPort {
   public static SerialPort getSerialPort(String comName) throws IOException, InterruptedException {



           SerialPort esp32 = SerialPort.getCommPorts()[EspData.comPort(comName)];
           ////Serial port config
           //esp.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING,0,0);
           esp32.setComPortParameters(115200, 8, 1, 0);
           esp32.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 0, 0);
           esp32.openPort();
           return esp32;

    }
}
