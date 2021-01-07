package de.luckydev.luckyms.column;

import java.lang.annotation.Annotation;

public enum ColumnOption {
    PRIMARY_KEY(PrimaryKey.class, "PRIMARY KEY"),
    NULL(Null.class, "NULL"),
    NOT_NULL(NotNull.class, "NOT NULL"),
    COMMENT(Comment.class, "COMMENT");
    public Class<? extends Annotation> annotation;
    public String sqlName;
    ColumnOption(Class<? extends Annotation> annotation, String sqlName) {
        this.annotation = annotation;
        this.sqlName = sqlName;
    }
}
