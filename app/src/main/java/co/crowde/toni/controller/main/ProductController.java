package co.crowde.toni.controller.main;

import android.app.Activity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import co.crowde.toni.adapter.ProductDashboardAdapter;
import co.crowde.toni.model.CategoryModel;
import co.crowde.toni.model.ProductModel;
import co.crowde.toni.view.fragment.Dashboard;
import co.crowde.toni.view.fragment.Inventory;

public class ProductController {

    public static ArrayList<String> categoryList = new ArrayList<>();
    public static ArrayList<String> categoryInventory = new ArrayList<>();
    public static ArrayList<String> statusInventory = new ArrayList<>();

    public static void productDashboardList(Activity activity){

    }

    public static void filterProductDashboard(Activity activity){
        List<ProductModel> filteredList = new ArrayList<>();

        for (ProductModel row : Dashboard.productModels) {
            for(int i=0; i<categoryList.size(); i++){
                if (row.getCategoryName().equalsIgnoreCase(categoryList.get(i))) {
                    filteredList.add(row);
                }
            }

        }

        Log.e("CategoryId", new Gson().toJson(categoryList));
        Log.e("Product", new Gson().toJson(Dashboard.productModels));
        Log.e("FilteredProduct", new Gson().toJson(filteredList));
        Log.e("Size", String.valueOf(filteredList.size()));

        Dashboard.productModelsFiltered = new ArrayList<>();
        Dashboard.productModelsFiltered = filteredList;
        Dashboard.productDashboardAdapter.notifyDataSetChanged();
        Dashboard.productList(activity);

//        Toast.makeText(activity, new Gson().toJson(Dashboard.productModelsFiltered), Toast.LENGTH_SHORT).show();

    }

    public static void filterInventory(Activity activity){
        List<ProductModel> filteredList = new ArrayList<>();

        for (ProductModel row : Inventory.productModels) {
            for(int i=0; i<categoryInventory.size(); i++){
                for(int j=0; j<statusInventory.size(); j++){
                    if(categoryInventory.size()>0 && statusInventory.size()>0){
                        if (row.getCategoryName().equalsIgnoreCase(categoryInventory.get(i)) &&
                                row.getStatus().equalsIgnoreCase(statusInventory.get(j))) {
                            filteredList.add(row);
                        }
                    }

                }
            }

        }



        Log.e("CategoryId", new Gson().toJson(categoryInventory));
        Log.e("Product", new Gson().toJson(Inventory.productModels));
        Log.e("FilteredProduct", new Gson().toJson(filteredList));
        Log.e("Size", String.valueOf(filteredList.size()));

        Inventory.productModelsFiltered = new ArrayList<>();
        Inventory.productModelsFiltered = filteredList;
        Inventory.inventoryAdapter.notifyDataSetChanged();
        Inventory.productList(activity);

//        Toast.makeText(activity, new Gson().toJson(Dashboard.productModelsFiltered), Toast.LENGTH_SHORT).show();

    }
}
