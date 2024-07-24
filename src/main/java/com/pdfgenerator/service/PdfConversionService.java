package com.pdfgenerator.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Service;

import com.aspose.cells.PdfSaveOptions;
import com.aspose.cells.Workbook;

@Service
public class PdfConversionService {

    private static final String FILE_LOCATION = "C:\\Users\\msk27\\Downloads\\Excel BI_ETLI_PGI_Plan_without Filter_V1 1.xlsm";
    private static final String PDF_DIRECTORY = "C:\\Users\\msk27\\Desktop\\pdfs\\"; // Directory to save PDFs

    public ByteArrayInputStream convertExcelToPdf(String name) throws Exception {
        File pdfDir = new File(PDF_DIRECTORY);
        if (!pdfDir.exists()) {
            pdfDir.mkdirs();
        }

        // Get current local date and time
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
        String formattedDateTime = LocalDateTime.now().format(formatter);

        // Create the PDF file name with the local time
        String pdfFileName = name + "_" + formattedDateTime + ".pdf";
        String pdfFilePath = PDF_DIRECTORY + pdfFileName;

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try (FileInputStream file = new FileInputStream(FILE_LOCATION)) {
            Workbook workbook = new Workbook(file);
            
            // Recalculate all formulas in the workbook
            workbook.calculateFormula();
           
            // Create PDF options
            PdfSaveOptions options = new PdfSaveOptions();
            options.setOnePagePerSheet(true);
            options.setPageIndex(1); // Set this according to which sheet you want to render
            options.setPageCount(1); // Number of sheets to render

            // Save the workbook as a PDF to the output stream
            workbook.save(outputStream, options);
        }

        // Save PDF locally
        try (FileOutputStream fos = new FileOutputStream(pdfFilePath)) {
            fos.write(outputStream.toByteArray());
        }

        return new ByteArrayInputStream(outputStream.toByteArray()); // Return as ByteArrayInputStream
    }
}
