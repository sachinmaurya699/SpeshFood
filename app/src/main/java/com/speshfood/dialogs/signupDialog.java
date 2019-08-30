package com.speshfood.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.speshfood.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public
class signupDialog extends AppCompatDialogFragment {
   private String phoneNumber,sentCode,password,cnfrmPswrd,userId,enteredOtp;
   private EditText phone,pass,cnfPass,otpET;
   private Button sndotp,verotp;
   private ProgressBar pb1;
    @NonNull
    @Override
    public
    Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater=getActivity().getLayoutInflater();
        View view=inflater.inflate(R.layout.dialog_signup,null,false);
        final AlertDialog dialog=new AlertDialog.Builder(getActivity()).setView(view)
                .setTitle("Sign up dialog").show();
        dialog.setCanceledOnTouchOutside(false);
        phone=view.findViewById (R.id.entNum);
        pass=view.findViewById (R.id.entPass);
        cnfPass=view.findViewById (R.id.cnfPass);
        otpET=view.findViewById (R.id.enterCode);
        sndotp=view.findViewById (R.id.sndotp);
        verotp=view.findViewById (R.id.verifyOtp);
        pb1=view.findViewById (R.id.progressBar1);
        pb1.setVisibility (View.GONE);
        otpET.setEnabled (false);
        verotp.setEnabled (false);
        sndotp.setOnClickListener (new View.OnClickListener () {
            @Override
            public
            void onClick(View view) {
                phoneNumber = phone.getText().toString().trim();
                password = pass.getText().toString().trim();
                cnfrmPswrd = cnfPass.getText().toString().trim();
                sentCode=String.valueOf (genOtp ());
                if (phoneNumber.isEmpty()) {
                    phone.setError("Enter Phone Number");
                    phone.setFocusable(true);
                }
                 else if (password.isEmpty()) {
                    pass.setError("Enter password");
                    pass.setFocusable(true);

                }
                else if (cnfrmPswrd.isEmpty()) {
                    cnfPass.setError("Re-enter password");
                    cnfPass.setFocusable(true);
                }
                else if (!password.equals(cnfrmPswrd)) {
                    cnfPass.setError("Password mismatch");
                    cnfPass.setFocusable(true);
                }
               else if (!Patterns.PHONE.matcher(phoneNumber).matches()) {
                    phone.setError("Enter valid Phone Number");
                    phone.setFocusable(true);
                }
                else if (password.length() < 6) {
                    pass.setError("Password length must be greater than 6");
                    pass.setFocusable(true);
            }
                else if (sentCode.isEmpty ()) {
                   Toast.makeText (getActivity (),"Cant generate otp",Toast.LENGTH_LONG).show ();
                }
                else{
                    pb1.setVisibility (View.VISIBLE);
                    sndotp.setEnabled (false);
                    hitSignUp (phoneNumber,password,sentCode);
                }
            }
        });
            verotp.setOnClickListener (new View.OnClickListener () {
                @Override
                public
                void onClick(View view) {
                    pb1.setVisibility (View.VISIBLE);
                    verotp.setEnabled (false);
                   enteredOtp= otpET.getText ().toString ();
                   if(userId.isEmpty () ){
                       Toast.makeText (getActivity (),"cant get userID",Toast.LENGTH_LONG).show ();
                   }
                   else if(enteredOtp.isEmpty () || enteredOtp.length ()!=5){
                       otpET.setError ("enter valid 5 digit otp");
                   }
                   else{
                    hitverifyEnteredOtpApi (userId,enteredOtp);
                   }
                }
            });
        return dialog;
    }
    public int genOtp() {
        Random r = new Random ( System.currentTimeMillis() );
        return 10000 + r.nextInt(20000);
    }
