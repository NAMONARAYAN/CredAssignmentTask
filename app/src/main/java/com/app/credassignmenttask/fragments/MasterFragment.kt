package com.app.credassignmenttask.fragments

import android.text.TextUtils
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.app.credassignmenttask.R

open class MasterFragment : DialogFragment() {

    var fragTag: String? = null

    fun onPopBackStack(): Boolean {
        return false
    }

    companion object {
        var NO_ANIMATION = -1
        fun addToBackStack(
            manager: FragmentManager?,
            fragment: MasterFragment?,
            defaultAnimation: Boolean
        ) {
            if (defaultAnimation) {
                addToBackStack(manager, fragment, R.id.fragment_base_container)
            } else {
                replace(manager, R.id.fragment_base_container, fragment, true)
            }
        }

        fun addToBackStack(manager: FragmentManager?, fragment: MasterFragment?, target: Int) {
            replace(manager, target, fragment, true)
        }

        fun replace(
            fragmentManager: FragmentManager?,
            targetId: Int,
            fragment: MasterFragment?,
            isAddToBackStack: Boolean
        ) {
            val transaction = fragmentManager
                ?.beginTransaction()
            transaction?.setTransition(FragmentTransaction.TRANSIT_NONE)

            val currentFragment = fragmentManager
                ?.findFragmentById(R.id.fragment_base_container)
            var tag = fragment?.fragTag
            if (TextUtils.isEmpty(tag)) {
                tag = fragment?.tag
                if (TextUtils.isEmpty(tag)) {
                    tag = fragment?.javaClass?.name
                }
            }

            try {
                if (fragment != null) {
                    transaction?.replace(targetId, fragment, tag)
                }
                if (isAddToBackStack) {
                    transaction?.addToBackStack(tag)
                } else {
                    if (currentFragment != null) {
                        transaction?.remove(currentFragment)
                    }
                }
                transaction?.commit()
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }

    open fun popBackStack(manager: FragmentManager?) {
        if (manager != null) {
            try {
                manager.popBackStack()
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        }
    }

    open fun addToBackStack(manager: FragmentManager?,fragment: MasterFragment?) {
        addToBackStack(manager, fragment, R.id.fragment_base_container)
    }

    open fun addFragment(manager: FragmentManager?, mfragment: Fragment) {
        manager?.beginTransaction()
            ?.add(R.id.fragment_base_container, mfragment)?.addToBackStack(null)
            ?.commit()
    }
}