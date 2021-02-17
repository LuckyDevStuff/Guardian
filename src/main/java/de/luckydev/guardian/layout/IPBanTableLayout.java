package de.luckydev.guardian.layout;

import de.luckydev.luckyms.column.*;

public class IPBanTableLayout {
    @PrimaryKey
    @NotNull
    @ColumnAttributes(type = ColumnType.VARCHAR, length = 64)
    Column ip;

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

    @NotNull
    @ColumnAttributes(type = ColumnType.VARCHAR, length = 64)
    Column uuid;
}
