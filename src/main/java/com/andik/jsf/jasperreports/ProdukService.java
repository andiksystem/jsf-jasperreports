/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.andik.jsf.jasperreports;

import com.andik.jsf.jasperreports.entity.Kategori;
import com.andik.jsf.jasperreports.entity.Produk;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author andik
 */
@Stateless
public class ProdukService {

    @PersistenceContext(unitName = "produk_app_pu")
    private EntityManager em;
    
    public List<Produk> findAll() {
        return em.createQuery("SELECT p FROM Produk p ORDER BY p.nama ASC")
                .getResultList();
    }
    
    public List<Produk> findByKategori(Kategori kategori) {
        return em.createQuery("SELECT p FROM Produk p WHERE p.kategori = :kategori ORDER BY p.nama ASC")
                .setParameter("kategori", kategori)
                .getResultList();
    }
    
    public List<Kategori> findAllKategori() {
        return em.createQuery("SELECT p FROM Kategori p ORDER BY p.nama ASC")
                .getResultList();        
    }

}
