package com.pdfgenerator.service;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;


public class UpdateDoB {
	
	
	public static void main(String[] args) {
		
			
				Scanner sc = new Scanner(System.in);
				System.out.println("Enter the input value : ");
				String date = sc.nextLine();
				
				WriteExcel("Input",6,2,date);
				System.out.println("Enter the premium value : ");
				int premium = sc.nextInt();
				System.out.println();
				writeExcelPremium("Input",17,2,premium);

				sc.close();
				
				Converter.pdfCreator();
				
	}
	
	
	
	
	public static  String ReadExcel(String sheetName, int rNum, int cNum) {
		String fileLocation = "C:\\Users\\msk27\\Downloads\\Excel BI_ETLI_PGI_Plan_without Filter_V1 1.xlsm";
		
			String date= "";
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		try {
			FileInputStream file = new FileInputStream(fileLocation);
			Workbook workbook = WorkbookFactory.create(file);
			Sheet s = workbook.getSheet(sheetName);
			Row r =s.getRow(rNum);
			Cell c =r.getCell(cNum);
			Date Celldate = c.getDateCellValue();
			date = dateFormat.format(Celldate);			
		}catch(Exception e ) {
			System.out.println("Read Excel Catch Block");
			e.printStackTrace();
		}
		
		return date;
	}

	
	public static void WriteExcel(String sheetName, int rNum, int cNum, String date) {
		String fileLocation = "C:\\Users\\msk27\\Downloads\\Excel BI_ETLI_PGI_Plan_without Filter_V1 1.xlsm";
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		
		try {
			Date cellDate  = dateFormat.parse(date);
			FileInputStream file = new FileInputStream(fileLocation);
			Workbook workbook = WorkbookFactory.create(file);
			Sheet s = workbook.getSheet(sheetName);
			Row r =s.getRow(rNum);
			Cell c =r.getCell(cNum);
			c.setCellValue(cellDate);
			FileOutputStream fos = new FileOutputStream(fileLocation);
			workbook.write(fos);
			
			
		}catch(Exception e ) {
			System.out.println("Write Excel Catch Block");
			e.printStackTrace();
		}
	}

	
	   public static void writeExcelPremium(String sheetName, int rowNum, int colNum, int premium) {
	        String fileLocation = "C:\\Users\\msk27\\Downloads\\Excel BI_ETLI_PGI_Plan_without Filter_V1 1.xlsm";
	        
	        try {
	            FileInputStream file = new FileInputStream(fileLocation);
	            Workbook workbook = WorkbookFactory.create(file);
	            Sheet sheet = workbook.getSheet(sheetName);

	            // Update the premium cell
	            Row row = sheet.getRow(rowNum);
	            Cell cell = row.getCell(colNum);
	            if (cell == null) {
	                cell = row.createCell(colNum);
	            }
	            cell.setCellValue(premium);

	            FileOutputStream fos = new FileOutputStream(fileLocation);
	            workbook.write(fos);
	            fos.close();
	            workbook.close();
	            file.close();
	        } catch (Exception e) {
	            System.out.println("Write Excel Premium Catch Block");
	            e.printStackTrace();
	        }
	    }
	}


