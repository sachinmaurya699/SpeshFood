package com.speshfood.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.FragmentManager;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.speshfood.R;
import com.speshfood.activites.MainActivity;
import com.speshfood.fragments.orderFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class signInDailog extends AppCompatDialogFragment {
    String phoneNumber,passWord;
    EditText phone,pass;
    Button signin;
    ProgressBar pb2;
    TextView sgnText;
    getLoginStatusInterface getl,get2;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            getl = (getLoginStatusInterface) context;

        } catch (ClassCastException e) {
            e.printStackTrace ();
            Log.d ("interfacelogin", e.toString ());
            throw new ClassCastException(context.toString() + " must implement onSomeEventListener");
        }
    }
    @Override
    public
    Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater=getActivity ().getLayoutInflater ();
        View rootView=inflater.inflate (R.layout.dialog_signin,null,false);
        final AlertDialog dialog=new AlertDialog.Builder(getActivity()).setView(rootView)
                .setTitle("Sign in dialog").show();
        dialog.setCanceledOnTouchOutside(false);
        phone=rootView.findViewById (R.id.phnsgnin);
        pass=rootView.findViewById (R.id.phnPass);
        signin=rootView.findViewById (R.id.btnsgn);
        sgnText=rootView.findViewById (R.id.signText);
        pb2=rootView.findViewById (R.id.progressBar2);
        pb2.setVisibility (View.GONE);
        SpannableString ss = new SpannableString ("Sign Up here! If you don't have an account");
        ClickableSpan clickableSpan=new ClickableSpan () {
            @Override
            public
            void onClick(View view) {
                dismiss ();
                openRegDia ();
               // Toast.makeText (getActivity (),"spannable",Toast.LENGTH_LONG).show ();


            }
            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
            }
        };
        ss.setSpan(clickableSpan, 0, 12, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        sgnText.setText(ss);
        sgnText.setMovementMethod(LinkMovementMethod.getInstance());
        sgnText.setHighlightColor(Color.TRANSPARENT);
        signin.setOnClickListener (new View.OnClickListener () {
            @Override
            public
            void onClick(View view) {
                phoneNumber= phone.getText().toString().trim();
                passWord = pass.getText().toString().trim();
                if (phoneNumber.isEmpty()) {
                    phone.setError("Enter Phone Number");
                    phone.setFocusable(true);
                }
                else if (passWord.isEmpty()) {
                    pass.setError("Enter password");
                    pass.setFocusable(true);

                }

                else if (!Patterns.PHONE.matcher(phoneNumber).matches()) {
                    phone.setError("Enter valid email");
                    phone.setFocusable(true);
                }
                else if (passWord.length() < 6) {
                    pass.setError ("Password length must be greater than 6");
                    pass.setFocusable (true);
                }
                else{
                    pb2.setVisibility (View.VISIBLE);
                    hitSignInApi (phoneNumber,passWord);
                }
            }
        });


        return dialog;
    }
    public void openRegDia() {
        FragmentManager fm =getActivity ().getSupportFragmentManager();
        signupDialog rd = new signupDialog ();
        rd.show(fm, "verify number");
    }
    private void hitSignInApi(String phoneNumber,String password){
        String s="http://www.speshfood.com/FoodWellHandler.ashx?RequestType=CustomerSignIN&MobileNo="+phoneNumber+"&Password="+password;
        StringRequest stringRequest=new StringRequest (Request.Method.GET, s, new Response.Listener<String> () {
            @Override
            public
            void onResponse(String response) {
                String betterString=response.replaceAll ("\\\\","");
                String bestString=betterString.substring (1,betterString.length ()-1);
                try {
                    JSONArray jsonArray=new JSONArray (bestString);
                    JSONObject jsonObject=jsonArray.getJSONObject (0);
                    String result=jsonObject.getString("Result");
                    String userId=jsonObject.getString ("ID");
                    if(result.equals ("1")){
                        getl.LoginStatus (true);
                        pb2.setVisibility (View.GONE);
                        Toast.makeText (getActivity (),"sign in successful",Toast.LENGTH_LONG).show ();
                         SharedPreferences login;
                         SharedPreferences.Editor editor;
                        login = getActivity ().getSharedPreferences("login", 0);
                        editor = login.edit();
                        editor.putBoolean ("isLoggedIn", true);
                        editor.putString ("UID",userId);
                        editor.apply ();
                        dismiss ();
                    }

                    else{

                        pb2.setVisibility (View.GONE);

                        // Toast.makeText (getActivity (),"username/password invalid",Toast.LENGTH_LONG).show ();
                    }
                } catch (JSONException e) {
                    pb2.setVisibility (View.GONE);
                    Toast.makeText (getActivity (),"username/password invalid",Toast.LENGTH_LONG).show ();
                    Log.d ("signinerror", e.toString ());
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
    public interface getLoginStatusInterface{
         boolean LoginStatus(boolean b);
    }
}
