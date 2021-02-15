package network

import data.Item
import retrofit2.Call
import retrofit2.http.*

interface RetrofitClient {
    @GET("/rules")
    fun getAllItems(): Call<List<Item>>

    @GET("/level/{lvl}")
    fun getAllByLevel(@Path("lvl") lvl: Int): Call<List<Item>>

    @GET("/rule/{id}")
    fun getItemById(@Path("id") id: Int): Call<Item>

    @POST("/rule")
    fun saveItem(@Body item: Item): Call<Item>

    @POST("/update")
    fun updateItem(@Body item: Item): Call<Item>

    @DELETE("/rule/{id}")
    fun deleteItem(@Path("id") id: Int)
}