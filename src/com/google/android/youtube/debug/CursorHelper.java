package com.google.android.youtube.debug;

import java.util.Arrays;

import android.database.Cursor;

public class CursorHelper {
	public static String getCursorColumn(Cursor cursor) {
		cursor.moveToFirst();
		StringBuilder sb = new StringBuilder();

		int columnsCount = cursor.getColumnCount();
		int count = cursor.getCount();
		sb.append("columnsCount = " + columnsCount + " count = " + count + "\n");

		for (int i = 0; i < columnsCount; i++) {
			sb.append(cursor.getColumnName(i) + " (" + getType(cursor.getType(i)) + ")  ");
		}
		sb.append((char) 10);
		return sb.toString();
	}

	public static String getCursorValue(Cursor cursor) {
		cursor.moveToFirst();
		StringBuilder sb = new StringBuilder();
		int columnsCount = cursor.getColumnCount();
		int j = 0;
		do {
			for (int i = 0; i < columnsCount; i++) {
				sb.append(getValue(cursor, i) + " | ");
			}
			sb.append((char) 10);
			j++;
			if (j == 10)
				break;
		} while (cursor.moveToNext());
		return sb.toString();
	}

	public static String getValue(Cursor cursor, int columnIndex) {
		switch (cursor.getType(columnIndex)) {
		case Cursor.FIELD_TYPE_NULL:
			return "null";
		case Cursor.FIELD_TYPE_INTEGER:
			return String.valueOf(cursor.getInt(columnIndex));
		case Cursor.FIELD_TYPE_FLOAT:
			return String.valueOf(cursor.getFloat(columnIndex));
		case Cursor.FIELD_TYPE_STRING:
			return cursor.getString(columnIndex);
		case Cursor.FIELD_TYPE_BLOB:
			return Arrays.toString(cursor.getBlob(columnIndex));
		}
		return "inv:" + cursor.getType(columnIndex);
	}

	public static String getType(int type) {
		switch (type) {
		case Cursor.FIELD_TYPE_NULL:
			return "null";
		case Cursor.FIELD_TYPE_INTEGER:
			return "integer";
		case Cursor.FIELD_TYPE_FLOAT:
			return "float";
		case Cursor.FIELD_TYPE_STRING:
			return "string";
		case Cursor.FIELD_TYPE_BLOB:
			return "blob";
		}
		return "inv:" + type;
	}
}
