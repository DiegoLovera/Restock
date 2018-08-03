package com.lovera.diego.restock;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.gson.Gson;
import com.lovera.diego.restock.models.Product;

public class AddDialogFragment extends DialogFragment {

    private Product mProduct;

    public AddDialogFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_dialog, container, false);

        if (getArguments() != null) {
            Gson gson = new Gson();
            mProduct = gson.fromJson(getArguments().getString("productJson"), Product.class);
        }
        if (getContext() != null){
            Spinner spinner = view.findViewById(R.id.spinner_quantity_fragment);
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                    R.array.quantity, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
        }

        return view;
    }

}