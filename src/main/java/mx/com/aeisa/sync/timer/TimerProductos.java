/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mx.com.aeisa.sync.timer;

import java.util.Date;
import java.util.TimerTask;

/**
 *
 * @author lasla
 */
public class TimerProductos extends TimerTask {

    @Override
    public void run() {
        System.out.println("Iniciando sincronización de productos: " + new Date());
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace(System.err);
        }
        System.out.println("Finalizando sincronización de productos: " + new Date());
    }
}
