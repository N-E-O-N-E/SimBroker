package de.neone.simbroker.ui.views.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlertDialogEraseAll(
    message: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {

    BasicAlertDialog(
        onDismissRequest = {
            onDismiss()
        }) {
        Surface(
            modifier = Modifier
                .wrapContentWidth()
                .wrapContentHeight(),
            shape = MaterialTheme.shapes.large,
            tonalElevation = AlertDialogDefaults.TonalElevation
        ) {
            Column(modifier = Modifier.padding(20.dp)) {

                Text(text = message, style = MaterialTheme.typography.bodyLarge)

                Spacer(modifier = Modifier.height(24.dp))

                TextButton(
                    onClick = {
                        onConfirm()
                        onDismiss()
                    }, modifier = Modifier.align(Alignment.End)
                ) { Text("Reset now!", style = MaterialTheme.typography.bodyLarge) }
            }
        }
    }
}