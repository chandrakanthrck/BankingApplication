package com.banking.springboot_bank.service.impl;

import com.banking.springboot_bank.dto.EmailDetails;
import com.banking.springboot_bank.entity.Transaction;
import com.banking.springboot_bank.entity.User;
import com.banking.springboot_bank.repository.TransactionRepository;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.properties.TextAlignment;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Cell;

@Component
@AllArgsConstructor
@Slf4j
public class BankStatement {
    //Fetch transaction details from transaction repository
    private TransactionRepository transactionRepository;
    private EmailService emailService;
    //create file to save the bank statement
    /**
     * retrieve list of transactions within a date range given an account number
     * generate a pdf file for transactions
     * send the file via email
     */
    //convert date format to string we are using startDate, endDate
    public List<Transaction> generateStatement(String accountNumber, String startDate, String endDate){
        //getting transactions by filtering the account number
        //filter by created at date
        LocalDate start = LocalDate.parse(startDate, DateTimeFormatter.ISO_DATE);
        LocalDate end = LocalDate.parse(endDate, DateTimeFormatter.ISO_DATE);
        return transactionRepository.findAll()
                .stream()
                .filter(transaction -> transaction.getAccountNumber().equals(accountNumber))
                .filter(transaction -> transaction.getCreatedAt() != null)  // Null check
                .filter(transaction -> !transaction.getCreatedAt().isBefore(start) && !transaction.getCreatedAt().isAfter(end))
                .toList();
    }
    //generating bank statement
    private static final String FILE = "/Users/chandrakanthrajesh/Desktop/myDocument.pdf";

    public void designStatement(List<Transaction> transactionList, String startDate, String endDate, String customerName, User user) throws FileNotFoundException {
        // Set up PDF document with A4 page size
        Rectangle statementSize = new Rectangle(PageSize.A4);
        PdfWriter writer = new PdfWriter(FILE);
        PdfDocument pdfDocument = new PdfDocument(writer);
        Document document = new Document(pdfDocument, new PageSize(statementSize));

        log.info("Setting size of document");

        // Add bank information
        Table bankInfoTable = new Table(1);
        Cell bankName = new Cell().add(new Paragraph("SpringBoot Bank"));
        bankName.setBorder(null);
        bankName.setBackgroundColor(new DeviceRgb(0, 102, 204));
        bankName.setPadding(10f);

        Cell bankAddress = new Cell().add(new Paragraph("One Bellevue Mall, Bellevue, Washington, USA - 98004"));
        bankAddress.setBorder(null);

        bankInfoTable.addCell(bankName);
        bankInfoTable.addCell(bankAddress);
        document.add(bankInfoTable);

        document.add(new Paragraph(" "));

        // Add statement information
        Table statementInfoTable = new Table(2);
        Cell customerInfo = new Cell().add(new Paragraph("Customer Name: " + customerName));
        customerInfo.setBorder(null);

        Cell statementTitle = new Cell().add(new Paragraph("STATEMENT OF ACCOUNT"));
        statementTitle.setBorder(null);
        statementTitle.setTextAlignment(TextAlignment.RIGHT);

        Cell startDateCell = new Cell().add(new Paragraph("Start Date: " + startDate));
        startDateCell.setBorder(null);

        Cell endDateCell = new Cell().add(new Paragraph("End Date: " + endDate));
        endDateCell.setBorder(null);
        statementTitle.setTextAlignment(TextAlignment.RIGHT);

        statementInfoTable.addCell(customerInfo);
        statementInfoTable.addCell(statementTitle);
        statementInfoTable.addCell(startDateCell);
        statementInfoTable.addCell(endDateCell);

        document.add(statementInfoTable);

        document.add(new Paragraph(" "));

        // Add transaction table
        Table transactionTable = new Table(5);
        transactionTable.addCell("Date");
        transactionTable.addCell("Transaction ID");
        transactionTable.addCell("Type");
        transactionTable.addCell("Amount");
        transactionTable.addCell("Status");

        for (Transaction transaction : transactionList) {
            transactionTable.addCell(transaction.getCreatedAt().toString());
            transactionTable.addCell(transaction.getTransactionId());
            transactionTable.addCell(transaction.getTransactionType());
            transactionTable.addCell(transaction.getAmount().toString());
            transactionTable.addCell(transaction.getStatus());
        }

        document.add(transactionTable);

        document.close();
        EmailDetails emailDetails = EmailDetails.builder()
                        .recipient(user.getEmail())
                                .subject("STATMENT OF ACCOUNT")
                                        .messageBody("Kindly find your requested account statement attached!")
                                                .attachment(FILE)
                                                        .build();
        emailService.sendEmailWithAttachment(emailDetails);
        log.info("PDF statement generated successfully");
    }
}
