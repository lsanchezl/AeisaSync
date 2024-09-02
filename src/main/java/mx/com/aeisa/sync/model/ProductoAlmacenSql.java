/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mx.com.aeisa.sync.model;

/**
 *
 * @author lasla
 */
public class ProductoAlmacenSql {

    private Long productoId;
    private Long almacenId;
    private Double existencia;

    public ProductoAlmacenSql(Object[] productoAlmacenSql) {
        productoId = Long.valueOf(productoAlmacenSql[0].toString());
        almacenId = Long.valueOf(productoAlmacenSql[1].toString());
        existencia = Double.valueOf(productoAlmacenSql[2].toString());
    }

    public Long getProductoId() {
        return productoId;
    }

    public void setProductoId(Long productoId) {
        this.productoId = productoId;
    }

    public Long getAlmacenId() {
        return almacenId;
    }

    public void setAlmacenId(Long almacenId) {
        this.almacenId = almacenId;
    }

    public Double getExistencia() {
        return existencia;
    }

    public void setExistencia(Double existencia) {
        this.existencia = existencia;
    }
}
