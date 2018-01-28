package com.github.khangnt.mcp.ui.jobmanager

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.khangnt.mcp.R
import com.github.khangnt.mcp.ui.common.*
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import java.util.concurrent.TimeUnit

/**
 * Created by Khang NT on 1/5/18.
 * Email: khang.neon.1997@gmail.com
 */

class RunningHeaderModel(
        header: String,
        val liveLogObservable: Observable<String>,
        val compositeDisposable: CompositeDisposable,
        idGenerator: IdGenerator
) : HeaderModel(header, idGenerator)

@SuppressLint("SetTextI18n")
class ItemHeaderRunningViewHolder(itemView: View) : ItemHeaderViewHolder<RunningHeaderModel>(itemView) {
    private var disposable: Disposable? = null
    private var speedSuffix = ""
    private var header = ""

    override fun bind(model: RunningHeaderModel, pos: Int) {
        header = model.header

        disposable?.dispose()
        disposable = model.liveLogObservable
                .throttleLast(500, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    speedSuffix = it
                    tvHeader.text = "$header $speedSuffix"
                }, {
                    speedSuffix = ""
                    tvHeader.text = header
                })
        model.compositeDisposable.add(disposable!!)

        tvHeader.text = "$header $speedSuffix"
    }

    object Factory : ViewHolderFactory {
        override fun invoke(inflater: LayoutInflater, parent: ViewGroup): CustomViewHolder<*> =
                ItemHeaderRunningViewHolder(inflater.inflate(R.layout.item_header, parent, false))
    }
}