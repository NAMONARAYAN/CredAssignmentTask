package com.app.credassignmenttask.activity

import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import com.app.credassignmenttask.R
import com.app.credassignmenttask.fragments.HomeFragment
import com.app.credassignmenttask.fragments.MasterFragment

class MainActivity : CredBaseActivity() {

    private var shouldFinish = false
    private val mHandler: Handler? = null
    private val cancelFinish = Runnable { shouldFinish = false }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val homePageMasterFragment: HomeFragment =
            HomeFragment.getNewInstance()
        MasterFragment.addToBackStack(getFragmentsManager(), homePageMasterFragment, false)
    }

    override fun onBackPressed() {
        //super.onBackPressed();
        if (!onPopBackStack()) {
            if (shouldFinish) {
                mHandler?.removeCallbacks(cancelFinish)
                finish()
            } else {
                shouldFinish = true
                mHandler?.postDelayed(cancelFinish, 1000)
                Toast.makeText(this, "Press again to exit.", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    protected fun onPopBackStack(): Boolean {
        val fragmentManager = supportFragmentManager
        if (fragmentManager.executePendingTransactions()) {
            return true
        }
        val fragment: MasterFragment? = fragmentManager.findFragmentById(R.id.fragment_base_container) as MasterFragment?
        val isBackHandledByFragment = (fragment != null && fragment.onPopBackStack())
        if (fragmentManager.backStackEntryCount > 1&& !isBackHandledByFragment) {
            fragmentManager.popBackStack()
            if (fragment != null) {
                fragmentManager.beginTransaction().remove(fragment).commit()
            }
            return true
        }
        return isBackHandledByFragment
    }
}