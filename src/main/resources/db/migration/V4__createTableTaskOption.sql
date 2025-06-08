CREATE TABLE Task_Option (
    id bigint(20) NOT NULL AUTO_INCREMENT,
    createdAt datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
    task_id bigint(20) NOT NULL,
    option_text varchar(80) NOT NULL,
    isCorrect tinyint(1) NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT FK_Task FOREIGN KEY (task_id) REFERENCES Task(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC; 