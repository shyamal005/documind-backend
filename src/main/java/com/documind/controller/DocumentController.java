package com.documind.controller;

import com.documind.model.StructuredDocument;
import com.documind.service.DocumentService;
import com.documind.service.LLMService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/docs")
@CrossOrigin(origins = "*")
public class DocumentController {

    @Autowired
    private DocumentService documentService;

    @Autowired
    private LLMService llmService;

     @GetMapping("/test")
    public String test() {
    return "WORKING";
    }

    // 📄 Upload & parse
    @PostMapping("/upload")
    public StructuredDocument upload(@RequestParam("file") MultipartFile file) throws Exception {
        return documentService.process(file);
    }

    // 📄 Upload & parse
    @PostMapping(value = "/upload", consumes = "multipart/form-data")
public StructuredDocument upload(@RequestParam("file") MultipartFile file) throws Exception {
    System.out.println("🔥 API HIT");
    return documentService.process(file);
}

    // 💬 Ask AI
    @PostMapping("/ask")
    public String ask(@RequestBody String query) {
        return llmService.ask(query, documentService.getLastDocument());
    }
}
