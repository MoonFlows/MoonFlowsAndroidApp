package org.uniom.moonflows.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import io.ipfs.kotlin.IPFS
import io.ipfs.kotlin.model.VersionInfo
import kotlinx.android.synthetic.main.init_ipfs.*
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
            /*serviceIntent.putExtra(
                    "Action",
                    ServiceAction.RUN_DAEMON
            )
            startService(serviceIntent)

            btStart.isEnabled = !State.isDaemonRunning*/


        }


        btStop.setOnClickListener {
            stopService(Intent(this, IPFSDaemonService::class.java))
        }
    }
}
