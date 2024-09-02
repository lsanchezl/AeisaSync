/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mx.com.aeisa.sync;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.Properties;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author lasla
 */
public class Util {

    public static EntityManagerFactory getEntityManagerFactorySql(String password) {
        Properties properties = new Properties();
        properties.put("javax.persistence.jdbc.url", "jdbc:sqlserver://localhost:1433;databaseName=acope;encrypt=false");
        properties.put("javax.persistence.transactionType", "RESOURCE_LOCAL");
        properties.put("javax.persistence.jdbc.user", "sa");
        properties.put("javax.persistence.jdbc.driver", "com.microsoft.sqlserver.jdbc.SQLServerDriver");
//        properties.put("javax.persistence.jdbc.password", "aeisa.123");
        properties.put("javax.persistence.jdbc.password", password);

        return Persistence.createEntityManagerFactory("MSSQL-PU", properties);
    }

    public static EntityManagerFactory getEntityManagerFactoryMySql(String password) {
        Properties properties = new Properties();
        properties.put("javax.persistence.jdbc.url", "jdbc:mysql://localhost:3306/acope?zeroDateTimeBehavior=CONVERT_TO_NULL");
        properties.put("javax.persistence.transactionType", "RESOURCE_LOCAL");
        properties.put("javax.persistence.jdbc.user", "root");
        properties.put("javax.persistence.jdbc.driver", "com.mysql.cj.jdbc.Driver");
//        properties.put("javax.persistence.jdbc.password", "adminadmin");
        properties.put("javax.persistence.jdbc.password", password);

        return Persistence.createEntityManagerFactory("MySQL-PU", properties);
    }

    public static long calcularTiempoEjecucion(ZonedDateTime inicio) {
        ZonedDateTime fin = ZonedDateTime.now();
        Duration duration = Duration.between(inicio, fin);
        return duration.toMillis();
    }
}
