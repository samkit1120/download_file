package dw.com.presentation.businessDelegate;

import dw.com.file.Paginas;
import dw.com.file.PaginasId;
import dw.com.file.control.IPaginasLogic;
import dw.com.file.control.PaginasLogic;
import dw.com.file.dto.PaginasDTO;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.context.annotation.Scope;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;

import java.util.Date;
import java.util.List;
import java.util.Set;


/**
* @author Zathura Code Generator http://code.google.com/p/zathura/
*
*/
public interface IBusinessDelegatorView {
    public List<Paginas> getPaginas() throws Exception;

    public void savePaginas(Paginas entity) throws Exception;

    public void deletePaginas(Paginas entity) throws Exception;

    public void updatePaginas(Paginas entity) throws Exception;

    public Paginas getPaginas(PaginasId id) throws Exception;

    public List<Paginas> findByCriteriaInPaginas(Object[] variables,
        Object[] variablesBetween, Object[] variablesBetweenDates)
        throws Exception;

    public List<Paginas> findPagePaginas(String sortColumnName,
        boolean sortAscending, int startRow, int maxResults)
        throws Exception;

    public Long findTotalNumberPaginas() throws Exception;

    public List<PaginasDTO> getDataPaginas() throws Exception;
}
