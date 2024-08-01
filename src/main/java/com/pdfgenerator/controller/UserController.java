package com.pdfgenerator.controller;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pdfgenerator.entity.UserDetails;
import com.pdfgenerator.service.ExcelUpdateService;
import com.pdfgenerator.service.PdfConversionService;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:4200/")
public class UserController {

    @Autowired
    private ExcelUpdateService excelUpdateService;

    @Autowired
    private PdfConversionService pdfConversionService;

    @PostMapping("/convert")
    public ResponseEntity<byte[]> convertToPdf(@RequestBody UserDetails userDetails) {
    	System.out.println("*****");
        try {
            // Update the Excel file with the new data
        	System.out.println(userDetails);
            excelUpdateService.updateExcelWithData(userDetails);
           
            String name = userDetails.getLifeAssuredFirstName()+" "+userDetails.getLifeAssuredLastName();
            // Convert the updated Excel file to PDF and get the InputStream
            ByteArrayInputStream pdfStream = pdfConversionService.convertExcelToPdf(name);

            // Convert InputStream to byte array for response
            byte[] pdfData;
            try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = pdfStream.read(buffer)) != -1) {
                    baos.write(buffer, 0, bytesRead);
                }
                pdfData = baos.toByteArray();
            }

            // Return the PDF as a byte array in the response
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=userDetails.pdf")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(pdfData);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
