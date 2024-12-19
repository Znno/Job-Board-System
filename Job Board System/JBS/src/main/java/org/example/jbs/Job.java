package org.example.jbs;

public class Job {
    private int id;
    private int employerId;
    private String title;
    private String description;
    private String requirements;

    public Job(int id, int employerId, String title, String description, String requirements) {
        this.id = id;
        this.employerId = employerId;
        this.title = title;
        this.description = description;
        this.requirements = requirements;
    }

    public int getId() {
        return id;
    }

    public int getEmployerId() {
        return employerId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getRequirements() {
        return requirements;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setRequirements(String requirements) {
        this.requirements = requirements;
    }
}