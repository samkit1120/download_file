package dw.com.presentation.backingBeans;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.TimeZone;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.WordUtils;
import org.apache.poi.POIDocument;
import org.apache.poi.POIOLE2TextExtractor;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.primefaces.component.commandbutton.CommandButton;
import org.primefaces.component.inputtext.InputText;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.RowEditEvent;
import org.primefaces.model.UploadedFile;

import dw.com.exceptions.ZMessManager;
import dw.com.file.Paginas;
import dw.com.file.PaginasId;
import dw.com.file.dto.PaginasDTO;
import dw.com.presentation.businessDelegate.IBusinessDelegatorView;
import dw.com.utilities.FacesUtils;


/**
 * @author Zathura Code Generator http://code.google.com/p/zathura/
 *
 */
@ManagedBean
@ViewScoped
public class PaginasView implements Serializable {
    private static final long serialVersionUID = 1L;
    private InputText txtUrlPagina;
    private InputText txtDescripcionPagina;
    private CommandButton btnSave;
    private CommandButton btnModify;
    private CommandButton btnDelete;
    private CommandButton btnClear;
    private List<PaginasDTO> data;
    private PaginasDTO selectedPaginas;
    private Paginas entity;
    private boolean showDialog;
    @ManagedProperty(value = "#{BusinessDelegatorView}")
    private IBusinessDelegatorView businessDelegatorView;
    
    private ArrayList<String> listaCaciones;

    public PaginasView() {
        super();
    }

    public void rowEventListener(RowEditEvent e) {
        try {
            PaginasDTO paginasDTO = (PaginasDTO) e.getObject();

            if (txtUrlPagina == null) {
                txtUrlPagina = new InputText();
            }

            txtUrlPagina.setValue(paginasDTO.getUrlPagina());

            if (txtDescripcionPagina == null) {
                txtDescripcionPagina = new InputText();
            }

            txtDescripcionPagina.setValue(paginasDTO.getDescripcionPagina());

            PaginasId id = new PaginasId();
            id.setUrlPagina((((txtUrlPagina.getValue()) == null) ||
                (txtUrlPagina.getValue()).equals("")) ? null
                                                      : FacesUtils.checkString(
                    txtUrlPagina));
            id.setDescripcionPagina((((txtDescripcionPagina.getValue()) == null) ||
                (txtDescripcionPagina.getValue()).equals("")) ? null
                                                              : FacesUtils.checkString(
                    txtDescripcionPagina));
            entity = businessDelegatorView.getPaginas(id);

            action_modify();
        } catch (Exception ex) {
        }
    }

    public String action_new() {
        action_clear();
        selectedPaginas = null;
        setShowDialog(true);

        return "";
    }

    public String action_clear() {
        entity = null;
        selectedPaginas = null;

        if (txtUrlPagina != null) {
            txtUrlPagina.setValue(null);
            txtUrlPagina.setDisabled(false);
        }

        if (txtDescripcionPagina != null) {
            txtDescripcionPagina.setValue(null);
            txtDescripcionPagina.setDisabled(false);
        }

        if (btnSave != null) {
            btnSave.setDisabled(true);
        }

        if (btnDelete != null) {
            btnDelete.setDisabled(true);
        }

        return "";
    }

    public void listener_txtId() {
        try {
            PaginasId id = new PaginasId();
            id.setUrlPagina((((txtUrlPagina.getValue()) == null) ||
                (txtUrlPagina.getValue()).equals("")) ? null
                                                      : FacesUtils.checkString(
                    txtUrlPagina));
            id.setDescripcionPagina((((txtDescripcionPagina.getValue()) == null) ||
                (txtDescripcionPagina.getValue()).equals("")) ? null
                                                              : FacesUtils.checkString(
                    txtDescripcionPagina));
            entity = (id != null) ? businessDelegatorView.getPaginas(id) : null;
        } catch (Exception e) {
            entity = null;
        }

        if (entity == null) {
            txtUrlPagina.setDisabled(false);
            txtDescripcionPagina.setDisabled(false);
            btnSave.setDisabled(false);
        } else {
            txtUrlPagina.setValue(entity.getId().getUrlPagina());
            txtUrlPagina.setDisabled(true);
            txtDescripcionPagina.setValue(entity.getId().getDescripcionPagina());
            txtDescripcionPagina.setDisabled(true);
            btnSave.setDisabled(false);

            if (btnDelete != null) {
                btnDelete.setDisabled(false);
            }
        }
    }

