package HRMS_project.service;

import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;

import com.itextpdf.kernel.pdf.*;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.*;

import HRMS_project.entity.Payslip;
import HRMS_project.repository.PayslipRepository;

@Service
public class PayslipService {

    @Autowired
    private PayslipRepository payslipRepo;

    private final String PDF_DIR = "payslips/";

    public Payslip generatePayslip(String email, String name, String month,
                                   double basicPay, double deductions) throws IOException {
        double netPay = basicPay - deductions;
        String fileName = UUID.randomUUID() + "_payslip_" + month + ".pdf";
        Path path = Paths.get(PDF_DIR + fileName);
        Files.createDirectories(path.getParent());

        // ✅ Generate proper PDF using iText 7
        try (PdfWriter writer = new PdfWriter(path.toString());
             PdfDocument pdf = new PdfDocument(writer);
             Document document = new Document(pdf)) {

            document.add(new Paragraph("Payslip for " + month)
                    .setFontSize(16)
                    .setBold()
                    .setMarginBottom(20));

            document.add(new Paragraph("Employee Name: " + name));
            document.add(new Paragraph("Employee Email: " + email));
            document.add(new Paragraph(""));
            document.add(new Paragraph("Basic Pay: ₹" + basicPay));
            document.add(new Paragraph("Deductions: ₹" + deductions));
            document.add(new Paragraph("Net Pay: ₹" + netPay));
            document.add(new Paragraph("\nGenerated Date: " + LocalDate.now()));
        }

        // Save payslip metadata to DB
        Payslip payslip = new Payslip();
        payslip.setEmployeeEmail(email);
        payslip.setEmployeeName(name);
        payslip.setMonth(month);
        payslip.setBasicPay(basicPay);
        payslip.setDeductions(deductions);
        payslip.setNetPay(netPay);
        payslip.setPdfPath(path.toAbsolutePath().toString().replace("\\", "/"));
        payslip.setGeneratedDate(LocalDate.now());

        return payslipRepo.save(payslip);
    }

    public List<Payslip> getPayslips(String email) {
        return payslipRepo.findByEmployeeEmail(email);
    }

    public Resource downloadPayslip(Long id) throws IOException {
        Payslip payslip = payslipRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Payslip not found"));
        Path path = Paths.get(payslip.getPdfPath());
        return new UrlResource(path.toUri());
    }
}
