function selectRow(tableName, rowIndex) {
    var table = PF(tableName);
    var pagRows = 0;
    if (table.paginator) {
        pagRows = table.paginator.getCurrentPage() * table.paginator.getRows();
    }

    table.selectRow(rowIndex - pagRows, false);
}