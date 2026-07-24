package com.uniread.notification.domain.entities;

import lombok.Getter;

@Getter
public enum NotificationType {

    EMAIL_CONFIRMATION("Email Confirmation", "Please confirm your account by verifying your email", "auth/email_confirmation"),
    EMAIL_UPDATE_CONFIRMATION("Update Email Confirmation", "Please confirm your update by verifying your email", "auth/update_email_confirmation"),
    WELCOME("Welcome to Uniread", "Thanks for joining Uniread", "welcome"),
    // Follow related
    NEW_FOLLOWER("New Follower", "Someone started following you", ""),
    FOLLOW_REQUEST_ACCEPTED("Follow Request Accepted", "Your follow request was accepted", ""),

    // Book related
    NEW_BOOK_RELEASED("New Book Released", "A new book you might like has been released", ""),
    BOOK_UPDATE("Book Update", "A book you follow has been updated", ""),
    BOOK_RECOMMENDATION("Book Recommendation", "We found a book you might like", ""),

    // Interaction related
    NEW_COMMENT("New Comment", "Someone commented on your book", ""),
    NEW_LIKE("New Like", "Someone liked your book", ""),
    NEW_REVIEW("New Review", "Someone reviewed your book", ""),

    // Achievement related
    ACHIEVEMENT_UNLOCKED("Achievement Unlocked", "You've unlocked a new achievement", ""),
    MILESTONE_REACHED("Milestone Reached", "You've reached a reading milestone", ""),

    // System related
    SYSTEM_ANNOUNCEMENT("System Announcement", "Important system update", ""),
    MAINTENANCE_ALERT("Maintenance Alert", "Scheduled maintenance", ""),

    // Reminder related
    READING_REMINDER("Reading Reminder", "Don't forget to continue reading", ""),
    EVENT_REMINDER("Event Reminder", "Upcoming event reminder", "");

    private final String defaultTitle;
    private final String defaultDescription;
    private final String defaultTemplate;

    NotificationType(String defaultTitle, String defaultDescription, String defaultTemplate) {
        this.defaultTitle = defaultTitle;
        this.defaultDescription = defaultDescription;
        this.defaultTemplate = defaultTemplate;
    }

}