package com.aba.corp;

import com.aba.corp.dto.BankAccountDataDto;
import com.aba.corp.dto.BankRecordDataDto;
import com.aba.corp.utils.Constants;
import com.aba.corp.utils.Utils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

public class ProcessorAccount
{
    static String PATH_TO_ANALYSE = "C:\\temp";
    static Integer startRow = 3;

    static Integer nameIndex = 0;
    static Integer codeIndex = 1;

    static void main() throws IOException
    {
        FileInputStream fis = new FileInputStream(PATH_TO_ANALYSE + "\\comptes.xlsx");
        doFullRead(fis, true);
    }

    public static Collection<BankAccountDataDto> doFullRead(InputStream is, boolean withLog) throws IOException
    {
        Collection<BankAccountDataDto> accountDataDtos = new ArrayList<>();

        readDatas(is, accountDataDtos);
        processDatas(accountDataDtos, withLog);
        decryptDatas(accountDataDtos);

        return accountDataDtos;
    }

    private static void readDatas(InputStream is, Collection<BankAccountDataDto> accountDataDtos) throws IOException
    {
        Workbook workbook = WorkbookFactory.create(is);
        Integer lastRow = 0;

        if (workbook.getNumberOfSheets() > 0)
        {
            Sheet sheet = workbook.getSheetAt(0);
            Double totalAmount = 0.0;
            lastRow = sheet.getLastRowNum();

            for (Row row : sheet)
            {
                if (row.getRowNum() < startRow || row.getRowNum() > lastRow)
                {
                    continue;
                }

                BankAccountDataDto accountDataDto = new BankAccountDataDto();

                Cell nameCell = row.getCell(nameIndex);
                Cell codeCell = row.getCell(codeIndex);

                if (nameCell != null)
                {
                    String name = nameCell.getStringCellValue();
                    accountDataDto.setName(name);
                }

                if (codeCell != null)
                {
                    String code = codeCell.getStringCellValue();
                    accountDataDto.setCode(code);
                }

                if (StringUtils.isNotEmpty(accountDataDto.getName()))
                {
                    accountDataDtos.add(accountDataDto);
                }
            }
        }

        workbook.close();
    }

    private static void processDatas(Collection<BankAccountDataDto> accountDataDtos, boolean withLog) throws IOException
    {
        for (BankAccountDataDto accountDataDto : accountDataDtos)
        {
            if (accountDataDto.getName().contains("COMPTE EPARGNE")
                || accountDataDto.getName().contains("LIVRET"))
            {
                accountDataDto.setType(Constants.AccountType.SAVING);
            }
            else if (accountDataDto.getName().contains("PRET"))
            {
                accountDataDto.setType(Constants.AccountType.LAON);
            }
            else
            {
                accountDataDto.setType(Constants.AccountType.ORDINARY);
            }

            if (withLog)
            {
                System.out.println(accountDataDto.getName() + " - " + accountDataDto.getCode() + " - "
                        + accountDataDto.getType());
            }
        }
    }

    private static void decryptDatas(Collection<BankAccountDataDto> accountDataDtos) throws IOException
    {
    }
}
