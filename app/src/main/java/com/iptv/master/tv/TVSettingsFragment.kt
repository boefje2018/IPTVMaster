package com.iptv.master.tv

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.leanback.preference.LeanbackPreferenceFragmentCompat
import androidx.preference.Preference
import androidx.preference.PreferenceScreen
import com.iptv.master.R

class TVSettingsFragment : LeanbackPreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.tv_settings, rootKey)

        findPreference<Preference>("theme_mode")?.setOnPreferenceChangeListener { _, newValue ->
            true
        }

        findPreference<Preference>("parental_controls")?.setOnPreferenceClickListener {
            parentFragmentManager.beginTransaction()
                .replace(android.R.id.content, TVSettingsFragment())
                .addToBackStack(null)
                .commit()
            true
        }

        findPreference<Preference>("manage_playlists")?.setOnPreferenceClickListener {
            true
        }

        findPreference<Preference>("clear_history")?.setOnPreferenceClickListener {
            true
        }

        findPreference<Preference>("about")?.setOnPreferenceClickListener {
            parentFragmentManager.beginTransaction()
                .replace(android.R.id.content, TVSettingsFragment())
                .addToBackStack(null)
                .commit()
            true
        }

        findPreference<Preference>("privacy_policy")?.setOnPreferenceClickListener {
            val url = "https://iptvmaster.app/privacy"
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
            true
        }

        findPreference<Preference>("open_source_licenses")?.setOnPreferenceClickListener {
            true
        }
    }
}
