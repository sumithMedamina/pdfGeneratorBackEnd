package com.pdfgenerator.service;

import java.io.*;
import java.net.URL;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Service;

import com.aspose.cells.PdfSaveOptions;
import com.aspose.cells.Workbook;

@Service
public class PdfConversionService {

    private static final String S3_FILE_URL = "ttps://myexcelsatish.s3.ap-south-1.amazonaws.com/Excel+BI_ETLI_PGI_Plan_without+Filter_V1+1.xlsm";
    private static final String DOWNLOAD_DIRECTORY = "C:\\Users\\User\\Documents\\getstockprice\\downloadingpdf\\src\\main\\resources\\static\\";
    private static final String PDF_DIRECTORY = "C:\\Users\\User\\Documents\\getstockprice\\downloadingpdf\\src\\main\\resources\\static\\"; // Directory to save PDFs
    private static final String DEFAULT_EXCEL_PATH = "C:\\Users\\User\\Documents\\getstockprice\\downloadingpdf\\src\\main\\resources\\defaultExcel.xlsm"; // Path to default Excel file

    public ByteArrayInputStream convertExcelToPdf(String name) throws Exception {
        // Attempt to download the Excel file from S3
        String excelFilePath;
        boolean isS3File = true;
        try {
            excelFilePath = downloadExcelFromS3();
        } catch (IOException e) {
            System.out.println("Failed to download Excel from S3. Using default Excel file.");
            // Copy the default Excel file to the download directory
            excelFilePath = copyDefaultExcelToDownloadDirectory();
            isS3File = false;
        }

        // Ensure the PDF directory exists
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
        try (FileInputStream file = new FileInputStream(excelFilePath)) {
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

        // Schedule deletion of the downloaded Excel file or default copy
        scheduleFileDeletion(excelFilePath);

        return new ByteArrayInputStream(outputStream.toByteArray()); // Return as ByteArrayInputStream
    }

    private String downloadExcelFromS3() throws IOException {
        // Set up the destination path
        String destinationPath = DOWNLOAD_DIRECTORY + "downloaded_excel.xlsm";

        // Download the Excel file from the S3 URL
        try (BufferedInputStream in = new BufferedInputStream(new URL(S3_FILE_URL).openStream());
             FileOutputStream fileOutputStream = new FileOutputStream(destinationPath)) {
            byte[] dataBuffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                fileOutputStream.write(dataBuffer, 0, bytesRead);
            }
            System.out.println("Excel file downloaded to " + destinationPath);
        } catch (IOException e) {
            e.getMessage();
            throw e;
        }

        return destinationPath; // Return the path of the downloaded Excel file
    }

    private String copyDefaultExcelToDownloadDirectory() throws IOException {
        // Set up the destination path
        String destinationPath = DOWNLOAD_DIRECTORY + "default_excel_copy.xlsm";

        // Copy the default Excel file to the download directory
        Files.copy(Paths.get(DEFAULT_EXCEL_PATH), Paths.get(destinationPath), StandardCopyOption.REPLACE_EXISTING);
        System.out.println("Default Excel file copied to " + destinationPath);

        return destinationPath; // Return the path of the copied default Excel file
    }

    private void scheduleFileDeletion(String filePath) {
        // Create a new thread to delete the file after 5 seconds
        new Thread(() -> {
            try {
                // Sleep for 5 seconds
                Thread.sleep(5000);

                // Delete the file
                File file = new File(filePath);
                if (file.exists() && file.delete()) {
                    System.out.println("Temporary file deleted successfully.");
                } else {
                    System.out.println("Failed to delete the temporary file.");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }
}
