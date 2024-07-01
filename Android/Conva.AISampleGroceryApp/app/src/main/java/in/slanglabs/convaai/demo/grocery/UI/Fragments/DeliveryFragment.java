package in.slanglabs.convaai.demo.grocery.UI.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.google.android.material.card.MaterialCardView;

import in.slanglabs.convaai.demo.grocery.R;
import in.slanglabs.convaai.demo.grocery.UI.Activities.PlaceOrderActivity;

public class DeliveryFragment extends Fragment {

    MaterialCardView materialCardViewFastest;
    MaterialCardView materialCardViewBestValue;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_delivery, container,
                false);

        materialCardViewFastest = view.findViewById(R.id.materialCardViewFastest);
        materialCardViewBestValue = view.findViewById(R.id.materialCardViewBestValue);


        materialCardViewFastest.setOnClickListener(view1 -> {
            Intent intent = new Intent(getActivity(), PlaceOrderActivity.class);
            startActivity(intent);
        });

        materialCardViewBestValue.setOnClickListener(view1 -> {
            Intent intent = new Intent(getActivity(), PlaceOrderActivity.class);
            startActivity(intent);
        });

        return view;

    }
}