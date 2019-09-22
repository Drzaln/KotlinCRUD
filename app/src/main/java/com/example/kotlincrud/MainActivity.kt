package com.example.kotlincrud

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kotlincrud.adapter.ListProductAdapter
import com.example.kotlincrud.ext.toast
import com.example.kotlincrud.data.model.Product
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var list: ArrayList<Product> = arrayListOf()

    private  var listProductAdapter: ListProductAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        showListProduct()
        setupAddProduct()
//        setListClickAction()
    }

    private fun setListClickAction() {
        listProductAdapter?.setOnItemClickCalllback(
            object : ListProductAdapter.OnItemClickCallback {
                override fun onItemClick(data: Product) {
                    startActivityForResult(
                        DetailProductActivity.editIntent(this@MainActivity, data),
                        DetailProductActivity.REQUEST_CODE_DETAIL_PRODUCT
                    )
                }
            }
        )
    }

    private fun setupAddProduct() {
        btnAddProduct.setOnClickListener {
            startActivityForResult(
                DetailProductActivity.addIntent(this@MainActivity),
                DetailProductActivity.REQUEST_CODE_DETAIL_PRODUCT
            )
        }
    }

    private fun showListProduct() {
        App.instance.repository.get({
            list.addAll(it)

            listProductAdapter = ListProductAdapter(list)

            rvProducts.apply {
                layoutManager = LinearLayoutManager(this@MainActivity)
                adapter = listProductAdapter
                setHasFixedSize(true)
            }

            setListClickAction()
        }, {
            it.printStackTrace()
            it.message?.toast(this@MainActivity)
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == DetailProductActivity.REQUEST_CODE_DETAIL_PRODUCT && resultCode == DetailProductActivity.RESULT_CODE_RELOAD_DATA){
            list = arrayListOf()
            showListProduct()
        }
    }
}
