package org.gabrieal.simplefirebaseauth

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseApp

open class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        onBindData()
        setupUI()
        setupViewModel()
        observeResponses()
    }

    open fun setupUI() {}
    open fun setupViewModel() {}
    open fun onBindData() {}
    open fun observeResponses() {}
}
