package com.pdfgenerator.service;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.stereotype.Service;

import com.pdfgenerator.entity.UserDetails;

@Service
public class ExcelUpdateService {

    private static final String FILE_LOCATION = "C:\\Users\\msk27\\Downloads\\Excel BI_ETLI_PGI_Plan_without Filter_V1 1.xlsm";
    private static final String SHEET_NAME = "Input";

    // Row and column indices for specific fields in the Excel sheet
    private static final int LIFE_ASSURED_TITLE_ROW = 3;
    private static final int LIFE_ASSURED_TITLE_COL = 2;
    private static final int LIFE_ASSURED_FIRST_NAME_ROW = 4;
    private static final int LIFE_ASSURED_FIRST_NAME_COL = 2;
    private static final int LIFE_ASSURED_LAST_NAME_ROW = 5;
    private static final int LIFE_ASSURED_LAST_NAME_COL = 2;
    private static final int LIFE_ASSURED_DOB_ROW = 6;
    private static final int LIFE_ASSURED_DOB_COL = 2;
    private static final int PLAN_OPTION_ROW = 8;
    private static final int PLAN_OPTION_COL = 2;
    private static final int SUB_OPTION_ROW = 9;
    private static final int SUB_OPTION_COL = 2;
    private static final int INCOME_BENEFIT_FREQUENCY_ROW = 10;
    private static final int INCOME_BENEFIT_FREQUENCY_COL = 2;
    private static final int FAMILY_INCOME_BENEFIT_ROW = 12;
    private static final int FAMILY_INCOME_BENEFIT_COL = 2;
    private static final int PREMIUM_PAYING_TERM_ROW = 14;
    private static final int PREMIUM_PAYING_TERM_COL = 2;
    private static final int POLICY_TERM_ROW = 15;
    private static final int POLICY_TERM_COL = 2;
    private static final int PREMIUM_PAYING_MODE_ROW = 16;
    private static final int PREMIUM_PAYING_MODE_COL = 2;
    private static final int INSTALLMENT_PREMIUM_ROW = 17;
    private static final int INSTALLMENT_PREMIUM_COL = 2;
    private static final int MOBILE_ROW = 3;
    private static final int MOBILE_COL = 6;
    private static final int EMAIL_ROW = 4;
    private static final int EMAIL_COL = 6;
    private static final int DISTRIBUTION_CHANNEL_ROW = 5;
    private static final int DISTRIBUTION_CHANNEL_COL = 6;
    private static final int CATEGORY_ROW = 6;
    private static final int CATEGORY_COL = 6;
    private static final int PROPOSER_DIFFERENT_ROW = 10;
    private static final int PROPOSER_DIFFERENT_COL = 6;
    private static final int PROPOSER_TITLE_ROW = 11;
    private static final int PROPOSER_TITLE_COL = 6;
    private static final int PROPOSER_FIRST_NAME_ROW = 12;
    private static final int PROPOSER_FIRST_NAME_COL = 6;
    private static final int PROPOSER_LAST_NAME_ROW = 13;
    private static final int PROPOSER_LAST_NAME_COL = 6;
    private static final int PROPOSER_DOB_ROW = 14;
    private static final int PROPOSER_DOB_COL = 6;

