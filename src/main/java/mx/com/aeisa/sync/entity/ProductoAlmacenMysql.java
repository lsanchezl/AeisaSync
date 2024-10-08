/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mx.com.aeisa.sync.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import mx.com.aeisa.sync.model.ProductoAlmacenSql;

/**
 *
 * @author lasla
 */
@Entity
@Table(name = "producto_almacen")
@NamedQuery(name = "ProductoAlmacenMysql.getAll",
        query = "SELECT pa FROM ProductoAlmacenMysql pa")
public class ProductoAlmacenMysql implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;

    @Column(name = "producto_id")
    private Long productoId;

    @Column(name = "almacen_id")
    private Long almacenId;

    @Column(name = "existencia")
    private Double existencia;

    @Column(name = "fecha_modificacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaModificacion;

    public ProductoAlmacenMysql() {
    }

    public ProductoAlmacenMysql(ProductoAlmacenSql productoAlmacenSql) {
        this.productoId = productoAlmacenSql.getProductoId();
        this.almacenId = productoAlmacenSql.getAlmacenId();
        this.existencia = productoAlmacenSql.getExistencia();
        this.fechaModificacion = new Date();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Date getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion(Date fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }

    @Override
    public String toString() {
        return "{" + "id=" + id + ", productoId=" + productoId + ", almacenId=" + almacenId + ", existencia=" + existencia + ", fechaModificacion=" + fechaModificacion + '}';
    }

    public boolean equals(ProductoAlmacenSql productoAlmacenSql) {
        return productoId.equals(productoAlmacenSql.getProductoId()) && almacenId.equals(productoAlmacenSql.getAlmacenId());
    }
}
