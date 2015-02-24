package dw.com.file.control;

import dw.com.file.Paginas;
import dw.com.file.PaginasId;
import dw.com.file.dto.PaginasDTO;

import java.math.BigDecimal;

import java.util.*;
import java.util.Date;
import java.util.List;
import java.util.Set;


/**
* @author Zathura Code Generator http://code.google.com/p/zathura/
*
*/
public interface IPaginasLogic {
    public List<Paginas> getPaginas() throws Exception;

    /**
         * Save an new Paginas entity
         */
    public void savePaginas(Paginas entity) throws Exception;

    /**
         * Delete an existing Paginas entity
         *
         */
    public void deletePaginas(Paginas entity) throws Exception;

    /**
        * Update an existing Paginas entity
        *
        */
    public void updatePaginas(Paginas entity) throws Exception;

    /**
         * Load an existing Paginas entity
         *
         */
    public Paginas getPaginas(PaginasId id) throws Exception;

    public List<Paginas> findByCriteria(Object[] variables,
        Object[] variablesBetween, Object[] variablesBetweenDates)
        throws Exception;

    public List<Paginas> findPagePaginas(String sortColumnName,
        boolean sortAscending, int startRow, int maxResults)
        throws Exception;

    public Long findTotalNumberPaginas() throws Exception;

    public List<PaginasDTO> getDataPaginas() throws Exception;
}