    public void updateExcelWithData(UserDetails userDetails) throws Exception {
        try (FileInputStream file = new FileInputStream(FILE_LOCATION);
             Workbook workbook = WorkbookFactory.create(file)) {
            Sheet sheet = workbook.getSheet(SHEET_NAME);

            // Update fields in the Excel sheet
            updateCell(sheet, LIFE_ASSURED_TITLE_ROW, LIFE_ASSURED_TITLE_COL, userDetails.getLifeAssuredTitle());
            updateCell(sheet, LIFE_ASSURED_FIRST_NAME_ROW, LIFE_ASSURED_FIRST_NAME_COL, userDetails.getLifeAssuredFirstName());
            updateCell(sheet, LIFE_ASSURED_LAST_NAME_ROW, LIFE_ASSURED_LAST_NAME_COL, userDetails.getLifeAssuredLastName());
            
            // Convert LocalDate to Date for DOB
            
            updateCell(sheet, LIFE_ASSURED_DOB_ROW, LIFE_ASSURED_DOB_COL, userDetails.getLifeAssuredDOB());
            
            updateCell(sheet, PLAN_OPTION_ROW, PLAN_OPTION_COL, userDetails.getPlanOption());
            updateCell(sheet, SUB_OPTION_ROW, SUB_OPTION_COL, userDetails.getSubOption());
            updateCell(sheet, INCOME_BENEFIT_FREQUENCY_ROW, INCOME_BENEFIT_FREQUENCY_COL, userDetails.getIncomeBenefitFrequency());
            updateCell(sheet, FAMILY_INCOME_BENEFIT_ROW, FAMILY_INCOME_BENEFIT_COL, userDetails.getFamilyIncomeBenefit());
            
            
            updateCell(sheet, PREMIUM_PAYING_TERM_ROW, PREMIUM_PAYING_TERM_COL,Integer.parseInt( userDetails.getPremiumPayingTerm()));
            updateCell(sheet, POLICY_TERM_ROW, POLICY_TERM_COL, Integer.parseInt(userDetails.getPolicyTerm()));
            updateCell(sheet, PREMIUM_PAYING_MODE_ROW, PREMIUM_PAYING_MODE_COL, userDetails.getPremiumPayingMode());

            // Convert installment premium to integer
            int installmentPremium = Integer.parseInt(userDetails.getInstallmentPremium());
            updateCell(sheet, INSTALLMENT_PREMIUM_ROW, INSTALLMENT_PREMIUM_COL, installmentPremium);

            updateCell(sheet, MOBILE_ROW, MOBILE_COL, userDetails.getMobile());
            updateCell(sheet, EMAIL_ROW, EMAIL_COL, userDetails.getEmail());
            updateCell(sheet, DISTRIBUTION_CHANNEL_ROW, DISTRIBUTION_CHANNEL_COL, userDetails.getDistributionChannel());
            updateCell(sheet, CATEGORY_ROW, CATEGORY_COL, userDetails.getCategory());

            // Update boolean value for proposerDifferent
            updateCell(sheet, PROPOSER_DIFFERENT_ROW, PROPOSER_DIFFERENT_COL, userDetails.isProposerDifferent() ? "Yes" : "No");

            updateCell(sheet, PROPOSER_TITLE_ROW, PROPOSER_TITLE_COL, userDetails.getProposerTitle());
            updateCell(sheet, PROPOSER_FIRST_NAME_ROW, PROPOSER_FIRST_NAME_COL, userDetails.getProposerFirstName());
            updateCell(sheet, PROPOSER_LAST_NAME_ROW, PROPOSER_LAST_NAME_COL, userDetails.getProposerLastName());

            // Convert proposer DOB to Date
           
            updateCell(sheet, PROPOSER_DOB_ROW, PROPOSER_DOB_COL, userDetails.getProposerDOB());

            // Write changes to the file
            try (FileOutputStream fos = new FileOutputStream(FILE_LOCATION)) {
                workbook.write(fos);
            }
        } catch (IOException e) {
            throw new IOException("Error accessing or writing to the Excel file", e);
        } catch (Exception e) {
            throw new Exception("Error updating Excel file", e);
        }
    }

    private void updateCell(Sheet sheet, int rowNum, int colNum, Object value) {
        Row row = sheet.getRow(rowNum);
        if (row == null) {
            row = sheet.createRow(rowNum);
        }
        Cell cell = row.getCell(colNum);
        if (cell == null) {
            cell = row.createCell(colNum);
        }

        if (value instanceof String) {
            cell.setCellValue((String) value);
        } else if (value instanceof Date) {
            CellStyle cellStyle = sheet.getWorkbook().createCellStyle();
            CreationHelper createHelper = sheet.getWorkbook().getCreationHelper();
            cellStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd/MM/yyyy"));
            cell.setCellStyle(cellStyle);
            cell.setCellValue((Date) value);
        } else if (value instanceof Integer) {
            cell.setCellValue((Integer) value);
        } else {
            throw new IllegalArgumentException("Unsupported cell value type");
        }
    }
}
