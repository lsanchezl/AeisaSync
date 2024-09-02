/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mx.com.aeisa.sync.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author lasla
 */
@Entity
@Table(catalog = "adaeisa_comercial_2022", schema = "dbo", name = "admalmacenes")
@NamedQuery(name = "AlmacenSql.getAll",
        query = "SELECT a FROM AlmacenSql a where a.id > 0")
public class AlmacenSql implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "cidalmacen")
    private Long id;

    @Column(name = "ccodigoalmacen")
    private String codigo;

    @Column(name = "cnombrealmacen")
    private String nombre;

    @Column(name = "ctextoextra1")
    private String siglas;

    public AlmacenSql() {
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

    public String getSiglas() {
        return siglas;
    }

    public void setSiglas(String siglas) {
        this.siglas = siglas;
    }

}
