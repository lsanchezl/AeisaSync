/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mx.com.aeisa.sync.timer;

import java.util.Date;
import java.util.List;
import java.util.TimerTask;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import mx.com.aeisa.syn.mysql.entity.AlmacenMysql;
import mx.com.aeisa.syn.sql.entity.AlmacenSql;

/**
 *
 * @author lasla
 */
public class TimerAlmacenes extends TimerTask {

    private static TimerAlmacenes instance;

    private final EntityManagerFactory emfSql;
    private final EntityManagerFactory emfMysql;

    private List<AlmacenSql> almacenesSql;
    private List<AlmacenMysql> almacenesMysql;

    private TimerAlmacenes() {
        emfSql = Persistence.createEntityManagerFactory("MSSQL-PU");
        emfMysql = Persistence.createEntityManagerFactory("MySQL-PU");
    }

    public static TimerAlmacenes getInstance() {
        if (instance == null) {
            instance = new TimerAlmacenes();
        }
        return instance;
    }

    private List<AlmacenSql> buscarAlmacenesSql() {
        emfSql.getCache().evictAll();
        EntityManager em = emfSql.createEntityManager();
        return em.createNamedQuery("AlmacenSql.getAll", AlmacenSql.class).getResultList();
    }

    private List<AlmacenMysql> buscarAlmacenesMysql() {
        emfMysql.getCache().evictAll();
        EntityManager em = emfMysql.createEntityManager();
        return em.createNamedQuery("AlmacenMysql.getAll", AlmacenMysql.class).getResultList();
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
        System.out.println("Iniciando sincronización de almacenes: " + new Date());

        almacenesSql = buscarAlmacenesSql();
        almacenesMysql = buscarAlmacenesMysql();

        AlmacenMysql almacenMysql;
        int indexMysql;
        for (AlmacenSql almacenSql : almacenesSql) {
            //Buscamos el almacén de SQL en la lista de almacenes de MySQL.
            indexMysql = findIndex(almacenSql);

            //Si el almacén no existe en MySQL entonces lo creamos.
            if (indexMysql == -1) {
                crearAlmacenMysql(new AlmacenMysql(almacenSql));
            } else {
                //Si el almacén ya existe, revisamos si es necesario actualizarlo.
                almacenMysql = procesarDiferencias(indexMysql, almacenSql);

                //Si no es nulo, significa que hubo diferencias, las guardamos.
                if (almacenMysql != null) {
                    actualizarAlmacenMysql(almacenMysql);
                }
                // almacen procesado, lo eliminamos de la lista de MySQL
                almacenesMysql.remove(indexMysql);
            }
        }

        // Si hay elementos en la lista de almacenes MySQL, significa que no se encontraron en SQL, ya no existen, los desactivamos.
        for (AlmacenMysql almacen : almacenesMysql) {
            almacen.setActivo(Boolean.FALSE);
            actualizarAlmacenMysql(almacen);
        }

        System.out.println("Finalizando sincronización de almacenes: " + new Date());
    }

}
