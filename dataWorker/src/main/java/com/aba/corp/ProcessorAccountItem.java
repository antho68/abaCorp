package com.aba.corp;

import com.aba.corp.dto.BankRecordDataDto;
import com.aba.corp.utils.Constants;
import com.aba.corp.utils.Utils;
import org.apache.poi.ss.usermodel.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

public class ProcessorAccountItem
{
    static String PATH_TO_ANALYSE = "C:\\temp";
    static Integer startRow = 6;

    static Integer dateIncomingCellIndex = 0;
    static Integer dateAccountCellIndex = 1;
    static Integer descriptionCellIndex = 2;
    static Integer debitCellIndex = 3;
    static Integer incomeCellIndex = 4;

    static void main() throws IOException
    {
        FileInputStream fis = new FileInputStream(PATH_TO_ANALYSE + "\\comptes.xlsx");
        doFullRead(fis, true);
    }

    public static Collection<BankRecordDataDto> doFullRead(InputStream is, boolean withLog) throws IOException
    {
        Collection<BankRecordDataDto> bankRecordDataDtos = new ArrayList<>();

        readDatas(is, bankRecordDataDtos);
        processDatas(bankRecordDataDtos);
        decryptDatas(bankRecordDataDtos, withLog);

        return bankRecordDataDtos;
    }

    private static void readDatas(InputStream fis, Collection<BankRecordDataDto> bankRecordDataDtos) throws IOException
    {
        Workbook workbook = WorkbookFactory.create(fis);
        Integer lastRow = 0;

        if (workbook.getNumberOfSheets() > 1)
        {
            for (int i = 1; i <= workbook.getNumberOfSheets() - 1; i++)
            {
                Sheet sheet = workbook.getSheetAt(i);
                String sheetName = sheet.getSheetName();
                if (sheetName.contains("hidden"))
                {
                    continue;
                }

                Double totalAmount = 0.0;
                String accountCode = sheetName.replaceAll("Cpt ", "");

                lastRow = sheet.getLastRowNum() - 3;

                for (Row row : sheet)
                {
                    if (row.getRowNum() < startRow || row.getRowNum() > lastRow)
                    {
                        continue;
                    }

                    BankRecordDataDto bankRecordDataDto = new BankRecordDataDto();

                    Cell dateCell = row.getCell(dateIncomingCellIndex);
                    Cell accountDateCell = row.getCell(dateAccountCellIndex);
                    Cell descriptionCell = row.getCell(descriptionCellIndex);
                    Cell debitCell = row.getCell(debitCellIndex);
                    Cell incomeCell = row.getCell(incomeCellIndex);

                    String rowData = "";

                    try
                    {
                        if (dateCell != null)
                        {
                            Date date = dateCell.getDateCellValue();
                            bankRecordDataDto.setDate(date);

                            if (accountDateCell != null)
                            {
                                Date accountDate = dateCell.getDateCellValue();
                                bankRecordDataDto.setEffectiveDate(accountDate);

                                if (accountDate != date)
                                {
                                    rowData += Utils.getDateFormatted(date) + " (" + Utils.getDateFormatted(accountDate) + ")";
                                }
                                else
                                {
                                    rowData += Utils.getDateFormatted(date);
                                }
                            }
                            else
                            {
                                rowData += Utils.getDateFormatted(date);
                            }
                        }

                        if (descriptionCell != null)
                        {
                            String description = " => " + descriptionCell.getStringCellValue();
                            rowData += description;

                            bankRecordDataDto.setDescription(description);
                        }

                        if (debitCell != null && debitCell.getCellType() == CellType.NUMERIC)
                        {
                            rowData += " " + debitCell.getNumericCellValue() + " €";
                            totalAmount += debitCell.getNumericCellValue();

                            bankRecordDataDto.setAmout(debitCell.getNumericCellValue());
                        }

                        if (incomeCell != null && incomeCell.getCellType() == CellType.NUMERIC)
                        {
                            rowData += " +" + incomeCell.getNumericCellValue() + " €";
                            totalAmount += incomeCell.getNumericCellValue();

                            bankRecordDataDto.setAmout(incomeCell.getNumericCellValue());
                        }

                        bankRecordDataDto.setAccountCode(accountCode);

                        rowData += " => " + totalAmount + " €";

                        bankRecordDataDtos.add(bankRecordDataDto);
                    }
                    catch (Exception e)
                    {
                        System.out.println("ERROR processing row: " + (row.getRowNum() + 1)
                                + " sheet: " + sheet.getSheetName());
                    }
                }
            }
        }

        workbook.close();
    }

