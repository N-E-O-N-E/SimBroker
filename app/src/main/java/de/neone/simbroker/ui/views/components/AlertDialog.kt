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

/**
 * Einfache AlertDialog-Variante mit einer Nachricht und einer „Okay“-Schaltfläche.
 *
 * - Zeigt die übergebene Nachricht an.
 * - Ruft onDismiss() auf, wenn der Dialog geschlossen oder die Schaltfläche gedrückt wird.
 *
 * @param message Der anzuzeigende Text im Dialog.
 * @param onDismiss Callback, der beim Schließen des Dialogs aufgerufen wird.
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlertDialog(
    message: String,
    onDismiss: () -> Unit,
) {
    //------------------------------------------------------------------------------------------
    // 1) Dialog-Container
    //------------------------------------------------------------------------------------------
    BasicAlertDialog(
        onDismissRequest = {
            onDismiss()
        }
    ) {
        //--------------------------------------------------------------------------------------
        // 2) Surface mit TonalElevation und Form
        //--------------------------------------------------------------------------------------
        Surface(
            modifier = Modifier
                .wrapContentWidth()
                .wrapContentHeight(),
            shape = MaterialTheme.shapes.large,
            tonalElevation = AlertDialogDefaults.TonalElevation
        ) {
            //----------------------------------------------------------------------------------
            // 3) Inhalts-Layout
            //----------------------------------------------------------------------------------
            Column(modifier = Modifier.padding(20.dp)) {
                //------------------------------------------------------------------------------
                // 3.1) Nachrichtentext
                //------------------------------------------------------------------------------
                Text(
                    text = message,
                    style = MaterialTheme.typography.bodyLarge
                )

                //------------------------------------------------------------------------------
                // 3.2) Abstand
                //------------------------------------------------------------------------------
                Spacer(modifier = Modifier.height(24.dp))

                //------------------------------------------------------------------------------
                // 3.3) Bestätigungs-Schaltfläche
                //------------------------------------------------------------------------------
                TextButton(
                    onClick = {
                        onDismiss()
                    },
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text(
                        text = "Okay",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    }
}