package com.documind.service;

import com.documind.model.StructuredDocument;
import com.documind.parser.PdfParser;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class DocumentService {

    @Autowired
    private PdfParser parser;

    @Autowired
    private LLMService llmService;

    private final ObjectMapper mapper = new ObjectMapper();

    private String lastDocument = "";

    // 🔥 MAIN METHOD
    public StructuredDocument process(MultipartFile file) throws Exception {

        System.out.println("📄 File received: " + file.getOriginalFilename());

        String rawText = parser.extract(file);
        lastDocument = rawText;

        String llmOutput = llmService.processFullDocument(rawText);

        System.out.println("RAW:\n" + llmOutput);

        // 🔥 Extract JSON
        int start = llmOutput.indexOf("{");
        int end = llmOutput.lastIndexOf("}");

        if (start != -1 && end != -1) {
            llmOutput = llmOutput.substring(start, end + 1);
        }

        // 🔥 Fix invalid JSON
        llmOutput = fixInvalidJson(llmOutput);

        System.out.println("FIXED JSON:\n" + llmOutput);

        try {
            return mapper.readValue(llmOutput, StructuredDocument.class);
        } catch (Exception e) {
            e.printStackTrace();

            StructuredDocument fallback = new StructuredDocument();
            StructuredDocument.Section section = new StructuredDocument.Section();

            section.setTitle("Raw Output (Parsing Failed)");
            section.setContent(llmOutput);

            fallback.setSections(java.util.List.of(section));
            return fallback;
        }
    }

    // 🔥 FIX INVALID JSON (INSIDE CLASS)
    private String fixInvalidJson(String json) {

        // remove invalid blocks like { dependent: false }
        json = json.replaceAll("\\{\\s*dependent: false\\s*}", "");

        // remove trailing commas
        json = json.replaceAll(",\\s*}", "}");
        json = json.replaceAll(",\\s*]", "]");

        return json;
    }

    // 🔥 FOR CHAT
    public String getLastDocument() {
        return lastDocument;
    }
}