package com.getfeetwithus.service;

import com.getfeetwithus.entity.Order;
import com.getfeetwithus.entity.OrderItem;
import com.getfeetwithus.entity.OrganizationDetails;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;

@Service
public class BillPdfService {

    public byte[] generateBillPdf(Order order, OrganizationDetails org) throws DocumentException {
        Document document = new Document(PageSize.A4, 36, 36, 40, 40);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, out);

        document.open();

        // Fonts
        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, new Color(40, 167, 69));
        Font subtitleFont = FontFactory.getFont(FontFactory.HELVETICA, 10, Color.DARK_GRAY);
        Font boldFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11, Color.BLACK);
        Font regularFont = FontFactory.getFont(FontFactory.HELVETICA, 10, Color.BLACK);
        Font headerCellFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, Color.WHITE);

        // Header Table
        PdfPTable headerTable = new PdfPTable(2);
        headerTable.setWidthPercentage(100);
        headerTable.setWidths(new float[]{60, 40});

        PdfPCell orgCell = new PdfPCell();
        orgCell.setBorder(Rectangle.NO_BORDER);
        orgCell.addElement(new Paragraph(org.getOrgName() != null ? org.getOrgName() : "GET FEET WITH US", titleFont));
        orgCell.addElement(new Paragraph(org.getAddress() != null ? org.getAddress() : "Health & Fitness Platform", subtitleFont));
        orgCell.addElement(new Paragraph("Phone: " + (org.getMobileNumber() != null ? org.getMobileNumber() : "N/A") + " | Email: " + (org.getEmail() != null ? org.getEmail() : "N/A"), subtitleFont));
        if (org.getWebsite() != null && !org.getWebsite().isBlank()) {
            orgCell.addElement(new Paragraph("Website: " + org.getWebsite(), subtitleFont));
        }
        headerTable.addCell(orgCell);

        PdfPCell invoiceCell = new PdfPCell();
        invoiceCell.setBorder(Rectangle.NO_BORDER);
        invoiceCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        Paragraph invTitle = new Paragraph("TAX INVOICE / BILL", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, Color.BLACK));
        invTitle.setAlignment(Element.ALIGN_RIGHT);
        invoiceCell.addElement(invTitle);

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        Paragraph invDetails = new Paragraph("Order #: " + order.getOrderId() + "\nDate: " + (order.getOrderDate() != null ? order.getOrderDate().format(dtf) : "N/A"), subtitleFont);
        invDetails.setAlignment(Element.ALIGN_RIGHT);
        invoiceCell.addElement(invDetails);

        headerTable.addCell(invoiceCell);
        document.add(headerTable);

        document.add(new Paragraph(" "));

        // Customer & Order Info Box
        PdfPTable infoTable = new PdfPTable(2);
        infoTable.setWidthPercentage(100);
        infoTable.setWidths(new float[]{50, 50});

        PdfPCell customerCell = new PdfPCell();
        customerCell.setBackgroundColor(new Color(245, 248, 250));
        customerCell.setPadding(8);
        customerCell.addElement(new Paragraph("Billed To / Customer Details:", boldFont));
        customerCell.addElement(new Paragraph("Name: " + (order.getUserName() != null ? order.getUserName() : "N/A"), regularFont));
        customerCell.addElement(new Paragraph("User ID: " + order.getUserId(), regularFont));
        customerCell.addElement(new Paragraph("Mobile: " + (order.getUserMobile() != null ? order.getUserMobile() : "N/A"), regularFont));
        if (order.getDeliveryAddress() != null) {
            customerCell.addElement(new Paragraph("Address: " + order.getDeliveryAddress().getFormattedAddress(), regularFont));
        }
        infoTable.addCell(customerCell);

        PdfPCell statusCell = new PdfPCell();
        statusCell.setBackgroundColor(new Color(245, 248, 250));
        statusCell.setPadding(8);
        statusCell.addElement(new Paragraph("Order & Payment Details:", boldFont));
        statusCell.addElement(new Paragraph("Order Status: " + order.getOrderStatus(), regularFont));
        statusCell.addElement(new Paragraph("Payment Status: " + order.getPaymentStatus(), regularFont));
        statusCell.addElement(new Paragraph("Payment Mode: Offline / Administrative Record", regularFont));
        statusCell.addElement(new Paragraph("Grand Total: Rs. " + order.getGrandTotal(), boldFont));
        infoTable.addCell(statusCell);

        document.add(infoTable);
        document.add(new Paragraph(" "));

        // Itemized Table
        PdfPTable table = new PdfPTable(5);
        table.setWidthPercentage(100);
        table.setWidths(new float[]{8, 42, 15, 15, 20});

        // Headers
        String[] headers = {"#", "Product Description", "Unit Price", "Qty", "Total (Rs.)"};
        for (String h : headers) {
            PdfPCell cell = new PdfPCell(new Phrase(h, headerCellFont));
            cell.setBackgroundColor(new Color(40, 167, 69));
            cell.setPadding(6);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
        }

        int index = 1;
        if (order.getItems() != null) {
            for (OrderItem item : order.getItems()) {
                PdfPCell c1 = new PdfPCell(new Phrase(String.valueOf(index++), regularFont));
                c1.setHorizontalAlignment(Element.ALIGN_CENTER);
                c1.setPadding(6);
                table.addCell(c1);

                PdfPCell c2 = new PdfPCell(new Phrase(item.getProductName(), regularFont));
                c2.setPadding(6);
                table.addCell(c2);

                PdfPCell c3 = new PdfPCell(new Phrase("Rs. " + item.getUnitPrice(), regularFont));
                c3.setHorizontalAlignment(Element.ALIGN_RIGHT);
                c3.setPadding(6);
                table.addCell(c3);

                PdfPCell c4 = new PdfPCell(new Phrase(String.valueOf(item.getQuantity()), regularFont));
                c4.setHorizontalAlignment(Element.ALIGN_CENTER);
                c4.setPadding(6);
                table.addCell(c4);

                PdfPCell c5 = new PdfPCell(new Phrase("Rs. " + item.getProductTotal(), regularFont));
                c5.setHorizontalAlignment(Element.ALIGN_RIGHT);
                c5.setPadding(6);
                table.addCell(c5);
            }
        }

        // Total Row
        PdfPCell totalLabelCell = new PdfPCell(new Phrase("Grand Total:", boldFont));
        totalLabelCell.setColspan(4);
        totalLabelCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        totalLabelCell.setPadding(6);
        totalLabelCell.setBackgroundColor(new Color(230, 245, 235));
        table.addCell(totalLabelCell);

        PdfPCell totalValCell = new PdfPCell(new Phrase("Rs. " + order.getGrandTotal(), boldFont));
        totalValCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        totalValCell.setPadding(6);
        totalValCell.setBackgroundColor(new Color(230, 245, 235));
        table.addCell(totalValCell);

        document.add(table);

        document.add(new Paragraph(" "));

        // Footer Note
        Paragraph note = new Paragraph("Thank you for choosing Get Feet With Us! For support or inquiries, please contact our support desk.", subtitleFont);
        note.setAlignment(Element.ALIGN_CENTER);
        document.add(note);

        document.close();
        return out.toByteArray();
    }
}
