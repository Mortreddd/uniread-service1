package com.bsit.uniread.domain.entities.collaborator;

public enum CollaboratorPermission {
    ADMINISTRATOR,
    // book section
    PUBLISH_BOOK,
    EDIT_BOOK,
    DELETE_BOOK,
    // book section
    ADD_CHAPTER,
    EDIT_CHAPTER,
    PUBLISH_CHAPTER,
    // collaborator sections
    ADD_COLLABORATOR,
    MODIFY_PERMISSIONS,

}
