package com.example.ram.pdf;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.text.SimpleDateFormat;
import javax.servlet.http.HttpServletResponse;

import com.example.ram.model.HistorialPagos;
import com.example.ram.model.Servicios;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.CMYKColor;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

public class PdfGenerator {

	public void generate(List<Servicios> serviciosList, HttpServletResponse response) throws DocumentException, IOException {
	    Document document = new Document(PageSize.A4);
	    PdfWriter.getInstance(document, response.getOutputStream());
	    document.open();

	    Font fontTitle = FontFactory.getFont(FontFactory.TIMES_ROMAN);
	    fontTitle.setSize(20);

	    // Título del documento
	    Paragraph paragraph = new Paragraph("Lista de Servicios", fontTitle);
	    paragraph.setAlignment(Paragraph.ALIGN_CENTER);
	    document.add(paragraph);

	    PdfPTable table = new PdfPTable(8); // Ajustado al número de columnas que estás utilizando

	    table.setWidthPercentage(100f);
	    table.setSpacingBefore(3);

	    // Configurar estilos para el encabezado
	    Font headerFont = FontFactory.getFont(FontFactory.TIMES_ROMAN);
	    headerFont.setColor(CMYKColor.WHITE);

	    // Añadir celdas de encabezado
	    addHeaderCell(table, "ID", headerFont);
	    addHeaderCell(table, "Nombre", headerFont);
	    addHeaderCell(table, "Descripción", headerFont);
	    addHeaderCell(table, "Costo", headerFont);
	    addHeaderCell(table, "Caracteristicas", headerFont);
	    addHeaderCell(table, "Proveedor", headerFont);
	    addHeaderCell(table, "Categoria", headerFont);
	    addHeaderCell(table, "Fecha de Lanzamiento", headerFont);
	    

	    // Añadir datos de servicios a la tabla (comentar esta sección si aún no tienes datos)
	    for (Servicios servicio : serviciosList) {
	        table.addCell(String.valueOf(servicio.getId()));
	        table.addCell(servicio.getNombre());
	        table.addCell(servicio.getDescripcion());
	        table.addCell(String.valueOf(servicio.getCosto()));
	        table.addCell(servicio.getCaracteristicas());
	        table.addCell(servicio.getProveedor());
	        table.addCell(servicio.getCategoria());
	        
	        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
	        String formattedDate = dateFormat.format(servicio.getFechaLanzamiento());
	        table.addCell(formattedDate);
	        
	    }

	    document.add(table);
	    document.close();
	}

	
	
	
	
	
	
	
	
	
	
	public void generatePDFHistory(List<HistorialPagos> historialPagos, HttpServletResponse response) throws DocumentException, IOException {
	    Document document = new Document(PageSize.A4);
	    PdfWriter.getInstance(document, response.getOutputStream());
	    document.open();

	    Font fontTitle = FontFactory.getFont(FontFactory.TIMES_ROMAN);
	    fontTitle.setSize(20);

	    // Título del documento
	    Paragraph paragraph = new Paragraph("Historial de Pagos", fontTitle);
	    paragraph.setAlignment(Paragraph.ALIGN_CENTER);
	    document.add(paragraph);

	    PdfPTable table = new PdfPTable(4); // Ajustado al número de columnas que estás utilizando

	    table.setWidthPercentage(100f);
	    table.setSpacingBefore(3);

	    // Configurar estilos para el encabezado
	    Font headerFont = FontFactory.getFont(FontFactory.TIMES_ROMAN);
	    headerFont.setColor(CMYKColor.WHITE);

	    // Añadir celdas de encabezado
	    addHeaderCell(table, "Numero de Pago", headerFont);
	    addHeaderCell(table, "Fecha de Compra", headerFont);
	    addHeaderCell(table, "Servicios Requeridos", headerFont);
	    addHeaderCell(table, "Total Pagado", headerFont);

	    

	    // Añadir datos de servicios a la tabla (comentar esta sección si aún no tienes datos)
	    for (HistorialPagos historial: historialPagos) {
	        table.addCell(String.valueOf(historial.getId()));

	        table.addCell(String.valueOf(historial.getFechaActual()));
	        
	        
	        String servicios = historial.getServicios().stream()
	                .map(Servicios::getNombre)  // Ajusta el nombre del método según la estructura de tu clase Servicio
	                .collect(Collectors.joining(", "));
	        
	        
	        table.addCell(servicios);
	        
	        table.addCell(String.valueOf(historial.getTotalPagado()));
	        
	        
	    }

	    document.add(table);
	    document.close();
	}

	
	
	
	
	
	
	
	
	private void addHeaderCell(PdfPTable table, String headerText, Font font) {
	    PdfPCell headerCell = new PdfPCell(new Phrase(headerText, font));
	    headerCell.setBackgroundColor(CMYKColor.BLUE);
	    headerCell.setPadding(5);
	    headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
	    table.addCell(headerCell);
	}



}
