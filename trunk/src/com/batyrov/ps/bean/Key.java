package com.batyrov.ps.bean;

import javax.persistence.*;
import java.sql.Blob;

/**
 * Created by IntelliJ IDEA.
 * User: sulic
 * Date: 12.03.2008
 * Time: 22:44:54
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "T_KEY")
public class Key {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private long id;

    @Column(name = "DATA")
    private byte[] data;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }
}
