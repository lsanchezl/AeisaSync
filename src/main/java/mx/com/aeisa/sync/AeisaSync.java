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
        //Password SQL, Password MySQL
        args = new String[]{"aeisa.123", "adminadmin"};

        TimerAlmacenes timerAlmacenes = TimerAlmacenes.getInstance(args[0], args[1]);
        TimerProductos timerProductos = TimerProductos.getInstance(args[0], args[1]);

        Timer t = new Timer();
        t.scheduleAtFixedRate(timerAlmacenes, 0, 5 * 1000);
        t.scheduleAtFixedRate(timerProductos, 0, 5 * 1000);

    }
}
