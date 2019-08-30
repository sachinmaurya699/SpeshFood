package com.speshfood.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.speshfood.R;
import com.speshfood.databaseHelpers.CartDBHelper;
import com.speshfood.models.cartModel;

import java.util.ArrayList;
import java.util.List;

public
class cartDialog extends AppCompatDialogFragment {
        RecyclerView rvCart;
        List <cartModel>cartList;
    private cartAdap mCartAdap;
    CartDBHelper db;
    @Override
    public
    Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater layoutInflater=getActivity ().getLayoutInflater ();
        View rootView=layoutInflater.inflate (R.layout.dialog_cart,null,false);
        db=new CartDBHelper (getActivity ());
        rvCart=rootView.findViewById (R.id.rvCart);
       final AlertDialog dialog=new AlertDialog.Builder (getActivity ()).setView (rootView)
               .setTitle ("CART").show ();
        rvCart.setLayoutManager(new LinearLayoutManager (getActivity ()));
        rvCart.setHasFixedSize(true);
        cartList=new ArrayList<> ();
        cartModel cartModelObject=new cartModel ();
        cartModelObject.setItemNo ("1");
        cartModelObject.setItemName ("idli");
        cartModelObject.setItemQty ("1");
        cartModelObject.setItemAmount ("100");
        cartModel cartModelObject2=new cartModel ();
        cartModelObject2.setItemNo ("1");
        cartModelObject2.setItemName ("idli");
        cartModelObject2.setItemQty ("1");
        cartModelObject2.setItemAmount ("100");
       // cartList.add (cartModelObject);
      //  cartList.add (cartModelObject2);

        int z=db.getProfilesCount ();
        for (int i=1;i<=z;i++){
            cartModel cartModelObject3=new cartModel ();
            cartModelObject3=db.getData (String.valueOf (i));
            cartList.add (cartModelObject3);
        }


        mCartAdap=new cartAdap (getActivity (),cartList);
        rvCart.setAdapter(mCartAdap);
       
       return dialog;
    }
    public class cartAdap extends RecyclerView.Adapter<cartAdap.ImageViewHolder>{
        private Context mContext;
        private List<cartModel> mCartModels;
        private cartAdap(Context context, List<cartModel> cartModels) {
            mContext =context;
            mCartModels = cartModels;
        }


        @NonNull
        @Override
		
        public
        ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v= LayoutInflater.from(mContext).inflate(R.layout.cart_list_card,parent,false);
            return new cartAdap.ImageViewHolder (v);
        }

        @Override
        public
        void onBindViewHolder(@NonNull final ImageViewHolder holder, int position) {
            final cartModel itemCurrent = mCartModels.get(position);
            holder.serialTv.setText (itemCurrent.getItemNo ());
            holder.nameTv.setText (itemCurrent.getItemName ());
            holder.qtyTv.setText (itemCurrent.getItemQty ());
            holder.amountTv.setText (itemCurrent.getItemAmount ());
            holder.addBtn.setOnClickListener (new View.OnClickListener () {
                @Override
                public
                void onClick(View view) {
                    int z=Integer.parseInt (itemCurrent.getItemQty ());
                    if(z>=0&&z<10){
                    itemCurrent.setItemQty (String.valueOf (++z));
                    holder.qtyTv.setText (String.valueOf (z));
                    float p=Float.parseFloat (itemCurrent.getItemAmount ());
                        float totalAmount=z*p;
                        holder.amountTv.setText (String.valueOf (totalAmount));}
                }
            });
            holder.subBtn.setOnClickListener (new View.OnClickListener () {
                @Override
                public
                void onClick(View view) {
                    int z=Integer.parseInt (itemCurrent.getItemQty ());
                    if(z>0&&z<=10){
                        z=z-1;
                    itemCurrent.setItemQty (String.valueOf (z));
                    holder.qtyTv.setText (String.valueOf (z));
                        float p=Float.parseFloat (itemCurrent.getItemAmount ());
                        float totalAmount=z*p;
                        holder.amountTv.setText (String.valueOf (totalAmount));
                        }
                }
            });
        }


        @Override
        public
        int getItemCount() {
            return mCartModels.size ();
        }
        private class ImageViewHolder extends RecyclerView.ViewHolder{
            TextView serialTv,nameTv,qtyTv,amountTv;
            ImageButton addBtn,subBtn;
            private ImageViewHolder(@NonNull View itemView) {
                super (itemView);
                serialTv = itemView.findViewById (R.id.serial);
                nameTv = itemView.findViewById (R.id.productName);
                qtyTv = itemView.findViewById (R.id.qtyTv);
                amountTv = itemView.findViewById (R.id.amountTv);
                addBtn=itemView.findViewById (R.id.addImageButton);
                subBtn=itemView.findViewById (R.id.subImageButton);

            }
        }
    }

}
