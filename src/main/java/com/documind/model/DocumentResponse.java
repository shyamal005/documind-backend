package com.documind.model;

import java.util.List;

public class DocumentResponse {

    private String rawText;
    private List<String> structuredOutput;

    public String getRawText() {
        return rawText;
    }

    public void setRawText(String rawText) {
        this.rawText = rawText;
    }

    public List<String> getStructuredOutput() {
        return structuredOutput;
    }

    public void setStructuredOutput(List<String> structuredOutput) {
        this.structuredOutput = structuredOutput;
    }
}