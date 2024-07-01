package in.slanglabs.convaai.demo.grocery.UI.Activities;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.preference.PreferenceManager;

import java.util.HashMap;
import java.util.Map;

import in.slanglabs.convaai.demo.grocery.R;
import in.slanglabs.convaai.demo.grocery.UI.Fragments.SettingsFragment;

public class SettingsActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        getSupportActionBar().setTitle("Settings");
        if (findViewById(R.id.idFrameLayout) != null) {
            if (savedInstanceState != null) {
                return;
            }
            getFragmentManager().beginTransaction().add(R.id.idFrameLayout, new SettingsFragment()).commit();
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(this);
        Map<String,Object> configOverrides = new HashMap<>();
        boolean bargeIn = sharedPreferences.getBoolean("barge_in",false);
        boolean delayedTTS = sharedPreferences.getBoolean("delayed_tts",false);
        boolean confirmationForNonVoiceUJ = sharedPreferences.getBoolean("confirmation_for_non_voice_uj",false);
        boolean enableFusedASRNLP = sharedPreferences.getBoolean("enable_fused_asr_nlp", false);
        boolean enableLocaleDetection = sharedPreferences.getBoolean("enable_lang_auto_detect", false);
        boolean localEntities = sharedPreferences.getBoolean("local_entities",false);
        int surfaceStyles = Integer.parseInt(sharedPreferences.getString("surface_styles","4"));
        int uiHints = Integer.parseInt(sharedPreferences.getString("ui_hints","1"));
        configOverrides.put("internal.subsystems.asr.enable_barge_in", bargeIn);
        configOverrides.put("internal.subsystems.platform.greeting_delayed_spoken_prompt", delayedTTS);
        configOverrides.put("assistant.non_voice_completion", confirmationForNonVoiceUJ);
        configOverrides.put("internal.subsystems.platform.entity_reverse_translation_enabled", localEntities);
        configOverrides.put("internal.subsystems.ui.surface_view_style_parameter", surfaceStyles);
        configOverrides.put("internal.subsystems.ui.surface_ui_hint_style", uiHints);
        configOverrides.put("internal.subsystems.init.enable_fused_asr_nlp", enableFusedASRNLP);
        if (enableLocaleDetection) {
            configOverrides.put("internal.subsystems.asr.enable_langid", true);
            configOverrides.put("internal.subsystems.asr.langid_mode", 1);
        }
        // TODO Implement Reinitialize mechanism
    }
}

