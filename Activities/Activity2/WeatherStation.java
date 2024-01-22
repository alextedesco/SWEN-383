/*
 * Class for a simple computer based weather station that reports the current
 * temperature (in Celsius) every second. The station is attached to a
 * sensor that reports the temperature as a 16-bit number (0 to 65535)
 * representing the Kelvin temperature to the nearest 1/100th of a degree.
 *
 * This class is implements Runnable so that it can be embedded in a Thread
 * which runs the periodic sensing.
 */

import java.util.Scanner;

public class WeatherStation implements Runnable {

    private static KelvinTempSensor sensor ; // Temperature sensor.

    private final long PERIOD = 1000 ;      // 1 sec = 1000 ms.

    /*
     * When a WeatherStation object is created, it in turn creates the sensor
     * object it will use.
     */
    public WeatherStation() {
        sensor = new KelvinTempSensor() ;
    }

    /*
     * The "run" method called by the enclosing Thread object when started.
     * Repeatedly sleeps a second, acquires the current temperature from
     * its sensor, and reports this as a formatted output string.
     */
    public void run() {
        int reading ;           // actual sensor reading.
        double celsius ;        // sensor reading transformed to celsius
        final int KTOC = -27315 ;   // Convert raw Kelvin reading to Celsius

        while( true ) {
            try {
                Thread.sleep(PERIOD) ;
            } catch (Exception e) {}    // ignore exceptions

            reading = sensor.reading() ;
            celsius = (reading + KTOC) / 100.0 ;
            /*
             * System.out.printf prints formatted data on the output screen.
             *
             * Most characters print as themselves.
             *
             * % introduces a format command that usually applies to the
             * next argument of printf:
             *   *  %6.2f formats the "celsius" (2nd) argument in a field
             *      at least 6 characters wide with 2 fractional digits.
             *   *  The %n at the end of the string forces a new line
             *      of output.
             *   *  %% represents a literal percent character.
             *
             * See docs.oracle.com/javase/tutorial/java/data/numberformat.html
             * for more information on formatting output.
             */
            //
            System.out.printf("Reading is %6.2f degrees C", celsius) ;
            double kelvin = (reading / 100.0);
            System.out.printf(" and %6.2f degrees K%n", kelvin) ;
        }
    }

    /*
     * Initial main method.
     *      Create the WeatherStation (Runnable).
     *      Embed the WeatherStation in a Thread.
     *      Start the Thread.
     */
    public static void main(String[] args) {
        WeatherStation ws = new WeatherStation();
        double reading;
        Scanner s = new Scanner(System.in);

        while (true) {
            System.out.println("Select an option:  \n1. SwingUI\n2. AWTUI\n3. Text");
            String option = s.nextLine(); 

            if (option.equals("1")) {
                SwingUI ui = new SwingUI();

                while (true) {
                    try {
                        Thread.sleep (1000);
                    } catch (Exception e) {
                        System.err.println("Error");
                    }
                    reading = sensor.reading();
                    ui.setKelvinJLabel((reading) / 100.0);
                    ui.setCelsiusJLabel((reading + -27315) / 100.0 );
                }

            } else if (option.equals("2")) {
                AWTUI ui = new AWTUI();
                
                while (true) {
                    try {
                        Thread.sleep (1000);
                    } catch (Exception e) {
                        System.err.println("Error");
                    }
                    reading = sensor.reading();
                    ui.kelvinField.setText(String.valueOf((reading) / 100.0));
                    ui.celsiusField.setText(String.valueOf((reading + -27315) / 100.0));
                }
            } else if (option.equals("3")) {
                ws.run();
            } else {
                System.out.println("Invalid input! Please try again");
            }
        }
    }    
}