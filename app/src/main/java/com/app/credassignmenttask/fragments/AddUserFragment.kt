package com.app.credassignmenttask.fragments

import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.app.credassignmenttask.R
import com.app.credassignmenttask.model.UserData
import com.app.credassignmenttask.viewmodels.HomeScreenViewModel
import kotlinx.android.synthetic.main.frag_add_user_screen.*

class AddUserFragment : MasterFragment(), View.OnClickListener {

    var id: Long? = 0

    companion object {
        fun getNewInstance(): AddUserFragment {
            val fragment = AddUserFragment()
            fragment.arguments = Bundle()
            return fragment
        }
    }

    var mMainActivityViewModel: HomeScreenViewModel? = null

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.frag_add_user_screen, container, false)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        /* Intialize View model */
        activity?.let {
            mMainActivityViewModel = ViewModelProviders.of(it).get(HomeScreenViewModel::class.java)
        }


        mMainActivityViewModel?.getAddUserProgressStatus()?.observe(this, Observer {
            it.let {
                if (it == true) {
                    showLoader()
                } else {
                    hideLoader()
                }
            }
        })

        mMainActivityViewModel?.userAddingStatus?.observe(this, Observer {
            it?.let {
                if(it.equals("User Added Sucessfully")){
                    mMainActivityViewModel?.selectdata?.postValue(null)
                    mMainActivityViewModel?.userAddingStatus?.postValue("")
                    Toast.makeText(activity,"Profile Added Successfully!!!", Toast.LENGTH_LONG).show()
                    Handler().postDelayed({
                        popBackStack(activity!!.supportFragmentManager)
                    },2000)
                }else if(it.equals("Error while Adding User")){
                    Toast.makeText(activity,it, Toast.LENGTH_LONG).show()

                }else{

                }
            }
        })

        mMainActivityViewModel?.selectdata?.observe(this, androidx.lifecycle.Observer {
            it?.let {
                if (it != null) {
                    val emailEditable = Editable.Factory.getInstance().newEditable(it?.email)
                    edt_email.text = emailEditable
                    val fnameEdiatble = Editable.Factory.getInstance().newEditable(it?.fname)
                    edt_first_name.text = fnameEdiatble
                    val lnameEdiatble = Editable.Factory.getInstance().newEditable(it?.lname)
                    edt_last_name.text = lnameEdiatble
                    val phoneEdiatble = Editable.Factory.getInstance().newEditable(it?.phone)
                    edt_phone_number.text = phoneEdiatble
                    val descriptionEdiatble =
                        Editable.Factory.getInstance().newEditable(it?.description)
                    edt_description.text = descriptionEdiatble
                    id = it?.id
                }
            }
        })
        edt_email?.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(
                searchString: CharSequence,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(
                searchString: CharSequence,
                start: Int,
                before: Int,
                count: Int
            ) {
            }

            override fun afterTextChanged(searchString: Editable) {
                validateEmail(edt_email)
            }


        })
        edt_first_name?.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(
                searchString: CharSequence,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(
                searchString: CharSequence,
                start: Int,
                before: Int,
                count: Int
            ) {
            }

            override fun afterTextChanged(searchString: Editable) {
                validate(edt_first_name)
            }


        })
        edt_last_name?.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(
                searchString: CharSequence,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(
                searchString: CharSequence,
                start: Int,
                before: Int,
                count: Int
            ) {
            }

            override fun afterTextChanged(searchString: Editable) {
                validate(edt_last_name)
            }


        })
        edt_phone_number?.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(
                searchString: CharSequence,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(
                searchString: CharSequence,
                start: Int,
                before: Int,
                count: Int
            ) {
            }

            override fun afterTextChanged(searchString: Editable) {
                validate(edt_phone_number)
            }


        })
        edt_description?.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(
                searchString: CharSequence,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(
                searchString: CharSequence,
                start: Int,
                before: Int,
                count: Int
            ) {
            }

            override fun afterTextChanged(searchString: Editable) {
                validate(edt_description)
            }


        })
        submit_linear.setOnClickListener(this)
        iv_back.setOnClickListener(this)
    }


    private fun saveCurrentUser(userData: UserData) {
        showLoader()
        mMainActivityViewModel?.uploadUserData(userData)
    }

    fun isEmailValid(email: CharSequence?): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun validateEmail(view: View): Boolean {
        val emailAddress = (view as EditText).text.toString().trim { it <= ' ' }
        if (TextUtils.isEmpty(emailAddress)) {
            (view as EditText).error = "Email cannot be blank"
            return true
        } else {
            if (!isEmailValid(emailAddress)) {
                (view as EditText).error = "Please provide valid Email Id!"
                return false
            }
        }
        return true
    }

    fun validate(view: View): Boolean {
        val textInput = (view as EditText).text.toString().trim { it <= ' ' }
        if (TextUtils.isEmpty(textInput)) {
            (view as EditText).error = "This Field cannot be blank"
            return false
        }
        return true
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.iv_back -> {
                popBackStack(activity!!.supportFragmentManager)
            }

            R.id.submit_linear -> {
                if (validate(edt_first_name) && validate(edt_last_name) && validate(edt_phone_number) && validate(
                        edt_description
                    ) && validateEmail(edt_email)
                ) {
                    if (id?.toInt() == 0) {
                        saveCurrentUser(
                            UserData((0 until 1000000).random().toLong(),
                                edt_first_name.text.toString(),
                                edt_last_name.text.toString(),
                                edt_email.text.toString(),
                                edt_phone_number.text.toString(),
                                edt_description.text.toString()
                            )
                        )

                    }
                    else{
                        saveCurrentUser(
                            UserData(id,
                                edt_first_name.text.toString(),
                                edt_last_name.text.toString(),
                                edt_email.text.toString(),
                                edt_phone_number.text.toString(),
                                edt_description.text.toString()
                            )
                        )
                    }
                }

            }

        }
    }


    protected fun showLoader() {
        if (progressBar != null) {
            progressBar.visibility = View.VISIBLE


        }

    }

    protected fun hideLoader() {
        if (progressBar != null) {
            progressBar.visibility = View.INVISIBLE
        }
    }
}