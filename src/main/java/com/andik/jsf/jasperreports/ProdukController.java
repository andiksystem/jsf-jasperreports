/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.andik.jsf.jasperreports;

import com.andik.jsf.jasperreports.entity.Kategori;
import com.andik.jsf.jasperreports.entity.Produk;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

/**
 *
 * @author andik
 */
@Named
@ViewScoped
public class ProdukController implements Serializable {

    private List<Kategori> kategoris;
    private Kategori selectedFilterKategori;
    private List<Produk> produks;

    @EJB
    private ProdukService produkService;

    @PostConstruct
    public void init() {
        kategoris = produkService.findAllKategori();
        selectedFilterKategori = null;
        search();
    }

    public void search() {
        if (selectedFilterKategori == null) {
            produks = produkService.findAll();
        } else {
            produks = produkService.findByKategori(selectedFilterKategori);
        }
    }

    public Kategori getSelectedFilterKategori() {
        return selectedFilterKategori;
    }

    public void setSelectedFilterKategori(Kategori selectedFilterKategori) {
        this.selectedFilterKategori = selectedFilterKategori;
    }

    public List<Produk> getProduks() {
        return produks;
    }

    public List<Kategori> getKategoris() {
        return kategoris;
    }

    public void cetakPdf() throws FileNotFoundException, JRException, IOException {
        HashMap<String, Object> paramMap = new HashMap<>();

        String reportPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("resources/reports/produk.jrxml");
        JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(produks);
        JasperReport jasperReport = JasperCompileManager.compileReport(reportPath);
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, paramMap, beanCollectionDataSource);
        HttpServletResponse httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
        String namaFile = "LAPORAN_PRODUK_" + UUID.randomUUID() + ".pdf";
        httpServletResponse.setContentType("application/pdf");
        httpServletResponse.setHeader("Content-disposition", "filename=" + namaFile);
        ServletOutputStream servletOutputStream = httpServletResponse.getOutputStream();
        JasperExportManager.exportReportToPdfStream(jasperPrint, servletOutputStream);
        FacesContext.getCurrentInstance().responseComplete();

    }

}
