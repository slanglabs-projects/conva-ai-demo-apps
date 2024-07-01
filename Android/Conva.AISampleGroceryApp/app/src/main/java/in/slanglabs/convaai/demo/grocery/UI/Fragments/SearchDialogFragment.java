package in.slanglabs.convaai.demo.grocery.UI.Fragments;

import android.app.Dialog;
import android.content.Context;
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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import in.slanglabs.convaai.demo.grocery.Model.ItemListUIModel;
import in.slanglabs.convaai.demo.grocery.Model.ListType;
import in.slanglabs.convaai.demo.grocery.R;
import in.slanglabs.convaai.demo.grocery.UI.ViewModel.AppViewModel;

public class SearchDialogFragment extends DialogFragment {

    private static final String TAG = "AutoCompleteDialogFragment";
    public ViewItemListener mViewItemListener;
    private EditText mFilterText;
    private NameAdapter mNameAdapter;
    private ImageView mSearchClearButton;
    private AppViewModel mAppViewModel;
    private String mSearchString;
    private TextView mSearchViewHeader;

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
        setStyle(DialogFragment.STYLE_NORMAL, R.style.full_screen_dialog);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        View view = inflater.inflate(R.layout.autocomplete_dialog_fragment, container,
                false);
        if (getArguments() != null) {
            mSearchString = getArguments().getString("searchString", "");
        }
        mFilterText = view.findViewById(R.id.search_text);
        mSearchViewHeader = view.findViewById(R.id.search_items_header);
        if (mFilterText.requestFocus()) {
            getDialog().getWindow().setSoftInputMode(
                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
        mSearchClearButton = view.findViewById(R.id.clear_text);
        mSearchClearButton.setOnClickListener(view1 -> {
            mFilterText.getText().clear();
            InputMethodManager imm = (InputMethodManager) view1.getContext()
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view1.getWindowToken(), 0);
        });

        mFilterText.setOnEditorActionListener((textView, actionId, keyEvent) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                mViewItemListener.onItemClicked(textView.getText().toString());
                dismiss();
                return true;
            }
            return false;
        });
        RecyclerView recyclerView = view.findViewById(R.id.list_item_view);
        if (savedInstanceState != null) {
            dismiss();
        }
        mNameAdapter = new NameAdapter(getContext(),
                R.layout.autocomplete_list_item);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(mNameAdapter);
        ImageButton cancelButton = view.findViewById(R.id.back_arrow);
        cancelButton.setOnClickListener(v -> dismiss());
        return view;
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
        mAppViewModel = new ViewModelProvider(this).get(
                AppViewModel.class);

        mAppViewModel.getSearchForNameMediator().observe(
                getViewLifecycleOwner(), items -> {
                    Set<String> names = new HashSet<>();
                    for(ItemListUIModel itemOfferCart: items) {
                        names.add(itemOfferCart.itemOfferCart.item.name);
                    }
                    mNameAdapter.setItems(names);
                });

        mFilterText.addTextChangedListener(filterTextWatcher);
        mFilterText.setText(mSearchString);
        mFilterText.setSelection(mFilterText.getText().length());
        if (mAppViewModel.getListType().getValue() != null) {
            mSearchString = getArguments() != null ? getArguments()
                    .getString("searchString", "") : "";
            @ListType String listType = mAppViewModel.getListType().getValue();
            switch (listType) {
                case ListType.GROCERY:
                    mSearchViewHeader.setText("Search Grocery Items");
                    break;
                case ListType.PHARMACY:
                    mSearchViewHeader.setText("Search Pharmacy Items");
                    break;
                case ListType.FASHION:
                    mSearchViewHeader.setText("Search Fashion Items");
                    break;
            }
        }
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
            mAppViewModel.getSearchItem(currentSearchTerm);
            mSearchClearButton.setVisibility(View.VISIBLE);
        }
    };

    public interface ViewItemListener {
        public void onItemClicked(String item);
    }

    public class NameHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        AppCompatTextView countryName;

        public NameHolder(Context context, @NonNull View itemView) {
            super(itemView);
            this.countryName = itemView.findViewById(R.id.auto_complete_title);
            itemView.setOnClickListener(this);
        }

        public void setName(String name) {
            this.countryName.setText(name);
        }

        @Override
        public void onClick(View view) {
            mViewItemListener.onItemClicked(countryName.getText().toString());
            SearchDialogFragment.this.dismiss();
        }
    }

    public class NameAdapter extends RecyclerView.Adapter<NameHolder> {

        void setItems(Set<String> items) {
            this.items.clear();
            this.items.addAll(items);
            notifyDataSetChanged();
        }

        private final ArrayList<String> items = new ArrayList<>();

        private final Context context;
        private final int itemResource;

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
            String name = items.get(position);
            holder.setName(name);
        }

        @Override
        public int getItemCount() {
            return this.items.size();
        }
    }
}