    public String action_edit(ActionEvent evt) {
        selectedPaginas = (PaginasDTO) (evt.getComponent().getAttributes()
                                           .get("selectedPaginas"));
        txtUrlPagina.setValue(selectedPaginas.getUrlPagina());
        txtUrlPagina.setDisabled(true);
        txtDescripcionPagina.setValue(selectedPaginas.getDescripcionPagina());
        txtDescripcionPagina.setDisabled(true);
        btnSave.setDisabled(false);
        setShowDialog(true);

        return "";
    }

    public String action_save() {
        try {
            if ((selectedPaginas == null) && (entity == null)) {
                action_create();
            } else {
                action_modify();
            }

            data = null;
        } catch (Exception e) {
            FacesUtils.addErrorMessage(e.getMessage());
        }

        return "";
    }

    public String action_create() {
        try {
            entity = new Paginas();

            PaginasId id = new PaginasId();
            id.setUrlPagina((((txtUrlPagina.getValue()) == null) ||
                (txtUrlPagina.getValue()).equals("")) ? null
                                                      : FacesUtils.checkString(
                    txtUrlPagina));
            id.setDescripcionPagina((((txtDescripcionPagina.getValue()) == null) ||
                (txtDescripcionPagina.getValue()).equals("")) ? null
                                                              : FacesUtils.checkString(
                    txtDescripcionPagina));

            entity.setId(id);
            businessDelegatorView.savePaginas(entity);
            FacesUtils.addInfoMessage(ZMessManager.ENTITY_SUCCESFULLYSAVED);
            action_clear();
        } catch (Exception e) {
            entity = null;
            FacesUtils.addErrorMessage(e.getMessage());
        }

        return "";
    }

    public String action_modify() {
        try {
            if (entity == null) {
                PaginasId id = new PaginasId();
                id.setUrlPagina(selectedPaginas.getUrlPagina());
                id.setDescripcionPagina(selectedPaginas.getDescripcionPagina());
                entity = businessDelegatorView.getPaginas(id);
            }

            businessDelegatorView.updatePaginas(entity);
            FacesUtils.addInfoMessage(ZMessManager.ENTITY_SUCCESFULLYMODIFIED);
        } catch (Exception e) {
            data = null;
            FacesUtils.addErrorMessage(e.getMessage());
        }

        return "";
    }

    public String action_delete_datatable(ActionEvent evt) {
        try {
            selectedPaginas = (PaginasDTO) (evt.getComponent().getAttributes()
                                               .get("selectedPaginas"));

            PaginasId id = new PaginasId();
            id.setUrlPagina(selectedPaginas.getUrlPagina());
            id.setDescripcionPagina(selectedPaginas.getDescripcionPagina());
            entity = businessDelegatorView.getPaginas(id);
            action_delete();
        } catch (Exception e) {
            FacesUtils.addErrorMessage(e.getMessage());
        }

        return "";
    }

    public String action_delete_master() {
        try {
            PaginasId id = new PaginasId();
            id.setUrlPagina((((txtUrlPagina.getValue()) == null) ||
                (txtUrlPagina.getValue()).equals("")) ? null
                                                      : FacesUtils.checkString(
                    txtUrlPagina));
            id.setDescripcionPagina((((txtDescripcionPagina.getValue()) == null) ||
                (txtDescripcionPagina.getValue()).equals("")) ? null
                                                              : FacesUtils.checkString(
                    txtDescripcionPagina));
            entity = businessDelegatorView.getPaginas(id);
            action_delete();
        } catch (Exception e) {
            FacesUtils.addErrorMessage(e.getMessage());
        }

        return "";
    }

    public void action_delete() throws Exception {
        try {
            businessDelegatorView.deletePaginas(entity);
            FacesUtils.addInfoMessage(ZMessManager.ENTITY_SUCCESFULLYDELETED);
            action_clear();
            data = null;
        } catch (Exception e) {
            throw e;
        }
    }

    public String action_closeDialog() {
        setShowDialog(false);
        action_clear();

        return "";
    }

