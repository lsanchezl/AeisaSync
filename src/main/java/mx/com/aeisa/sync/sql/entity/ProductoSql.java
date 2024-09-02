package mx.com.aeisa.sync.sql.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * A ProductoSql.
 */
@Entity
@Table(catalog = "adaeisa_comercial_2022", schema = "dbo", name = "admproductos")
@NamedQuery(name = "ProductoSql.getAll",
        query = "SELECT p FROM ProductoSql p WHERE p.estatus = 1 AND p.id > 0")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProductoSql implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "cidproducto")
    private Long id;

    @Column(name = "ccodigoproducto")
    private String codigo;

    @Column(name = "cnombreproducto")
    private String nombre;

    @Column(name = "cstatusproducto")
    private Integer estatus;

    public ProductoSql() {
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

    public Integer getEstatus() {
        return estatus;
    }

    public void setEstatus(Integer estatus) {
        this.estatus = estatus;
    }

}
