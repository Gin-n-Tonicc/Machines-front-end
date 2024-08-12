package com.machines.machines_front_end.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Advertisement extends BaseEntity {
    private String title;          // Title or name of the advertisement
    private Set<File> pictures;
    private String targetUrl;      // URL to the site when the ad is clicked
    private String position;       // Position of the ad on the site (e.g., "Top", "Center", "Bottom", "Gallery")
    private int width;             // Width of the ad in pixels
    private int height;            // Height of the ad in pixels
    private LocalDate startDate;   // Start date for the ad campaign
    private LocalDate endDate;     // End date for the ad campaign
    private boolean active;        // Whether the ad is active or not
}