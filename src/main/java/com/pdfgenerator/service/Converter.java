package com.pdfgenerator.service;

import com.aspose.cells.PdfSaveOptions;
import com.aspose.cells.Workbook;

public class Converter {
    
    public static void pdfCreator() {
        try {
            // Load the Excel file
            Workbook workbook = new Workbook("C:\\Users\\msk27\\Downloads\\Excel BI_ETLI_PGI_Plan_without Filter_V1 1.xlsm");
            workbook.calculateFormula();
           
            // Create PDF options
            PdfSaveOptions options = new PdfSaveOptions();
            options.setOnePagePerSheet(true);
            
            // To render sheet2 only
            options.setPageIndex(1);
            options.setPageCount(1);

            // Save the document as a PDF
            workbook.save("C:\\Users\\msk27\\Desktop\\pdfs\\Excel-to-PDF.pdf", options);
  
            System.out.println("PDF conversion complete!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
