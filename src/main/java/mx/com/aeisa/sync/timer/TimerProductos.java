/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mx.com.aeisa.sync.timer;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimerTask;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import mx.com.aeisa.sync.Util;
import mx.com.aeisa.sync.entity.ProductoAlmacenMysql;
import mx.com.aeisa.sync.entity.ProductoMysql;
import mx.com.aeisa.sync.entity.ProductoSql;
import mx.com.aeisa.sync.model.ProductoAlmacenSql;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author lasla
 */
public class TimerProductos extends TimerTask {

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
    private static final Logger logger = LogManager.getLogger(TimerProductos.class);

    private static TimerProductos instance;

    private final EntityManagerFactory emfSql;
    private final EntityManagerFactory emfMysql;

    private List<ProductoSql> productosSql;
    private List<ProductoAlmacenSql> productosAlmacenSql;
    private List<ProductoMysql> productosMysql;
    private List<ProductoAlmacenMysql> productosAlmacenMysql;

    private TimerProductos(String passwordSql, String passwordMySql) {
        emfSql = Util.getEntityManagerFactorySql(passwordSql);
        emfMysql = Util.getEntityManagerFactoryMySql(passwordMySql);
    }

    public static TimerProductos getInstance(String passwordSql, String passwordMySql) {
        if (instance == null) {
            instance = new TimerProductos(passwordSql, passwordMySql);
        }
        return instance;
    }

    private void buscarProductosSql() {
        emfSql.getCache().evictAll();
        EntityManager em = emfSql.createEntityManager();
        productosSql = em.createNamedQuery("ProductoSql.getAll", ProductoSql.class).getResultList();
    }

    private void buscarProductosAlmacenSql() {
        emfSql.getCache().evictAll();

        int mes = LocalDate.now().getMonth().getValue();
        mes = 6;
        Query nativeQuery = emfSql.createEntityManager().createNativeQuery(
                "SELECT prods.cidproducto,"
                + " existenciaCosto.cidalmacen,"
                + " ISNULL((CAST((existenciaCosto.centradasperiodo" + mes
                + " - existenciaCosto.csalidasperiodo" + mes
                + ") AS VARCHAR)), '0') existencia"
                + " FROM adAEISA_COMERCIAL_2022.dbo.admproductos prods"
                + " JOIN"
                + " (SELECT Max(cidejercicio) cidejercicio"
                + " FROM adAEISA_COMERCIAL_2022.dbo.admejercicios) AS ejercicio ON 1 = 1"
                + " JOIN adAEISA_COMERCIAL_2022.dbo.admexistenciacosto existenciaCosto ON prods.cidproducto = existenciaCosto.cidproducto"
                + " AND existenciaCosto.cidejercicio = ejercicio.cidejercicio"
                + " WHERE prods.cstatusproducto = 1"
                + " ORDER BY prods.ccodigoproducto");

        List<Object[]> productosAlmacenObject = nativeQuery.getResultList();
        productosAlmacenSql = new ArrayList<>();

        for (Object[] productoAlmacenSql : productosAlmacenObject) {
            productosAlmacenSql.add(new ProductoAlmacenSql(productoAlmacenSql));
        }
    }

    private void buscarProductosMysql() {
        emfMysql.getCache().evictAll();
        EntityManager em = emfMysql.createEntityManager();
        productosMysql = em.createNamedQuery("ProductoMysql.getAll", ProductoMysql.class).getResultList();
    }

    private void buscarProductosAlmacenMysql() {
        emfMysql.getCache().evictAll();
        EntityManager em = emfMysql.createEntityManager();
        productosAlmacenMysql = em.createNamedQuery("ProductoAlmacenMysql.getAll", ProductoAlmacenMysql.class).getResultList();
    }

