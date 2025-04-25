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
 * Zeigt einen Bestätigungsdialog zum vollständigen Zurücksetzen aller Daten an.
 *
 * - Präsentiert eine Nachricht und bietet eine Schaltfläche zum Bestätigen.
 * - Ruft onConfirm() und onDismiss() in der richtigen Reihenfolge auf, wenn bestätigt.
 * - Ruft onDismiss() auf, wenn der Dialog anderweitig geschlossen wird.
 *
 * @param message Text, der im Dialog angezeigt wird.
 * @param onConfirm Callback-Funktion, die bei Bestätigung ausgeführt wird.
 * @param onDismiss Callback-Funktion, die beim Schließen des Dialogs ausgeführt wird.
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlertDialogEraseAll(
    message: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    //------------------------------------------------------------------------------------------
    // 1) Dialog-Anforderung und Dismiss-Handling
    //------------------------------------------------------------------------------------------
    BasicAlertDialog(
        onDismissRequest = {
            onDismiss()
        }
    ) {
        //--------------------------------------------------------------------------------------
        // 2) Dialog-Oberfläche
        //--------------------------------------------------------------------------------------
        Surface(
            modifier = Modifier
                .wrapContentWidth()
                .wrapContentHeight(),
            shape = MaterialTheme.shapes.large,
            tonalElevation = AlertDialogDefaults.TonalElevation
        ) {
            //----------------------------------------------------------------------------------
            // 3) Inhaltsbereich mit Nachricht und Abstand
            //----------------------------------------------------------------------------------
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    text = message,
                    style = MaterialTheme.typography.bodyLarge
                )

                Spacer(modifier = Modifier.height(24.dp))

                //------------------------------------------------------------------------------
                // 4) Bestätigungs-Schaltfläche
                //------------------------------------------------------------------------------
                TextButton(
                    onClick = {
                        onConfirm()
                        onDismiss()
                    },
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text(
                        text = "Reset now!",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    }
}