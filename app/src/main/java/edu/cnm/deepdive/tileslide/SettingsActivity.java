//package edu.cnm.deepdive.tileslide;
//
//import android.os.Bundle;
//import android.preference.ListPreference;
//import android.preference.PreferenceActivity;
//import android.preference.PreferenceFragment;
//import android.support.annotation.Nullable;
//
//public class SettingsActivity extends PreferenceActivity {
//
//  @Override
//  public void onCreate(@Nullable Bundle savedInstanceState) {
//    super.onCreate(savedInstanceState);
//
//    getFragmentManager().beginTransaction().replace(android.R.id.content,
//        new MainSettingsFragment()).commit();
//  }
//  public static class MainSettingsFragment extends PreferenceFragment {
//
//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//      super.onCreate(savedInstanceState);
//      addPreferencesFromResource(R.xml.setting_screen);
//    }
//  }
//}