    private void crearProductoMysql(ProductoMysql producto) {
        EntityManager em = null;
        try {
            em = emfMysql.createEntityManager();
            em.getTransaction().begin();
            em.persist(producto);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    private void crearProductoAlmacenMysql(ProductoAlmacenMysql productoAlmacen) {
        EntityManager em = null;
        try {
            em = emfMysql.createEntityManager();
            em.getTransaction().begin();
            em.persist(productoAlmacen);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    private void actualizarProductoMysql(ProductoMysql producto) {
        EntityManager em = null;
        try {
            em = emfMysql.createEntityManager();
            em.getTransaction().begin();
            producto.setFechaModificacion(new Date());
            em.merge(producto);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    private void actualizarProductoAlmacenMysql(ProductoAlmacenMysql productoAlmacen) {
        EntityManager em = null;
        try {
            em = emfMysql.createEntityManager();
            em.getTransaction().begin();
            productoAlmacen.setFechaModificacion(new Date());
            em.merge(productoAlmacen);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    private int findIndexProducto(ProductoSql productoSql) {
        int index = -1;
        for (int i = 0; i < productosMysql.size(); i++) {
            if (productosMysql.get(i).getId().equals(productoSql.getId())) {
                index = i;
                break;
            }
        }
        return index;
    }

    private int findIndexProductoAlmacen(ProductoAlmacenSql productoAlmacenSql) {
        int index = -1;
        for (int i = 0; i < productosAlmacenMysql.size(); i++) {
            if (productosAlmacenMysql.get(i).equals(productoAlmacenSql)) {
                index = i;
                break;
            }
        }
        return index;
    }

    private ProductoMysql procesarDiferenciasProductos(int indexMysql, ProductoSql productoSql) {
        boolean isActualizado = false;

        ProductoMysql productoMysql = productosMysql.get(indexMysql);

        if (!productoMysql.getCodigo().equals(productoSql.getCodigo())) {
            productoMysql.setCodigo(productoSql.getCodigo());
            isActualizado = true;
        }

        if (!productoMysql.getNombre().equals(productoSql.getNombre())) {
            productoMysql.setNombre(productoSql.getNombre());
            isActualizado = true;
        }

        return isActualizado ? productoMysql : null;
    }

    private ProductoAlmacenMysql procesarDiferenciasProductosAlmacen(int indexMysql, ProductoAlmacenSql productoAlmacenSql) {
        boolean isActualizado = false;

        ProductoAlmacenMysql productoAlmacenMysql = productosAlmacenMysql.get(indexMysql);

        if (!productoAlmacenMysql.getExistencia().equals(productoAlmacenSql.getExistencia())) {
            productoAlmacenMysql.setExistencia(productoAlmacenSql.getExistencia());
            isActualizado = true;
        }

        return isActualizado ? productoAlmacenMysql : null;
    }

    @Override
    public void run() {
        sincronizarProductos();
        sincronizarProductosAlmacen();
    }

    private void sincronizarProductos() {
        try {
            ZonedDateTime horaInicio = ZonedDateTime.now();
            ZonedDateTime horaActual = ZonedDateTime.now();

            logger.info("Iniciando sincronización de productos.");

            buscarProductosSql();
            logger.info("{} productos encontrados en SQL, en {} mss.", productosSql.size(), Util.calcularTiempoEjecucion(horaActual));

            horaActual = ZonedDateTime.now();
            buscarProductosMysql();
            logger.info("{} productos encontrados en MySQL, en {} mss.", productosMysql.size(), Util.calcularTiempoEjecucion(horaActual));

            ProductoMysql productoMysql;
            int indexMysql;
            for (ProductoSql productoSql : productosSql) {
                //Buscamos el producto de SQL en la lista de productos de MySQL.
                indexMysql = findIndexProducto(productoSql);

                //Si el producto no existe en MySQL entonces lo creamos.
                if (indexMysql == -1) {
                    horaActual = ZonedDateTime.now();

                    productoMysql = new ProductoMysql(productoSql);
                    crearProductoMysql(productoMysql);

                    logger.info("Producto insertado en MySQL, {} en {} mss.", productoMysql, Util.calcularTiempoEjecucion(horaActual));
                } else {
                    //Si el prodcuto ya existe, revisamos si es necesario actualizarlo.
                    productoMysql = procesarDiferenciasProductos(indexMysql, productoSql);

                    //Si no es nulo, significa que hubo diferencias, las guardamos.
                    if (productoMysql != null) {
                        horaActual = ZonedDateTime.now();

                        actualizarProductoMysql(productoMysql);
                        logger.info("Producto actualizado en MySQL, {} en {} mss.", productoMysql, Util.calcularTiempoEjecucion(horaActual));
                    }
                    //Producto procesado, lo eliminamos de la lista de MySQL
                    productosMysql.remove(indexMysql);
                }
            }

            //Si hay elementos en la lista de productos MySQL, ya no existen en SQL, los desactivamos.
            for (ProductoMysql producto : productosMysql) {
                if (producto.getActivo()) {
                    horaActual = ZonedDateTime.now();

                    producto.setActivo(Boolean.FALSE);
                    actualizarProductoMysql(producto);

                    logger.info("Producto desactivado en MySQL, {} en {} mss.", producto, Util.calcularTiempoEjecucion(horaActual));
                }
            }

            logger.info("Sincronización de productos finalizada en {} mss.", Util.calcularTiempoEjecucion(horaInicio));
        } catch (Exception e) {
            logger.error("Ocurrió un error al sincronizar los productos: " + e);
        }
    }

    private void sincronizarProductosAlmacen() {
        try {
            ZonedDateTime horaInicio = ZonedDateTime.now();
            ZonedDateTime horaActual = ZonedDateTime.now();

            logger.info("Iniciando sincronización de existencias.");

            buscarProductosAlmacenSql();
            logger.info("{} productosAlmacen encontrados en SQL, en {} mss.", productosAlmacenSql.size(), Util.calcularTiempoEjecucion(horaActual));

            horaActual = ZonedDateTime.now();
            buscarProductosAlmacenMysql();
            logger.info("{} productosAlmacen encontrados en MySQL, en {} mss.", productosAlmacenMysql.size(), Util.calcularTiempoEjecucion(horaActual));

            ProductoAlmacenMysql productoAlmacenMysql;
            int indexMysql;
            for (ProductoAlmacenSql productoAlmacenSql : productosAlmacenSql) {
                //Buscamos el productoAlmacen de SQL en la lista de productosAlmacen de MySQL.
                indexMysql = findIndexProductoAlmacen(productoAlmacenSql);

                //Si el productoAlmacen no existe en MySQL entonces lo creamos.
                if (indexMysql == -1) {
                    horaActual = ZonedDateTime.now();

                    productoAlmacenMysql = new ProductoAlmacenMysql(productoAlmacenSql);
                    crearProductoAlmacenMysql(productoAlmacenMysql);

                    logger.info("ProductoAlmacen insertado en MySQL, {} en {} mss.", productoAlmacenMysql, Util.calcularTiempoEjecucion(horaActual));
                } else {
                    //Si el prodcutoAlmacen ya existe, revisamos si es necesario actualizarlo.
                    productoAlmacenMysql = procesarDiferenciasProductosAlmacen(indexMysql, productoAlmacenSql);

                    //Si no es nulo, significa que hubo diferencias, las guardamos.
                    if (productoAlmacenMysql != null) {
                        horaActual = ZonedDateTime.now();

                        actualizarProductoAlmacenMysql(productoAlmacenMysql);
                        logger.info("ProductoAlmacen actualizado en MySQL, {} en {} mss.", productoAlmacenMysql, Util.calcularTiempoEjecucion(horaActual));
                    }
                    //Producto procesado, lo eliminamos de la lista de MySQL
                    productosAlmacenMysql.remove(indexMysql);
                }
            }

            //No es necesario procesar las existencias que ya no se encontraron en SQL.
            logger.info("Sincronización de productosAlmacen finalizada en {} mss.", Util.calcularTiempoEjecucion(horaInicio));
        } catch (Exception e) {
            logger.error("Ocurrió un error al sincronizar los productosAlmacen: " + e);
        }
    }
}
