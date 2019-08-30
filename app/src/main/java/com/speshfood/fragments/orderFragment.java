package com.speshfood.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.speshfood.R;
import com.speshfood.dialogs.cartDialog;
import com.speshfood.dialogs.signInDailog;
import com.speshfood.activites.hotelActivity;
import com.speshfood.dialogs.testDialog;
import com.speshfood.models.hotelModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
public
class orderFragment extends Fragment implements signInDailog.getLoginStatusInterface{
    String api1,api2;
    AutoCompleteTextView actv;
    ArrayAdapter api1Adapter;
    ArrayList <String> arrayListApi1;
    resultAdapter mAdapter;
    ArrayList <hotelModel> arrayListApi2;
    ArrayList<JSONObject>arrayListsuggest;
    RecyclerView mRecyclerView;
    String subLocality;
    Button signin;
    SharedPreferences sharedPreferences;


    public
    void onBackPressed() {
        if(mRecyclerView.getVisibility ()==View.VISIBLE){
            mRecyclerView.setVisibility (View.GONE);
            signin.setVisibility (View.VISIBLE);
        }
        else {
        //super.onBackPressed ();
            }
    }


    @Nullable
    @Override
    public
    View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate (R.layout.fragment_order,container,false);
        actv=v.findViewById (R.id.actv);

        signin=v.findViewById (R.id.sign_in);


