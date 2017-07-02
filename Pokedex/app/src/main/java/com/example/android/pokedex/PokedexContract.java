package com.example.android.pokedex;

import android.provider.BaseColumns;

/**
 * Created by rakesh on 6/29/2017.
 */

public class PokedexContract{

    public class PokedexEntry implements BaseColumns{

        public static final String TABLENAME = "POKEMON";
        public static final String NAME_COLUMN = "NAME";
        public static final String IMAGE_COLUMN = "IMAGE";
    }
}
