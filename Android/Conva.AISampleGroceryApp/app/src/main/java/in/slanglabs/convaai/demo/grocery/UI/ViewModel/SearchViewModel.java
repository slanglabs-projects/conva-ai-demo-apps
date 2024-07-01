package in.slanglabs.convaai.demo.grocery.UI.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;

public class SearchViewModel extends AppViewModel {

    public SearchViewModel(@NonNull Application application) {
        super(application);
        getSearchItem("");
    }

}
