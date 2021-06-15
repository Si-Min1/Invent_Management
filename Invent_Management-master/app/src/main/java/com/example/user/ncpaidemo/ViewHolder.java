package com.example.user.ncpaidemo;

import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;


public class ViewHolder {
    public static class WideUserItemHolder {
        EditText name;
        EditText price;
        EditText count;
        EditText nDay;
        EditText unit_amount;
        EditText sCategory;
        Spinner unit;
        Spinner lCategory;
        int ref;
    }
    public static class SelfUserItemHolder{
        EditText name;
        TextView category;
        EditText nDay;
        EditText unit_amount;
        EditText price;
        EditText count;
        Spinner unit;
        int ref;
    }

    public static class ViewHolderW {
        EditText count;
        EditText amount;
        TextView sCategory;
        TextView unit;
        int ref;
    }
    public static class RecipeViewHolder{
        EditText name;
        EditText count;
        EditText price;
        int ref;
    }

}

