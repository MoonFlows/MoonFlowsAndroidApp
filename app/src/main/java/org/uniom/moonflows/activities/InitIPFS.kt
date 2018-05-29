package org.uniom.moonflows.activities

import android.app.ProgressDialog
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import io.ipfs.kotlin.IPFS
import io.ipfs.kotlin.model.VersionInfo
import kotlinx.android.synthetic.main.init_ipfs.*
import org.ligi.kaxt.startActivityFromClass
import org.uniom.moonflows.App
import org.uniom.moonflows.R
import org.uniom.moonflows.ipfs.State
import org.uniom.moonflows.ipfs.IPFSDaemon
import org.uniom.moonflows.ipfs.IPFSDaemonService
import javax.inject.Inject

class InitIPFS : AppCompatActivity() {
    private val ipfsDaemon = IPFSDaemon(this)

    @Inject
    lateinit var ipfs: IPFS

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        App.component().inject(this)
        setContentView(R.layout.init_ipfs)


        val currentVersionText = ipfsDaemon.getVersionFile().let {
            if (it.exists()) it.readText() else ""
        }
        val availableVersionText = assets.open("version").reader().readText()
        if (!State.isDaemonRunning && !ipfsDaemon.isReady() && (currentVersionText.isEmpty() || currentVersionText != availableVersionText)){
            ipfsDaemon.download(this, runInit = true) {
                ipfsDaemon.getVersionFile().writeText(assets.open("version").reader().readText())
            }
        }


        btStart.setOnClickListener{
            var serviceIntent = Intent(this, IPFSDaemonService::class.java);
            startService(serviceIntent)

            btStart.isEnabled = false

            val progressDialog = ProgressDialog(this)
            progressDialog.setMessage("starting daemon")
            progressDialog.show()


            Thread(Runnable {
                var version: VersionInfo? = null
                while (version == null) {
                    try {
                        version = ipfs.info.version()
                        version?.let { ipfsDaemon.getVersionFile().writeText(it.Version) }
                    } catch (ignored: Exception) {
                    }
                }

                runOnUiThread {
                    progressDialog.dismiss()
                    startActivityFromClass(Login::class.java)
                }
            }).start()

        }


        btStop.setOnClickListener {
            btStart.isEnabled = true
            stopService(Intent(this, IPFSDaemonService::class.java))
        }
    }
}
