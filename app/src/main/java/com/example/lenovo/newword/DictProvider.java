package com.example.lenovo.newword;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

/**
 * Created by lenovo on 2017/2/18.
 */
public class DictProvider extends ContentProvider {
    private MyDatabaseHelper dbOpenHelper;
    private final static int WORDS = 1;
    private final static int WORD = 2;
    private static  final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        matcher.addURI(Words.AUTHORITY,"words",WORDS);
        matcher.addURI(Words.AUTHORITY,"words/#",WORD);
    }
    @Override
    public boolean onCreate() {
        dbOpenHelper = new MyDatabaseHelper(this.getContext(),"MyDict.3",1);
        return true;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        switch (matcher.match(uri)){
            case WORDS:
                return "vnd.android.cursor.dir/com.example.dict";
            case WORD:
                return "vnd.android.cursor.item/com.example.dict";
            default:
                throw new IllegalArgumentException("未知Uri" + uri);
        }
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
        switch (matcher.match(uri)){
            case WORDS:
                return db.query("dict",projection,selection,selectionArgs,null, null,sortOrder);
            case WORD:
                long id = ContentUris.parseId(uri);
                String whereClause = Words.Word._ID + "=" + id;
                if(selection != null && !"".equals(selection)){
                    whereClause = whereClause + "and" + selection;
                }
                return db.query("dict",projection,whereClause,selectionArgs,null, null,sortOrder);

            default:
                throw new IllegalArgumentException("未知Uri" + uri);

        }

    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
        switch (matcher.match(uri)){
            case WORDS:
                long rowId = db.insert("dict",Words.Word._ID,values);
                if(rowId > 0){
                    Uri wordUri = ContentUris.withAppendedId(uri,rowId);
                    getContext().getContentResolver().notifyChange(wordUri,null);
                    return wordUri;
                }
                break;

            default:
                throw new IllegalArgumentException("未知Uri" + uri);

        }

        return null;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
        int num  = 0;
        switch (matcher.match(uri)){
            case WORDS:
                num = db.update("dict",values,selection,selectionArgs);
                break;
            case WORD:
                long id = ContentUris.parseId(uri);
                String whereClause = Words.Word._ID + "=" + id;
                if(selection != null && !selection.equals("")){
                    whereClause = whereClause + "=" + id;
                }
                num = db.update("dict",values,whereClause,selectionArgs);
                break;

            default:
                throw new IllegalArgumentException("未知Uri" + uri);
        }
        getContext().getContentResolver().notifyChange(uri,null);
        return num;


    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
        int num = 0;
        switch (matcher.match(uri)){
            case WORDS:
                num = db.delete("dict",selection,selectionArgs);
                break;
            case WORD:
                long id = ContentUris.parseId(uri);
                String whereClause = Words.Word._ID + "=" + id;
                if(selection != null && !selection.equals("")){
                    whereClause = whereClause + "=" + id;
                }
                num = db.delete("dict",whereClause,selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("未知Uri" + uri);
        }
        getContext().getContentResolver().notifyChange(uri,null);
        return num;

    }
}
