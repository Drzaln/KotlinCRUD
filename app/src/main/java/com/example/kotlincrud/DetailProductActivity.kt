package com.example.kotlincrud

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.example.kotlincrud.ext.toast
import com.example.kotlincrud.data.model.Product
import kotlinx.android.synthetic.main.activity_detail_product.*
import kotlin.random.Random

class DetailProductActivity : AppCompatActivity() {

    companion object {
        fun editIntent(context: Context, product: Product): Intent {
            return Intent(context, DetailProductActivity::class.java).apply {
                putExtra(EXTRA_PRODUCT, product)
                putExtra(EXTRA_EDIT, true)
            }
        }

        fun addIntent(context: Context): Intent {
            return Intent(context, DetailProductActivity::class.java)
        }

        const val REQUEST_CODE_DETAIL_PRODUCT: Int = 123
        const val RESULT_CODE_RELOAD_DATA: Int = 124
        const val EXTRA_PRODUCT = "extra_product"
        const val EXTRA_EDIT = "extra_edit"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_product)

        val isEdit = intent.getBooleanExtra(EXTRA_EDIT, false)
        val data = intent.getSerializableExtra(EXTRA_PRODUCT)

        val product = if (data == null) Product() else data as Product
        render(isEdit, product)
    }

    private fun render(isEdit: Boolean, product: Product) {
        if (isEdit) {
            showProductDetail(product)
        }

        btnDeleteProduct.visibility = if (isEdit) View.VISIBLE else View.GONE
        btnDeleteProduct.setOnClickListener {
            product.id?.let { id -> actionDeleteData(id) }
        }
        btnSaveProduct.setOnClickListener {
            if (isEdit) {
                actionUpdateData(product)
            } else {
                actionSaveData()
            }
        }
    }

    private fun actionSaveData() {
        val random = Random.nextInt(10, 100)
        val product = Product(
            etProductDetailName.text.toString(),
            etProductPriceDetail.text.toString().toInt(),
            if (etProductImageDetail.text.toString().isNotEmpty())
                etProductImageDetail.text.toString()
            else
                "https://loremflickr.com/100/100?lock=$random"
        )
        App.instance.repository.save(product, {
            "Data saved".toast(this@DetailProductActivity)
            setResult(RESULT_CODE_RELOAD_DATA)
            finish()
        }, {
            it.printStackTrace()
            it.message?.toast(this@DetailProductActivity)
        })
    }

    private fun actionUpdateData(product: Product) {
        product.apply {
            if (etProductDetailName.text.toString().isNotEmpty()) name =
                etProductDetailName.text.toString()
            if (etProductPriceDetail.text.toString().isNotEmpty()) price =
                etProductPriceDetail.text.toString().toInt()
            if (etProductImageDetail.text.toString().isNotEmpty()) {
                image = etProductImageDetail.text.toString()
            } else {
                val random = Random.nextInt(10, 100)
                image = "https://loremflickr.com/100/100?lock=$random"
            }

        }

        product.id?.let {
            App.instance.repository.update(it, product, {
                "data updated".toast(this@DetailProductActivity)
                setResult(RESULT_CODE_RELOAD_DATA)
                finish()
            }, {
                it.printStackTrace()
                it.message?.toast(this@DetailProductActivity)
            })
        }
    }

    private fun actionDeleteData(id: Int) {
        App.instance.repository.delete(id, {
            "data deleted".toast(this@DetailProductActivity)
            setResult(RESULT_CODE_RELOAD_DATA)
            finish()
        }, {
            it.printStackTrace()
            it.message?.toast(this@DetailProductActivity)
        })
    }

    private fun showProductDetail(product: Product) {
        with(product) {
            etProductDetailName.setText(name)
            etProductPriceDetail.setText(price.toString())
            etProductImageDetail.setText(image)
            Glide.with(this@DetailProductActivity).load(image).into(imgProductDetail)
        }
    }
}
