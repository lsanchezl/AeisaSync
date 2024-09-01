/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package mx.com.aeisa.sync;

import java.util.Timer;
import mx.com.aeisa.sync.timer.TimerAlmacenes;
import mx.com.aeisa.sync.timer.TimerProductos;

/**
 *
 * @author lasla
 */
public class AeisaSync {

    public static void main(String[] args) {
        TimerAlmacenes timerAlmacenes = TimerAlmacenes.getInstance();
        TimerProductos timerProductos = new TimerProductos();

        Timer t = new Timer();
        t.scheduleAtFixedRate(timerAlmacenes, 0, 5 * 1000);
        //t.scheduleAtFixedRate(timerProductos, 0, 60 * 1000);
    }
}
