package com.example.minidelivery_customer.order.korean

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.minidelivery_customer.R
import com.example.minidelivery_customer.databinding.ActivityKoreanDetailBinding
import com.example.minidelivery_customer.order.CartItem
import com.example.minidelivery_customer.order.MenuAdapter
import com.example.minidelivery_customer.order.MenuItem
import com.example.minidelivery_customer.payment.PaymentActivity

class KoreanDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityKoreanDetailBinding
    private val cartItems = mutableMapOf<MenuItem, Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityKoreanDetailBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_korean_detail)

        setupToolbar()
        setupRecyclerView()
        setupOrderButton()
        updateCartCount()
    }

    // Back Button : 이전화면으로 이동
    private fun setupToolbar() {
        binding.backButton.setOnClickListener {
            onBackPressed()
        }
    }

    private fun setupRecyclerView() {
        binding.menuRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@KoreanDetailActivity)
            adapter = MenuAdapter(getFakeMenuItems()) { menuItem ->
                addToCart(menuItem)
            }
        }
    }

    private fun addToCart(menuItem: MenuItem) {
        cartItems[menuItem] = cartItems.getOrDefault(menuItem, 0) + 1
        updateCartCount()
    }

    private fun updateCartCount() {
        val totalCount = cartItems.values.sum()
        binding.cartCount.text = totalCount.toString()
        binding.cartCount.visibility = if (totalCount > 0) View.VISIBLE else View.GONE
    }


    private fun setupOrderButton() {
        binding.orderButton.setOnClickListener {
            val intent = Intent(this, PaymentActivity::class.java)
            val cartItemList = cartItems.map { (item, quantity) ->
                CartItem(item.name, item.price, quantity, item.imageResId)
            }
            intent.putParcelableArrayListExtra("cartItems", ArrayList(cartItemList))
            startActivity(intent)
        }
    }

    private fun getFakeMenuItems(): List<MenuItem> {
        return listOf(
            MenuItem("korean", "4,500원", R.drawable.americano),
            MenuItem("korean", "5,500원", R.drawable.affogato),
            MenuItem("korean", "7,500원", R.drawable.coffe_sparkling),
            MenuItem("korean", "8,500원", R.drawable.latte_shakerato),
            MenuItem("korean", "7,000원", R.drawable.latte_lavender),
            MenuItem("korean", "6,700원", R.drawable.latte_glazed),
            MenuItem("korean", "7,200원", R.drawable.latte_french),
            MenuItem("korean", "6,800원", R.drawable.shake_passion),
            MenuItem("korean", "6,800원", R.drawable.shake_mango)
        )
    }
}