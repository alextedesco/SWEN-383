/*
 * Initial Author
 *      Michael J. Lutz
 *
 * Other Contributers
 *
 * Acknowledgements
 */
 
/*
 * Class for a simple computer based weather station that reports the current
 * temperature (in Celsius) every second. The station is attached to a
 * sensor that reports the temperature as a 16-bit number (0 to 65535)
 * representing the Kelvin temperature to the nearest 1/100th of a degree.
 *
 * This class is implements Runnable so that it can be embedded in a Thread
 * which runs the periodic sensing.
 *
 * The class also extends Observable so that it can notify registered
 * objects whenever its state changes. Convenience functions are provided
 * to access the temperature in different schemes (Celsius, Kelvin, etc.)
 */

import java.util.Observable ;
@SuppressWarnings("deprecation")


public class WeatherStation extends Observable implements Runnable {

    private final KelvinTempSensor sensor ; // Temperature sensor.
    private final Barometer barometer; // Barometer sensor.

    private final long PERIOD = 1000 ;      // 1 sec = 1000 ms
    private final int KTOC = -27315 ;       // Kelvin to Celsius conversion.

    private int currentTempReading ;
    private double currentBaromReading ;

    /*
     * When a WeatherStation object is created, it in turn creates the sensor
     * object it will use.
     */
    public WeatherStation() {
        sensor = new KelvinTempSensor() ;
        barometer = new Barometer();
        currentTempReading = sensor.reading() ;
        currentBaromReading = barometer.pressure();
    }

    /*
     * The "run" method called by the enclosing Thread object when started.
     * Repeatedly sleeps a second, acquires the current temperature from its
     * sensor, and notifies registered Observers of the change.
     */
    public void run() {
        while( true ) {
            try {
                Thread.sleep(PERIOD) ;
            } catch (Exception e) {}    // ignore exceptions

            /*
             * Get next reading and notify any Observers.
             */
            synchronized(this) {
                currentTempReading = sensor.reading() ;
                currentBaromReading = barometer.pressure();
                
            }
            setChanged() ;
            notifyObservers() ;
        }
    }

    /*
     * Return the current reading in degrees celsius as a
     * double precision number.
     */
    public synchronized double getCelsius() {
        return (currentTempReading + KTOC) / 100.0 ;
    }

    /*
     * Return the current reading in degrees Kelvin as a
     * double precision number.
     */
    public synchronized double getKelvin() {
        return currentTempReading / 100.0 ;
    }

    public synchronized double getFahrenheit () {
        return ((((currentTempReading / 100.0) - 273.15) * 1.8) + 32);
    }

    public synchronized double getInches () {
        return currentBaromReading;
    }

    public synchronized double getMillibars () {
        return (currentBaromReading * 33.864);
    }
}
