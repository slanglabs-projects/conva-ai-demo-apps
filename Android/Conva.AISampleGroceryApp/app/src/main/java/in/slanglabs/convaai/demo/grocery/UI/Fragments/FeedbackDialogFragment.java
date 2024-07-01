package in.slanglabs.convaai.demo.grocery.UI.Fragments;

import android.app.Dialog;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import in.slanglabs.convaai.demo.grocery.Model.FeedbackItem;
import in.slanglabs.convaai.demo.grocery.R;
import in.slanglabs.convaai.demo.grocery.UI.ViewModel.AppViewModel;

public class FeedbackDialogFragment extends DialogFragment {

    private static final String TAG = "FeedbackDialogFragment";
    private FeedbackItem feedbackItem;
    private AppViewModel appViewModel;
    private EditText feedback;

    private View feedBackConditionView;
    private View feedBackUtteranceView;
    private TextView feedBackUtteranceText;
    private AppCompatImageButton crossButton;
    private ImageButton feedbackPositiveButton;
    private ImageButton feedbackNegativeButton;

    public static FeedbackDialogFragment newInstance(FeedbackItem feedbackItem) {
        FeedbackDialogFragment myFragment = new FeedbackDialogFragment();
        Bundle args = new Bundle();
        args.putSerializable("feedback", feedbackItem);
        myFragment.setArguments(args);
        return myFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.full_screen_dialog);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_feedback_form, container,
                false);

        feedBackConditionView = view.findViewById(R.id.feedback_contion_view);
        feedBackUtteranceView = view.findViewById(R.id.feedback_utterance_view);
        feedBackUtteranceText = view.findViewById(R.id.feedback_recognized_utterance);

        if (getArguments() != null) {
            feedbackItem = (FeedbackItem) getArguments().getSerializable("feedback");
        }

        feedbackPositiveButton = view.findViewById(R.id.feedback_positive_button);
        feedbackNegativeButton = view.findViewById(R.id.feedback_negative_button);
        feedback = view.findViewById(R.id.feedback_text);
        Button sendButton = view.findViewById(R.id.send_button);
        sendButton.setOnClickListener(view1 -> sendTheFeedback());
        crossButton = view.findViewById(R.id.cross_button);

        feedbackPositiveButton.setOnClickListener(view13 -> {
            updateForPositiveFeedback();
        });

        feedbackNegativeButton.setOnClickListener(view12 -> {
            updateForNegativeFeedback();
        });

        updateUI(feedbackItem);

        return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        feedbackItem.feedbackComments = feedback.getText().toString();
        outState.putSerializable("feedback", feedbackItem);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null &&
                savedInstanceState.getSerializable("feedback") != null) {
            feedbackItem = (FeedbackItem) savedInstanceState.getSerializable("feedback");
            updateUI(feedbackItem);
        }
    }

    private void updateUI(FeedbackItem feedbackItem) {
        if (feedbackItem.isPositiveFeedback == null) {
            crossButton.setOnClickListener(view1 -> dismiss());
            feedBackConditionView.setVisibility(View.VISIBLE);
            feedBackUtteranceView.setVisibility(View.GONE);
        } else {
            feedBackConditionView.setVisibility(View.GONE);
            feedBackUtteranceView.setVisibility(View.VISIBLE);
        }
        updateForPositiveFeedback();
        if (feedbackItem.isPositiveFeedback != null) {
            if (!feedbackItem.isPositiveFeedback) {
                updateForNegativeFeedback();
            }
        }
        feedback.setText(feedbackItem.feedbackComments);
        String utteranceString = feedbackItem.utterance.replaceAll("::","\n");
        feedBackUtteranceText.setText(utteranceString);
    }

    private void updateForPositiveFeedback() {
        feedbackPositiveButton.setImageTintList(
                ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));
        feedbackNegativeButton.setImageTintList(
                ColorStateList.valueOf(Color.GRAY));
        feedbackItem.isPositiveFeedback = true;
    }

    private void updateForNegativeFeedback() {
        feedbackNegativeButton.setImageTintList(
                ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));
        feedbackPositiveButton.setImageTintList(
                ColorStateList.valueOf(Color.GRAY));
        feedbackItem.isPositiveFeedback = false;
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
    }

    private void sendTheFeedback() {
        feedbackItem.feedbackComments = feedback.getText().toString();
        appViewModel.sendAppFeedBack(feedbackItem);
        dismiss();
        Toast.makeText(getActivity(),
                "Awesome! thanks for letting us know.",
                Toast.LENGTH_SHORT).show();
    }
}
