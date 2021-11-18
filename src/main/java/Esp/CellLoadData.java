package Esp;

import com.fazecast.jSerialComm.SerialPort;
import javafx.application.Platform;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class CellLoadData extends Label{
    private final SerialPort esp32;
     private final Label label1;
    private final Label label2;
    private final Label label3;
    private final Label label4;
//    private String comName;
    private final List<String> list = new ArrayList<>();


    private final ScheduledExecutorService scheduledExecutorService;
//    public static List<XYChart.Series<String,Number>> list = new ArrayList<>();


    public CellLoadData( Label label1,Label label2,Label label3,Label label4,String comName) throws IOException, InterruptedException {
        this.esp32 = EspSerialPort.getSerialPort(comName);
        this.scheduledExecutorService = new ScheduledThreadPoolExecutor(1);
        this.label1 = label1;
        this.label2 =label2;
        this.label3 =label3;
        this.label4 =label4;
    }

    public void cellData(int period) {

        this.scheduledExecutorService.scheduleAtFixedRate(() -> {

            Platform.runLater(() -> {



                Scanner data = new Scanner(this.esp32.getInputStream());


                if (data.hasNext()) {
                    //String[] split = t.split(",");
                    String t = data.nextLine();
                    try{

                        String split = splitData(t, 0);
                        String split1 = splitData(t, 1);
                        String split2 = splitData(t, 2);
                        String split3 = splitData(t, 3);
                        if(split !=null && split1!=null && split2!=null && split3!=null){
//                        System.out.println(split +";"+split1+";"+split2+";"+split3);
                            list.add("Reading1 : "+split +" lb ; Reading2 : "+split1+" lb ; Reading3 : "+split2+" lb ; Reading4 : "+split3+" lb\n");
                            this.label1.setText(split);
                            this.label2.setText(split1);
                            this.label3.setText(split2);
                            this.label4.setText(split3);
                        }
                    }catch (Exception e){
//                        System.out.println("Data is null");
                    }


                    // System.out.println(t);
                    //Chart update to series graph

                }


            });

        }, -1, period, TimeUnit.MILLISECONDS);


    }


    public String splitData(String s, int dataNumber) {
        String[] split = s.split(";");
        if (split.length == 4 && !split[0].isEmpty() && !split[1].isEmpty() && !split[2].isEmpty() && !split[3].isEmpty()) {
            return split[dataNumber];
        }

        return null;
    }



    public void shutDownService(){

        this.esp32.closePort();
        this.scheduledExecutorService.shutdownNow();
//        list.forEach(System.out::println);
    }

    public List<String> getData(){
        return this.list;
    }
}
