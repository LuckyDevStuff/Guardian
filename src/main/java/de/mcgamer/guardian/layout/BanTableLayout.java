package de.mcgamer.guardian.layout;

import de.luckydev.luckyms.column.*;

public class BanTableLayout {
    @PrimaryKey
    @NotNull
    @ColumnAttributes(type= ColumnType.VARCHAR, length = 64)
    Column uuid;

    @NotNull
    @ColumnAttributes(type = ColumnType.VARCHAR, length = 16)
    Column banid;

    @NotNull
    @ColumnAttributes(type = ColumnType.BIGINT)
    Column bannedat;

    @NotNull
    @ColumnAttributes(type = ColumnType.BIGINT)
    Column bantime;

    @NotNull
    @ColumnAttributes(type = ColumnType.VARCHAR)
    Column reason;

    @NotNull
    @ColumnAttributes(type = ColumnType.VARCHAR)
    Column bannername;

    @NotNull
    @ColumnAttributes(type = ColumnType.BOOLEAN)
    Column permanently;
}
