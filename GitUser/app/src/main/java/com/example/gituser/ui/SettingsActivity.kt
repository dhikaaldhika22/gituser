package com.example.gituser.ui

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.CompoundButton
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.gituser.R
import com.example.gituser.databinding.ActivitySettingsBinding
import com.example.gituser.viewmodel.Factory
import com.example.gituser.viewmodel.SettingsViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.collect

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
class SettingsActivity : AppCompatActivity(), CompoundButton.OnCheckedChangeListener {

    private var _binding: ActivitySettingsBinding? = null
    private val binding get() = _binding!!

    private val settingViewModel: SettingsViewModel by viewModels {
        Factory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setToolbar(getString(R.string.settings))

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    settingViewModel.getTheme.collect { state ->
                        if (state) AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                        else AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

                        binding.themeDark.isChecked = state
                    }
                }
            }
        }
        binding.themeDark.setOnCheckedChangeListener(this)
    }

    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
        when (buttonView?.id) {
            R.id.theme_dark -> settingViewModel.saveTheme(isChecked)
        }
    }

    private fun setToolbar(title: String) {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            setDisplayShowHomeEnabled(true)
            setDisplayHomeAsUpEnabled(true)
            this.title = title
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onDestroy() {
        _binding = null

        super.onDestroy()
    }
}