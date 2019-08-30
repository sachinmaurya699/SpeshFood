package com.speshfood.activites;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.speshfood.R;
import com.speshfood.fragments.orderFragment;
import com.speshfood.models.hotelModel;
import com.speshfood.models.userAddressModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public
class addressbookActivity extends AppCompatActivity implements isDataFetched {
    Button edit;
    RecyclerView addRv;
    List<userAddressModel> userAddressModelList;
    addressAdapter adapter;
    List<String> localities;
    List<String> subLocalities;
    ArrayAdapter localAdapter, sublocalAdapter;
    List<JSONObject> localityArray;
    private isDataFetched i1;
    int currentPosition;

    @Override
    protected
    void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_addressbook);
        addRv = findViewById (R.id.addRv);
        addRv.setLayoutManager (new LinearLayoutManager (this));
        addRv.setHasFixedSize (true);
        i1=(isDataFetched)this;
        userAddressModelList = new ArrayList<> ();
        SharedPreferences sharedPreferences = getSharedPreferences ("login", 0);
        String uid = sharedPreferences.getString ("UID", "");
        final String newUID = "0" + uid;
        fillAddress (newUID);
        getLocalityList ();
    }

    public
    void fillAddress(String custCode) {
        String s = "http://www.speshfood.com/FoodWellHandler.ashx?RequestType=FillCustomerAddress&CustomerCode=" + custCode;
        StringRequest stringRequest = new StringRequest (Request.Method.GET, s, new Response.Listener<String> () {
            @Override
            public
            void onResponse(String response) {
                String betterString = response.replaceAll ("\\\\", "");
                String bestString = betterString.substring (1, betterString.length () - 1);
                try {
                    JSONArray jsonArray = new JSONArray (bestString);
                    for (int i = 0; i < jsonArray.length (); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject (i);
                        userAddressModel userAddressModelnew = new userAddressModel ();
                        userAddressModelnew.setFlatNo (jsonObject.getString ("FlatNo"));
                        userAddressModelnew.setLandmark (jsonObject.getString ("LandMark"));
                        userAddressModelnew.setSubLocName (jsonObject.getString ("subLocalityName"));
                        userAddressModelnew.setLocName (jsonObject.getString ("localityName"));
                        userAddressModelnew.setApartment (jsonObject.getString ("Apartment"));
                        userAddressModelnew.setCompName (jsonObject.getString ("CompanyName"));
                        userAddressModelnew.setAltCon (jsonObject.getString ("AlternatContactNo"));
                        userAddressModelnew.setAddressId (jsonObject.getString ("AddressID"));
                        userAddressModelList.add (userAddressModelnew);
                    }
                    adapter = new addressAdapter (getApplicationContext (), userAddressModelList);
                    addRv.setAdapter (adapter);
                } catch (JSONException e) {
                    e.printStackTrace ();
                }

            }
        }, new Response.ErrorListener () {
            @Override
            public
            void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected
            Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String> ();
                headers.put ("Content-Type", "application/json; charset=utf-8");
                //               headers.put("providerName", "");
                return headers;
            }
        };
        stringRequest.setRetryPolicy (new DefaultRetryPolicy (
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 48,
                2,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        stringRequest.setShouldCache (false);
        Volley.newRequestQueue (this).add (stringRequest);
    }

    @Override
    public
    void isSublocalitiesfetched(Boolean b) {
        if(b){
            Log.d ("sublocality", "ActivityisSublocalitiesfetched: ");
            Toast.makeText (getApplicationContext (), "Acdata fetched", Toast.LENGTH_SHORT).show ();
        }
        else {
            Log.d ("sublocality", "ActivityisSublocalitiesfetched: ");

            Toast.makeText (getApplicationContext (), "Acnoooo", Toast.LENGTH_SHORT).show ();

        }

    }

    public
    class addressAdapter extends RecyclerView.Adapter<addressAdapter.addImageViewHolder> {
        private Context mContext;
        private List<userAddressModel> mItemsList;

        private
        addressAdapter(Context context, List<userAddressModel> infoClasseItem) {
            mContext = context;
            mItemsList = infoClasseItem;

        }

        @NonNull
        @Override
        public
        addImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from (mContext).inflate (R.layout.address_card, parent, false);
            return new addImageViewHolder (v);
        }

        @Override
        public
        void onBindViewHolder(@NonNull final addImageViewHolder holder, final int position) {
            final userAddressModel currentItem = mItemsList.get (position);
          //  final userAddressModel editedItem= mItemsList.get (arg)
            holder.landEt.setText (currentItem.getLandmark ());
            holder.AltConEt.setText (currentItem.getAltCon ());
            holder.compEt.setText (currentItem.getCompName ());
            holder.flatEt.setText (currentItem.getFlatNo ());
            holder.localEt.setText (currentItem.getLocName ());
            holder.subLocalEt.setText (currentItem.getSubLocName ());
            holder.apartEt.setText (currentItem.getApartment ());
            holder.localEt.setOnItemClickListener (new AdapterView.OnItemClickListener () {
                @Override
                public
                void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    String localityName = holder.localEt.getText ().toString ();
                    for (int t = 0; t < localityArray.size (); t++) {
                        try {
                            if (localityArray.get (t).getString ("localityName").equals (localityName)) {
                                String localityID = localityArray.get (t).getString ("localityId");
                                getSublocalityList (localityID,position);
                                Toast.makeText (getApplicationContext (),localityID,Toast.LENGTH_LONG).show ();
                                holder.subLocalEt.setEnabled (true);
                                holder.subLocalEt.setAdapter (sublocalAdapter);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace ();
                        }
                    }

                }
            });
            holder.editAdd.setOnClickListener (new View.OnClickListener () {
                @Override
                public
                void onClick(View view) {
                    holder.flatEt.setEnabled (true);
                    holder.apartEt.setEnabled (true);
                    holder.compEt.setEnabled (true);
                    holder.AltConEt.setEnabled (true);
                    holder.landEt.setEnabled (true);
                    holder.localEt.setEnabled (true);
                    holder.localEt.setText ("");
                    holder.subLocalEt.setText ("");
                    holder.saveChanges.setVisibility (View.VISIBLE);
                    holder.cancelChanges.setVisibility (View.VISIBLE);
                    if (!localities.isEmpty ()) {
                        //Toast.makeText (getApplicationContext (),"list found",Toast.LENGTH_LONG).show ();
                        holder.localEt.setAdapter (localAdapter);
                    } else {
                        Toast.makeText (getApplicationContext (), "list empty", Toast.LENGTH_LONG).show ();
                    }
                }
            });
            holder.saveChanges.setOnClickListener (new View.OnClickListener () {
                @Override
                public
                void onClick(View view) {

                }
            });
            holder.cancelChanges.setOnClickListener (new View.OnClickListener () {
                @Override
                public
                void onClick(View view) {
                    holder.landEt.setText (currentItem.getLandmark ());
                    holder.AltConEt.setText (currentItem.getAltCon ());
                    holder.compEt.setText (currentItem.getCompName ());
                    holder.flatEt.setText (currentItem.getFlatNo ());
                    holder.localEt.setText (currentItem.getLocName ());
                    holder.subLocalEt.setText (currentItem.getSubLocName ());
                    holder.apartEt.setText (currentItem.getApartment ());
                    holder.flatEt.setEnabled (false);
                    holder.apartEt.setEnabled (false);
                    holder.compEt.setEnabled (false);
                    holder.AltConEt.setEnabled (false);
                    holder.landEt.setEnabled (false);
                    holder.localEt.setEnabled (false);
                    holder.subLocalEt.setEnabled (false);
                    holder.saveChanges.setVisibility (View.GONE);
                    holder.cancelChanges.setVisibility (View.GONE);

                }
            });
        }

        @Override
        public
        int getItemCount() {
            return mItemsList.size ();
        }




    public
    class addImageViewHolder extends RecyclerView.ViewHolder{
        EditText flatEt, apartEt, compEt, AltConEt, landEt;
        ImageButton editAdd;
        AutoCompleteTextView localEt, subLocalEt;
        TextView saveChanges, cancelChanges;
        View itemViewCurrent;

        private
        addImageViewHolder(@NonNull View itemView) {
            super (itemView);
            itemViewCurrent=itemView;
            localEt = itemView.findViewById (R.id.localityName);
            subLocalEt = itemView.findViewById (R.id.subLocalityName);
            flatEt = itemView.findViewById (R.id.flatNo);
            apartEt = itemView.findViewById (R.id.apartment);
            AltConEt = itemView.findViewById (R.id.altCon);
            compEt = itemView.findViewById (R.id.companyName);
            landEt = itemView.findViewById (R.id.landmark);
            editAdd = itemView.findViewById (R.id.edit_add);
            saveChanges = itemView.findViewById (R.id.saveChanges);
            cancelChanges = itemView.findViewById (R.id.cancelChanges);
            saveChanges.setVisibility (View.GONE);
            cancelChanges.setVisibility (View.GONE);
            flatEt.setEnabled (false);
            apartEt.setEnabled (false);
            compEt.setEnabled (false);
            AltConEt.setEnabled (false);
            landEt.setEnabled (false);
            localEt.setEnabled (false);
            subLocalEt.setEnabled (false);
        }


        }
    }


    private void getLocalityList(){
        localities=new ArrayList<> ();
        localityArray=new ArrayList<> ();
        String s="http://www.speshfood.com/FoodWellHandler.ashx?RequestType=GetWithCommonProc&Flag=0&Param=";
        StringRequest stringRequest=new StringRequest (Request.Method.GET, s, new Response.Listener<String> () {
            @Override
            public
            void onResponse(String response) {
                String betterString=response.replaceAll ("\\\\","");
                String bestString=betterString.substring (1,betterString.length ()-1);
                try {
                    JSONArray jsonArray=new JSONArray (bestString);
                    for (int x=0;x<jsonArray.length ();x++){
                        localityArray.add (jsonArray.getJSONObject (x));
                    }
                    for(int i=0;i<jsonArray.length ();i++){
                        localities.add (jsonArray.getJSONObject (i).getString ("localityName"));
                    }
                    localAdapter=new ArrayAdapter<> (getApplicationContext (),android.R.layout.simple_list_item_1,localities);
                }
                catch (JSONException e){

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
        Volley.newRequestQueue(this).add (stringRequest);
    }
    private void getSublocalityList(final String localityId,final int position){
        subLocalities=new ArrayList<> ();
        final String s="http://www.speshfood.com/FoodWellHandler.ashx?RequestType=GetWithCommonProc&Flag=2&Param=";
        StringRequest stringRequest=new StringRequest (Request.Method.GET, s, new Response.Listener<String> () {
            @Override
            public
            void onResponse(String response) {
                String betterString=response.replaceAll ("\\\\","");
                String bestString=betterString.substring (1,betterString.length ()-1);
                try {
                    JSONArray jsonArray=new JSONArray (bestString);
                    for (int i=0;i<jsonArray.length ();i++){
                        String currentlocalityID=jsonArray.getJSONObject (i).getString ("localityId");
                        if(currentlocalityID.equals (localityId)) {
                            String sublocality = jsonArray.getJSONObject (i).getString ("subLocalityName");
                            subLocalities.add (sublocality);
                        }
                    }
                    if(!subLocalities.isEmpty ()){
                        i1.isSublocalitiesfetched (true);
                        Toast.makeText (getApplicationContext (),"sub found",Toast.LENGTH_LONG).show ();
                        sublocalAdapter=new ArrayAdapter<> (getApplicationContext (),android.R.layout.simple_list_item_1,subLocalities);

                    }
                    else{
                        Toast.makeText (getApplicationContext (),"cant get sub",Toast.LENGTH_LONG).show ();
                    }


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
        Volley.newRequestQueue(this).add (stringRequest);
    }
    private void updateAddress(String addID,String custId,String company_Name,String flat_no,String appartment,String localityId,String subLocalityId,String altCon,String landMark){
        String s="http://www.speshfood.com/FoodWellHandler.ashx?RequestType=SaveUpdateCustomerAddress&addAddress="+addID+":"+custId+":"+company_Name+":"+flat_no+":"+appartment+":"+localityId+":"+subLocalityId+":"+altCon+":"+landMark;
        StringRequest stringRequest=new StringRequest (Request.Method.GET, s, new Response.Listener<String> () {
            @Override
            public
            void onResponse(String response) {
                try {
                    JSONArray jsonArray=new JSONArray (response);
                    JSONObject jsonObject=jsonArray.getJSONObject (0);
                    String result=jsonObject.getString ("Result");
                    if (result.equals ("0")){
                        Toast.makeText (getApplicationContext (), "Address Updated", Toast.LENGTH_SHORT).show ();
                    }
                    else{
                        Toast.makeText (getApplicationContext (), "update Failed", Toast.LENGTH_SHORT).show ();
                    }
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
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy (
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS*48,
                2,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        stringRequest.setShouldCache (false);
        Volley.newRequestQueue(this).add (stringRequest);
    }

}
