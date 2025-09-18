package qdt.hcmute.vn.dqtbook_backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDate;

@Getter
@Setter
public class UserResponseDTO {
    private Integer id;
    
    @JsonProperty("full_name")
    private String fullName;
    
    private String email;
    
    @JsonProperty("first_name")
    private String firstName;
    
    @JsonProperty("last_name")
    private String lastName;
    
    private String gender;
    
    @JsonProperty("date_of_birth")
    private LocalDate dateOfBirth;
    
    @JsonProperty("avatar_url")
    private String avatarUrl;
    
    @JsonProperty("cover_photo_url")
    private String coverPhotoUrl;
    
    @JsonProperty("bio")
    private String bio;
    
    @JsonProperty("school_id")
    private String schoolId;
    
    @JsonProperty("department_id")
    private Integer departmentId;
    
    @JsonProperty("academic_year")
    private String academicYear;
    
    private String role;
    
    private String phone;
    
    private String website;
    
    private String country;
    
    private String city;
    
    private String education;
    
    private String workplace;
    
    @JsonProperty("facebook_url")
    private String facebookUrl;
    
    @JsonProperty("instagram_url")
    private String instagramUrl;
    
    @JsonProperty("linkedin_url")
    private String linkedinUrl;
    
    @JsonProperty("twitter_url")
    private String twitterUrl;
    
    @JsonProperty("last_seen_at")
    private Instant lastSeenAt;
    
    @JsonProperty("created_at")
    private Instant createdAt;
    
    @JsonProperty("updated_at")
    private Instant updatedAt;
}
