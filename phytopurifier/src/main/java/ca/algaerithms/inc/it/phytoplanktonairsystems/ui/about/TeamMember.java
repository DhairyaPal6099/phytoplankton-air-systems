package ca.algaerithms.inc.it.phytoplanktonairsystems.ui.about;

public class TeamMember {
    public String name;
    public String role;
    public String bio;
    public int imageRes;
    public String linkedInUrl;
    public String githubUrl;

    public TeamMember(String name, String role, String bio, int imageRes, String linkedInUrl, String githubUrl) {
        this.name = name;
        this.role = role;
        this.bio = bio;
        this.imageRes = imageRes;
        this.linkedInUrl = linkedInUrl;
        this.githubUrl = githubUrl;
    }
}
