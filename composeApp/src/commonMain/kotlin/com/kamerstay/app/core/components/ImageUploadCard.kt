package com.kamerstay.app.core.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kamerstay.app.core.theme.*

// ── Zone upload rectangulaire (photos hôtel / chambre) ────
@Composable
fun ImageUploadCard(
    imagePicked: Boolean,
    onPickImage: () -> Unit,
    onRemoveImage: () -> Unit,
    label: String = "Upload Image",
    hint: String = "PNG, JPG or JPEG (max. 10MB)",
    compact: Boolean = false,
    modifier: Modifier = Modifier
) {
    if (imagePicked) {
        // ── État sélectionné ──────────────────────────
        Box(
            modifier = modifier
                .fillMaxWidth()
                .height(if (compact) 72.dp else 160.dp)
                .clip(RoundedCornerShape(12.dp))
        ) {
            // Simulated image preview
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(Color(0xFF0D2A4A), Color(0xFF1A4A6A))
                        )
                    )
            )

            // Overlay top-right: remove button
            Box(
                modifier = Modifier
                    .padding(8.dp)
                    .size(28.dp)
                    .clip(CircleShape)
                    .background(Color.Black.copy(0.5f))
                    .align(Alignment.TopEnd)
                    .clickable { onRemoveImage() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Outlined.Close,
                    contentDescription = "Remove image",
                    tint = Color.White,
                    modifier = Modifier.size(14.dp)
                )
            }

            // Bottom bar: filename + change button
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomStart)
                    .background(Color.Black.copy(0.55f))
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        Icons.Outlined.Image,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(14.dp)
                    )
                    Text(
                        text = "photo_selected.jpg",
                        fontSize = 12.sp,
                        color = Color.White,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(Primary)
                        .clickable { onPickImage() }
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "Change",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = OnPrimary
                    )
                }
            }
        }
    } else {
        if (compact) {
            // ── État vide compact (72dp, style original) ──
            Box(
                modifier = modifier
                    .fillMaxWidth()
                    .height(72.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .border(1.5.dp, Primary.copy(0.5f), RoundedCornerShape(10.dp))
                    .background(Primary.copy(0.05f))
                    .clickable { onPickImage() },
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        Icons.Outlined.AddAPhoto,
                        contentDescription = null,
                        tint = Primary,
                        modifier = Modifier.size(18.dp)
                    )
                    Text(
                        text = label,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Primary
                    )
                }
            }
        } else {
            // ── État vide normal (grand, pour RegisterHotel) ──
            Box(
                modifier = modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .border(1.5.dp, Divider, RoundedCornerShape(12.dp))
                    .background(LocalAppColors.current.background)
                    .clickable { onPickImage() }
                    .padding(28.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(52.dp)
                            .clip(CircleShape)
                            .background(Primary),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Outlined.FileUpload,
                            contentDescription = null,
                            tint = OnPrimary,
                            modifier = Modifier.size(26.dp)
                        )
                    }
                    Text(
                        text = label,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = LocalAppColors.current.textPrimary
                    )
                    Text(
                        text = hint,
                        fontSize = 12.sp,
                        color = OnSurfaceSecondary
                    )
                    OutlinedButton(
                        onClick = { onPickImage() },
                        shape = RoundedCornerShape(20.dp),
                        border = ButtonDefaults.outlinedButtonBorder,
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Secondary)
                    ) {
                        Text(
                            text = "Browse Files",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium,
                            color = Secondary
                        )
                    }
                }
            }
        }
    }
}

// ── Badge camera sur avatar circulaire ────────────────────
@Composable
fun AvatarWithImagePicker(
    imagePicked: Boolean,
    onPickImage: () -> Unit,
    modifier: Modifier = Modifier,
    avatarSize: Int = 100,
    badgeSize: Int = 34
) {
    Box(modifier = modifier.size((avatarSize + badgeSize / 2).dp)) {
        // Avatar circle
        Box(
            modifier = Modifier
                .size(avatarSize.dp)
                .clip(CircleShape)
                .border(3.dp, Primary, CircleShape)
                .background(
                    if (imagePicked) Primary.copy(0.3f)
                    else OnSurfaceSecondary.copy(0.2f)
                )
                .align(Alignment.Center),
            contentAlignment = Alignment.Center
        ) {
            if (imagePicked) {
                Icon(
                    Icons.Outlined.Person,
                    contentDescription = null,
                    tint = Primary,
                    modifier = Modifier.size((avatarSize * 0.5f).dp)
                )
                // Simulated "image selected" overlay
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(Color(0xFF0D2A4A), Color(0xFF1A4A6A))
                            )
                        )
                )
                Icon(
                    Icons.Outlined.Person,
                    contentDescription = null,
                    tint = Color.White.copy(0.8f),
                    modifier = Modifier.size((avatarSize * 0.45f).dp)
                )
            } else {
                Icon(
                    Icons.Outlined.Person,
                    contentDescription = null,
                    tint = OnSurfaceSecondary,
                    modifier = Modifier.size((avatarSize * 0.5f).dp)
                )
            }
        }

        // Camera badge
        Box(
            modifier = Modifier
                .size(badgeSize.dp)
                .clip(CircleShape)
                .background(Secondary)
                .border(2.dp, Color.White, CircleShape)
                .align(Alignment.BottomEnd)
                .clickable { onPickImage() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                if (imagePicked) Icons.Outlined.Edit else Icons.Outlined.CameraAlt,
                contentDescription = "Pick image",
                tint = Color.White,
                modifier = Modifier.size((badgeSize * 0.47f).dp)
            )
        }
    }
}