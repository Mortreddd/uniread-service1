package com.bsit.uniread.application.constants;

/**
 * An object holds all api endpoints for controllers
 */
public class ApiEndpoints {
    // Authentication prefix
    public static final String AUTH = "/api/v1/auth";
    // Current User Profile
    public static final String PROFILE = "/api/v1/profile";
    // Authors prefix
    public static final String AUTHORS = "/api/v1/authors";
    // Books prefix
    public static final String BOOKS = "/api/v1/books";
    // Users prefix
    public static final String USERS = "/api/v1/users";
    // User's Conversation prefix
    public static final String CONVERSATIONS = "/api/v1/conversations";
    // Genres prefix
    public static final String GENRES = "/api/v1/genres";
    // Book Chapters prefix
    public static final String BOOK_CHAPTERS = "/api/v1/books/{bookId}/chapters";
    // Book Chapter Paragraphs prefix
    public static final String BOOK_CHAPTER_PARAGRAPHS = "/api/v1/books/{bookId}/chapters/{chapterId}/paragraphs";
    // Book Comments prefix
    public static final String BOOK_COMMENTS = "/api/v1/books/{bookId}/comments";
    // Book Comments Reaction prefix
    public static final String BOOK_COMMENTS_REACTIONS = "/api/v1/books/{bookId}/comments/{commentId}/reactions";
    // Book collaborators
    public static final String BOOK_COLLABORATORS = "/api/v1/books/{bookId}/collaborators";
    // User notifications prefix
    public static final String NOTIFICATIONS = "/api/v1/notifications";

}
