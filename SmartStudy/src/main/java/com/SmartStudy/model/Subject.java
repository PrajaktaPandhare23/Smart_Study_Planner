package com.SmartStudy.model;

import java.io.Serializable;

public class Subject implements Serializable {
    private static final long serialVersionUID = 1L;

    private final int subjectId;
    private String subjectName;

    public Subject(int subjectId, String subjectName) {
        this.subjectId = subjectId;
        this.subjectName = subjectName;
    }

    public int getSubjectId() {
        return subjectId;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    @Override
    public String toString() {
        return "ID: " + subjectId + " | Subject: " + subjectName;
    }
}
