package com.example.resolver;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by lenovo on 2017/2/19.
 */
public class Words {
    public static final String AUTHORITY = "com.example.providers.dictprovider";
    public static final class Word implements BaseColumns {
        public static final  String _ID = "id";
        public static final  String WORD = "word";
        public static final  String DETAIL = "detail";

        public static final Uri DICT_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/words");
        public static final Uri  WORD_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/word");
    }
}