        sharedPreferences=getActivity ().getSharedPreferences ("login",0);
        boolean b=sharedPreferences.getBoolean ("isLoggedIn",false);
        if(b){
            signin.setText ("Log out");
        }
        else{
            signin.setText ("Log in");
        }
        signin.setOnClickListener (new View.OnClickListener () {
            @Override
            public
            void onClick(View view) {
                //openTestDia ();
              boolean b1=sharedPreferences.getBoolean ("isLoggedIn",false);
                if(b1){
                    // Log out user
                    Toast.makeText (getActivity (),"logging out",Toast.LENGTH_LONG).show ();
                    signin.setText ("Log In");
                    SharedPreferences.Editor editor=sharedPreferences.edit ();
                    editor.putBoolean ("isLoggedIn",false);
                    editor.putString ("UID",null);
                    editor.apply ();
                }
                else{
                    // Log in user
                    openSigninDia ();
                }
            }
        });
        mRecyclerView=v.findViewById (R.id.rv);
        mRecyclerView.setLayoutManager(new LinearLayoutManager (getActivity ()));
        mRecyclerView.setHasFixedSize(true);
        api1="http://www.speshfood.com/FoodWellHandler.ashx?RequestType=AutoSuggestSubLocality&SearchText=";
        arrayListApi1=new ArrayList <>();
        arrayListApi2=new ArrayList <>();
        arrayListsuggest=new ArrayList <>();
        hitApi1 ();
        actv.setOnItemClickListener (new AdapterView.OnItemClickListener () {
            @Override
            public
            void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // Toast.makeText (getApplicationContext (),"size="+arrayListsuggest.size (),Toast.LENGTH_LONG).show ();
                for(int j=0;j<arrayListsuggest.size ();j++){
                    try {
                        if(arrayListsuggest.get(j).getString("Area").equals (actv.getText ().toString ())){
                            subLocality=arrayListsuggest.get(j).getString("subLocalityId");
                            // Toast.makeText (getApplicationContext (),subLocality,Toast.LENGTH_LONG).show ();

                        }
                    } catch (JSONException e) {
                        e.printStackTrace ();
                    }
                }
                if(!subLocality.isEmpty ()){
                    int z=subLocality.indexOf (':');
                    String betterSubLocality=subLocality.substring (0,z);
                    Toast.makeText (getActivity (),betterSubLocality,Toast.LENGTH_LONG).show ();
                    hitApi2 (betterSubLocality);
                }
                else {
                    Toast.makeText (getActivity (),"data null",Toast.LENGTH_LONG).show ();
                }
            }

        });
        return v;
    }
    public void changeLoginStatus(boolean b){
        Log.d ("interfacelogin", "LoginStatus: ");
//        Toast.makeText (getActivity (),"interface",Toast.LENGTH_LONG).show ();
        if(b){
            //temp2=v.findViewById (R.id.sign_in);
            Log.d ("interfacelogin", "LoginStatus: ");
//            Toast.makeText (getActivity (),"interface",Toast.LENGTH_LONG).show ();

           // temp2.setText ("Log out");

        }
        else{
            Log.d ("interfacelogin", "LoginStatus: ");
         //   Toast.makeText (getActivity (),"interfac",Toast.LENGTH_LONG).show ();
            //temp2.setText ("Log In");
        }
    }

    @Override
    public
    void onPause() {
        super.onPause ();
        Toast.makeText (getActivity (),"paused",Toast.LENGTH_LONG).show ();
    }

    @Override
    public
    void onResume() {
        super.onResume ();
        Toast.makeText (getActivity (),"resume",Toast.LENGTH_LONG).show ();

    }

    public void openSigninDia() {
        FragmentManager fm = (getActivity ().getSupportFragmentManager());
        signInDailog rd = new signInDailog();
        rd.show(fm, "verify number");
    }
    public void openTestDia(){
        FragmentManager fm = (getActivity ().getSupportFragmentManager());
         testDialog rd = new testDialog();
        rd.show(fm, "verify number");
    }
    public void hitApi1(){
        final JsonObjectRequest jsonObjectRequest=new JsonObjectRequest (Request.Method.POST,api1, null, new Response.Listener<JSONObject> () {
            @Override
            public
            void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray =response.getJSONArray ("FOODAPP");
                    Toast.makeText (getActivity (),"response",Toast.LENGTH_LONG).show ();
                    for(int i=0;i<jsonArray.length ();i++){
                        arrayListsuggest.add (jsonArray.getJSONObject (i));
                    }
                    for(int i=0;i<arrayListsuggest.size ();i++){
                        arrayListApi1.add (arrayListsuggest.get (i).getString ("Area"));
                    }
                    api1Adapter=new ArrayAdapter <>(getActivity (),android.R.layout.simple_list_item_1,arrayListApi1);
                    actv.setDropDownBackgroundDrawable(
                            getActivity ().getResources().getDrawable(R.drawable.actv_drop));
                    actv.setAdapter (api1Adapter);

                } catch (JSONException e) {
                    Log.d ("ERROR>>>>", e.toString ());
                    Toast.makeText (getActivity (),e.toString (),Toast.LENGTH_LONG).show ();
                    e.printStackTrace ();
                }

            }
        }, new Response.ErrorListener () {
            @Override
            public
            void onErrorResponse(VolleyError error) {
                Log.d ("ERROR>>>>", error.toString ());
                Toast.makeText (getActivity (),error.getMessage (),Toast.LENGTH_LONG).show ();
            }
        }){

            @Override
            protected
            Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                //               headers.put("providerName", "");
                return headers;
            }

        };
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy (
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS*48,
                2,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        jsonObjectRequest.setShouldCache (false);
        Volley.newRequestQueue(getActivity ()).add (jsonObjectRequest);
    }
    public void hitApi2(String subLocality){
        api2="http://www.speshfood.com/FoodWellHandler.ashx?RequestType=RestaurantAPI&cityID=1&SubLocalityID="+subLocality+"&CuisinesCode=0&RestaurantName=";
    StringRequest stringRequest=new StringRequest (Request.Method.POST, api2, new Response.Listener<String> () {
        @Override
        public
        void onResponse(String response) {
            Toast.makeText (getActivity (),"response2",Toast.LENGTH_LONG).show ();
            String betterResponse=response.replaceAll ("\\\\","");
            String bestResponse=betterResponse.substring (1,betterResponse.length ()-1);
            Log.d ("string",bestResponse);
            try {
                JSONObject jsonObjectApi2=new JSONObject (bestResponse);
                JSONArray jsonArray=jsonObjectApi2.getJSONArray ("FOODAPP");
                Log.d ("json",jsonArray.toString ());
                ArrayList<hotelModel> hotelModels =new ArrayList<> ();
                for(int i=0;i<jsonArray.length ();i++){
                    hotelModel hotelModel =new hotelModel ();
                   hotelModel.setHotelName (jsonArray.getJSONObject (i).getString ("RestaurantName"));
                   hotelModel.setCategory (jsonArray.getJSONObject (i).getString ("Cuisines"));
                   hotelModel.setHotelCode (jsonArray.getJSONObject (i).getString ("RestaurantCode"));
                   hotelModels.add (hotelModel);
                }
                signin.setVisibility (View.GONE);
                mAdapter=new resultAdapter (getActivity (), hotelModels);
                mRecyclerView.setAdapter(mAdapter);

            } catch (JSONException e) {
                Log.d ("error",e.toString ());
                e.printStackTrace ();
            }


        }
    }, new Response.ErrorListener () {
        @Override
        public
        void onErrorResponse(VolleyError error) {
            Toast.makeText (getActivity (),error.toString (),Toast.LENGTH_LONG).show ();
        }
    }){
        @Override
        protected
        Map<String, String> getParams() throws AuthFailureError {
            HashMap<String, String> headers = new HashMap<String, String>();
            headers.put("Content-Type", "application/json; charset=utf-8");
            //               headers.put("providerName", "");
            return headers;
        }

    };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy (
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS*48,
                2,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        stringRequest.setShouldCache (false);
        Volley.newRequestQueue(getActivity ()).add (stringRequest);

    }

    @Override
    public
    boolean LoginStatus(boolean b) {
        Log.d ("interfacelogin", "LoginStatus: ");
        Toast.makeText (getActivity (),"interface",Toast.LENGTH_LONG).show ();
        if(b){
            Log.d ("interfacelogin", "LoginStatus: ");
            Toast.makeText (getActivity (),"interface",Toast.LENGTH_LONG).show ();
            signin.setText ("Log out");

        }
        else{
            Log.d ("interfacelogin", "LoginStatus: ");
            Toast.makeText (getActivity (),"interfac",Toast.LENGTH_LONG).show ();
            signin.setText ("Log In");
         }
        return false;
    }

    public class resultAdapter extends RecyclerView.Adapter<resultAdapter.ImageViewHolder>{
        private Context mContext;
        private List<hotelModel> mItemsAdapter;

        private resultAdapter(Context context, List<hotelModel> infoClasseItem) {
            mContext =context;
            mItemsAdapter = infoClasseItem;
        }
        @NonNull
        @Override
        public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v= LayoutInflater.from(mContext).inflate(R.layout.hotel_card,parent,false);
            return new ImageViewHolder(v);
        }

        @Override
        public
        void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
            final hotelModel itemCurrent = mItemsAdapter.get(position);
            holder.tv1.setText (itemCurrent.getHotelName ());
            holder.tv2.setText (itemCurrent.getCategory ());
            holder.tv3.setText (itemCurrent.getHotelCode ());
            holder.iv.setImageResource (R.mipmap.ic_launcher);
        }
        @Override
        public
        int getItemCount() {
            return mItemsAdapter.size ();
        }
        private class ImageViewHolder extends RecyclerView.ViewHolder{
            TextView tv1,tv2,tv3;
            ImageView iv;
            CardView c1;
            private
            ImageViewHolder(@NonNull View itemView) {
                super (itemView);
                tv1=itemView.findViewById (R.id.hotel_name);
                tv2=itemView.findViewById (R.id.category);
                tv3=itemView.findViewById (R.id.code);
                iv=itemView.findViewById (R.id.hotel_logo);
                c1=itemView.findViewById (R.id.c1);
                c1.setOnClickListener (new View.OnClickListener () {
                    @Override
                    public
                    void onClick(View view) {
                        String send=tv3.getText ().toString ();
                        Intent i=new Intent (getActivity (), hotelActivity.class);
                        i.putExtra ("hotelCode",send);
                        startActivity (i);
                    }
                });
            }
        }

    }
    }
