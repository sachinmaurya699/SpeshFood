package com.speshfood.dialogs;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.speshfood.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class changePassDia extends AppCompatDialogFragment {
    EditText oldPass,newPass;
    Button chngePass;
    @Override
    public
    Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater layoutInflater=getActivity ().getLayoutInflater ();
        View rootView=layoutInflater.inflate (R.layout.dialog_change_pass,null,false);
        final androidx.appcompat.app.AlertDialog dialog=new AlertDialog.Builder (getActivity ()).setView (rootView)
                .setTitle ("change").show ();
        oldPass=rootView.findViewById (R.id.oldie);
        newPass=rootView.findViewById (R.id.newPass);
        chngePass=rootView.findViewById (R.id.changePass);
        chngePass.setOnClickListener (new View.OnClickListener () {
            @Override
            public
            void onClick(View view) {
                if(oldPass.getText ().toString ().isEmpty ()||newPass.getText ().toString ().isEmpty ()){
                    Toast.makeText (getActivity (),"Enter details",Toast.LENGTH_LONG).show ();
                }
                else{
                    SharedPreferences sharedPreferences = getActivity ().getSharedPreferences ("login", 0);
                    String uid = sharedPreferences.getString ("UID", "");
                    String newUID="0"+uid;
                    changeUserPassword (newUID,oldPass.getText ().toString ().trim (),newPass.getText ().toString ().trim ());
                }
            }
        });
        return dialog;
    }
    private void changeUserPassword(String CustCode,String oldPass,String newPass){
        String s="http://www.speshfood.com/FoodWellHandler.ashx?RequestType=ChangeCustomerPassword&CustCode="+CustCode+"&OldPassword="+oldPass+"&NewPassword="+newPass;
        StringRequest stringRequest=new StringRequest (Request.Method.GET, s, new Response.Listener<String> () {
            @Override
            public
            void onResponse(String response) {
                String betterString=response.replaceAll ("\\\\","");
                String bestString=betterString.substring (1,betterString.length ()-1);
                try {
                    JSONArray jsonArray=new JSONArray (bestString);
                    JSONObject jsonObject=jsonArray.getJSONObject (0);
                    String result=jsonObject.getString ("Result");
                    if(result.equals ("0")){
                        Toast.makeText (getActivity (),"success",Toast.LENGTH_LONG).show ();
                        dismiss ();
                    }
                    else {
                        Toast.makeText (getActivity (),"error",Toast.LENGTH_LONG).show ();
                            dismiss ();
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
}
