
package com.reindeermobile.hunapknotifier.providers;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public class HunApkInfoItem {
    public static final class HunApkInfoItems implements BaseColumns {
        public static final String HUNAPKINFO_TABLE_NAME = "hunapkinfo";

        public static final Uri CONTENT_URI = Uri.parse("content://" + HunApkProvider.PROVIDER_NAME
                + "/hunapkinfos");
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
                + "/com.reindeermobile.hunapknotifier.hunapkinfo";
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
                + "/com.reindeermobile.hunapknotifier.hunapkinfo";

        public static final int HUNAPKINFOS = 1;
        public static final int HUNAPKINFOS_ID = 2;

        public static final String ID = "_id";
        public static final String NAME = "name";
        public static final String LINK = "link";
        public static final String AUTHOR = "author";

        private HunApkInfoItems() {
        };
    }

}
