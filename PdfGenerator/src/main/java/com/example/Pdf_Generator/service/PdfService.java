package com.example.Pdf_Generator.service;

import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;

import java.io.IOException;
import java.sql.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class PdfService extends BaseService {

    private final String id;

    private final static String DB_CONNECTION = "jdbc:postgresql://localhost:30001/customerdb?user=postgres&password=postgres";

    public PdfService(String inDestination, String outDestination, String brokerUrl) {
        super(inDestination, outDestination, brokerUrl);

        this.id = UUID.randomUUID().toString();

        System.out.println("PDF Service created with id " + id);
    }


    @Override
    protected String executeInternal(String input) {
        String jobId = UUID.randomUUID().toString();


        System.out.println("input:");
        System.out.println(input);

        if(input.indexOf(",")<1){
            return "ok";
        }

        String sumPart = input.split(",")[0];
        String customerId = input.split(",")[1];

        String textPart = "Summe:  " + sumPart;
        String customer = "";


        try(Connection conn = connect()){
            String sql = "SELECT first_name, last_name FROM customer WHERE id = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setInt(1,Integer.parseInt(customerId));

            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                customer = rs.getString("first_name") + " " + rs.getString("last_name");
                System.out.println("Customer:");
                System.out.println(customer);
            }

        }catch (SQLException e){
            return "error";
        }

        String filename = jobId + ".pdf";
        System.out.println("Created bill into " + filename);
        createBill(textPart, filename, sumPart, customer);

        return "ok";
    }

    public void createBill(String text, String filename, String sum, String customer) {
        try {
            PdfWriter writer = new PdfWriter(filename);
            PdfDocument pdf = new PdfDocument(writer);
            Document doc = new Document(pdf);

            doc.add(new Paragraph("Invoice").setFontSize(18).setBold());
            doc.add(new Paragraph("Customer: " + customer).setFontSize(14).setBold());
            doc.add( new Paragraph(text).setFontSize(14).setBold() );
            doc.add( new Paragraph(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)) );

            doc.add( new Paragraph("Total: " + sum + " kWh").setFontColor(ColorConstants.RED) );

            doc.close();

        } catch (IOException e) {
            System.err.println("Failed to create bill " + e.getMessage());
        }
    }

    private Connection connect() throws SQLException {
        return DriverManager.getConnection(DB_CONNECTION);
    }
}