    public String actionDeleteDataTableEditable(ActionEvent evt) {
        try {
            selectedPaginas = (PaginasDTO) (evt.getComponent().getAttributes()
                                               .get("selectedPaginas"));

            PaginasId id = new PaginasId();
            id.setUrlPagina(selectedPaginas.getUrlPagina());
            id.setDescripcionPagina(selectedPaginas.getDescripcionPagina());
            entity = businessDelegatorView.getPaginas(id);
            businessDelegatorView.deletePaginas(entity);
            data.remove(selectedPaginas);
            FacesUtils.addInfoMessage(ZMessManager.ENTITY_SUCCESFULLYDELETED);
            action_clear();
        } catch (Exception e) {
            FacesUtils.addErrorMessage(e.getMessage());
        }

        return "";
    }

    public String action_modifyWitDTO(String urlPagina, String descripcionPagina)
        throws Exception {
        try {
            businessDelegatorView.updatePaginas(entity);
            FacesUtils.addInfoMessage(ZMessManager.ENTITY_SUCCESFULLYMODIFIED);
        } catch (Exception e) {
            //renderManager.getOnDemandRenderer("PaginasView").requestRender();
            FacesUtils.addErrorMessage(e.getMessage());
            throw e;
        }

        return "";
    }
    
    public void leerDocumento(FileUploadEvent event) throws IOException, BiffException{
    	
    	UploadedFile uploadedFile = event.getFile();

    	String extension = FilenameUtils.getExtension(uploadedFile.getFileName());
    	
         try{
        	 
        	if(extension.equals("xlsx")){
         		readExcelXLXS(guardaEnFicheroTemporal(event));
         	}
        	 
        	if(extension.equals("xls")){
          		readExcelXLS(guardaEnFicheroTemporal(event));
          	}
        	
        	if(extension.equals("docx")){
        		readWordDOCX(guardaEnFicheroTemporal(event));
          	}
        	
        	if(extension.equals("doc")){
        		readWordDOC(guardaEnFicheroTemporal(event));
          	}
        	
         }catch(IOException e){
         }
    }
        
    
    /**
	 * Permite crear un archivo temporal
	 * @param event(Evento del FileUploadEvent al subir el archivo)
	 * @throws IOException 
	 */
	public static File guardaEnFicheroTemporal(FileUploadEvent event) throws IOException {
		
		File file = null;
		
		FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();
    	
    	UploadedFile uploadedFile = event.getFile();
    	
    	File ruta = new File(externalContext.getRealPath("/uploadTemp/"));
    	String nombreImagen = FilenameUtils.getBaseName(uploadedFile.getFileName()); 
    	String extension = FilenameUtils.getExtension(uploadedFile.getFileName());
    	file = (File.createTempFile(nombreImagen + "-", "." + extension, ruta));
    	
    	try (InputStream input = uploadedFile.getInputstream()) {
    	    Files.copy(input, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
    	}

		return file;
	}
	
	public void readExcelXLS(File file) throws IOException, BiffException{
		
        Workbook workbook = Workbook.getWorkbook(file);
 		
 		//Seleccionamos la hoja que vamos a leer
 		Sheet sheet = workbook.getSheet(0);
 		String nombre;
 	
 		listaCaciones =  new ArrayList<>();
 		//recorremos las filas
 		for (int fila = 1; fila < sheet.getRows(); fila++) {
 			
 			//recorremos las columnas
 			for (int columna = 0; columna < sheet.getColumns(); columna++) {
 				
 				//setear la celda leida a nombre
 				nombre = sheet.getCell(columna, fila).getContents();
 				
 				// imprimir nombre
 				listaCaciones.add(nombre);
 				//System.out.print(nombre + ""); 
 			} 			
// 			System.out.println("\n");
// 			System.out.println("————————————-");
 		}
		
	}
	
    public void readExcelXLXS(File file) throws IOException {

    	try {
	         
            FileInputStream fis = new FileInputStream(file);

            // Finds the workbook instance for XLSX file
            XSSFWorkbook myWorkBook = new XSSFWorkbook (fis);
           
            // Return first sheet from the XLSX workbook
            XSSFSheet mySheet = myWorkBook.getSheetAt(0);
           
            // Get iterator to all the rows in current sheet
            Iterator<Row> rowIterator = mySheet.iterator();
           
            listaCaciones =  new ArrayList<>();
            
            // Traversing over each row of XLSX file
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();

                // For each row, iterate through each columns
                Iterator<Cell> cellIterator = row.cellIterator();
                while (cellIterator.hasNext()) {

                    Cell cell = cellIterator.next();

                    switch (cell.getCellType()) {
                    case Cell.CELL_TYPE_STRING:
                    	listaCaciones.add(cell.getStringCellValue());
//                        System.out.print(cell.getStringCellValue() + "\t");
                        break;
                    case Cell.CELL_TYPE_NUMERIC:
//                        System.out.print(cell.getNumericCellValue() + "\t");
                        break;
                    case Cell.CELL_TYPE_BOOLEAN:
//                        System.out.print(cell.getBooleanCellValue() + "\t");
                        break;
                    default :
                 
                    }
                }
                System.out.println("");
            }
            
            file.delete();
      
        } catch (IOException e) {
       
        }
	}
    
    
    public void readWordDOCX(File file) throws IOException{

    	//Creamos el stream fijense bien los objetos usados
    	FileInputStream fis = new FileInputStream(file);
    	InputStream entradaArch = fis;
    	listaCaciones =  new ArrayList<>();
    	
    	//Se crea un documento que la POI entiende pasandole el stream
    	XWPFDocument ardocx = new XWPFDocument(entradaArch); 
    	
    	//instanciamos el obj para extraer contenido pasando el documento
    	XWPFWordExtractor xwpf_we = new XWPFWordExtractor(ardocx);
    	
    	//leer el texto para un .docx
    	listaCaciones.add(xwpf_we.getText());
    	String texto = xwpf_we.getText(); 
    	System.out.println(texto);

    	//se imprime 
    	System.out.println(listaCaciones); 
    }
    
