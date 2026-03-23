package com.documind.model;

import java.util.List;

public class StructuredDocument {

    private List<Section> sections;

    public List<Section> getSections() {
        return sections;
    }

    public void setSections(List<Section> sections) {
        this.sections = sections;
    }

    public static class Section {

        private String title;

        // 🔥 flexible (handles string, list, object)
        private Object content;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public Object getContent() {
            return content;
        }

        public void setContent(Object content) {
            this.content = content;
        }
    }
}