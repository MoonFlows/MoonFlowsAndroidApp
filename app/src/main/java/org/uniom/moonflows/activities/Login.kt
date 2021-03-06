package org.uniom.moonflows.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.login.*
import org.uniom.moonflows.App
import org.uniom.moonflows.R
import org.uniom.moonflows.ipfs.IPFSDaemon

class Login : AppCompatActivity() {
    private val ipfsDaemon = IPFSDaemon(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.component().inject(this)
        setContentView(R.layout.login)
        editText2.text.insert(0, ipfsDaemon.getConfig().readText().subSequence(0,200))
    }
}
