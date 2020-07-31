package ru.skillbranch.devintensive.ui.profile

import android.graphics.*

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.activity_profile.*
import ru.skillbranch.devintensive.R
import ru.skillbranch.devintensive.models.Profile
import ru.skillbranch.devintensive.ui.custom.TextBitmapBuilder
import ru.skillbranch.devintensive.utils.Utils
import ru.skillbranch.devintensive.viewmodels.ProfileViewModel

class ProfileActivity : AppCompatActivity() {
    private val TAG = "M_ProfileActivity"

    companion object {
        const val IS_EDIT_MODE = "IS_EDIT_MODE"
    }

    private lateinit var viewModel: ProfileViewModel
    private var isEditMode = false
    private lateinit var viewFields: Map<String, TextView>
    private var userInitials: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        initViews(savedInstanceState)
        initViewModel()
        Log.d(TAG, "onCreate")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(IS_EDIT_MODE, isEditMode)
    }

    private fun initViewModel() {
        viewModel = ViewModelProviders.of(this).get(ProfileViewModel::class.java)
        viewModel.getProfileData().observe(this, androidx.lifecycle.Observer { updateUI(it) })
        viewModel.getTheme().observe(this, androidx.lifecycle.Observer { updateTheme(it) })
    }

    private fun updateTheme(mode: Int) {
        Log.d(TAG, "updateTheme")
        delegate.localNightMode = mode
    }

    private fun updateUI(profile: Profile) {
        profile.toMap().also {
            for ((k, v) in viewFields) {
                v.text = it[k].toString()
            }
        }

        updateAvatar(profile)
    }

    private fun initViews(savedInstanceState: Bundle?) {
        viewFields = mapOf(
            "nickname" to tv_nick_name,
            "rank" to tv_rank,
            "rating" to tv_rating,
            "respect" to tv_respect,
            "firstName" to et_first_name,
            "lastName" to et_last_name,
            "about" to et_about,
            "repository" to et_repository
        )

        isEditMode = savedInstanceState?.getBoolean(IS_EDIT_MODE, false) ?: false
        showCurrentMode(isEditMode)

        btn_edit.setOnClickListener {
            if (isEditMode) saveProfileInfo()
            isValid()
            isEditMode = !isEditMode
            showCurrentMode(isEditMode)
        }

        btn_switch_theme.setOnClickListener {
            viewModel.switchTheme()
        }
    }

    private fun showCurrentMode(isEdit: Boolean) {
        val info = viewFields.filter {
            setOf("firstName", "lastName", "about", "repository").contains(it.key)
        }

        for ((_, v) in info) {
            v as EditText
            v.isFocusable = isEdit
            v.isFocusableInTouchMode = isEdit
            v.isEnabled = isEdit
            v.background.alpha = if (isEdit) 255 else 0
        }

        ic_eye.visibility = if (isEdit) View.GONE else View.VISIBLE
        wr_about.isCounterEnabled = isEdit

        with(btn_edit) {
            val filter: ColorFilter? = if (isEdit) {
                PorterDuffColorFilter(
                    resources.getColor(
                        if (delegate.localNightMode == AppCompatDelegate.MODE_NIGHT_NO)
                        R.color.color_accent else R.color.color_accent_night, theme),
                        PorterDuff.Mode.SRC_IN)
            } else {
                null
            }

            val icon = if (isEdit) {
                resources.getDrawable(R.drawable.ic_save_black_24dp, theme)
            } else {
                resources.getDrawable(R.drawable.ic_edit_black_24dp, theme)
            }

            background.colorFilter = filter
            setImageDrawable(icon)
        }
    }

    private fun saveProfileInfo() {
        Profile(
            firstName = et_first_name.text.toString(),
            lastName = et_last_name.text.toString(),
            about = et_about.text.toString(),
            repository = if (isValid()) et_repository.text.toString() else ""

        ).apply {
            viewModel.saveProfileData(this)
        }
    }

    private fun isValid(): Boolean {
        et_repository.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val content = et_repository.text?.toString()
                if (!content.isNullOrEmpty()) {
                    val pathCount = content.split(Regex("/[^/]"))
                    wr_repository.error = when {
                        content.contains(Regex("""https://github\.com/*""")) &&
                                pathCount.size == 3 -> validationExceptions(content)
                        content.contains(Regex("""https://www\.github\.com/*""")) &&
                                pathCount.size == 3 -> validationExceptions(content)
                        content.contains(Regex("""www.github\.com/*""")) &&
                                pathCount.size == 2 -> validationExceptions(content)
                        content.contains(Regex("""github\.com/*""")) &&
                                pathCount.size == 2 -> validationExceptions(content)
                        else -> "Невалидный адрес репозитория"
                    }
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        return wr_repository.error == null
    }

    private fun validationExceptions(content: String): String? {
        val exceptions = listOf(
            "enterprise",
            "features",
            "topics",
            "collections",
            "trending",
            "events",
            "marketplace",
            "pricing",
            "nonprofit",
            "customer-stories",
            "security",
            "login",
            "join"
        )

        exceptions.forEach{
            if (content.contains(it)) {
                return "Невалидный адрес репозитория"
            }
        }

        return null
    }

    private fun updateAvatar(profile: Profile){
        Utils.toInitials(profile.firstName, profile.lastName)?.let {
            if (it != userInitials) {
                val avatar = getAvatarBitmap(it)
                iv_avatar.setImageBitmap(avatar)
            }
        } ?: iv_avatar.setImageResource(R.drawable.avatar_default)
    }

    private fun getAvatarBitmap(text: String): Bitmap {
        val color = TypedValue()
        theme.resolveAttribute(R.attr.colorAccent, color, true)

        return TextBitmapBuilder(iv_avatar.layoutParams.width, iv_avatar.layoutParams.height)
            .setBackgroundColor(color.data)
            .setText(text)
            .setTextSize(Utils.convertSpToPx(this, 48))
            .setTextColor(Color.WHITE)
            .build()
    }
}