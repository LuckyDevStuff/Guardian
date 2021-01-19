package de.mcgamer.guardian.layout;

import de.luckydev.luckyms.column.*;

public class UUIDBanTableLayout {
    @PrimaryKey
    @NotNull
    @ColumnAttributes(type = ColumnType.VARCHAR, length = 64)
    Column uuid;

    @NotNull
    @ColumnAttributes(type = ColumnType.BIGINT)
    Column banId;

    @NotNull
    @ColumnAttributes(type = ColumnType.BIGINT)
    Column bannedAt;

    @NotNull
    @ColumnAttributes(type = ColumnType.BIGINT)
    Column banTime;

    @NotNull
    @ColumnAttributes(type = ColumnType.VARCHAR)
    Column reason;

    @NotNull
    @ColumnAttributes(type = ColumnType.VARCHAR)
    Column bannedBy;
}
