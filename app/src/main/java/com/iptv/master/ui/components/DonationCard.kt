package com.iptv.master.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Coffee
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.iptv.master.domain.model.DonationInfo

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DonationCard(
    donationInfo: DonationInfo,
    onBuyMeACoffee: () -> Unit,
    onPayPal: () -> Unit,
    onGitHub: () -> Unit,
    onBitcoin: () -> Unit,
    onEthereum: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        shape = MaterialTheme.shapes.large
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.Coffee,
                contentDescription = null,
                modifier = Modifier.size(48.dp),
                tint = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = donationInfo.message,
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(20.dp))

            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = onBuyMeACoffee,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFFFDD00),
                        contentColor = Color.Black
                    )
                ) {
                    Text("Buy Me a Coffee")
                }

                Button(
                    onClick = onPayPal,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF0070BA),
                        contentColor = Color.White
                    )
                ) {
                    Text("PayPal")
                }

                OutlinedButton(onClick = onGitHub) {
                    Text("GitHub")
                }

                if (donationInfo.bitcoin.isNotEmpty()) {
                    OutlinedButton(onClick = onBitcoin) {
                        Text("BTC")
                    }
                }

                if (donationInfo.ethereum.isNotEmpty()) {
                    OutlinedButton(onClick = onEthereum) {
                        Text("ETH")
                    }
                }
            }
        }
    }
}
