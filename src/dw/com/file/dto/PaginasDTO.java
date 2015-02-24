package dw.com.file.dto;

import dw.com.file.Paginas;

import java.io.Serializable;

import java.util.Date;


/**
*
* @author Zathura Code Generator http://code.google.com/p/zathura/
*
*/
public class PaginasDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private String urlPagina;
    private String descripcionPagina;

    public String getUrlPagina() {
        return urlPagina;
    }

    public void setUrlPagina(String urlPagina) {
        this.urlPagina = urlPagina;
    }

    public String getDescripcionPagina() {
        return descripcionPagina;
    }

    public void setDescripcionPagina(String descripcionPagina) {
        this.descripcionPagina = descripcionPagina;
    }
}
