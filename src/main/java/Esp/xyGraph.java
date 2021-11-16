package Esp;

import com.fazecast.jSerialComm.SerialPort;
import javafx.application.Platform;
import javafx.scene.chart.XYChart;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class xyGraph {

    private final SerialPort[] esp32;
    private final SimpleDateFormat simpleDateFormat;
    private final XYChart.Series<String, Number> series;
    private final XYChart.Series<String, Number> series1;
    private final ScheduledExecutorService scheduledExecutorService;
    public static List<XYChart.Series<String,Number>> list = new ArrayList<>();


    private final Date previuosTime = new Date();
    private final Date curentTime = new Date();


    public xyGraph(SerialPort[] esp32, SimpleDateFormat simpleDateFormat, XYChart.Series<String, Number> series, XYChart.Series<String, Number> series1) {
        this.esp32 = esp32;
        this.simpleDateFormat = simpleDateFormat;
        this.series1 = series1;
        this.scheduledExecutorService =  new ScheduledThreadPoolExecutor(1);//Executors.newScheduledThreadPool(1);
        this.series = series;



    }

    public ScheduledExecutorService getScheduledExecutorService() {
        return scheduledExecutorService;
    }


    public void chart(int number, int period) {

        this.scheduledExecutorService.scheduleAtFixedRate(() -> {

            Platform.runLater(() -> {


                chartColor();//color of chart changes to blue
                // get current time
                Date nowTime = new Date();
                curentTime.setTime(nowTime.getTime() - previuosTime.getTime());


                Scanner data = new Scanner(this.esp32[0].getInputStream());


                if (data.hasNext()) {
                    //String[] split = t.split(",");
                    String t = data.nextLine();


                    // System.out.println(t);
                    //Chart update to series graph
                    chartsSeries(number, t);

                }


            });

        }, -1, period, TimeUnit.MILLISECONDS);


    }

    private void chartColor() {
        for (XYChart.Data<String, Number> entry : series.getData()) {
            entry.getNode().setStyle("-fx-background-color: blue, white;\n"
                    + "    -fx-background-insets: 0, 2;\n"
                    + "    -fx-background-radius: 5px;\n"
                    + "    -fx-padding: 5px;");
        }
        series.getNode().lookup(".chart-series-line").setStyle("-fx-stroke: blue;");
    }

    private void chartsSeries(int number, String t) {
        try {
            String split = splitData(t, number);
            String split1 = splitData(t, 0);
            if (split != null) {
                this.series.getData().add(new XYChart.Data<>(this.simpleDateFormat.format(curentTime), Double.valueOf(split)));
                this.series1.getData().add(new XYChart.Data<>(this.simpleDateFormat.format(curentTime), Double.valueOf(split1)));
                list.add(series);
            }
        } catch (Exception e) {
            System.out.println("Data is null");

        }
    }


//    public double tryParseDouble(String value, double defaultVal) {
//        try {
//            return Double.parseDouble(value);
//        } catch (NumberFormatException e) {
//            return defaultVal;
//        }
//    }

    public String splitData(String s, int dataNumber) {
        String[] split = s.split(";");
        if (split.length == 4 && !split[0].isEmpty() && !split[1].isEmpty() && !split[2].isEmpty() && !split[3].isEmpty()) {
            return split[dataNumber];
        }

        return null;
    }



    public void shutDownService(){
        this.scheduledExecutorService.shutdownNow();
    }

}
