package ca.jonestremblay.jonesgroceries.models;

import android.content.Context;

import ca.jonestremblay.jonesgroceries.R;

public class CatalogSingleton {
    // genius ?
//    private static CatalogSingleton uniqueInstance = null;
//    public String categories[];
//    public int iconCategories[];
//    private Context context;
//
//    private CatalogSingleton(Context context){
//        this.context = context;
//        this.categories = getAllCategories();
//        this.iconCategories = getAllIconCategories();
//    }
//
//    public static synchronized CatalogSingleton getInstance(Context context){
//        if (uniqueInstance == null){
//            uniqueInstance = new CatalogSingleton(context);
//        }
//        return uniqueInstance;
//    }
//
//    private String[] getAllCategories() {
//        return context.getResources().getStringArray(R.array.categories);
//    }
//
//    private int[] getAllIconCategories(){
//        return new int[0];
//    }
//
//    public void printCatalog(){
//        for (String category : this.categories) {
//            System.out.println(category + "\n");
//        }
//    }

}
