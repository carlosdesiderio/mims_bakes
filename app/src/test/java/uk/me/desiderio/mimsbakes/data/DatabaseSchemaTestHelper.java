package uk.me.desiderio.mimsbakes.data;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Code taken from https://gist.github.com/izmajlowiczl/61ce2edc4d881b78ca06b45d91b91b17
 */

public class DatabaseSchemaTestHelper {
    
    public static final String DB_ROW_TYPE_INTEGER = "INTEGER";
    public static final String DB_ROW_TYPE_REAL = "REAL";
    public static final String DB_ROW_TYPE_TEXT = "TEXT";

    public static boolean doesTableExist(SQLiteDatabase db, String tableName) {
        String sql = "SELECT name FROM sqlite_master WHERE type='table';";
        Cursor cursor = db.rawQuery(sql,null);
        while (cursor.moveToNext()) {
            String name = cursor.getString(0);
            if (cursor.getString(0).equals(tableName)) {
                return true;
            }
        }
        return false;

    }

    public static TableDescription getColumnDetailsOrNull(SQLiteDatabase db, String table, String column) {
        Cursor c = queryForTableInfo(db, table);

        while (c.moveToNext()) {
            String name = ColumnProperties.getName(c);
            if (name.equals(column)) {
                return new TableDescription(name,
                        ColumnProperties.getType(c),
                        ColumnProperties.isNotNull(c),
                        ColumnProperties.isPrimaryKey(c));
            }
        }

        return null;
    }

    public static List<String> listColumnNames(SQLiteDatabase db, String tableName) {
        List<String> columns = new ArrayList<>();

        Cursor c = queryForTableInfo(db, tableName);
        while (c.moveToNext()) {
            columns.add(c.getString(c.getColumnIndex("name")));
        }

        return columns;
    }

    public static int getCurrentDatabaseVersion(SQLiteDatabase db) {
        Cursor c = db.rawQuery("pragma user_version;", null);

        if (!c.moveToNext()) {
            throw new RuntimeException(String.format("failed executing: %s", "pragma user_version;"));
        }

        return c.getInt(0);
    }

    /**
        Table_info PRAGMA Statement returns data in format:
        position| name | type | Nullable | Default value | Primary Key.
     */
    private static Cursor queryForTableInfo(SQLiteDatabase db, String tableName) {
        return db.rawQuery("pragma table_info(" + tableName + ");", null);
    }

    public static final class TableDescription {
        final String name;
        final String type;
        final boolean isNotNull;
        final boolean isPk;

        private TableDescription(String name, String type, boolean isNotNull, boolean isPk) {
            this.name = name;
            this.type = type;
            this.isNotNull = isNotNull;
            this.isPk = isPk;
        }
    }

    public static final class ColumnProperties {
        private static final String NAME = "name";
        private static final String TYPE = "type";
        private static final String PK = "pk";
        private static final String NOT_NULL = "notnull";

        public static String getName(Cursor c) {
            return c.getString(c.getColumnIndex(NAME));
        }

        public static String getType(Cursor c) {
            return c.getString(c.getColumnIndex(TYPE));
        }

        public static boolean isPrimaryKey(Cursor c) {
            return c.getInt(c.getColumnIndex(PK)) == 1;
        }

        /**
         * Not Null is kinda stupid.. it works only if there is explicit declaration of NOT NULL.
         * It should work as well for each PK columns (which are not null by default), but it doesn't
         */
        public static boolean isNotNull(Cursor c) {
            return c.getInt(c.getColumnIndex(NOT_NULL)) == 1;
        }
    }
}
