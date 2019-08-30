package com.speshfood.activites;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.speshfood.R;
import com.speshfood.databaseHelpers.CartDBHelper;
import com.speshfood.dialogs.cartDialog;
import com.speshfood.dialogs.signInDailog;
import com.speshfood.models.categoryModel;
import com.speshfood.models.itemModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class hotelActivity extends AppCompatActivity implements signInDailog.getLoginStatusInterface{
        private boolean isMenuItemChecked;
        private RecyclerView itemRv;
        private itemAdap mAdap;
        private ImageButton category,openCart;
        private String hotelCode;
        private List<itemModel> itemModels;
        private List<itemModel> itemModelsNew;
        private List<categoryModel> categoryArrayList;
        private SharedPreferences settings;
        private SharedPreferences.Editor editor;
        private CartDBHelper db ;
    @Override
    protected
    void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_hotel);
        Toolbar toolbar = findViewById(R.id.toolbar);
        category=findViewById (R.id.category);
        openCart=findViewById (R.id.openCart);
        setSupportActionBar(toolbar);
        db = new CartDBHelper (this);
        openCart.setOnClickListener (new View.OnClickListener () {
            @Override
            public
            void onClick(View view) {
                openCartDia ();
            }
        });
        isMenuItemChecked=false;
        try {
            hotelCode=getIntent ().getStringExtra ("hotelCode");
            Toast.makeText (getApplicationContext (),hotelCode,Toast.LENGTH_LONG).show ();
        } catch (NullPointerException e) {
            Toast.makeText (getApplicationContext (),"hotelCode null",Toast.LENGTH_LONG).show ();
           hotelCode="6";
            e.printStackTrace ();
        }
        itemRv=findViewById (R.id.rv2);
        itemRv.setLayoutManager(new LinearLayoutManager (this));
        itemRv.setHasFixedSize(true);
        itemModels=new ArrayList<> ();
        itemModelsNew=new ArrayList<> ();
        categoryArrayList=new ArrayList<> ();
        hitApi3 (hotelCode);
        hitApi4 (hotelCode);
        settings = getSharedPreferences("settings", 0);
        editor = settings.edit();
        editor.putInt ("checkedItemId", 969);
        category.setOnClickListener (new View.OnClickListener () {
            @Override
            public
            void onClick(View view) {
                PopupMenu popup = new PopupMenu(hotelActivity.this, category);
                Menu menu=popup.getMenu ();
                if(!categoryArrayList.isEmpty ()){
                    for(int i=0;i<categoryArrayList.size ();i++){
                        menu.add (Menu.NONE,categoryArrayList.get (i).getCategoryId (), Menu.NONE, categoryArrayList.get (i).categoryName);
                    }}
                else{
                    Toast.makeText (getApplicationContext (),"Empty",Toast.LENGTH_LONG).show ();
                }
                settings = getSharedPreferences("settings", 0);
                int itemId;
                try {
                    itemId = settings.getInt("checkedItemId", 969);
                    MenuItem item= menu.findItem (itemId);
                    if(itemId==969){
                        Toast.makeText (getApplicationContext (),"item null",Toast.LENGTH_LONG).show ();
                    }
                    else{
                        item.setChecked (true);
                    }
                } catch (Exception e) {
                    e.printStackTrace ();
                }
                menu.setGroupCheckable(0, true, true);
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        itemModelsNew.clear ();
                        Toast.makeText(hotelActivity.this, "You Clicked : " + item.getTitle(), Toast.LENGTH_SHORT).show();
                        item.setChecked (true);
                        editor.putString ("checkedItemName", item.getTitle ().toString ());
                        editor.putInt ("checkedItemId", item.getItemId ());
                        editor.apply ();
                        for(int i=0;i<itemModels.size ();i++){
                            if(Integer.parseInt (itemModels.get (i).getCategoryCode ())== (item.getItemId ())){
                                itemModelsNew.add (itemModels.get (i));
                            }
                        }
                        mAdap=new itemAdap (getApplicationContext (),itemModelsNew);
                        itemRv.setAdapter(mAdap);
                        return true;
                    }
                });
                popup.show();
            }

        });
    }
    private void hitApi3(String hotelId){
        String s= "http://www.speshfood.com/FoodWellHandler.ashx?RequestType=GetWithCommonProc&Flag=25&Param="+hotelId;
        StringRequest stringRequest=new StringRequest (Request.Method.POST, s, new Response.Listener<String> () {
            @Override
            public
            void onResponse(String response) {
                String betterString=response.replaceAll ("\\\\","");
                String bestString=betterString.substring (1,betterString.length ()-1);
                try {
                    JSONArray jsonArray=new JSONArray (bestString);
                    Log.d ("bestString3",jsonArray.toString ());
                    for(int i=0;i<jsonArray.length ();i++) {
                        categoryModel categoryModel=new categoryModel ();
                        categoryModel.setCategoryName(jsonArray.getJSONObject (i).getString ("CategoryName"));
                        categoryModel.setCategoryId(jsonArray.getJSONObject (i).getInt ("CategoryCode"));
                        categoryArrayList.add (categoryModel);
                    }
                    Log.d ("array", categoryArrayList.toString ());
                } catch (JSONException e) {
                    e.printStackTrace ();
                    Toast.makeText (getApplicationContext (),e.getMessage (),Toast.LENGTH_LONG).show ();

                }
            }
        }, new Response.ErrorListener () {
            @Override
            public
            void onErrorResponse(VolleyError error) {
                    Toast.makeText (getApplicationContext (),error.getMessage (),Toast.LENGTH_LONG).show ();
            }
        }){
            @Override
            protected
            Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                //               headers.put("providerName", "");
                return headers;
            }};
        stringRequest.setRetryPolicy(new DefaultRetryPolicy (
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS*48,
                2,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        stringRequest.setShouldCache (false);
        Volley.newRequestQueue(hotelActivity.this).add (stringRequest);
    }
    private void hitApi4(String hotelCode){
        String s="http://www.speshfood.com/FoodWellHandler.ashx?RequestType=getRestaurantInfo&Restaurantcode="+hotelCode;
        StringRequest stringRequest=new StringRequest (Request.Method.POST, s, new Response.Listener<String> () {
            @Override
            public
            void onResponse(String response) {
                String betterString=response.replaceAll ("\\\\","");
                String bestString=betterString.substring (1,betterString.length ()-1);
                try {
                    JSONArray jsonArray=new JSONArray (bestString);
                    Log.d ("bestString",jsonArray.toString ());
                    for(int i=0;i<jsonArray.length ();i++){
                        JSONObject jsonObject=jsonArray.getJSONObject(i);
                        itemModel itemModel=new itemModel ();
                        itemModel.setItemName (jsonObject.getString ("ItemName"));
                        itemModel.setQtyType (jsonObject.getString ("QtyType"));
                        itemModel.setPrice (jsonObject.getString ("Rate"));
                        itemModel.setCategoryName (jsonObject.getString ("CategoryName"));
                        itemModel.setCategoryCode(jsonObject.getString ("CategoryCode"));
                        itemModel.setItemCode(jsonObject.getString ("IDForItemCode"));
                        itemModels.add (itemModel);
                    }
                    mAdap=new itemAdap (getApplicationContext (),itemModels);
                    itemRv.setAdapter(mAdap);
                } catch (JSONException e) {
                    e.printStackTrace ();
                }
            }
        }, new Response.ErrorListener () {
            @Override
            public
            void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected
            Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                //               headers.put("providerName", "");
                return headers;
        }};
        stringRequest.setRetryPolicy(new DefaultRetryPolicy (
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS*48,
                2,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        stringRequest.setShouldCache (false);
        Volley.newRequestQueue(hotelActivity.this).add (stringRequest);
    }

    @Override
    public
    boolean LoginStatus(boolean b) {
        return false;
    }

    public class itemAdap extends RecyclerView.Adapter<itemAdap.ImageViewHolder>{
        private Context mContext;
        private List<itemModel> mItemModels;
        private itemAdap(Context context, List<itemModel> itemModels) {
            mContext =context;
            mItemModels = itemModels;
        }
        @NonNull
        @Override
        public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v= LayoutInflater.from(mContext).inflate(R.layout.item_card,parent,false);
            return new itemAdap.ImageViewHolder (v);
        }

        @Override
        public
        void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
            final itemModel itemCurrent = mItemModels.get(position);
            holder.tv1.setText (itemCurrent.getItemName ());
            holder.tv2.setText (itemCurrent.getQtyType ());
            holder.tv3.setText (itemCurrent.getPrice ());
            holder.addCart.setOnClickListener (new View.OnClickListener () {
                @Override
                public
                void onClick(View view) {
                    SharedPreferences sharedPreferences = getSharedPreferences ("login", 0);
                    boolean b = sharedPreferences.getBoolean ("isLoggedIn", false);
                    if(b){
                        boolean result=db.addItemToCart (itemCurrent.getItemCode (),itemCurrent.getItemName (),"1",itemCurrent.getPrice ());
                        if(result){
                           // Toast.makeText (getApplicationContext (),"db inserted",Toast.LENGTH_LONG).show ();
                            Log.d ("result>>", String.valueOf (result));
                        }
                        else {
                            Log.d ("result>>", String.valueOf (result));

                           // Toast.makeText (getApplicationContext (),"db failed",Toast.LENGTH_LONG).show ();
                        }
                        // open cart dialog
                        //openCartDia ();
                        Toast.makeText (getApplicationContext (),"Added to cart",Toast.LENGTH_LONG).show ();
                    }
                    else{
                        Toast.makeText (getApplicationContext (),"Sign in First",Toast.LENGTH_LONG).show ();

                        openSigninDia ();}
                }
            });
        }
        @Override
        public int getItemCount() {
            return mItemModels.size ();
        }
        private class ImageViewHolder extends RecyclerView.ViewHolder{
            TextView tv1,tv2,tv3;
            ImageView addCart;
            private ImageViewHolder(@NonNull View itemView) {
                super (itemView);
                tv1 = itemView.findViewById (R.id.productName);
                tv2 = itemView.findViewById (R.id.qtyType);
                tv3 = itemView.findViewById (R.id.price);
                addCart = itemView.findViewById (R.id.addCart);

                }
            }}



    public void openSigninDia() {
        FragmentManager fm = ((AppCompatActivity) this).getSupportFragmentManager();
        signInDailog rd = new signInDailog();
        rd.show(fm, "verify number");
    }
    public void openCartDia() {
        FragmentManager fm = ((AppCompatActivity) this).getSupportFragmentManager();
        cartDialog rd = new cartDialog ();
        rd.show(fm, "verify number");
    }
}
