package mx.com.aeisa.sync.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * A Producto.
 */
@Entity
@Table(name = "producto")
@NamedQuery(name = "ProductoMysql.getAll",
        query = "SELECT p FROM ProductoMysql p WHERE p.id > 0")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProductoMysql implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "codigo")
    private String codigo;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "activo")
    private Boolean activo;

    @Column(name = "fecha_modificacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaModificacion;

    public ProductoMysql() {
    }

    public ProductoMysql(ProductoSql productoSql) {
        this.activo = true;
        this.codigo = productoSql.getCodigo();
        this.fechaModificacion = new Date();
        this.id = productoSql.getId();
        this.nombre = productoSql.getNombre();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public Date getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion(Date fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }

    @Override
    public String toString() {
        return "{" + "id=" + id + ", codigo=" + codigo + ", nombre=" + nombre + ", activo=" + activo + ", fechaModificacion=" + fechaModificacion + '}';
    }

}
