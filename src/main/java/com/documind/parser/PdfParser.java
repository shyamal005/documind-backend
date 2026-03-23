package com.documind.parser;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class PdfParser {

    public String extract(MultipartFile file) throws Exception {
        PDDocument doc = PDDocument.load(file.getInputStream());
        PDFTextStripper stripper = new PDFTextStripper();
        String text = stripper.getText(doc);
        doc.close();
        return text;
    }
}