/*    private void hitFast2Sms(String phone,String sentOtp){
        String s="https://www.fast2sms.com/dev/bulk?authorization=eCluTdnXJjEPwxQ0rWfLsVY6ZSbAogcNItq37U5FvBD18iaGy2Y5Xkr0NyVplEUWPqOiFb31hBMjmtDH&sender_id=FSTSMS&language=english&route=qt&numbers="+phone+"&message=13781&variables={AA}&variables_values="+sentOtp;
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest (Request.Method.GET, s, null, new Response.Listener<JSONObject> () {
            @Override
            public
            void onResponse(JSONObject response) {
                try {
                    if(response.getBoolean ("return")){
                        pb1.setVisibility (View.GONE);
                        otpET.setEnabled (true);
                        verotp.setEnabled (true);
                        Toast.makeText (getActivity (),"otp sent",Toast.LENGTH_LONG).show ();
                    }
                } catch (JSONException e) {
                    e.printStackTrace ();
                    Toast.makeText (getActivity (),e.toString (),Toast.LENGTH_LONG).show ();

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
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy (
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS*48,
                2,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        jsonObjectRequest.setShouldCache (false);
        Volley.newRequestQueue(getActivity ()).add (jsonObjectRequest);

    }*/
    private void sendOtptouser(String number,String otp){
        String s="http://api.textlocal.in/send/?username=jyonaar@gmail.com&hash=2875733f9fb479a42a3e8f12553b394879a333ee&sender=SPFOOD&numbers="+number+"&message=Your otp for Spesh Food is"+otp;
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest (Request.Method.GET, s, null, new Response.Listener<JSONObject> () {
            @Override
            public
            void onResponse(JSONObject response) {
                try {
                    if(response.getString ("status").equals ("success")){
                        pb1.setVisibility (View.GONE);
                        otpET.setEnabled (true);
                        verotp.setEnabled (true);
                        Toast.makeText (getActivity (),"otp sent",Toast.LENGTH_LONG).show ();
                    }
                    else{
                        Toast.makeText (getActivity (),"could not send otp",Toast.LENGTH_LONG).show ();

                    }
                } catch (JSONException e) {
                    e.printStackTrace ();
                    Toast.makeText (getActivity (),e.toString (),Toast.LENGTH_LONG).show ();

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
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy (
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS*48,
                2,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        jsonObjectRequest.setShouldCache (false);
        Volley.newRequestQueue(getActivity ()).add (jsonObjectRequest);
    }
    private void hitSignUp(final String phoneNumber, String password, final String otp){
        String s="http://www.speshfood.com/FoodWellHandler.ashx?RequestType=CustomerSignUp&MobileNo="+phoneNumber+"&Password="+password+"&OTP="+otp+"&IpAddress=127.0.0.1";
        StringRequest stringRequest=new StringRequest (Request.Method.GET, s, new Response.Listener<String> () {
            @Override
            public
            void onResponse(String response) {
                String betterString=response.replaceAll ("\\\\","");
                String bestString=betterString.substring (1,betterString.length ()-1);
                try {
                    JSONArray jsonArray=new JSONArray (bestString);
                    JSONObject jsonObject=jsonArray.getJSONObject (0);
                   String result= jsonObject.getString ("Result");
                   userId=jsonObject.getString ("ID");
                   if(result.equals ("1")){
                       otpET.setEnabled (true);
                       verotp.setEnabled (true);
                       sendOtptouser (phoneNumber,otp);
                       Toast.makeText (getActivity (),"Registration Successful,please verify otp",Toast.LENGTH_LONG).show ();
                       pb1.setVisibility (View.GONE);
                   }
                   else{
                       pb1.setVisibility (View.GONE);
                       Toast.makeText (getActivity (),"user already exist! Please sign in",Toast.LENGTH_LONG).show ();
                        sndotp.setEnabled (true);
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
        Volley.newRequestQueue(getActivity ()).add (stringRequest);

    }
    private void hitverifyEnteredOtpApi(String userID,String enteredOtp){
        String s="http://www.speshfood.com/FoodWellHandler.ashx?RequestType=CheckAndUpdateOTP&ID="+userID+"&OTP="+enteredOtp+"&TimeInMin=15";
        StringRequest stringRequest=new StringRequest (Request.Method.GET, s, new Response.Listener<String> () {
            @Override
            public
            void onResponse(String response) {
                String betterString=response.replaceAll ("\\\\","");
                String bestString=betterString.substring (1,betterString.length ()-1);
                try {
                    JSONArray jsonArray=new JSONArray (bestString);
                    JSONObject jsonObject=jsonArray.getJSONObject (0);
                    String result= jsonObject.getString ("Flg");
                    if(result.equals ("1")){
                        pb1.setVisibility (View.GONE);
                        Toast.makeText (getActivity (),"otp verified",Toast.LENGTH_LONG).show ();
                        dismiss ();
                    }
                    else{
                        pb1.setVisibility (View.GONE);
                        Toast.makeText (getActivity (),"wrong otp",Toast.LENGTH_LONG).show ();

                    }
                } catch (JSONException e) {
                    e.printStackTrace ();
                    pb1.setVisibility (View.GONE);
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
        Volley.newRequestQueue(getActivity ()).add (stringRequest);
    }


}
