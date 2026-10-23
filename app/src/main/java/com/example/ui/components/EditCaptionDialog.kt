package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.model.CaptionItem
import com.example.ui.theme.BrandAqua
import com.example.ui.theme.BrandBorder
import com.example.ui.theme.BrandDarkCard
import com.example.ui.theme.BrandDarkCharcoal
import com.example.ui.theme.BrandDarkSurface
import com.example.ui.theme.BrandLightGrey
import com.example.ui.theme.BrandPink
import com.example.ui.theme.BrandSubtleText

@Composable
fun EditCaptionDialog(
    caption: CaptionItem,
    onDismiss: () -> Unit,
    onSave: (String) -> Unit
) {
    var editedText by remember { mutableStateOf(caption.text) }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = BrandDarkCard,
            border = BorderStroke(1.dp, BrandBorder),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Text(
                    text = "Edit Caption",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = BrandAqua
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Personalize your caption before copying or sharing.",
                    fontSize = 13.sp,
                    color = BrandSubtleText
                )

                Spacer(modifier = Modifier.height(14.dp))

                OutlinedTextField(
                    value = editedText,
                    onValueChange = { editedText = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .testTag("edit_caption_input"),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = BrandLightGrey,
                        unfocusedTextColor = BrandLightGrey,
                        focusedBorderColor = BrandAqua,
                        unfocusedBorderColor = BrandBorder,
                        focusedContainerColor = BrandDarkSurface,
                        unfocusedContainerColor = BrandDarkSurface,
                        cursorColor = BrandAqua
                    ),
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "${editedText.length} chars",
                        fontSize = 12.sp,
                        color = BrandSubtleText
                    )
                    Text(
                        text = "${editedText.split(Regex("\\s+")).filter { it.isNotBlank() }.size} words",
                        fontSize = 12.sp,
                        color = BrandSubtleText
                    )
                }

                Spacer(modifier = Modifier.height(18.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        border = BorderStroke(1.dp, BrandBorder),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = BrandLightGrey
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Cancel")
                    }

                    Spacer(modifier = Modifier.height(0.dp))
                    androidx.compose.foundation.layout.Spacer(modifier = Modifier.padding(horizontal = 4.dp))

                    Button(
                        onClick = {
                            if (editedText.isNotBlank()) {
                                onSave(editedText.trim())
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = BrandAqua,
                            contentColor = BrandDarkCharcoal
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.testTag("save_caption_button")
                    ) {
                        Text(
                            text = "Save Changes",
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}
