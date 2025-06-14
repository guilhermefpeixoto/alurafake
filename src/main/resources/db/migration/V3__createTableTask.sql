CREATE TABLE Task(
    id bigint(20) NOT NULL AUTO_INCREMENT,
    createdAt datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
    course_id bigint(20) NOT NULL,
    statement varchar(255) NOT NULL,
    `order` integer unsigned NOT NULL,
    type enum('OPEN_TEXT', 'SINGLE_CHOICE', 'MULTIPLE_CHOICE') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci,
    PRIMARY KEY (id),
    CONSTRAINT FK_Course FOREIGN KEY (course_id) REFERENCES Course(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC;