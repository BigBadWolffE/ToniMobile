package co.crowde.toni.controller.product;

import android.app.Activity;

import java.util.ArrayList;

public class ProductController {

    public static ArrayList<String> categoryList = new ArrayList<>();
    public static ArrayList<String> categoryInventory = new ArrayList<>();
    public static ArrayList<String> statusInventory = new ArrayList<>();

    public static void productDashboardList(Activity activity){

    }

//    public static void filterProductDashboard(Activity activity){
//        List<ProductModel> filteredList = new ArrayList<>();
//
//        for (ProductModel row : DashboardFragment.productModels) {
//            for(int i=0; i<categoryList.size(); i++){
//                if (row.getCategoryName().equalsIgnoreCase(categoryList.get(i))) {
//                    filteredList.add(row);
//                }
//            }
//
//        }
//
//        Log.e("CategoryId", new Gson().toJson(categoryList));
//        Log.e("Product", new Gson().toJson(DashboardFragment.productModels));
//        Log.e("FilteredProduct", new Gson().toJson(filteredList));
//        Log.e("Size", String.valueOf(filteredList.size()));
//
//        DashboardFragment.productModelsFiltered = new ArrayList<>();
//        DashboardFragment.productModelsFiltered = filteredList;
//        DashboardFragment.productDashboardAdapter.notifyDataSetChanged();
//
//        boolean tabletSize = activity.getResources().getBoolean(R.bool.isTablet);
//        if (tabletSize) {
//            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
//            DashboardFragment.productListLanscape(activity);
//        } else {
//            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//            DashboardFragment.productListPotrait(activity);
//        }
//
////        Toast.makeText(activity, new Gson().toJson(DashboardFragment.productModelsFiltered), Toast.LENGTH_SHORT).show();
//
//    }

//    public static void filterInventory(Activity activity){
//        List<ProductModel> filteredList = new ArrayList<>();
//
//        for (ProductModel row : InventoryFragment.productModels) {
//            for(int i=0; i<categoryInventory.size(); i++){
//                for(int j=0; j<statusInventory.size(); j++){
//                    if(categoryInventory.size()>0 && statusInventory.size()>0){
//                        if (row.getCategoryName().equalsIgnoreCase(categoryInventory.get(i)) &&
//                                row.getStatus().equalsIgnoreCase(statusInventory.get(j))) {
//                            filteredList.add(row);
//                        }
//                    }
//
//                }
//            }
//
//        }
//
//
//
//        Log.e("CategoryId", new Gson().toJson(categoryInventory));
//        Log.e("Product", new Gson().toJson(InventoryFragment.productModels));
//        Log.e("FilteredProduct", new Gson().toJson(filteredList));
//        Log.e("Size", String.valueOf(filteredList.size()));
//
//        InventoryFragment.productModelsFiltered = new ArrayList<>();
//        InventoryFragment.productModelsFiltered = filteredList;
//        InventoryFragment.inventoryAdapter.notifyDataSetChanged();
//        InventoryFragment.productList(activity);
//
////        Toast.makeText(activity, new Gson().toJson(DashboardFragment.productModelsFiltered), Toast.LENGTH_SHORT).show();
//
//    }
}