    private static void processDatas(Collection<BankRecordDataDto> bankRecordDataDtos) throws IOException
    {
        for (BankRecordDataDto bankRecordDataDto : bankRecordDataDtos)
        {
            extractPaymentType(bankRecordDataDto);
            extractOwner(bankRecordDataDto);
            extractType(bankRecordDataDto);
            extractScope(bankRecordDataDto);
        }
    }

    private static void extractOwner(BankRecordDataDto bankRecordDataDto)
    {
        if (bankRecordDataDto.getDescription().contains("CARTE 2016")
                || bankRecordDataDto.getDescription().contains("PLAN ASSUR VIE OY000014652865")
                || bankRecordDataDto.getDescription().contains("PRLV SEPA COMPAGNIE GENERALE")
                || bankRecordDataDto.getDescription().contains("PRLV SEPA FREE MOBILE")
                || bankRecordDataDto.getDescription().contains("PRLV SEPA URSSAF DE FRANCHE COM")
                || bankRecordDataDto.getDescription().contains("VIR ARGENT POUR LE MOIS ANTHO"))
        {
            bankRecordDataDto.setOwner(Constants.BankRecordDataOwner.ANTHO);
        }
        else if (bankRecordDataDto.getDescription().contains("CARTE 9019")
                || bankRecordDataDto.getDescription().contains("PLAN ASSUR VIE OY000014539275")
                || bankRecordDataDto.getDescription().contains("SALAIRE AUTO ECOLE")
                || bankRecordDataDto.getDescription().contains("VIR ARGENT POUR LE MOIS MALO"))
        {
            bankRecordDataDto.setOwner(Constants.BankRecordDataOwner.MALO);
        }
        else if (bankRecordDataDto.getDescription().contains("ASSURANCE ANIMAUX DE COMPAGNIE")
                || bankRecordDataDto.getDescription().contains("VIR INST CROQUETTE"))
        {
            bankRecordDataDto.setOwner(Constants.BankRecordDataOwner.RIO);
        }
        else if (bankRecordDataDto.getDescription().contains("PAJEMPLOI")
                || bankRecordDataDto.getDescription().contains("VIR PLACEMENT POUR NOHAN")
                || bankRecordDataDto.getDescription().contains("PRLV SEPA INELAND - NOUNOU-TOP"))
        {
            bankRecordDataDto.setOwner(Constants.BankRecordDataOwner.NOHAN);
        }
        else if (bankRecordDataDto.getDescription().contains("POUBELLE")
                || bankRecordDataDto.getDescription().contains("ECH PRET CAP+IN")
                || bankRecordDataDto.getDescription().contains("VIR IMPOT FONCIER PAS TOUCHER")
                || bankRecordDataDto.getDescription().contains("VIR IMPOT NE PAS TOUCHER")
                || bankRecordDataDto.getDescription().contains("ECH PRET CAP+IN")
                || bankRecordDataDto.getDescription().contains("PRLV SEPA AXA FRANCE VIE")
                || bankRecordDataDto.getDescription().contains("PRLV SEPA EDF CLIENTS")
                || bankRecordDataDto.getDescription().contains("PRLV SEPA FREE TELECOM")
                || bankRecordDataDto.getDescription().contains("PRLV SEPA DIRECTION GENERALE")
                || bankRecordDataDto.getDescription().contains("VIR EPARGNE VACANCE/MARIAGE"))
        {
            bankRecordDataDto.setOwner(Constants.BankRecordDataOwner.COUPLE);
        }
    }

    private static void extractPaymentType(BankRecordDataDto bankRecordDataDto)
    {
        if (bankRecordDataDto.getDescription().contains("PAIEMENT CB"))
        {
            bankRecordDataDto.setPaymentType(Constants.BankRecordDataPaymentType.CB);
        }
        else if (bankRecordDataDto.getDescription().contains("PAIEMENT PSC"))
        {
            bankRecordDataDto.setPaymentType(Constants.BankRecordDataPaymentType.CB_THROW_SP);
        }
        else if (bankRecordDataDto.getDescription().contains("PRLV SEPA")
                || bankRecordDataDto.getDescription().contains("ECH PRET CAP+IN")
                || bankRecordDataDto.getDescription().contains("ASSURANCE ANIMAUX DE COMPAGNIE")
                || bankRecordDataDto.getDescription().contains("PLAN ASSUR VIE"))
        {
            bankRecordDataDto.setPaymentType(Constants.BankRecordDataPaymentType.PREV);
        }
        else if (bankRecordDataDto.getDescription().contains("VIR")
                || bankRecordDataDto.getDescription().contains("SALAIRE AUTO ECOLE MEYER"))
        {
            bankRecordDataDto.setPaymentType(Constants.BankRecordDataPaymentType.VIRMENT);
        }
        else
        {
            bankRecordDataDto.setPaymentType(Constants.BankRecordDataPaymentType.OTHER);
        }
    }

