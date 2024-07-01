package in.slanglabs.convaai.demo.grocery.UI.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import java.util.Date;

import in.slanglabs.convaai.demo.grocery.Model.CheckoutInfoModel;

public class CheckoutViewModel extends AppViewModel {

    private final MutableLiveData<CheckoutInfoModel> mCheckoutLiveDate =
            new MutableLiveData<>();

    public CheckoutViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<CheckoutInfoModel> getCheckoutLiveDate() {
        return mCheckoutLiveDate;
    }

    public void handleCheckout(String name, String phoneNumber, Date deliveryDate) {
        CheckoutInfoModel checkoutInfoModel = new CheckoutInfoModel(name, phoneNumber, deliveryDate);
        validateAndMove(checkoutInfoModel);
    }

    public void handleCheckoutItem(CheckoutInfoModel checkoutInfoModel) {
        mCheckoutLiveDate.postValue(checkoutInfoModel);
        validateAndMove(checkoutInfoModel);
    }

    private void validateAndMove(CheckoutInfoModel checkoutInfoModel) {
        mRepository.onNavigation("checkout");
    }

}