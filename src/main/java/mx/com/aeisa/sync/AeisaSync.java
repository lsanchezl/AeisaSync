/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package mx.com.aeisa.sync;

import java.util.Timer;
import mx.com.aeisa.sync.timer.TimerAlmacenes;
import mx.com.aeisa.sync.timer.TimerProductos;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author lasla
 */
public class AeisaSync {

    public static void main2(String[] args) {
        //Password SQL, Password MySQL
        args = new String[]{"aeisa.123", "NNtm4t93uDHsgf4qtDVo"};

        TimerAlmacenes timerAlmacenes = TimerAlmacenes.getInstance(args[0], args[1]);
        TimerProductos timerProductos = TimerProductos.getInstance(args[0], args[1]);

        Timer t = new Timer();
        t.scheduleAtFixedRate(timerAlmacenes, 0, 5 * 1000);
        t.scheduleAtFixedRate(timerProductos, 0, 5 * 1000);

    }

    public static void main(String[] args) throws Exception {
//        https://www.baeldung.com/httpclient-post-http-request
        URL obj = new URL("http://coesi.us-west-2.elasticbeanstalk.com/api/authenticate");
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("POST");
//        con.setRequestProperty("User-Agent", USER_AGENT);

        // For POST only - START
        con.setDoOutput(true);
        OutputStream os = con.getOutputStream();
        os.write("{'username':'mirna.lopez@ibmc.com.mx','password':'S3cr3taria.2021','rememberMe':false}".getBytes());
        os.flush();
        os.close();
        // For POST only - END

        int responseCode = con.getResponseCode();
        System.out.println("POST Response Code :: " + responseCode);

        if (responseCode == HttpURLConnection.HTTP_OK) { //success
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // print result
            System.out.println(response.toString());
        } else {
            System.out.println("POST request did not work.");
        }
    }
}