    private static void extractType(BankRecordDataDto bankRecordDataDto)
    {
        if (bankRecordDataDto.getDescription().contains("PRLV SEPA FREE TELECOM")
                || bankRecordDataDto.getDescription().contains("PRLV SEPA FREE MOBILE"))
        {
            bankRecordDataDto.setType(Constants.BankRecordDataType.INTERNET_MOBILE);
        }
        else if (bankRecordDataDto.getDescription().contains("EPARGNE RETRAITE 0010426457")
                || bankRecordDataDto.getDescription().contains("PLAN ASSUR VIE OY000014539275")
                || bankRecordDataDto.getDescription().contains("VIR EPARGNE VACANCE/MARIAGE")
                || bankRecordDataDto.getDescription().contains("VIR PLACEMENT POUR NOHAN"))
        {
            bankRecordDataDto.setType(Constants.BankRecordDataType.SAVES);
        }
        else if (bankRecordDataDto.getDescription().contains("PRLV SEPA CENTRE PAJEMPLOI")
                || bankRecordDataDto.getDescription().contains("VIR SEPA POUBELLE")
                || bankRecordDataDto.getDescription().contains("ECH PRET CAP+IN"))
        {
            bankRecordDataDto.setType(Constants.BankRecordDataType.HOUSEHOLD);
        }
        else if (bankRecordDataDto.getDescription().contains("CARTE 9019"))
        {
            bankRecordDataDto.setType(Constants.BankRecordDataType.HOBBY);
        }
        else if (bankRecordDataDto.getDescription().contains("VIR IMPOT FONCIER PAS TOUCHER")
                || bankRecordDataDto.getDescription().contains("VIR IMPOT NE PAS TOUCHER"))
        {
            bankRecordDataDto.setType(Constants.BankRecordDataType.TAXES);
        }
        else if (bankRecordDataDto.getDescription().contains("PRLV SEPA AXA FRANCE VIE SA"))
        {
            bankRecordDataDto.setType(Constants.BankRecordDataType.HEALTH);
        }
        else if (bankRecordDataDto.getDescription().contains("PRLV SEPA COMPAGNIE GENERALE DE CGL"))
        {
            bankRecordDataDto.setType(Constants.BankRecordDataType.CAR);
        }
    }

    private static void extractScope(BankRecordDataDto bankRecordDataDto)
    {
        if (bankRecordDataDto.getDescription().contains("VIR IMPOT FONCIER PAS TOUCHER")
                || bankRecordDataDto.getDescription().contains("VIR IMPOT NE PAS TOUCHER"))
        {
            bankRecordDataDto.setScope(Constants.BankRecordDataScope.INTERNAL);
        }
        else if (bankRecordDataDto.getDescription().contains("PRLV SEPA FREE TELECOM")
                || bankRecordDataDto.getDescription().contains("PRLV SEPA FREE MOBILE"))
        {
            bankRecordDataDto.setScope(Constants.BankRecordDataScope.EXTERNAL);
        }
    }

    private static void decryptDatas(Collection<BankRecordDataDto> bankRecordDataDtos, boolean withLog) throws IOException
    {
        if (withLog)
        {
            System.out.println("START - OTHER PAYMENT TYPE");
            for (BankRecordDataDto bankRecordDataDto : bankRecordDataDtos)
            {
                if (Constants.BankRecordDataPaymentType.OTHER.equals(bankRecordDataDto.getPaymentType()))
                {
                    System.out.println("    " + bankRecordDataDto.getDataFromRecordDataDto());
                }
            }
            System.out.println("END - OTHER PAYMENT TYPE");
            System.out.println(" ");
            System.out.println("START - OWNER ");
            for (BankRecordDataDto bankRecordDataDto : bankRecordDataDtos)
            {
                if (bankRecordDataDto.getOwner() == null)
                {
                    System.out.println("    " + bankRecordDataDto.getDataFromRecordDataDto());
                }
            }
            System.out.println("END - OWNER ");
            System.out.println(" ");
            System.out.println("START - TYPE ");
            for (BankRecordDataDto bankRecordDataDto : bankRecordDataDtos)
            {
                if (bankRecordDataDto.getType() == null)
                {
                    System.out.println("    " + bankRecordDataDto.getDataFromRecordDataDto());
                }
            }
            System.out.println("END - TYPE ");
            System.out.println(" ");
            System.out.println("START - SCOPE ");
            for (BankRecordDataDto bankRecordDataDto : bankRecordDataDtos)
            {
                if (bankRecordDataDto.getScope() == null)
                {
                    System.out.println("    " + bankRecordDataDto.getDataFromRecordDataDto());
                }
            }
            System.out.println("END - SCOPE ");
        }
    }
}
