package com.m.mvvmkotlin

import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.m.annotation.BindViewModel
import com.m.annotation.InjectViewModel
import com.m.mvvmkotlin.bind.ViewModelFactory
import com.m.mvvmkotlin.databinding.ActivityMainBinding
import com.m.mvvmkotlin.sample.BaseActivity
import com.m.mvvmkotlin.sample.DemoViewModel
import com.m.mvvmkotlin.sample.DemoViewModel2
import kotlinx.android.synthetic.main.activity_main.*

/**
 * [InjectViewModel] 注入ViewModel到activity
 * [BindViewModel] 绑定ViewModel 获取实例
 * [BaseActivity] 基类名称命名 用于创建 [ViewModelFactory] 注入model 时判断
 *[BaseFragment]
 */
@InjectViewModel
class MainActivity : BaseActivity(), View.OnClickListener {

    @BindViewModel
    lateinit var demoViewModel: DemoViewModel

    //绑定多个model
    @BindViewModel
    lateinit var demoViewModel2: DemoViewModel2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        binding.onClick = this
        binding.viewModel = demoViewModel
        binding.viewModel2 = demoViewModel2
    }

    override fun initView(savedInstanceState: Bundle?) {

    }

    override fun initData() {

    }

    override fun onClick(view: View) {
        demoViewModel.get(edt.text.toString())
    }
}
