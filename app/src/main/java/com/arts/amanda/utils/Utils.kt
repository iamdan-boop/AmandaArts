package com.arts.amanda.utils


import androidx.fragment.app.Fragment
import com.arts.amanda.R
import com.google.android.material.snackbar.Snackbar


fun Fragment.snack(data: String) {
    view?.rootView?.let {
        Snackbar.make(it, data, Snackbar.LENGTH_SHORT).setAnchorView(R.id.bottomNavigationView).show()
    }
}
