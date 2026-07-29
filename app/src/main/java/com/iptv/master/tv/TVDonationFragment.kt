package com.iptv.master.tv

import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.iptv.master.R
import com.iptv.master.domain.model.DonationInfo
import com.iptv.master.util.QRCodeGenerator
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class TVDonationFragment : Fragment() {

    @Inject
    lateinit var donationInfo: DonationInfo

    private var qrBitcoin: ImageView? = null
    private var qrEthereum: ImageView? = null
    private var messageText: TextView? = null
    private var btnBuyMeACoffee: Button? = null
    private var btnPayPal: Button? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_donation_tv, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        qrBitcoin = view.findViewById(R.id.qr_bitcoin)
        qrEthereum = view.findViewById(R.id.qr_ethereum)
        messageText = view.findViewById(R.id.donation_message)
        btnBuyMeACoffee = view.findViewById(R.id.btn_buy_me_a_coffee)
        btnPayPal = view.findViewById(R.id.btn_paypal)

        messageText?.text = donationInfo.message

        if (donationInfo.bitcoin.isNotBlank()) {
            qrBitcoin?.setImageBitmap(QRCodeGenerator.generate(donationInfo.bitcoin, 256))
        }

        if (donationInfo.ethereum.isNotBlank()) {
            qrEthereum?.setImageBitmap(QRCodeGenerator.generate(donationInfo.ethereum, 256))
        }

        btnBuyMeACoffee?.setOnClickListener {
            val intent = android.content.Intent(android.content.Intent.ACTION_VIEW)
            intent.data = android.net.Uri.parse(donationInfo.buyMeACoffee)
            startActivity(intent)
        }

        btnPayPal?.setOnClickListener {
            val intent = android.content.Intent(android.content.Intent.ACTION_VIEW)
            intent.data = android.net.Uri.parse(donationInfo.paypal)
            startActivity(intent)
        }
    }
}
