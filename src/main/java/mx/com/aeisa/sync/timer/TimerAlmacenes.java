/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mx.com.aeisa.sync.timer;

import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;
import java.util.TimerTask;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import mx.com.aeisa.sync.Util;
import mx.com.aeisa.sync.entity.AlmacenMysql;
import mx.com.aeisa.sync.entity.AlmacenSql;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 *
 * @author lasla
 */
public class TimerAlmacenes extends TimerTask {

    /*
    INSERT INTO [dbo].[admAlmacenes]
           ([CIDALMACEN]
           ,[CCODIGOALMACEN]
           ,[CNOMBREALMACEN]
           ,[CTEXTOEXTRA1]
		   )
     VALUES
           (60
           ,'Código 60'
           ,'Almacen 60'
           ,'SIGLAS-60'
		   );

    update admAlmacenes set CCODIGOALMACEN = 'Código 527' where CIDALMACEN = 52;
    update admAlmacenes set CNOMBREALMACEN = 'Almacen 547' where CIDALMACEN = 53;
    update admAlmacenes set CTEXTOEXTRA1 = 'SIGLAS-547' where CIDALMACEN = 54;
    update admAlmacenes set CCODIGOALMACEN = 'Código 557',CNOMBREALMACEN = 'Almacen 557', CTEXTOEXTRA1 = 'SIGLAS-557' where CIDALMACEN = 55;
    delete from admAlmacenes where CIDALMACEN > 55;

     */
    private static final Logger logger = LogManager.getLogger(TimerAlmacenes.class);

    private static TimerAlmacenes instance;

    private final EntityManagerFactory emfSql;
    private final EntityManagerFactory emfMysql;

    private List<AlmacenSql> almacenesSql;
    private List<AlmacenMysql> almacenesMysql;

    private TimerAlmacenes(String passwordSql, String passwordMySql) {
        emfSql = Util.getEntityManagerFactorySql(passwordSql);
        emfMysql = Util.getEntityManagerFactoryMySql(passwordMySql);
    }

    public static TimerAlmacenes getInstance(String passwordSql, String passwordMySql) {
        if (instance == null) {
            instance = new TimerAlmacenes(passwordSql, passwordMySql);
        }
        return instance;
    }

    private void buscarAlmacenesSql() {
        emfSql.getCache().evictAll();
        EntityManager em = emfSql.createEntityManager();
        almacenesSql = em.createNamedQuery("AlmacenSql.getAll", AlmacenSql.class).getResultList();
    }

    private void buscarAlmacenesMysql() {
        emfMysql.getCache().evictAll();
        EntityManager em = emfMysql.createEntityManager();
        almacenesMysql = em.createNamedQuery("AlmacenMysql.getAll", AlmacenMysql.class).getResultList();
    }

    private void crearAlmacenMysql(AlmacenMysql almacen) {
        EntityManager em = null;
        try {
            em = emfMysql.createEntityManager();
            em.getTransaction().begin();
            em.persist(almacen);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    private void actualizarAlmacenMysql(AlmacenMysql almacen) {
        EntityManager em = null;
        try {
            em = emfMysql.createEntityManager();
            em.getTransaction().begin();
            almacen.setFechaModificacion(new Date());
            em.merge(almacen);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    private int findIndex(AlmacenSql almacenSql) {
        int index = -1;
        for (int i = 0; i < almacenesMysql.size(); i++) {
            if (almacenesMysql.get(i).getId().equals(almacenSql.getId())) {
                index = i;
                break;
            }
        }
        return index;
    }

    private AlmacenMysql procesarDiferencias(int indexMysql, AlmacenSql almacenSql) {
        boolean isActualizado = false;

        AlmacenMysql almacenMysql = almacenesMysql.get(indexMysql);

        if (!almacenMysql.getCodigo().equals(almacenSql.getCodigo())) {
            almacenMysql.setCodigo(almacenSql.getCodigo());
            isActualizado = true;
        }

        if (!almacenMysql.getNombre().equals(almacenSql.getNombre())) {
            almacenMysql.setNombre(almacenSql.getNombre());
            isActualizado = true;
        }

        if (!almacenMysql.getSiglas().equals(almacenSql.getSiglas())) {
            almacenMysql.setSiglas(almacenSql.getSiglas());
            isActualizado = true;
        }

        return isActualizado ? almacenMysql : null;
    }

    @Override
    public void run() {
        try {
            ZonedDateTime horaInicio = ZonedDateTime.now();
            logger.info("Iniciando sincronización de almacenes.");

            ZonedDateTime horaActual = ZonedDateTime.now();
            buscarAlmacenesSql();
            logger.info("{} almacenes encontrados en SQL, en {} mss.", almacenesSql.size(), Util.calcularTiempoEjecucion(horaActual));

            horaActual = ZonedDateTime.now();
            buscarAlmacenesMysql();
            logger.info("{} almacenes encontrados en MySQL, en {} mss.", almacenesMysql.size(), Util.calcularTiempoEjecucion(horaActual));

            AlmacenMysql almacenMysql;
            int indexMysql;
            for (AlmacenSql almacenSql : almacenesSql) {
                //Buscamos el almacén de SQL en la lista de almacenes de MySQL.
                indexMysql = findIndex(almacenSql);

                //Si el almacén no existe en MySQL entonces lo creamos.
                if (indexMysql == -1) {
                    horaActual = ZonedDateTime.now();

                    almacenMysql = new AlmacenMysql(almacenSql);
                    crearAlmacenMysql(almacenMysql);

                    logger.info("Almacén insertado en MySQL, {} en {} mss.", almacenMysql, Util.calcularTiempoEjecucion(horaActual));
                } else {
                    //Si el almacén ya existe, revisamos si es necesario actualizarlo.
                    almacenMysql = procesarDiferencias(indexMysql, almacenSql);

                    //Si no es nulo, significa que hubo diferencias, las guardamos.
                    if (almacenMysql != null) {
                        horaActual = ZonedDateTime.now();

                        actualizarAlmacenMysql(almacenMysql);
                        logger.info("Almacén actualizado en MySQL, {} en {} mss.", almacenMysql, Util.calcularTiempoEjecucion(horaActual));
                    }
                    //Almacén procesado, lo eliminamos de la lista de MySQL
                    almacenesMysql.remove(indexMysql);
                }
            }

            //Si hay elementos en la lista de almacenes MySQL, ya no existen en SQL, los desactivamos.
            for (AlmacenMysql almacen : almacenesMysql) {
                if (almacen.getActivo()) {
                    horaActual = ZonedDateTime.now();

                    almacen.setActivo(Boolean.FALSE);
                    actualizarAlmacenMysql(almacen);

                    logger.info("Almacén desactivado en MySQL, {} en {} mss.", almacen, Util.calcularTiempoEjecucion(horaActual));
                }
            }

            logger.info("Sincronización de almacenes finalizada en {} mss.", Util.calcularTiempoEjecucion(horaInicio));
        } catch (Exception e) {
            logger.error("Ocurrió un error al sincronizar los almacenes: " + e);
        }
    }
}
