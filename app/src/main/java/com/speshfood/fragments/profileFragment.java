package com.speshfood.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.speshfood.R;
import com.speshfood.activites.addressbookActivity;
import com.speshfood.dialogs.changePassDia;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public
class profileFragment extends Fragment {
    EditText firstNameEt,emailEt;
    TextView mobileTv,customerCode,addBook;
    Button updateDetails;
    @Nullable
    @Override
    public
    View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate (R.layout.fragment_profile,container,false);
        firstNameEt=v.findViewById (R.id.firstNameEt);
        emailEt=v.findViewById (R.id.emailEt);
        customerCode=v.findViewById (R.id.custCodeTv);
        mobileTv=v.findViewById (R.id.mobileTV);
        updateDetails=v.findViewById (R.id.update);
        addBook=v.findViewById (R.id.addBookTv);
        updateDetails.setEnabled (false);
        SharedPreferences sharedPreferences = getActivity ().getSharedPreferences ("login", 0);
        String uid = sharedPreferences.getString ("UID", "");
        final String newUID="0"+uid;
        getUserDetails (newUID);
        addBook.setOnClickListener (new View.OnClickListener () {
            @Override
            public
            void onClick(View view) {
                Intent i=new Intent (getActivity (), addressbookActivity.class);
                startActivity (i);
            }
        });
        updateDetails.setOnClickListener (new View.OnClickListener () {
            @Override
            public
            void onClick(View view) {
                if(firstNameEt.getText ().toString ().isEmpty ()||emailEt.getText ().toString ().isEmpty ()){
                    Toast.makeText (getActivity (),"please fill all details",Toast.LENGTH_LONG).show ();
                }
                else{
                    String firstName=firstNameEt.getText ().toString ().trim ();
                    String lastName="";
                    if(firstName.contains (" ")){
                        int z=firstName.indexOf (" ");
                        lastName=firstName.substring (z).trim ();
                    }
                    else{
                        lastName="";
                    }
                    SharedPreferences sharedPreferences = getActivity ().getSharedPreferences ("login", 0);
                    String uid = sharedPreferences.getString ("UID", "");
                   // Toast.makeText (getActivity (),uid,Toast.LENGTH_LONG).show ();
                    String newUID="0"+uid;
                    updateUserDetails (newUID,firstName,lastName,emailEt.getText ().toString ().trim (),"");
                }
            }
        });
        return v;
    }
    private void getUserDetails(final String custCode){
        String s="http://www.speshfood.com/FoodWellHandler.ashx?RequestType=GetWithCommonProc&Flag=3&Param="+custCode;
        StringRequest stringRequest=new StringRequest (Request.Method.GET, s, new Response.Listener<String> () {
            @Override
            public
            void onResponse(String response) {
                String betterString=response.replaceAll ("\\\\","");
                String bestString=betterString.substring (1,betterString.length ()-1);
                try {
                    JSONArray jsonArray=new JSONArray (bestString);
                    JSONObject jsonObject=jsonArray.getJSONObject (0);
                    String first_name=jsonObject.getString ("FirstName");
                    String last_name=jsonObject.getString ("LasteName");
                    String mobile=jsonObject.getString ("MobileNo");
                    String email=jsonObject.getString ("EmailId");
                    firstNameEt.setText (first_name.concat (" ").concat (last_name));
                    emailEt.setText (email);
                    customerCode.setText (custCode);
                    mobileTv.setText (mobile);
                    updateDetails.setEnabled (true);



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
        Volley.newRequestQueue(getActivity ()).add (stringRequest);
    }
    private void updateUserDetails(String custCode,String first_name,String last_name,String email,String phone){
        //http://www.speshfood.com/FoodWellHandler.ashx?RequestType=AccountSettings&addAccountSettings=022:Vaibhav:Gautam:v.g.581c@gmail.com:9212033809
        String s="http://www.speshfood.com/FoodWellHandler.ashx?RequestType=AccountSettings&addAccountSettings="+custCode+":"+first_name+":"+last_name+":"+email+":"+phone;
        StringRequest stringRequest=new StringRequest (Request.Method.GET, s, new Response.Listener<String> () {
            @Override
            public
            void onResponse(String response) {
                String betterString=response.replaceAll ("\\\\","");
                String bestString=betterString.substring (1,betterString.length ()-1);
                try {
                    JSONArray jsonArray=new JSONArray (bestString);
                    JSONObject jsonObject=jsonArray.getJSONObject (0);
                    String status=jsonObject.getString ("Result");
                    if(status.equals ("0")){
                        Toast.makeText (getActivity (),"Details Updated",Toast.LENGTH_LONG).show ();

                    }
                    else{
                        Toast.makeText (getActivity (),"failure",Toast.LENGTH_LONG).show ();


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
        Volley.newRequestQueue(getActivity ()).add (stringRequest);
    }


    private void openChangePassDia(){
        FragmentManager fm =getActivity ().getSupportFragmentManager();
        changePassDia rd = new changePassDia ();
        rd.show(fm, "change");
    }
}
