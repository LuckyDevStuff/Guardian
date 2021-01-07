package de.luckydev.luckyms.column;

public enum ColumnType {
    INT(64), VARCHAR(32), BIGINT(128), BOOLEAN(null);
    public Integer defaultLength;
    ColumnType(Integer defaultLength) {
        this.defaultLength = defaultLength;
    }
}
