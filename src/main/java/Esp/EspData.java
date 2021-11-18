package Esp;

import com.fazecast.jSerialComm.SerialPort;

import java.io.IOException;
import java.util.Scanner;

public class EspData {
    public static final String TEXT_RED = "\u001B[31m";
    public static void comPortFinalData(int com) {
        try {
            SerialPort esp32 = SerialPort.getCommPorts()[com];
            //esp.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING,0,0);
            esp32.setComPortParameters(115200, 8, 1, 0);
            esp32.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 0, 0);
            esp32.openPort();


            //System.out.println(esp32.isOpen());
            Scanner data = new Scanner(esp32.getInputStream());
            while (data.hasNext()) {
                System.out.println(data.nextLine());
            }
        }catch (Exception e){
            System.out.println(TEXT_RED+"Impossible(error connexion) !!!");
        }
    }

    public static int comPort(String comName) throws IOException, InterruptedException {
        int comLengh = 0;
        String portTest = "";
        for(int i = 0;i<SerialPort.getCommPorts().length;i++){
            SerialPort esp = SerialPort.getCommPorts()[i];

            if(esp.getDescriptivePortName().contains(comName)){

                portTest = portName(esp.getDescriptivePortName());

                comLengh = PowerShell.main(null,portTest);
                if(comLengh != -1){
                    System.out.println(esp.getDescriptivePortName());
                    return i;
                }

            }

        }

        return -1;
    }
    public static String portName(String message){
        String[] array = message.split(" ");
        StringBuilder str = new StringBuilder();
        for(int i =0 ; i<array.length ;i++){
            if(array[i].startsWith("(COM")){
                String s = array[i];
                for(char j : s.toCharArray()){
                    if(j == '(' || j == ')'){
                        j = ' ';
                    }
                    str.append(j);
                }

            }
        }
        return str.toString();
    }

}
