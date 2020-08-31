package de.tommy13.sugar.database;

import android.content.Context;

import java.util.ArrayList;

import de.tommy13.sugar.R;

/**
 * Created by tommy on 09.03.2017.
 * Class for the Standard Categories.
 */

class StandardCategories {

    public static ArrayList<CategoryItem> get(Context context) {
        ArrayList<CategoryItem> items = new ArrayList<>();

        // drinks alkoholfrei
        CategoryItem cola = new CategoryItem(0, context.getResources().getString(R.string.preset_cola),38f,9f,0f,200f);
        items.add(cola);
        CategoryItem fanta = new CategoryItem(0, context.getResources().getString(R.string.preset_fanta),38f,9.5f,0f,200f);
        items.add(fanta);
        CategoryItem apfelsaft = new CategoryItem(0, context.getResources().getString(R.string.preset_apfelsaft),46f,10f,0.1f,200f);
        items.add(apfelsaft);
        CategoryItem milch = new CategoryItem(0, context.getResources().getString(R.string.preset_milch),42f,3.5f,0f,200f);
        items.add(milch);
        CategoryItem tee = new CategoryItem(0, context.getResources().getString(R.string.preset_tee),1f,0f,0f,200f);
        items.add(tee);
        CategoryItem kaffee = new CategoryItem(0, context.getResources().getString(R.string.preset_kaffee),0f,0f,0f,200f);
        items.add(kaffee);
        CategoryItem orangensaft = new CategoryItem(0, context.getResources().getString(R.string.preset_orangensaft),45f,8f,0.2f,200f);
        items.add(orangensaft);

        // drinks alkoholisch
        CategoryItem bier = new CategoryItem(0, context.getResources().getString(R.string.preset_bier),43f,0f,0f,200f);
        items.add(bier);
        CategoryItem wein = new CategoryItem(0, context.getResources().getString(R.string.preset_wein),83f,0.8f,0f,200f);
        items.add(wein);
        CategoryItem schnaps = new CategoryItem(0, context.getResources().getString(R.string.preset_schnaps),250f,0f,0f,20f);
        items.add(schnaps);

        // fisch
        CategoryItem fischstaebchen = new CategoryItem(0, context.getResources().getString(R.string.preset_fischstaebchen),249f,2.5f,13f,100f);
        items.add(fischstaebchen);

        // salat
        CategoryItem kopfsalat = new CategoryItem(0, context.getResources().getString(R.string.preset_kopfsalat),12f,1f,0f,100f);
        items.add(kopfsalat);
        CategoryItem kartoffelsalat = new CategoryItem(0, context.getResources().getString(R.string.preset_kartoffelsalat),143f,0f,8f,100f);
        items.add(kartoffelsalat);

        // beilagen
        CategoryItem pasta = new CategoryItem(0, context.getResources().getString(R.string.preset_pasta),138f,0.4f,2.1f,100f);
        items.add(pasta);
        CategoryItem pommes = new CategoryItem(0, context.getResources().getString(R.string.preset_pommes),312f,15f,0.3f,100f);
        items.add(pommes);
        CategoryItem spaetzle = new CategoryItem(0, context.getResources().getString(R.string.preset_spaetzle),138f,0.4f,2.1f,100f);
        items.add(spaetzle);
        CategoryItem brot = new CategoryItem(0, context.getResources().getString(R.string.preset_brot),265f,5f,3.2f,100f);
        items.add(brot);

        // essen
        CategoryItem linsen = new CategoryItem(0, context.getResources().getString(R.string.preset_linsen),116f,0f,0.4f,100f);
        items.add(linsen);
        CategoryItem spaghettiBolognese = new CategoryItem(0, context.getResources().getString(R.string.preset_spaghetti_bolognese),171f,1.4f,5.4f,100f);
        items.add(spaghettiBolognese);
        CategoryItem schweineschnitzel = new CategoryItem(0, context.getResources().getString(R.string.preset_schweineschnitzel),209f,0.2f,9.3f,100f);
        items.add(schweineschnitzel);
        CategoryItem maultaschen = new CategoryItem(0, context.getResources().getString(R.string.preset_maultaschen),146f,0.5f,5.5f,100f);
        items.add(maultaschen);
        CategoryItem wurstsalat = new CategoryItem(0, context.getResources().getString(R.string.preset_wurstsalat),298f,0.8f,30.4f,100f);
        items.add(wurstsalat);
        CategoryItem wurst = new CategoryItem(0, context.getResources().getString(R.string.preset_wurst),301f,0f,27f,100f);
        items.add(wurst);
        CategoryItem gulaschsuppe = new CategoryItem(0, context.getResources().getString(R.string.preset_gulaschsuppe),104f, 1f,5.2f,100f);
        items.add(gulaschsuppe);
        CategoryItem gemuesebruehe = new CategoryItem(0, context.getResources().getString(R.string.preset_gemuesebruehe),267f,17f,14f,100f);
        items.add(gemuesebruehe);

        // dessert
        CategoryItem pfannkuchen = new CategoryItem(0, context.getResources().getString(R.string.preset_pfannkuchen),227f,16f,10f,100f);
        items.add(pfannkuchen);
        CategoryItem schokolade = new CategoryItem(0, context.getResources().getString(R.string.preset_schokolade),546f,48f,31f,100f);
        items.add(schokolade);
        CategoryItem gummibaerchen = new CategoryItem(0, context.getResources().getString(R.string.preset_gummibaerchen),340f,78f,0f,100f);
        items.add(gummibaerchen);
        CategoryItem kartoffelchips = new CategoryItem(0, context.getResources().getString(R.string.preset_kartoffelchips),536f,0.2f,35f,100f);
        items.add(kartoffelchips);

        return items;
    }
}
