package com.example.habitude.activities

import android.content.Intent
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.habitude.databinding.ActivityIntroBinding

class IntroActivity : BaseActivity() {

    private lateinit var binding: ActivityIntroBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIntroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val typeface: Typeface =
            Typeface.createFromAsset(assets, "carbon bl.ttf")
        binding.tvAppNameIntro.typeface = typeface

        binding.btnSignInIntro.setOnClickListener {
            startActivity(Intent(this@IntroActivity, SignInActivity::class.java))
        }

        binding.btnSignUpIntro.setOnClickListener {
            startActivity(Intent(this@IntroActivity, SignUpActivity::class.java))
        }
    }
}