    public void readWordDOC(File file) throws IOException{
   	
    	//Creamos el stream fijense bien los objetos usados
    	FileInputStream fis = new FileInputStream(file);
    	InputStream entradaArch = fis;
    	listaCaciones =  new ArrayList<>();
    	
    	HWPFDocument doc = new HWPFDocument(fis);
    	
    	//Creamos el extractor pasandole el stream
    	WordExtractor we = new WordExtractor(doc);
    	
    	//Leemos y guardamos en un String
    	listaCaciones.add(we.getText());
    	String texto = we.getText();
    		
    	//Lo imprimimos para probar
    	System.out.print(listaCaciones); 
    }
    
    

    public InputText getTxtUrlPagina() {
        return txtUrlPagina;
    }

    public void setTxtUrlPagina(InputText txtUrlPagina) {
        this.txtUrlPagina = txtUrlPagina;
    }

    public InputText getTxtDescripcionPagina() {
        return txtDescripcionPagina;
    }

    public void setTxtDescripcionPagina(InputText txtDescripcionPagina) {
        this.txtDescripcionPagina = txtDescripcionPagina;
    }

    public List<PaginasDTO> getData() {
        try {
            if (data == null) {
                data = businessDelegatorView.getDataPaginas();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return data;
    }

    public void setData(List<PaginasDTO> paginasDTO) {
        this.data = paginasDTO;
    }

    public PaginasDTO getSelectedPaginas() {
        return selectedPaginas;
    }

    public void setSelectedPaginas(PaginasDTO paginas) {
        this.selectedPaginas = paginas;
    }

    public CommandButton getBtnSave() {
        return btnSave;
    }

    public void setBtnSave(CommandButton btnSave) {
        this.btnSave = btnSave;
    }

    public CommandButton getBtnModify() {
        return btnModify;
    }

    public void setBtnModify(CommandButton btnModify) {
        this.btnModify = btnModify;
    }

    public CommandButton getBtnDelete() {
        return btnDelete;
    }

    public void setBtnDelete(CommandButton btnDelete) {
        this.btnDelete = btnDelete;
    }

    public CommandButton getBtnClear() {
        return btnClear;
    }

    public void setBtnClear(CommandButton btnClear) {
        this.btnClear = btnClear;
    }

    public TimeZone getTimeZone() {
        return java.util.TimeZone.getDefault();
    }

    public IBusinessDelegatorView getBusinessDelegatorView() {
        return businessDelegatorView;
    }

    public void setBusinessDelegatorView(
        IBusinessDelegatorView businessDelegatorView) {
        this.businessDelegatorView = businessDelegatorView;
    }

    public ArrayList<String> getListaCaciones() {
		return listaCaciones;
	}

	public void setListaCaciones(ArrayList<String> listaCaciones) {
		this.listaCaciones = listaCaciones;
	}

	public boolean isShowDialog() {
        return showDialog;
    }

    public void setShowDialog(boolean showDialog) {
        this.showDialog = showDialog;
    }
}
