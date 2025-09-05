package com.aod.aivoicechat.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import com.aod.aivoicechat.activities.MainActivity

abstract class BaseFragment<Binding : ViewDataBinding>(@LayoutRes private val layoutResId: Int) :
    Fragment() {
    lateinit var binding: Binding
    lateinit var mActivity: MainActivity

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mActivity = (requireActivity() as MainActivity)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return DataBindingUtil.inflate<Binding>(inflater, layoutResId, container, false)
            .apply { lifecycleOwner = viewLifecycleOwner }
            .also { binding = it }
            .root
    }

    protected fun NavController.goToFragment(
        fragmentId: Int,
        popUp: Boolean = false,
        popUpInclusive: Boolean = false
    ) {
        val navOptionsBuilder = NavOptions.Builder()
        if (popUp) navOptionsBuilder.setPopUpTo(fragmentId, popUpInclusive)
        val navOptions = navOptionsBuilder.build()

        this.navigate(fragmentId, null, navOptions)
    }

    protected fun NavController.popBack(@IdRes destinationId: Int, inclusive: Boolean = false) {
        this.popBackStack(destinationId = destinationId, inclusive = inclusive)
    }
}