package com.example.kotlincrud.data.model.repository

import com.example.kotlincrud.data.model.Product
import com.example.kotlincrud.data.model.remote.BelanjaApi
import com.example.kotlincrud.data.model.remote.response.ProductResponse
import com.example.kotlincrud.data.model.remote.response.ProductsResponse
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductRepository(val api: BelanjaApi.Api) {
    fun get(onSuccess: (List<Product>) -> Unit, onError: (Throwable) -> Unit) {
        api.getProducts().enqueue(object : Callback<ProductsResponse> {
            override fun onFailure(call: Call<ProductsResponse>, t: Throwable) {
                onError(t)
            }

            override fun onResponse(
                call: Call<ProductsResponse>,
                response: Response<ProductsResponse>
            ) {
                if (response.isSuccessful) {
                    val result = response.body()?.data?.map {
                        with(it) {
                            Product(name, price, image, id)
                        }
                    }
                    result?.let {
                        onSuccess(it)
                    }
                } else {
                    onError(Throwable("Something wrong!"))
                }
            }
        })
    }

    fun save(product: Product, onSuccess: (Product) -> Unit, onError: (Throwable) -> Unit) {
        api.saveProduct(product).enqueue(object : Callback<ProductResponse> {
            override fun onResponse(
                call: Call<ProductResponse>,
                response: Response<ProductResponse>
            ) {
                if (response.isSuccessful) {
                    response.body()?.data?.let {
                        with(it) {
                            onSuccess(
                                Product(
                                    name,
                                    price,
                                    image,
                                    id
                                )
                            )
                        }
                    }
                } else {
                    onError(Throwable("Something Wrong!!"))
                }
            }

            override fun onFailure(call: Call<ProductResponse>, t: Throwable) {
                onError(t)
            }
        })
    }

    fun update(
        id: Int,
        product: Product,
        onSuccess: (Product) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        api.updateProduct(id, product).enqueue(object : Callback<ProductResponse> {
            override fun onFailure(call: Call<ProductResponse>, t: Throwable) {
                onError(t)
            }

            override fun onResponse(
                call: Call<ProductResponse>,
                response: Response<ProductResponse>
            ) {
                if (response.isSuccessful) {
                    response.body()?.data?.let {
                        with(it) {
                            onSuccess(
                                Product(
                                    name,
                                    price,
                                    image,
                                    id
                                )
                            )
                        }
                    }

                } else {
                    onError(Throwable("Something wrong!"))
                }
            }

        })
    }

    fun delete(id: Int, onSuccess: (Int) -> Unit, onError: (Throwable) -> Unit) {
        api.deleteProduct(id).enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    onSuccess(id)
                } else {
                    onError(Throwable("Something Wrong!!"))
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                onError(t)
            }

        })
    }
}