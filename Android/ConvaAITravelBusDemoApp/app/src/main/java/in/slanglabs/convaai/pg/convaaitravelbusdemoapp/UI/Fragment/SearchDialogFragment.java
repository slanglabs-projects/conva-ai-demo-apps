package in.slanglabs.convaai.pg.convaaitravelbusdemoapp.UI.Fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import in.slanglabs.convaai.pg.convaaitravelbusdemoapp.Model.Place;
import in.slanglabs.convaai.pg.convaaitravelbusdemoapp.R;
import in.slanglabs.convaai.pg.convaaitravelbusdemoapp.UI.ViewModel.AppViewModel;

public class SearchDialogFragment extends DialogFragment {

    private static final String TAG = "AutoCompleteDialogFragment";
    public ViewItemListener viewItemListener;
    private EditText filterText;
    private NameAdapter nameAdapter;
    private ImageView searchClearButton;
    private AppViewModel appViewModel;
    private String searchString;
    private Context mContext;

    public static SearchDialogFragment newInstance(String searchText) {
        SearchDialogFragment myFragment = new SearchDialogFragment();

        Bundle args = new Bundle();
        args.putString("searchString", searchText);
        myFragment.setArguments(args);

        return myFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) dismiss();
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.full_screen_dialog);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        View view = inflater.inflate(R.layout.search_dialog_fragment, container,
                false);
        filterText = view.findViewById(R.id.search_text);
        if (getArguments() != null) {
            searchString = getArguments().getString("searchString", "");
        }
        if(searchString.length() == 0) {
            if (filterText.requestFocus()) {
                getDialog().getWindow().setSoftInputMode(
                        WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
            }
        }
        searchClearButton = view.findViewById(R.id.clear_text);
        searchClearButton.setOnClickListener(view1 -> {
            filterText.getText().clear();
            InputMethodManager imm = (InputMethodManager) view1.getContext()
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view1.getWindowToken(), 0);
        });

        filterText.setOnEditorActionListener((textView, actionId, keyEvent) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                dismiss();
                return true;
            }
            return false;
        });
        RecyclerView recyclerView = view.findViewById(R.id.list_item_view);
        if (savedInstanceState != null) {
            dismiss();
        }
        nameAdapter = new NameAdapter(getContext(),
                R.layout.search_list_item);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(nameAdapter);
        ImageButton cancelButton = view.findViewById(R.id.back_arrow);
        cancelButton.setOnClickListener(v ->{
            InputMethodManager imm = (InputMethodManager) cancelButton.getContext()
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(cancelButton.getWindowToken(), 0);
            dismiss();
        });
        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        appViewModel = new ViewModelProvider(this).get(
                AppViewModel.class);
        filterText.addTextChangedListener(filterTextWatcher);
        filterText.setText(searchString);
        filterText.setSelection(filterText.getText().length());
    }

    private TextWatcher filterTextWatcher = new TextWatcher() {

        public void afterTextChanged(Editable s) {
        }

        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
            String currentSearchTerm = s.toString();
            if(currentSearchTerm.length() > 1) {
                appViewModel.getPlacesForName(currentSearchTerm).observe(
                        getViewLifecycleOwner(), new Observer<List<Place>>() {
                            @Override
                            public void onChanged(List<Place> places) {
                                nameAdapter.setNames(places);
                            }
                        });
            }
            else {
                appViewModel.getAllPlaces().observe(
                        getViewLifecycleOwner(), new Observer<List<Place>>() {
                            @Override
                            public void onChanged(List<Place> places) {
                                nameAdapter.setNames(places);
                            }
                        });
            }
            searchClearButton.setVisibility(View.VISIBLE);
        }
    };

    public interface ViewItemListener {
        public void onItemClicked(Place place);
    }

    public class NameHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        AppCompatTextView itemName;
        Place place;

        public NameHolder(Context context, @NonNull View itemView) {
            super(itemView);
            this.itemName = itemView.findViewById(R.id.auto_complete_title);
            itemView.setOnClickListener(this);
        }

        public void setItem(Place place) {
            this.place = place;
            this.itemName.setText(place.city+", "+place.stop+" ("+place.state+")");
        }

        @Override
        public void onClick(View view) {
            viewItemListener.onItemClicked(place);
            InputMethodManager imm = (InputMethodManager) view.getContext()
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            SearchDialogFragment.this.dismiss();
        }
    }

    public class NameAdapter extends RecyclerView.Adapter<NameHolder> {

        void setNames(List<Place> items) {
            this.items.clear();
            this.items.addAll(items);
            notifyDataSetChanged();
        }

        private ArrayList<Place> items = new ArrayList<>();

        private Context context;
        private int itemResource;

        NameAdapter(Context context, int itemResource) {
            this.context = context;
            this.itemResource = itemResource;
        }

        @NonNull
        @Override
        public NameHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                             int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(this.itemResource, parent, false);
            return new NameHolder(this.context, view);
        }

        @Override
        public void onBindViewHolder(@NonNull NameHolder holder, int position) {
            holder.setItem(items.get(position));
        }

        @Override
        public int getItemCount() {
            return this.items.size();
        }
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
    }
}

